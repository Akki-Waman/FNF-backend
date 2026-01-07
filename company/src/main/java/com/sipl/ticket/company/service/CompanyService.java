package com.sipl.ticket.company.service;


import com.sipl.ticket.core.dto.request.CompaniesRequestDto;
import com.sipl.ticket.core.dto.request.CompanySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CompanyDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CompanyService {
    ApiResponseDTO<CompanyDto> saveCompany(
            CompaniesRequestDto companiesRequestDto
    );

    ApiResponseDTO<CompanyDto> updateCompany(
            CompaniesRequestDto companiesRequestDto
    );

    ApiResponseDTO<CompanyDto> getById(
            Long companyId
    );

    ApiResponseDTO<String> deleteById(
            Long companyId
    );

    ApiResponseDTO<CompanyDto> getAllCompanies();

    ApiResponseDTO<PagedResponse<CompanyDto>> searchCompanies(
            CompanySearchRequestDto requestDto
    );
}
