package com.sipl.ticket.user.service;

import com.sipl.ticket.core.dto.request.CustomerVerificationRequestDTO;
import com.sipl.ticket.core.dto.request.UsersRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CustomerVerificationResponseDTO;
import com.sipl.ticket.core.dto.response.UsersResponseDTO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UsersService {
    ApiResponseDTO<?> findAll(Boolean unpaginated, Pageable pageable);

    ApiResponseDTO<?> findAllActive(Boolean unpaginated, Pageable pageable);

    ApiResponseDTO<UsersResponseDTO> findById(Long id);

    ApiResponseDTO<UsersResponseDTO> create(UsersRequestDTO dto);

    ApiResponseDTO<UsersResponseDTO> update(Long id, UsersRequestDTO dto);

    ApiResponseDTO<Void> delete(Long id);
    ApiResponseDTO<Void> downloadUsersExcel(HttpServletResponse response) throws IOException;

    ApiResponseDTO<?> validateCustomerMobile(Long mobileNumber);
}
