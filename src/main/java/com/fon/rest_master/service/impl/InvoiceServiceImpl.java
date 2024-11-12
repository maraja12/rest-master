package com.fon.rest_master.service.impl;

import com.fon.rest_master.converter.impl.InvoiceConverter;
import com.fon.rest_master.domain.Company;
import com.fon.rest_master.domain.Invoice;
import com.fon.rest_master.domain.InvoiceId;
import com.fon.rest_master.dto.InvoiceDto;
import com.fon.rest_master.repository.CompanyRepository;
import com.fon.rest_master.repository.InvoiceRepository;
import com.fon.rest_master.service.InvoiceService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private EntityManager entityManager;
    private InvoiceRepository invoiceRepository;
    private InvoiceConverter invoiceConverter;
    private CompanyRepository companyRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                              InvoiceConverter invoiceConverter,
                              CompanyRepository companyRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceConverter = invoiceConverter;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<InvoiceDto> getAll() {
        return invoiceRepository.findAll().stream()
                .map(entity -> invoiceConverter.toDto(entity))
                .collect(Collectors.toList());
    }

    public void doesCompanyExist(int pib){
        Optional<Company> company = companyRepository.findById(pib);
        if(company.isEmpty()) {
            throw new EntityNotFoundException("Company with pib = " + pib + " is not found");
        }
    }

    @Override
    public InvoiceDto getById(Long id, int companyPib) throws EntityNotFoundException {
        doesCompanyExist(companyPib);
        InvoiceId invoiceId = new InvoiceId(id, companyPib);
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);
        if(invoiceOpt.isPresent()){
            Invoice invoice = invoiceOpt.get();
            return invoiceConverter.toDto(invoice);
        }
        else{
            throw new EntityNotFoundException("Invoice with id = " + id + " and company pib " +
                    companyPib + " is not found");
        }
    }

    private Long generateNewId() {
        return (Long) entityManager.createNativeQuery("SELECT NEXT VALUE FOR invoice_id_seq;")
                .getSingleResult();
    }

    @Override
    @Transactional
    public InvoiceDto save(InvoiceDto invoiceDto) {
        Optional<Company> companyOpt = companyRepository.findById(invoiceDto.getPib());
        if(companyOpt.isEmpty()) {
            throw new EntityNotFoundException("Company with pib = " + invoiceDto.getPib() + " is not found");
        }
        Company company = companyOpt.get();
        if(invoiceDto.getId() == 0){
            Long generatedId = generateNewId();
            invoiceDto.setId(generatedId);
        }
        Invoice invoice = invoiceConverter.toEntity(invoiceDto);
        invoice.setCompany(company);
        invoice = invoiceRepository.save(invoice);
        return invoiceConverter.toDto(invoice);
    }

    @Override
    public InvoiceDto update(InvoiceDto invoiceDto) throws EntityNotFoundException {
        doesCompanyExist(invoiceDto.getPib());
        InvoiceId invoiceId = new InvoiceId(invoiceDto.getId(), invoiceDto.getPib());
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);
        if(invoiceOpt.isPresent()){
            Invoice invoice = invoiceOpt.get();
            invoice.setIssueDate(invoiceDto.getIssueDate());
            invoice.setPaymentDate(invoiceDto.getPaymentDate());
            invoice.setStatus(invoiceDto.getStatus());
            invoice = invoiceRepository.save(invoice);
            return invoiceConverter.toDto(invoice);
        }
        else{
            throw new EntityNotFoundException("Invoice with id = " + invoiceId.getId() + " and company pib " +
                    invoiceDto.getPib() + " is not found");
        }
    }

    @Override
    public void delete(Long id, int companyPib) throws EntityNotFoundException {
        doesCompanyExist(companyPib);
        InvoiceId invoiceId = new InvoiceId(id, companyPib);
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);
        if(invoiceOpt.isPresent()){
            Invoice invoice = invoiceOpt.get();
            invoiceRepository.delete(invoice);
        }
        else{
            throw new EntityNotFoundException("Invoice with id = " + id + " and company pib " +
                    companyPib + " is not found");
        }
    }
}
