package com.sipl.ticket.core.dto.response;



import com.sipl.ticket.core.dto.request.ClientRequestDto;
import com.sipl.ticket.core.dto.request.SearchRequestDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchClientRequestDto extends ClientRequestDto{

    private String clientCode;

    private String clientName;
}

