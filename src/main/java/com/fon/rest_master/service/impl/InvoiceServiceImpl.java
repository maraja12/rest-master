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

    public Company findCompanyByPib(int pib){
        Optional<Company> company = companyRepository.findById(pib);
        if(company.isPresent()){
            return company.get();
        }else{
            throw new EntityNotFoundException("Company with pib = " + pib + " is not found");
        }
    }

    @Override
    public InvoiceDto getById(Long id, int company_pib) throws EntityNotFoundException {
        Company company = findCompanyByPib(company_pib);
        InvoiceId invoiceId = new InvoiceId(id, company);
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);
        if(invoiceOpt.isPresent()){
            Invoice invoice = invoiceOpt.get();
            return invoiceConverter.toDto(invoice);
        }
        else{
            throw new EntityNotFoundException("Invoice with id = " + id + " and company pib " +
                    company_pib + " is not found");
        }
    }

    private Long generateNewId() {
        return (Long) entityManager.createNativeQuery("SELECT NEXT VALUE FOR invoice_id_seq;")
                .getSingleResult();
    }

    @Override
    @Transactional
    public InvoiceDto save(InvoiceDto invoiceDto) {
        Optional<Company> companyOptional = companyRepository.findById(invoiceDto.getCompanyDto().getPib());
        if(companyOptional.isEmpty()){
            throw new EntityNotFoundException("Company with pib = " + invoiceDto.getCompanyDto().getPib() +
                    " is not found");
        }
        if(invoiceDto.getId() == 0){
            Long generatedId = generateNewId();
            invoiceDto.setId(generatedId);
        }
        Invoice invoice = invoiceConverter.toEntity(invoiceDto);
        invoice = invoiceRepository.save(invoice);
        return invoiceConverter.toDto(invoice);
    }

    @Override
    public InvoiceDto update(InvoiceDto invoiceDto) throws EntityNotFoundException {
        Company company = findCompanyByPib(invoiceDto.getCompanyDto().getPib());
        InvoiceId invoiceId = new InvoiceId(invoiceDto.getId(), company);
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
                    invoiceDto.getCompanyDto().getPib() + " is not found");
        }
    }

    @Override
    public void delete(Long id, int company_pib) throws EntityNotFoundException {
        Company company = findCompanyByPib(company_pib);
        InvoiceId invoiceId = new InvoiceId(id, company);
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);
        if(invoiceOpt.isPresent()){
            Invoice invoice = invoiceOpt.get();
            invoiceRepository.delete(invoice);
        }
        else{
            throw new EntityNotFoundException("Invoice with id = " + id + " and company pib " +
                    company_pib + " is not found");
        }
    }
}
