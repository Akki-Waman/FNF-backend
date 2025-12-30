package com.sipl.ticket.company.controller;

import com.sipl.ticket.core.dto.request.CompaniesRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CompanyDto;
import com.sipl.ticket.core.dto.response.PagedResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/companies")
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "Company APIs")
public interface CompanyController {

    @ApiOperation(
            value = "Create a new company",
            notes = "Provide the necessary company information to save a new company",
            response = CompanyDto.class
    )
    @PostMapping("/save")
    ResponseEntity<ApiResponseDTO<CompanyDto>> saveCompany(
            @RequestBody CompaniesRequestDto companiesRequestDto
    );

    @ApiOperation(
            value = "Update company details",
            notes = "Provide company ID and updated company information",
            response = CompanyDto.class
    )
    @PutMapping("/update")
    ResponseEntity<ApiResponseDTO<CompanyDto>> updateCompany(
            @RequestBody CompaniesRequestDto companiesRequestDto
    );

    @ApiOperation(
            value = "Get company by ID",
            notes = "Fetch company details using company ID",
            response = CompanyDto.class
    )
    @GetMapping("/get/{companyId}")
    ResponseEntity<ApiResponseDTO<CompanyDto>> getById(
            @PathVariable Long companyId
    );

    @ApiOperation(
            value = "Delete company",
            notes = "Soft delete company by company ID",
            response = String.class
    )
    @DeleteMapping("/delete/{companyId}")
    ResponseEntity<ApiResponseDTO<String>> deleteById(
            @PathVariable Long companyId
    );

    @ApiOperation(
            value = "Get all companies",
            notes = "Fetch all active companies with pagination support",
            response = CompanyDto.class
    )
    @GetMapping("")
    ResponseEntity<ApiResponseDTO<PagedResponse<CompanyDto>>> getAllCompanies();
}
