package com.sipl.ticket.branch.service;

import com.sipl.ticket.core.dto.request.BranchRequestDto;
import com.sipl.ticket.core.dto.request.BranchSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.BranchDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public interface BranchService {

    ApiResponseDTO<BranchDto> saveBranch(BranchRequestDto dto);

    ApiResponseDTO<BranchDto> updateBranch(BranchRequestDto dto);

    ApiResponseDTO<BranchDto> getById(Integer branchId);

    ApiResponseDTO<String> deleteById(Integer branchId);

    ApiResponseDTO<PagedResponse<BranchDto>> searchBranches(
            BranchSearchRequestDto requestDto
    );

    ApiResponseDTO<BranchDto> getAllBranches();

    void exportBranchesCsv(HttpServletResponse response);

}
