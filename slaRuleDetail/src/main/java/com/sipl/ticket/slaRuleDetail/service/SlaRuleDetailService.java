package com.sipl.ticket.slaRuleDetail.service;

import com.sipl.ticket.core.dto.request.SlaRuleDetailsSearchRequestDto;
import com.sipl.ticket.core.dto.response.ApiResponseDTO;
import com.sipl.ticket.core.dto.response.PagedResponse;
import com.sipl.ticket.core.dto.response.SlaRuleDetailsDto;

public interface SlaRuleDetailService {

    ApiResponseDTO<SlaRuleDetailsDto> save(SlaRuleDetailsDto dto);

    ApiResponseDTO<SlaRuleDetailsDto> update(SlaRuleDetailsDto dto);

    ApiResponseDTO<SlaRuleDetailsDto> getById(Integer id);

    ApiResponseDTO<String> delete(Integer id);

    ApiResponseDTO<SlaRuleDetailsDto> getAll();

    ApiResponseDTO<PagedResponse<SlaRuleDetailsDto>> search(
            SlaRuleDetailsSearchRequestDto dto
    );
}
