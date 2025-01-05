package com.fon.rest_master.service;

import com.fon.rest_master.dto.InvoiceDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceService {

    List<InvoiceDto> getAll();
    InvoiceDto getById(Long id, int companyPib) throws EntityNotFoundException;
    InvoiceDto save(InvoiceDto invoiceDto);
    InvoiceDto update(InvoiceDto invoiceDto) throws EntityNotFoundException;
    void delete(Long id, int companyPib) throws EntityNotFoundException;
    List<Object> findInvoicesByCompanyPib(int companyPib) throws EntityNotFoundException;
    List<Object> findUnpaidInvoicesByCompanyPib(int companyPib) throws EntityNotFoundException;
    Object getTotalDeptByCompanyPib(int companyPib) throws EntityNotFoundException;
    Object findProjectsForCertainCompanyInvoice(int companyPib, Long invoiceId) throws EntityNotFoundException;
    Object findEmployeesForCertainCompanyInvoice(int pib, Long invoiceId) throws EntityNotFoundException;
    Object findEmployeeRoleOnProjectForCompanyInvoiceItem(int pib, Long invoiceId, int seqNum) throws EntityNotFoundException;
}
