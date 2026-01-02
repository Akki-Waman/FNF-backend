package com.sipl.ticket.task.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipl.client.dms.dto.response.DmsResponseDTO;
import com.sipl.client.dms.dto.response.DocumentDTO;
import com.sipl.client.dms.impl.DocumentClientService;
import com.sipl.ticket.core.dao.entity.*;
import com.sipl.ticket.core.dao.repository.*;
import com.sipl.ticket.core.dto.request.TaskRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CombinedTaskResponseDto;
import com.sipl.ticket.core.mapper.*;
import com.sipl.ticket.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskAssigneeRepository taskAssigneeRepository;
    private final TaskFollowerRepository taskFollowerRepository;
    private final TaskTagRepository taskTagRepository;
    private final TaskAttachmentRepository taskAttachmentRepository;

    private final UsersRepository usersRepository;
    private final BranchRepository branchRepository;
    private final TicketRepository ticketRepository;
    private final TagsRepository tagsRepository;

    private final TaskMapper taskMapper;
    private final TaskAssigneeMapper taskAssigneeMapper;
    private final TaskFollowerMapper taskFollowerMapper;
    private final TaskTagMapper taskTagMapper;
    private final TaskAttachmentMapper taskAttachmentMapper;
    private final ObjectMapper objectMapper;
    private final DocumentClientService documentClientService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponseDTO<CombinedTaskResponseDto> addTask(
            String taskRequestDto,
            List<MultipartFile> files) {

        log.info("<<START>> addTask service");

        try {
            TaskRequestDto dto =
                    objectMapper.readValue(taskRequestDto, TaskRequestDto.class);

            Task task = saveTask(dto);
            List<TaskAssignee> assignees = saveAssignees(dto.getAssigneeUserIds(), task);
            List<TaskFollower> followers = saveFollowers(dto.getFollowerUserIds(), task);
            List<TaskTag> tags = saveTags(dto.getTagIds(), task);
            List<TaskAttachment> attachments =
                    saveAttachments(files, task);

            CombinedTaskResponseDto responseDto =
                    new CombinedTaskResponseDto(
                            taskMapper.toDto(task),
                            taskAssigneeMapper.mapToDtoList(assignees),
                            taskFollowerMapper.mapToDtoList(followers),
                            taskTagMapper.mapToDtoList(tags),
                            taskAttachmentMapper.mapToDtoList(attachments)
                    );

            log.info("<<END>> addTask service SUCCESS");
            return new ApiResponseDTO<>(
                    responseDto,
                    null,
                    null,
                    "Task created successfully",
                    HttpStatus.CREATED,
                    false,
                    null,
                    null
            );

        } catch (Exception e) {
            log.error("Exception in addTask service", e);
            return new ApiResponseDTO<>(
                    null,
                    null,
                    null,
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true,
                    null,
                    null
            );
        }

    }

    private Task saveTask(TaskRequestDto dto) {

        validateTaskRequest(dto);
        validateTaskDates(dto);
        validateAssignee(dto);
        validateNoDuplicateUsers(dto);

        Branches branch = getBranch(dto.getBranchId());
        Ticket ticket = getTicket(dto.getTicketId());

        Task task = new Task();
        mapBasicTaskFields(dto, task);
        mapOptionalTaskFields(dto, task);

        task.setBranch(branch);
        task.setTicket(ticket);

        Task savedTask = taskRepository.save(task);
        log.info("Task saved with id={}", savedTask.getTaskId());

        return savedTask;
    }

    private void validateTaskRequest(TaskRequestDto dto) {

        if (dto == null) {
            throw new RuntimeException("Task request cannot be null");
        }

        if (dto.getSubject() == null || dto.getSubject().trim().isEmpty()) {
            throw new RuntimeException("Task subject is required");
        }

        if (dto.getBranchId() == null) {
            throw new RuntimeException("Branch is required");
        }

        if (dto.getTicketId() == null) {
            throw new RuntimeException("Ticket is required");
        }
    }

    private void validateTaskDates(TaskRequestDto dto) {

        LocalDate today = LocalDate.now();

        LocalDate startDate = dto.getStartDate();
        LocalDate dueDate = dto.getDueDate();

        if (startDate != null && startDate.isBefore(today)) {
            throw new RuntimeException("Start date cannot be in the past");
        }

        if (dueDate != null && dueDate.isBefore(today)) {
            throw new RuntimeException("Due date cannot be in the past");
        }

        if (startDate != null && dueDate != null && dueDate.isBefore(startDate)) {
            throw new RuntimeException("Due date cannot be before start date");
        }
    }

    private Branches getBranch(Integer branchId) {
        return branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
    }

    private Ticket getTicket(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    private void mapBasicTaskFields(TaskRequestDto dto, Task task) {

        task.setSubject(dto.getSubject().trim());
        task.setDescription(dto.getDescription());

        task.setIsPublic(Boolean.TRUE.equals(dto.getIsPublic()));

        task.setIsBillable(
                dto.getIsBillable() != null ? dto.getIsBillable() : Boolean.TRUE
        );
    }

    private void mapOptionalTaskFields(TaskRequestDto dto, Task task) {

        if (dto.getHourlyRate() != null) {
            task.setHourlyRate(dto.getHourlyRate());
        }

        if (dto.getStartDate() != null) {
            task.setStartDate(dto.getStartDate());
        }

        if (dto.getDueDate() != null) {
            task.setDueDate(dto.getDueDate());
        }

        if (dto.getPriority() != null) {
            task.setPriority(dto.getPriority());
        }

        if (dto.getRepeatType() != null) {
            task.setRepeatType(dto.getRepeatType());
        }

        if (dto.getRelatedToType() != null) {
            task.setRelatedToType(dto.getRelatedToType());
        }

        if (dto.getStatus() != null) {
            task.setStatus(dto.getStatus());
        }
    }

    private List<TaskAssignee> saveAssignees(List<Long> userIds, Task task) {

        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<TaskAssignee> assignees = userIds.stream()
                .map(userId -> {
                    Users user = usersRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    TaskAssignee assignee = new TaskAssignee();
                    assignee.setTask(task);
                    assignee.setUser(user);
                    return assignee;
                })
                .collect(Collectors.toList());

        return taskAssigneeRepository.saveAll(assignees);
    }

    private List<TaskFollower> saveFollowers(List<Long> userIds, Task task) {

        if (userIds == null || userIds.isEmpty())
            return Collections.emptyList();

        List<TaskFollower> followers = userIds.stream()
                .map(userId -> {
                    Users user = usersRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    TaskFollower follower = new TaskFollower();
                    follower.setTask(task);
                    follower.setUser(user);
                    return follower;
                }).collect(Collectors.toList());

        return taskFollowerRepository.saveAll(followers);
    }

    private List<TaskTag> saveTags(List<Long> tagIds, Task task) {

        if (tagIds == null || tagIds.isEmpty())
            return Collections.emptyList();

        List<TaskTag> tags = tagIds.stream()
                .map(tagId -> {
                    Tags tag = tagsRepository.findById(tagId)
                            .orElseThrow(() -> new RuntimeException("Tag not found"));
                    TaskTag taskTag = new TaskTag();
                    taskTag.setTask(task);
                    taskTag.setTag(tag);
                    return taskTag;
                }).collect(Collectors.toList());

        return taskTagRepository.saveAll(tags);
    }

    private List<TaskAttachment> saveAttachments(
            List<MultipartFile> files,
            Task task) {

        if (files == null || files.isEmpty())
            return Collections.emptyList();

        List<TaskAttachment> attachments = new ArrayList<>();

        for (MultipartFile file : files) {

            DmsResponseDTO<?> response =
                    documentClientService.upload(file, "tasks/");

            DocumentDTO document =
                    objectMapper.convertValue(response.getData(), DocumentDTO.class);

            DmsDocument dmsDocument = new DmsDocument();
            dmsDocument.setDocumentId(document.getDocumentId());

            TaskAttachment attachment = new TaskAttachment();
            attachment.setTask(task);
            attachment.setDmsDocument(dmsDocument);

            attachments.add(attachment);
        }

        return taskAttachmentRepository.saveAll(attachments);
    }

    private void validateAssignee(TaskRequestDto dto) {

        if (dto.getAssigneeUserIds() == null || dto.getAssigneeUserIds().isEmpty()) {
            throw new RuntimeException("At least one assignee is required");
        }
    }


    private void validateNoDuplicateUsers(TaskRequestDto dto) {

        if (dto.getAssigneeUserIds() == null || dto.getFollowerUserIds() == null) {
            return;
        }

        Set<Long> assignees = new HashSet<>(dto.getAssigneeUserIds());
        assignees.retainAll(dto.getFollowerUserIds());

        if (!assignees.isEmpty()) {
            throw new RuntimeException("Same user cannot be assignee and follower");
        }
    }

}
