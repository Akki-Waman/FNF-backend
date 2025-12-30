package com.sipl.ticket.branch.controller;

import com.sipl.ticket.core.dto.request.BranchSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.BranchDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/branches")
@CrossOrigin("*")
@Api(tags = "Branch APIs")
public interface BranchController {

    @ApiOperation(
            value = "Create a new branch",
            response = BranchDto.class
    )
    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<BranchDto>> saveBranch(
            @RequestBody BranchDto dto
    );

    @ApiOperation(
            value = "Update branch",
            response = BranchDto.class
    )
    @PutMapping("/update")
    ResponseEntity<ApiResponseDTO<BranchDto>> updateBranch(
            @RequestBody BranchDto dto
    );

    @ApiOperation(
            value = "Get branch by ID",
            response = BranchDto.class
    )
    @GetMapping("/get/{branchId}")
    ResponseEntity<ApiResponseDTO<BranchDto>> getById(
            @PathVariable Integer branchId
    );

    @ApiOperation(
            value = "Delete branch",
            response = String.class
    )
    @DeleteMapping("/delete/{branchId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Integer branchId
    );

    @ApiOperation(
            value = "Search branches",
            response = BranchDto.class
    )
    @PostMapping("/search")
    ResponseEntity<ApiResponseDTO<PagedResponse<BranchDto>>> searchBranches(
            @RequestBody BranchSearchRequestDto requestDto
    );
}
