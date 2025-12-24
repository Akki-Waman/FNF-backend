package com.sipl.ticket.user.service.impl;

import com.sipl.ticket.core.dao.entity.Customers;
import com.sipl.ticket.core.dao.entity.Roles;
import com.sipl.ticket.core.dao.entity.Users;
import com.sipl.ticket.core.dao.repository.CustomerRepository;
import com.sipl.ticket.core.dao.repository.UserRolesRepository;
import com.sipl.ticket.core.dao.repository.UsersRepository;
import com.sipl.ticket.core.dto.request.CustomerVerificationRequestDTO;
import com.sipl.ticket.core.dto.request.UsersRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CustomerVerificationResponseDTO;
import com.sipl.ticket.core.dto.response.UsersResponseDTO;
import com.sipl.ticket.core.helper.UserExcelGenerator;
import com.sipl.ticket.core.mapper.UsersMapper;
import com.sipl.ticket.user.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
//    private final CustomerRepository customerRepository;
    private final UserRolesRepository userRolesRepository;

    @Override
    public ApiResponseDTO<?> findAll(Boolean unpaginated, Pageable pageable) {
        if (unpaginated) {
            log.info("Fetching all users ");
            List<Users> usersList = usersRepository.findAll();
            List<UsersResponseDTO> dtos = usersMapper.toDtoList(usersList);
            return new ApiResponseDTO<>(dtos, "Users list fetched successfully", HttpStatus.OK, false);
        }
        log.info("Fetching all users with pagination: {}", pageable);
        Page<Users> page = usersRepository.findAll(pageable);
        List<UsersResponseDTO> dtos = page.getContent().stream().map(usersMapper::toDto).collect(Collectors.toList());
        PageImpl<UsersResponseDTO> pageImpl = new PageImpl<UsersResponseDTO>(dtos, pageable, page.getTotalElements());
        return new ApiResponseDTO<>(pageImpl, "Paginated users list fetched successfully", HttpStatus.OK, false);
    }

    @Override
    public ApiResponseDTO<?> findAllActive(Boolean unpaginated, Pageable pageable) {
        log.info("Fetching all active users with pagination: {}", pageable);
        if (unpaginated) {
            List<Users> usersList = usersRepository.findByIsActiveTrue();
            List<UsersResponseDTO> dtos = usersMapper.toDtoList(usersList);
            return new ApiResponseDTO<>(dtos, "Active users list fetched successfully", HttpStatus.OK, false);
        }
        Page<Users> page = usersRepository.findByIsActiveTrue(pageable);
        List<UsersResponseDTO> dtos = page.getContent().stream().map(usersMapper::toDto).collect(Collectors.toList());
        PageImpl<UsersResponseDTO> pageImpl = new PageImpl<>(dtos, pageable, page.getTotalElements());
        return new ApiResponseDTO<>(pageImpl, "Paginated active users list fetched successfully", HttpStatus.OK, false);
    }

    @Override
    public ApiResponseDTO<UsersResponseDTO> findById(Long id) {
        log.info("Fetching user by id: {}", id);
        Users user = usersRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        UsersResponseDTO dto = usersMapper.toDto(user);
        return new ApiResponseDTO<>(dto, "User details fetched successfully", HttpStatus.OK, false);
    }

    @Override
    public ApiResponseDTO<UsersResponseDTO> create(UsersRequestDTO dto) {
        log.info("Creating user: {}", dto);
        Users users = usersMapper.toEntity(dto);
        users.setIsActive(true);
        Users saved = usersRepository.save(users);
        log.info("User created with id: {}", saved.getId());
        UsersResponseDTO usersResponseDTO = usersMapper.toDto(saved);
        return new ApiResponseDTO<>(usersResponseDTO, "User created successfully", HttpStatus.OK, false);
    }

    @Override
    public ApiResponseDTO<UsersResponseDTO> update(Long id, UsersRequestDTO dto) {
        log.info("Updating user id: {} with data: {}", id, dto);
        Users users = usersRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));

        Users updated = usersMapper.partialUpdate(dto, users);
        updated = usersRepository.save(updated);
        log.info("User updated: {}", updated.getId());
        UsersResponseDTO usersResponseDTO = usersMapper.toDto(updated);
        return new ApiResponseDTO<>(usersResponseDTO, "User updated successfully", HttpStatus.OK, false);
    }

    @Override
    public ApiResponseDTO<Void> delete(Long id) {
        log.info("Deleting user id: {}", id);
        Users user = usersRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
        usersRepository.delete(user);
        log.info("User deleted: {}", id);
        return new ApiResponseDTO<>("User deleted successfully", HttpStatus.OK, false);
    }

    @Override
    public ApiResponseDTO<Void> downloadUsersExcel(HttpServletResponse response) throws IOException {
        try {
            List<Users> usersList = usersRepository.findByIsActiveTrue();

            if (usersList == null || usersList.isEmpty()) {
                return new ApiResponseDTO<>("No Users data found", HttpStatus.NOT_FOUND, true);
            }
            List<UsersResponseDTO> dtos = usersMapper.toDtoList(usersList);
            UserExcelGenerator.generateCsv(dtos, response);

            return new ApiResponseDTO<>("Users CSV downloaded successfully", HttpStatus.OK, false);
        } catch (Exception e) {
            return new ApiResponseDTO<>("Error occurred while generating Users CSV: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, true);
        }
    }

    @Override
    public ApiResponseDTO<?> validateCustomerMobile(Long mobileNumber) {
            try {
//                String mobileStr = String.valueOf(mobileNumber);
//                Optional<Users> userByMobile = usersRepository.findByMobileNumber(mobileStr);
//                if (userByMobile.isPresent()) {
//                    return new ApiResponseDTO<>(
//                            null,
//                            "This mobile number is already registered. Please log in instead.",
//                            HttpStatus.CONFLICT,
//                            true
//                    );
//                }
//                Optional<Customers> customerByMobile = customerRepository.findByContactNo(mobileNumber);
//                if (customerByMobile.isEmpty()) {
//                    return new ApiResponseDTO<>(
//                            null,
//                            "This mobile number does not exist in our system. Please contact the administrator.",
//                            HttpStatus.BAD_REQUEST,
//                            true
//                    );
//                }
//                return new ApiResponseDTO<>(
//                        null,
//                        "Verification successful.",
//                        HttpStatus.OK,
//                        false
//                );
            } catch (Exception e) {
                return new ApiResponseDTO<>(
                        null,
                        "An unexpected error occurred while validating customer: " + e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        true
                );
            }
        }

}
