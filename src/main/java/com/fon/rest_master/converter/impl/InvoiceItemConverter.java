package com.fon.rest_master.converter.impl;

import com.fon.rest_master.converter.DtoEntityConverter;
import com.fon.rest_master.domain.InvoiceId;
import com.fon.rest_master.domain.InvoiceItem;
import com.fon.rest_master.domain.InvoiceItemId;
import com.fon.rest_master.dto.InvoiceItemDto;
import org.springframework.stereotype.Component;

@Component
public class InvoiceItemConverter implements DtoEntityConverter<InvoiceItemDto, InvoiceItem> {
    @Override
    public InvoiceItemDto toDto(InvoiceItem invoiceItem) {
        return InvoiceItemDto.builder()
                .seqNum(invoiceItem.getId().getSeqNo())
                .invoiceId(invoiceItem.getId().getInvoiceId().getId())
                .companyPib(invoiceItem.getId().getInvoiceId().getPib())
                .description(invoiceItem.getDescription())
                .pricePerHour(invoiceItem.getPricePerHour())
                .build();
    }

    @Override
    public InvoiceItem toEntity(InvoiceItemDto invoiceItemDto) {
        return InvoiceItem.builder()
                .id(InvoiceItemId.builder()
                        .seqNo(invoiceItemDto.getSeqNum())
                        .invoiceId(InvoiceId.builder()
                                .id(invoiceItemDto.getInvoiceId())
                                .pib(invoiceItemDto.getCompanyPib())
                                .build())
                        .build())
                .description(invoiceItemDto.getDescription())
                .pricePerHour(invoiceItemDto.getPricePerHour())
                .build();
    }
}
