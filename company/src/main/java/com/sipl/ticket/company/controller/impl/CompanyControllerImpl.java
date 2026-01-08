package com.sipl.ticket.company.controller.impl;

import com.sipl.ticket.company.controller.CompanyController;
import com.sipl.ticket.company.service.CompanyService;
import com.sipl.ticket.core.dto.request.CompaniesRequestDto;
import com.sipl.ticket.core.dto.request.CompanySearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CompanyDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CompanyControllerImpl implements CompanyController {

    private final CompanyService companiesService;

    @Override
    public ResponseEntity<ApiResponseDTO<CompanyDto>> saveCompany(
             CompaniesRequestDto dto) {

        log.info("<<Start>> saveCompany <<Start>>");

        ApiResponseDTO<CompanyDto> response =
                companiesService.saveCompany(dto);

        log.info("<<End>> saveCompany <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<CompanyDto>> updateCompany(
            CompaniesRequestDto dto) {

        log.info("<<Start>> updateCompany <<Start>>");

        ApiResponseDTO<CompanyDto> response =
                companiesService.updateCompany(dto);

        log.info("<<End>> updateCompany <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<CompanyDto>> getById(
            Long companyId) {

        log.info("<<Start>> getCompanyById <<Start>>");

        ApiResponseDTO<CompanyDto> response =
                companiesService.getById(companyId);

        log.info("<<End>> getCompanyById <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<String>> deleteById(
            Long companyId) {

        log.info("<<Start>> deleteCompany <<Start>>");

        ApiResponseDTO<String> response =
                companiesService.deleteById(companyId);

        log.info("<<End>> deleteCompany <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<CompanyDto>> getAllCompanies() {

        log.info("<<Start>> getAllCompanies <<Start>>");

        ApiResponseDTO<CompanyDto> response =
                companiesService.getAllCompanies();

        log.info("<<End>> getAllCompanies <<End>>");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponseDTO<PagedResponse<CompanyDto>>> searchCompanies(CompanySearchRequestDto requestDto) {
        log.info("Searching companies with request: {}", requestDto);

        ApiResponseDTO<PagedResponse<CompanyDto>> response =
                companiesService.searchCompanies(requestDto);

        return ResponseEntity.status(response.getStatus()).body(response);
    }
    @Override
    public void downloadExcel(HttpServletResponse response) {

        log.info("<<Start>> exportCompaniesExcel <<Start>>");

        try {
            companiesService.exportCompanies(response);
            log.info("<<Success>> exportCompaniesExcel <<Success>>");
        } catch (Exception e) {
            log.error("<<Error>> exportCompaniesExcel failed", e);
            throw new RuntimeException("Error while exporting companies excel", e);
        }

        log.info("<<End>> exportCompaniesExcel <<End>>");
    }

}
