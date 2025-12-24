package com.sipl.ticket.core.dto.response;

import com.sipl.ticket.core.dto.request.UserRequestDTO;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleApiResponse {
    private UserRequestDTO data;
    private List<UserRequestDTO> dataList;
    private HttpStatus status;
    private String message;
    private boolean error;
    private LocalDateTime timestamp ;
}
