package com.sipl.ticket.task.controller.impl;

import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.dto.request.DeleteTasksRequestDTO;
import com.sipl.ticket.core.dto.request.ExportSearchRequestDTO;
import com.sipl.ticket.core.dto.request.TaskSearchRequestDto;
import com.sipl.ticket.core.dto.response.*;
import com.sipl.ticket.core.helper.UserManager;
import com.sipl.ticket.task.controller.TaskController;
import com.sipl.ticket.task.service.TaskService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TaskControllerImpl implements TaskController {

    private final TaskService taskService;
    private final UserManager userManager;


    @Override
    public ResponseEntity<ApiResponseDTO<CombinedTaskResponseDto>> addTask(String taskRequestDto, List<MultipartFile> files) {
        log.info("<<START>> addTask <<START>>");
        ApiResponseDTO<CombinedTaskResponseDto> response =
                taskService.addTask(taskRequestDto, files);
        log.info("<<END>> addTask <<END>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<TaskCombinedSearchResponseDTO>>> searchTasks(
            TaskSearchRequestDto requestDto) {

        log.info("<<START>> searchTasks <<START>>");

        ApiResponseDTO<PagedResponse<TaskCombinedSearchResponseDTO>> response =
                taskService.searchTasks(requestDto);
        log.info("<<END>> searchTasks <<END>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<CombinedTaskResponseDto>> updateTask(String taskRequestDto, List<MultipartFile> files) {
        log.info("<<START>> updateTask <<START>>");
        ApiResponseDTO<CombinedTaskResponseDto> response =
                taskService.updateTask(taskRequestDto, files);
        log.info("<<END>> updateTask <<END>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<List<TaskStatusCountDto>>> getTaskSummary(
            HttpServletRequest servletRequest) {

        log.info("<<START>> getTaskSummary <<START>>");

        Users user = userManager.getUser(servletRequest);
        log.debug("Fetched user from request, userId={}", user != null ? user.getId() : null);

        ApiResponseDTO<List<TaskStatusCountDto>> apiResponse =
                taskService.getTaskSummary(user);

        log.info("<<END>> getTaskSummary <<END>>");

        return ResponseEntity.ok(apiResponse);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<Void>> deleteTasks(
            DeleteTasksRequestDTO requestDTO) {

        log.info("<<START>> deleteTasks controller");

        ApiResponseDTO<Void> response =
                taskService.deleteTasks(requestDTO);

        log.info("<<END>> deleteTasks controller");

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> exportTasks(
            ExportSearchRequestDTO request,
            HttpServletResponse response
    ) {

        log.info("<<Start>> exportTasks endpoint called");

        taskService.exportTasks(request, response);

        log.info("<<End>> exportTasks endpoint called");

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ApiResponseDTO<Long>> getAllTaskIds() {

        log.info("<<START>> getAllTaskIds controller");

        ApiResponseDTO<Long> response =
                taskService.getAllTaskIds();

        log.info("<<END>> getAllTaskIds controller");

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<TaskDto>> startStopTaskTimer(Long taskId) {
        log.info("<<START>> startStopTaskTimer <<START>>");
        ApiResponseDTO<TaskDto> response =
                taskService.startStopTaskTimer(taskId);
        log.info("<<END>> startStopTaskTimer <<END>>");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<CombinedTaskResponseDto>> getTaskById(Long taskId) {
        log.info("<<START>> getTaskById <<START>>");
        ApiResponseDTO<CombinedTaskResponseDto> response =
                taskService.getTaskById(taskId);
        log.info("<<END>> getTaskById <<END>>");
        return ResponseEntity.ok(response);
    }

}
