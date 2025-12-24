package com.sipl.ticket.user.controller.impl;

import com.sipl.ticket.core.dto.request.CustomerVerificationRequestDTO;
import com.sipl.ticket.core.dto.request.UsersRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CustomerVerificationResponseDTO;
import com.sipl.ticket.core.dto.response.UsersResponseDTO;
import com.sipl.ticket.user.controller.UsersController;
import com.sipl.ticket.user.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UsersControllerImpl implements UsersController {


    private final UsersService usersService;



    @Override
    public ResponseEntity<ApiResponseDTO<?>> findAll(Boolean unpaginated, Pageable pageable) {
        return ResponseEntity.ok(usersService.findAll(unpaginated, pageable));
    }

    @Override
    public ResponseEntity<ApiResponseDTO<?>> findAllActive(Boolean unpaginated, Pageable pageable) {
        return ResponseEntity.ok(usersService.findAllActive(unpaginated, pageable));
    }

    @Override
    public ResponseEntity<ApiResponseDTO<UsersResponseDTO>> findById(Long id) {
        return ResponseEntity.ok(usersService.findById(id));
    }

    @Override
    public ResponseEntity<ApiResponseDTO<UsersResponseDTO>> create(UsersRequestDTO dto) {
        return ResponseEntity.ok(usersService.create(dto));
    }

    @Override
    public ResponseEntity<ApiResponseDTO<UsersResponseDTO>> update(Long id, UsersRequestDTO dto) {
        return ResponseEntity.ok(usersService.update(id, dto));
    }

    @Override
    public ResponseEntity<ApiResponseDTO<Void>> delete(Long id) {
        return ResponseEntity.ok(usersService.delete(id));
    }

    @Override
    public ResponseEntity<Void> downloadUsersCsv(HttpServletResponse response) throws IOException {
        usersService.downloadUsersExcel(response);
        return ResponseEntity.ok().build();
    }


    @Override
    public ResponseEntity<ApiResponseDTO<?>> validateCustomerMobile(Long mobileNumber) {
        return ResponseEntity.ok(usersService.validateCustomerMobile(mobileNumber));
    }


}
