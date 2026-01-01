package com.sipl.ticket.branch.controller.impl;

import com.sipl.ticket.branch.controller.BranchController;
import com.sipl.ticket.branch.service.BranchService;
import com.sipl.ticket.core.dto.request.BranchRequestDto;
import com.sipl.ticket.core.dto.request.BranchSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.BranchDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BranchControllerImpl implements BranchController {

    private final BranchService branchService;

    @Override
    public ResponseEntity<ApiResponseDTO<BranchDto>> saveBranch(BranchRequestDto dto) {
        ApiResponseDTO<BranchDto> response = branchService.saveBranch(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<BranchDto>> updateBranch(BranchRequestDto dto) {
        ApiResponseDTO<BranchDto> response = branchService.updateBranch(dto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<BranchDto>> getById(Integer branchId) {
        ApiResponseDTO<BranchDto> response = branchService.getById(branchId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(Integer branchId) {
        ApiResponseDTO<String> response = branchService.deleteById(branchId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<BranchDto>>> searchBranches(
            BranchSearchRequestDto requestDto) {

        ApiResponseDTO<PagedResponse<BranchDto>> response =
                branchService.searchBranches(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<BranchDto>> getAllBranches() {
        ApiResponseDTO<BranchDto> response = branchService.getAllBranches();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
