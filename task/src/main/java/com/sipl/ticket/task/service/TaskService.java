package com.sipl.ticket.task.service;

import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.dto.request.DeleteTasksRequestDTO;
import com.sipl.ticket.core.dto.request.TaskSearchRequestDto;
import com.sipl.ticket.core.dto.response.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    ApiResponseDTO<CombinedTaskResponseDto> updateTask(
            String taskRequestDto,
            List<MultipartFile> files);

    ApiResponseDTO<List<TaskStatusCountDto>> getTaskSummary(Users user);

    ApiResponseDTO<Void> deleteTasks(DeleteTasksRequestDTO requestDTO);

    void exportTasks(
            String format,
            String query,
            HttpServletResponse response
    );


}
