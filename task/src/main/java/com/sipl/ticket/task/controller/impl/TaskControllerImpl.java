package com.sipl.ticket.task.controller.impl;

import com.sipl.ticket.core.dto.request.TaskSearchRequestDto;
import com.sipl.ticket.core.dto.response.*;
import com.sipl.ticket.task.controller.TaskController;
import com.sipl.ticket.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TaskControllerImpl implements TaskController {

    private final TaskService taskService;

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
    public ResponseEntity<ApiResponseDTO<TaskSummaryResponseDto>> getTaskSummary(
            HttpServletRequest servletRequest) {

        log.info("<<START>> getTaskSummary called <<START>>");

        ApiResponseDTO<TaskSummaryResponseDto> apiResponse =
                taskService.getTaskSummary(servletRequest);

        log.info("<<END>> getTaskSummary <<END>>");

        return ResponseEntity.ok(apiResponse);
    }


}
