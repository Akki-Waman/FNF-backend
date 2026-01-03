package com.sipl.ticket.task.service;

import com.sipl.ticket.core.dto.request.TaskSearchRequestDto;
import com.sipl.ticket.core.dto.response.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface TaskService {

    ApiResponseDTO<CombinedTaskResponseDto> addTask(
            String taskRequestDto,
            List<MultipartFile> files
    );

    ApiResponseDTO<PagedResponse<TaskCombinedSearchResponseDTO>> searchTasks(
            TaskSearchRequestDto requestDto
    );

}
