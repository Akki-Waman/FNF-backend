package com.sipl.ticket.user.controller;

import com.sipl.ticket.core.dto.request.UsersRequestDTO;
import com.sipl.ticket.core.dto.request.UsersRequestDTO;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.CustomerVerificationResponseDTO;
import com.sipl.ticket.core.dto.response.UsersResponseDTO;
import io.swagger.annotations.Api;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("api/v1/users")
@CrossOrigin("*")
@Api(tags = "Users")
public interface UsersController {
    @GetMapping("find/all")
    ResponseEntity<ApiResponseDTO<?>> findAll(
            @RequestParam(required = false, defaultValue = "false") Boolean unpaginated, @PageableDefault @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable);

    @GetMapping("find/all/active")
    ResponseEntity<ApiResponseDTO<?>> findAllActive(@RequestParam(required = false, defaultValue = "false") Boolean unpaginated, @PageableDefault @SortDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable);

    @GetMapping("find/{id}")
    ResponseEntity<ApiResponseDTO<UsersResponseDTO>> findById(@PathVariable Long id);

    @PostMapping("create")
    ResponseEntity<ApiResponseDTO<UsersResponseDTO>> create(@Valid @RequestBody UsersRequestDTO dto);

    @PutMapping("update/{id}")
    ResponseEntity<ApiResponseDTO<UsersResponseDTO>> update(@PathVariable Long id, @Valid @RequestBody UsersRequestDTO dto);

    @DeleteMapping("delete/{id}")
    ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Long id);

    @GetMapping("/download/csv")
    public ResponseEntity<Void> downloadUsersCsv(HttpServletResponse response) throws IOException;

    @PostMapping("verify/customer-mobile")
    ResponseEntity<ApiResponseDTO<?>> validateCustomerMobile(
            @RequestParam Long mobileNumber);

}
