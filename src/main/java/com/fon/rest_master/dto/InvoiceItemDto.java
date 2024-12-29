package com.fon.rest_master.dto;

import com.fon.rest_master.domain.Month;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceItemDto implements Serializable {

    @NotNull
    private Long seqNum;
    @NotNull(message = "Invoice id is mandatory!")
    private Long invoiceId;
    @NotNull(message = "Company pib is mandatory!")
    private int companyPib;
    private String description;
    @NotNull(message = "Price per hour is mandatory!")
    private BigDecimal pricePerHour;

    private EngagementDto engagementDto;
}
