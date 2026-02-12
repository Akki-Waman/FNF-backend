package com.sipl.ticket.task.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipl.client.dms.dto.response.DmsResponseDTO;
import com.sipl.client.dms.dto.response.DocumentDTO;
import com.sipl.client.dms.impl.DocumentClientService;
import com.sipl.ticket.activityLog.annotation.ActivityLoggable;
import com.sipl.ticket.core.dao.entity.*;
import com.sipl.ticket.core.dao.repository.*;
import com.sipl.ticket.core.dto.request.*;
import com.sipl.ticket.core.dto.response.*;
import com.sipl.ticket.core.exception.custom.ResourceNotFoundException;
import com.sipl.ticket.core.helper.TaskExcelExportHelper;
import com.sipl.ticket.core.mapper.*;
import com.sipl.ticket.core.util.UserManager;
import com.sipl.ticket.master.service.MasterService;
import com.sipl.ticket.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private final UserRolesRepository userRolesRepository;
    private final UserManager userManager;
    private final MasterService masterService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @ActivityLoggable(
            action = "ADD",
            module = "TASK",
            description = "Task {0} created successfully"
    )
    public ApiResponseDTO<CombinedTaskResponseDto> addTask(
            String taskRequestDto,
            List<MultipartFile> files) {
        try {
            TaskRequestDto dto =
                    objectMapper.readValue(taskRequestDto, TaskRequestDto.class);
            Optional<Ticket> fetchTicketData = ticketRepository.findById(dto.getTicketId());
            if (fetchTicketData.isPresent()) {
                Ticket ticket = fetchTicketData.get();
                if (Boolean.TRUE.equals(ticket.getIsApproverRequired())
                        || ticket.getStatus().equals(7)) {
                    return new ApiResponseDTO<>(
                            null,
                            null,
                            null,
                            "This ticket is currently under approval. Please wait for further updates.",
                            HttpStatus.BAD_REQUEST,
                            true,
                            null,
                            null
                    );
                }
            }
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

            return new ApiResponseDTO<>(
                    responseDto,
                    null,
                    null,
                    "Task #" + task.getTaskId() + " created successfully",
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

    @ActivityLoggable(
            action = "CREATE",
            module = "TASK",
            description = "Task {0} created successfully"
    )
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
            log.warn("Task request is null");
            throw new RuntimeException("Task request cannot be null");
        }

        if (dto.getSubject() == null || dto.getSubject().trim().isEmpty()) {
            log.warn("Task subject is missing in request");
            throw new RuntimeException("Task subject is required");
        }

        if (dto.getBranchId() == null) {
            log.warn("Branch ID is missing for Task with subject={}", dto.getSubject());
            throw new RuntimeException("Branch is required");
        }

        if (dto.getTicketId() == null) {
            log.warn("Ticket ID is missing for Task with subject={}", dto.getSubject());
            throw new RuntimeException("Ticket is required");
        }
    }

    private void validateTaskDates(TaskRequestDto dto) {

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate startDate = dto.getStartDate();
        LocalDate dueDate = dto.getDueDate();

        if (startDate != null && startDate.isBefore(today)) {
            log.warn("Start date {} is in the past for Task with subject={}", startDate, dto.getSubject());
            throw new RuntimeException("Start date cannot be in the past");
        }

        if (dueDate != null && dueDate.isBefore(yesterday)) {
            log.warn("Due date {} is in the past for Task with subject={}", dueDate, dto.getSubject());
            throw new IllegalArgumentException("Due date cannot be in the past");
        }

        if (startDate != null && dueDate != null && dueDate.isBefore(startDate)) {
            log.warn("Due date {} is before start date {} for Task with subject={}", dueDate, startDate, dto.getSubject());
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

        if (files == null || files.isEmpty()) {
            log.warn("No files provided for Task id={}", task.getTaskId());
            return Collections.emptyList();
        }

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
            log.warn("No assignee provided for Task with subject={}", dto.getSubject());
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
            log.warn("Duplicate users found in assignee and follower list for Task with subject={}", dto.getSubject());
            throw new RuntimeException("Same user cannot be assignee and follower");
        }
    }

    @Override
    public ApiResponseDTO<PagedResponse<TaskCombinedSearchResponseDTO>> searchTasks(
            TaskSearchRequestDto dto) {

        log.info("Searching tasks with request: {}", dto);

        String sortBy = dto.getSortBy();

        if ("ticketId".equalsIgnoreCase(sortBy)) {
            sortBy = "ticket.ticketId";
        } else if ("id".equalsIgnoreCase(sortBy)) {
            sortBy = "taskId";
        }

        Sort sort = dto.getSortDir().equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(
                dto.getPage(),
                dto.getSize(),
                sort
        );
        Page<Task> pageResult = taskRepository.searchTasks(
                dto.getTicketId(),
                dto.getBranchId(),
                dto.getQuery(),
                dto.getTaskStatus(),
                pageable
        );
        if (pageResult.isEmpty()) {
            return new ApiResponseDTO<>(
                    null,
                    "No tasks found",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        List<TaskCombinedSearchResponseDTO> content =
                pageResult.getContent().stream()
                        .map(task -> {

                            Long taskId = task.getTaskId();

                            TaskCombinedSearchResponseDTO response =
                                    new TaskCombinedSearchResponseDTO();

                            // 🔹 Main task
                            response.setTaskCustomResponseDTO(
                                    taskMapper.toCustomDto(task)
                            );

                            // 🔹 Assignees
                            response.setTaskAssigneeCustomResponseDTOS(
                                    taskAssigneeRepository.findByTaskTaskId(taskId)
                                            .stream()
                                            .map(taskAssigneeMapper::toCustomDto)
                                            .collect(Collectors.toList())
                            );

                            // 🔹 Followers
                            response.setTaskFollowerCustomResponseDTOS(
                                    taskFollowerRepository.findByTaskTaskId(taskId)
                                            .stream()
                                            .map(taskFollowerMapper::toCustomDto)
                                            .collect(Collectors.toList())
                            );

                            // 🔹 Tags
                            response.setTaskTagCustomResponseDTOS(
                                    taskTagRepository.findByTaskTaskId(taskId)
                                            .stream()
                                            .map(taskTagMapper::toCustomDto)
                                            .collect(Collectors.toList())
                            );

                            // 🔹 Attachments
                            response.setTaskAttachmentCustomResponseDTOS(
                                    taskAttachmentRepository.findByTaskTaskId(taskId)
                                            .stream()
                                            .map(taskAttachmentMapper::toCustomDto)
                                            .collect(Collectors.toList())
                            );

                            return response;
                        })
                        .collect(Collectors.toList());

        return new ApiResponseDTO<>(
                new PagedResponse<>(
                        content,
                        pageResult.getNumber(),
                        pageResult.getTotalElements(),
                        pageResult.getTotalPages(),
                        pageResult.getSize(),
                        pageResult.isLast()
                ),
                "Tasks fetched successfully",
                HttpStatus.OK,
                false
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @ActivityLoggable(
            action = "UPDATE",
            module = "TASK",
            description = "Task {0} updated successfully"
    )
    public ApiResponseDTO<CombinedTaskResponseDto> updateTask(
            String taskRequestDto,
            List<MultipartFile> files) {

        try {
            TaskRequestDto dto =
                    objectMapper.readValue(taskRequestDto, TaskRequestDto.class);

            log.info("Updating task with taskId={}", dto.getTaskId());
            Task task = getExistingTask(dto.getTaskId());

            validateTaskRequest(dto);
            //validateTaskDates(dto);
            validateAssignee(dto);
            validateNoDuplicateUsers(dto);

            updateTaskFields(dto, task);

            Task updatedTask = taskRepository.save(task);
            log.info("Task updated successfully with id={}", updatedTask.getTaskId());

            List<TaskAssignee> assignees = updateAssignees(dto.getAssigneeUserIds(), task);
            List<TaskFollower> followers = updateFollowers(dto.getFollowerUserIds(), task);
            List<TaskTag> tags = updateTags(dto.getTagIds(), task);
            List<TaskAttachment> attachments = saveAttachments(files, task);

            log.info("Relations updated for taskId={} | assignees={}, followers={}, tags={}, attachments={}",
                    task.getTaskId(),
                    assignees.size(),
                    followers.size(),
                    tags.size(),
                    attachments.size());

            CombinedTaskResponseDto responseDto =
                    new CombinedTaskResponseDto(
                            taskMapper.toDto(updatedTask),
                            taskAssigneeMapper.mapToDtoList(assignees),
                            taskFollowerMapper.mapToDtoList(followers),
                            taskTagMapper.mapToDtoList(tags),
                            taskAttachmentMapper.mapToDtoList(attachments)
                    );

            return new ApiResponseDTO<>(
                    responseDto,
                    null,
                    null,
                    "Task #" + updatedTask.getTaskId() + " updated successfully",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception e) {
            log.error("Exception in updateTask service. ", e);
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


    private Task getExistingTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.warn("Task not found for update, taskId={}", taskId);
                    return new RuntimeException("Task not found");
                });
    }

    private void updateTaskFields(TaskRequestDto dto, Task task) {

        if (dto.getSubject() != null) {
            task.setSubject(dto.getSubject().trim());
        }

        if (dto.getDescription() != null) {
            task.setDescription(dto.getDescription());
        }

        if (dto.getIsPublic() != null) {
            task.setIsPublic(dto.getIsPublic());
        }

        if (dto.getIsBillable() != null) {
            task.setIsBillable(dto.getIsBillable());
        }

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
        if (dto.getComment() != null) {
            task.setComment(dto.getComment());
        }

        if (dto.getBranchId() != null) {
            task.setBranch(getBranch(dto.getBranchId()));
        }

        if (dto.getTicketId() != null) {
            task.setTicket(getTicket(dto.getTicketId()));
        }
    }

    private List<TaskAssignee> updateAssignees(
            List<Long> incomingUserIds,
            Task task) {

        if (incomingUserIds == null) {
            log.warn("No assignee list provided for taskId={}", task.getTaskId());
            return taskAssigneeRepository.findByTask(task);
        }

        List<TaskAssignee> existingAssignees =
                taskAssigneeRepository.findByTask(task);

        Set<Long> existingUserIds = existingAssignees.stream()
                .map(a -> a.getUser().getId())
                .collect(Collectors.toSet());

        Set<Long> incomingSet = new HashSet<>(incomingUserIds);

        /* -------- DELETE -------- */
        List<TaskAssignee> toDelete = existingAssignees.stream()
                .filter(a -> !incomingSet.contains(a.getUser().getId()))
                .collect(Collectors.toList());

        if (!toDelete.isEmpty()) {
            taskAssigneeRepository.deleteAll(toDelete);
            log.info("Deleted {} assignees for taskId={}",
                    toDelete.size(), task.getTaskId());
        }

        /* -------- ADD -------- */
        List<TaskAssignee> toAdd = incomingSet.stream()
                .filter(id -> !existingUserIds.contains(id))
                .map(id -> {
                    Users user = usersRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    TaskAssignee assignee = new TaskAssignee();
                    assignee.setTask(task);
                    assignee.setUser(user);
                    return assignee;
                })
                .collect(Collectors.toList());

        if (!toAdd.isEmpty()) {
            taskAssigneeRepository.saveAll(toAdd);
            log.info("Added {} new assignees for taskId={}",
                    toAdd.size(), task.getTaskId());
        }

        return taskAssigneeRepository.findByTask(task);
    }


    private List<TaskFollower> updateFollowers(
            List<Long> incomingUserIds,
            Task task) {

        log.info("Updating followers for taskId={}", task.getTaskId());

        if (incomingUserIds == null) {
            log.warn("No follower list provided for taskId={}", task.getTaskId());
            return taskFollowerRepository.findByTask(task);
        }

        List<TaskFollower> existingFollowers =
                taskFollowerRepository.findByTask(task);

        Set<Long> existingUserIds = existingFollowers.stream()
                .map(f -> f.getUser().getId())
                .collect(Collectors.toSet());

        Set<Long> incomingSet = new HashSet<>(incomingUserIds);

        /* DELETE */
        List<TaskFollower> toDelete = existingFollowers.stream()
                .filter(f -> !incomingSet.contains(f.getUser().getId()))
                .collect(Collectors.toList());

        if (!toDelete.isEmpty()) {
            taskFollowerRepository.deleteAll(toDelete);
            log.info("Deleted {} followers for taskId={}",
                    toDelete.size(), task.getTaskId());
        }

        /* ADD */
        List<TaskFollower> toAdd = incomingSet.stream()
                .filter(id -> !existingUserIds.contains(id))
                .map(id -> {
                    Users user = usersRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    TaskFollower follower = new TaskFollower();
                    follower.setTask(task);
                    follower.setUser(user);
                    return follower;
                })
                .collect(Collectors.toList());

        taskFollowerRepository.saveAll(toAdd);

        return taskFollowerRepository.findByTask(task);
    }


    private List<TaskTag> updateTags(
            List<Long> tagIds,
            Task task) {

        log.info("Updating tags for taskId={}", task.getTaskId());

        if (tagIds == null) {
            log.warn("No tag list provided for taskId={}", task.getTaskId());
            return taskTagRepository.findByTask(task);
        }

        List<TaskTag> existingTags =
                taskTagRepository.findByTask(task);

        Set<Long> existingTagIds = existingTags.stream()
                .map(t -> t.getTag().getTagId())
                .collect(Collectors.toSet());

        Set<Long> incomingSet = new HashSet<>(tagIds);

        /* DELETE */
        List<TaskTag> toDelete = existingTags.stream()
                .filter(t -> !incomingSet.contains(t.getTag().getTagId()))
                .collect(Collectors.toList());

        if (!toDelete.isEmpty()) {
            taskTagRepository.deleteAll(toDelete);
            log.info("Deleted {} tags for taskId={}",
                    toDelete.size(), task.getTaskId());
        }

        /* ADD */
        List<TaskTag> toAdd = incomingSet.stream()
                .filter(id -> !existingTagIds.contains(id))
                .map(id -> {
                    Tags tag = tagsRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Tag not found"));
                    TaskTag taskTag = new TaskTag();
                    taskTag.setTask(task);
                    taskTag.setTag(tag);
                    return taskTag;
                })
                .collect(Collectors.toList());

        taskTagRepository.saveAll(toAdd);

        return taskTagRepository.findByTask(task);
    }

    @Override
    public ApiResponseDTO<List<TaskStatusCountDto>> getTaskSummary(Users user) {
        try {

            log.info("Fetching unified task summary for userId={}", user.getId());

            List<TaskStatusCountDto> summary =
                    taskRepository.getTaskStatusSummaryWithUserAssignment(user.getId());

            log.debug("Unified task summary fetched, rowCount={}",
                    summary != null ? summary.size() : 0);

            return new ApiResponseDTO<List<TaskStatusCountDto>>(
                    summary,
                    "SUCCESS",
                    HttpStatus.OK,
                    false
            );

        } catch (Exception e) {
            log.error("Unexpected error while fetching task summary", e);
            return new ApiResponseDTO<>(
                    "INTERNAL_SERVER_ERROR",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }


    private ApiResponseDTO<List<TaskStatusCountDto>> validateUser(Users user) {

        if (user == null) {
            log.warn("User validation failed: user not found");
            return new ApiResponseDTO<>(
                    "User not found.",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        UserRoles userRole =
                userRolesRepository.findSingleByUserId(user.getId());

        if (userRole == null || !userRole.getIsActive()) {
            log.warn("User validation failed: inactive role, userId={}", user.getId());
            return new ApiResponseDTO<>(
                    "User role not found. Please contact administrator.",
                    HttpStatus.NOT_FOUND,
                    true
            );
        }

        log.debug("User validation successful, userId={}, roleId={}",
                user.getId(),
                userRole.getUserUserRoleId());

        return null;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @ActivityLoggable(
            action = "DELETE",
            module = "TASK",
            description = "Task id {0} deleted successfully"
    )
    public ApiResponseDTO<Void> deleteTasks(DeleteTasksRequestDTO requestDTO) {

        try {
            List<Long> taskIds = requestDTO.getTaskIds();

            if (taskIds == null || taskIds.isEmpty()) {
                log.warn("deleteTasks | empty or null taskIds");

                return new ApiResponseDTO<>(
                        null, null, null,
                        "Please select at least one task to delete.",
                        HttpStatus.BAD_REQUEST,
                        true, null, null
                );
            }

            log.info("deleteTasks | taskIds count: {}", taskIds.size());

            int updatedCount = taskRepository.softDeleteByIds(taskIds);

            log.info("deleteTasks | rows affected: {}", updatedCount);

            if (updatedCount == 0) {
                return new ApiResponseDTO<>(
                        null, null, null,
                        "Selected tasks not found or already deleted.",
                        HttpStatus.NOT_FOUND,
                        true, null, null
                );
            }

            return new ApiResponseDTO<>(
                    null, null, null,
                    "Selected tasks deleted successfully.",
                    HttpStatus.OK,
                    false, null, null
            );

        } catch (Exception e) {
            log.error("deleteTasks unexpected error", e);

            return new ApiResponseDTO<>(
                    null,
                    "Internal server error",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void exportTasks(
            ExportSearchRequestDTO request,
            HttpServletResponse response
    ) {

        String format = request.getFormat();
        ExportFilterDTO filters = request.getFilters();
        String search = filters != null ? filters.getSearch() : null;

        log.info("Task export requested | format={} | search={}", format, search);

        if (format == null ||
                !List.of("excel", "csv", "pdf")
                        .contains(format.toLowerCase())) {
            throw new IllegalArgumentException("Unsupported export format");
        }

        try {

            List<Task> tasks =
                    taskRepository
                            .searchTasks(null, null, search, null, Pageable.unpaged())
                            .getContent();

            log.info("Tasks fetched successfully | count={}", tasks.size());

            Map<Integer, String> statusMap =
                    masterService.getTaskStatusMap();

            Map<Integer, String> priorityMap =
                    masterService.getTaskPriorityMap();

            MasterContext masterContext =
                    new MasterContext(priorityMap, statusMap);

            List<TaskExportDTO> dtos =
                    tasks.stream()
                            .map(task -> {

                                TaskExportDTO dto =
                                        taskMapper.toExportDto(task, masterContext);


                                String assignedTo =
                                        taskAssigneeRepository.findByTask(task)
                                                .stream()
                                                .map(a ->
                                                        a.getUser().getFirstName() + " " +
                                                                a.getUser().getLastName()
                                                )
                                                .collect(Collectors.joining(", "));
                                dto.setAssignedTo(assignedTo);

                                String tags =
                                        taskTagRepository.findByTask(task)
                                                .stream()
                                                .map(tt -> tt.getTag().getTagName())
                                                .collect(Collectors.joining(", "));
                                dto.setTags(tags);

                                return dto;
                            })
                            .collect(Collectors.toList());

            TaskExcelExportHelper.export(dtos, format, response);

            log.info(
                    "Task export completed successfully | format={} | records={}",
                    format, dtos.size()
            );

        } catch (Exception e) {
            log.error("Task export failed", e);
            throw new RuntimeException("Failed to export tasks", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponseDTO<Long> getAllTaskIds() {

        try {

            List<Long> taskIds =
                    taskRepository.findAllActiveTaskIds();

            if (taskIds == null || taskIds.isEmpty()) {

                log.warn("getAllTaskIds | No active tasks found");

                return new ApiResponseDTO<>(
                        null,
                        Collections.emptyList(),
                        null,
                        "No active tasks are available at the moment.",
                        HttpStatus.NOT_FOUND,
                        false,
                        null,
                        null
                );
            }

            log.info(
                    "getAllTaskIds | Successfully fetched {} active task IDs",
                    taskIds.size()
            );

            return new ApiResponseDTO<>(
                    null,
                    taskIds,
                    null,
                    "Active task IDs fetched successfully.",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception e) {

            log.error(
                    "getAllTaskIds | Unexpected error while fetching active task IDs",
                    e
            );

            return new ApiResponseDTO<>(
                    null,
                    null,
                    null,
                    "Something went wrong while fetching task IDs. Please try again later.",
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    true,
                    null,
                    null
            );
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @ActivityLoggable(
            action = "UPDATE",
            module = "TASK",
            description = "Task timer updated for task {0}"
    )
    public ApiResponseDTO<TaskDto> startStopTaskTimer(Long taskId) {

        try {

            Task task = getTask(taskId);
            LocalDateTime now = LocalDateTime.now();

            if (isFirstTimeStart(task)) {
                return startTimer(task, now);
            }

            if (isStopRequired(task)) {
                return stopTimer(task, now);
            }

            return buildSuccessResponse(
                    task,
                    "Task timer has already been started and stopped"
            );

        } catch (Exception e) {
            log.error("Exception in startStopTaskTimer", e);
            return buildErrorResponse(e.getMessage());
        }
    }

    private Task getTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    private boolean isFirstTimeStart(Task task) {
        return task.getTimerStartTime() == null;
    }

    private boolean isStopRequired(Task task) {
        return task.getTimerStartTime() != null
                && task.getTimerStopTime() == null;
    }

    private ApiResponseDTO<TaskDto> startTimer(Task task, LocalDateTime now) {

        task.setTimerStartTime(now);
        task.setTimerStopTime(null);

        taskRepository.save(task);

        return buildSuccessResponse(
                task,
                "Task timer started successfully"
        );
    }

    private ApiResponseDTO<TaskDto> stopTimer(Task task, LocalDateTime now) {

        task.setTimerStopTime(now);

        BigDecimal hours = calculateTrackedHours(
                task.getTimerStartTime(),
                task.getTimerStopTime()
        );

        task.setTotalTrackedHours(
                getExistingTrackedHours(task).add(hours)
        );

        taskRepository.save(task);

        return buildSuccessResponse(
                task,
                "Task timer stopped successfully"
        );
    }

    private BigDecimal calculateTrackedHours(
            LocalDateTime start,
            LocalDateTime stop) {

        long minutes = ChronoUnit.MINUTES.between(start, stop);

        return BigDecimal.valueOf(minutes)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal getExistingTrackedHours(Task task) {
        return task.getTotalTrackedHours() != null
                ? task.getTotalTrackedHours()
                : BigDecimal.ZERO;
    }

    private ApiResponseDTO<TaskDto> buildSuccessResponse(
            Task task,
            String message) {

        return new ApiResponseDTO<>(
                taskMapper.toDto(task),
                null,
                null,
                message,
                HttpStatus.OK,
                false,
                null,
                null
        );
    }

    private ApiResponseDTO<TaskDto> buildErrorResponse(String message) {

        return new ApiResponseDTO<>(
                null,
                null,
                null,
                message,
                HttpStatus.INTERNAL_SERVER_ERROR,
                true,
                null,
                null
        );
    }

    @Override
    public ApiResponseDTO<CombinedTaskResponseDto> getTaskById(Long taskId) {

        try {

            Task task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new RuntimeException("Task not found"));

            List<TaskAssignee> assignees =
                    taskAssigneeRepository.findByTaskTaskId(taskId);

            List<TaskFollower> followers =
                    taskFollowerRepository.findByTaskTaskId(taskId);

            List<TaskTag> tags =
                    taskTagRepository.findByTaskTaskId(taskId);

            List<TaskAttachment> attachments =
                    taskAttachmentRepository.findByTaskTaskId(taskId);

            CombinedTaskResponseDto responseDto =
                    new CombinedTaskResponseDto(
                            taskMapper.toDto(task),
                            taskAssigneeMapper.mapToDtoList(assignees),
                            taskFollowerMapper.mapToDtoList(followers),
                            taskTagMapper.mapToDtoList(tags),
                            taskAttachmentMapper.mapToDtoList(attachments)
                    );

            return new ApiResponseDTO<>(
                    responseDto,
                    null,
                    null,
                    "Task details fetched successfully",
                    HttpStatus.OK,
                    false,
                    null,
                    null
            );

        } catch (Exception e) {
            log.error("Exception in getTaskById", e);
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

    @Override
    public ApiResponseDTO<TaskDto> updateTaskStatus(
            TaskStatusRequestDTO dto) {

        log.info("Update task status request received. taskId={}, status={}",
                dto != null ? dto.getTaskId() : null,
                dto != null ? dto.getStatus() : null
        );

        try {
            Task task = taskRepository.findById(dto.getTaskId())
                    .orElseThrow(() -> {
                        log.warn("Task not found. taskId={}", dto.getTaskId());
                        return new ResourceNotFoundException("Task not found");
                    });

            Integer oldStatus = task.getStatus();

            Map<Integer, String> statusMap =
                    masterService.getTaskStatusMap();   // task status master

            if (!statusMap.containsKey(dto.getStatus())) {
                log.warn("Invalid task status code. status={}", dto.getStatus());
                throw new IllegalArgumentException(
                        "Invalid task status: " + dto.getStatus()
                );
            }

            // If trying to complete task (5), timer must be started & stopped
            if (dto.getStatus() != null && dto.getStatus() == 5) {

                if (task.getTimerStartTime() == null
                        || task.getTimerStopTime() == null) {

                    log.warn(
                            "Cannot complete task without timer start/stop. taskId={}",
                            task.getTaskId()
                    );

                    throw new IllegalStateException(
                            "Please start and stop task timer before completing the task."
                    );
                }
            }

            log.info("Updating task status. taskId={}, oldStatus={}, newStatus={}",
                    dto.getTaskId(), oldStatus, dto.getStatus());

            task.setStatus(dto.getStatus());
            Task updatedTask = taskRepository.save(task);

            TaskDto responseDto =
                    taskMapper.toDto(updatedTask);

            return new ApiResponseDTO<>(
                    responseDto,
                    "Task status updated successfully",
                    HttpStatus.OK,
                    false
            );

        }
        catch (IllegalStateException ex) {

            log.warn(
                    "Task status validation failed. taskId={}, message={}",
                    dto != null ? dto.getTaskId() : null,
                    ex.getMessage()
            );

            return new ApiResponseDTO<>(
                    null,
                    ex.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    true
            );
        } catch (Exception ex) {
            log.error("Error while updating task status. taskId={}",
                    dto != null ? dto.getTaskId() : null, ex);

            throw new RuntimeException(
                    "Failed to update task status",
                    ex
            );
        }
    }


}