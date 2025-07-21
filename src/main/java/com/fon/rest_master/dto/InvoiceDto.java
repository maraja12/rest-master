package com.fon.rest_master.dto;

import com.fon.rest_master.domain.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDto implements Serializable {

    @NotNull(message = "Invoice id is mandatory!")
    private Long id;
    @NotNull(message = "Company pib is mandatory!")
    private int pib;
    @NotNull(message = "Issue date is mandatory!")
    private LocalDate issueDate;
    private LocalDate paymentDate;
    @NotNull(message = "Status of invoice is mandatory!")
    private Status status;
    private List<InvoiceItemDto> invoiceItems;
}
