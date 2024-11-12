package com.fon.rest_master.service;

import com.fon.rest_master.dto.InvoiceItemDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface InvoiceItemService {

    List<InvoiceItemDto> getAll();
    InvoiceItemDto getById(Long seqNum, Long invoiceId, int companyPib) throws EntityNotFoundException;
    InvoiceItemDto save(InvoiceItemDto invoiceItemDto);
    InvoiceItemDto update(InvoiceItemDto invoiceItemDto) throws EntityNotFoundException;
    void delete(Long seqNum, Long invoiceId, int companyPib) throws EntityNotFoundException;
}
