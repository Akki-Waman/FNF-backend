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

        log.info("Task search API called with request: {}", requestDto);

        ApiResponseDTO<PagedResponse<TaskCombinedSearchResponseDTO>> response =
                taskService.searchTasks(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
