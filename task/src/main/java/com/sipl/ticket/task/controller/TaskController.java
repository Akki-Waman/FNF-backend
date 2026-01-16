package com.sipl.ticket.task.controller;

import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.dto.request.DeleteTasksRequestDTO;
import com.sipl.ticket.core.dto.request.ExportSearchRequestDTO;
import com.sipl.ticket.core.dto.request.TaskSearchRequestDto;
import com.sipl.ticket.core.dto.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/api/v1/tasks")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "Task APIs")
public interface TaskController {

    @ApiOperation(
            value = "Create a new task",
            notes = "Provide task details to create a new task"
    )
    @PostMapping("/save")
    public ResponseEntity<ApiResponseDTO<CombinedTaskResponseDto>> addTask(
            @RequestPart("taskRequestDto") String taskRequestDto,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    );

    @PostMapping("/search")
    ResponseEntity<ApiResponseDTO<PagedResponse<TaskCombinedSearchResponseDTO>>> searchTasks(
            @RequestBody TaskSearchRequestDto requestDto
    );

    @ApiOperation(
            value = "Update a existing task",
            notes = "Provide task details to update a task"
    )
    @PutMapping("/update")
    public ResponseEntity<ApiResponseDTO<CombinedTaskResponseDto>> updateTask(
            @RequestParam("taskRequestDto") String taskRequestDto,
            @RequestParam(value = "files", required = false) List<MultipartFile> files);

    @GetMapping("/task-summary")
    @ApiOperation(value = "Get Task Summary by status")
    ResponseEntity<ApiResponseDTO<List<TaskStatusCountDto>>> getTaskSummary(
            HttpServletRequest request
    );


    @DeleteMapping("/delete")
    ResponseEntity<ApiResponseDTO<Void>> deleteTasks(
            @RequestBody DeleteTasksRequestDTO requestDTO
    );

    @ApiOperation("Export tasks in Excel / CSV / PDF")
    @PostMapping("/tasks/export")
    ResponseEntity<Void> exportTasks(
            @RequestBody ExportSearchRequestDTO request,
            HttpServletResponse response
    );

    @ApiOperation(
            value = "Get list of all task IDs",
            notes = "Fetches list of active task IDs"
    )
    @GetMapping("/tasks/ids")
    ResponseEntity<ApiResponseDTO<Long>> getAllTaskIds();

}
