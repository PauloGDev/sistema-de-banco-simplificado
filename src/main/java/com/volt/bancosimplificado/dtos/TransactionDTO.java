package com.volt.bancosimplificado.dtos;

import java.math.BigDecimal;

public record TransactionDTO(BigDecimal value, String senderDoc, String senderPass, String receiverDoc) {
}
