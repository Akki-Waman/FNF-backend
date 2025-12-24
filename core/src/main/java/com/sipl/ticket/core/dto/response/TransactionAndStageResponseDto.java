package com.sipl.ticket.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionAndStageResponseDto {
    private TransactionResponseDTO transactionResponseDTO;
    private TransactionStageResponseDTO transactionStageResponseDTO;
}
