package com.fon.rest_master.converter.impl;

import com.fon.rest_master.converter.DtoEntityConverter;
import com.fon.rest_master.domain.Invoice;
import com.fon.rest_master.domain.InvoiceId;
import com.fon.rest_master.dto.InvoiceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class InvoiceConverter implements DtoEntityConverter<InvoiceDto, Invoice> {

    @Autowired
    private CompanyConverter companyConverter;
    @Autowired
    private InvoiceItemConverter invoiceItemConverter;

    @Override
    public InvoiceDto toDto(Invoice invoice) {
        return InvoiceDto.builder()
                .id(invoice.getId().getId())
                .pib(invoice.getId().getPib())
                .issueDate(invoice.getIssueDate())
                .paymentDate(invoice.getPaymentDate())
                .status(invoice.getStatus())
//                .invoiceItems(
//                        invoice.getInvoiceItems().stream().map(entity ->
//                                invoiceItemConverter.toDto(entity)).collect(Collectors.toList())
//                )
                .build();
    }

    @Override
    public Invoice toEntity(InvoiceDto invoiceDto) {
        return Invoice.builder()
                .id(InvoiceId.builder()
                        .id(invoiceDto.getId())
                        .pib(invoiceDto.getPib())
                        .build())
                .issueDate(invoiceDto.getIssueDate())
                .paymentDate(invoiceDto.getPaymentDate())
                .status(invoiceDto.getStatus())
//                .invoiceItems(
//                invoiceDto.getInvoiceItems().stream().map(dto ->
//                        invoiceItemConverter.toEntity(dto)).collect(Collectors.toList())
//        )
                .build();
    }
}
