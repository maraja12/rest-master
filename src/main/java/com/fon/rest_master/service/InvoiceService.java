package com.fon.rest_master.service;

import com.fon.rest_master.dto.InvoiceDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface InvoiceService {

    List<InvoiceDto> getAll();
    InvoiceDto getById(Long id, int companyPib) throws EntityNotFoundException;
    InvoiceDto save(InvoiceDto invoiceDto);
    InvoiceDto update(InvoiceDto invoiceDto) throws EntityNotFoundException;
    void delete(Long id, int companyPib) throws EntityNotFoundException;
}
