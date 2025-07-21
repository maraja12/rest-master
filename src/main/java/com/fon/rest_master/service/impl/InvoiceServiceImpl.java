package com.fon.rest_master.service.impl;

import com.fon.rest_master.converter.impl.InvoiceConverter;
import com.fon.rest_master.converter.impl.InvoiceItemConverter;
import com.fon.rest_master.domain.*;
import com.fon.rest_master.dto.InvoiceDto;
import com.fon.rest_master.dto.InvoiceItemDto;
import com.fon.rest_master.repository.CompanyRepository;
import com.fon.rest_master.repository.EngagementRepository;
import com.fon.rest_master.repository.InvoiceItemRepository;
import com.fon.rest_master.repository.InvoiceRepository;
import com.fon.rest_master.service.InvoiceItemService;
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
    private InvoiceItemRepository invoiceItemRepository;
    private EngagementRepository engagementRepository;
    private InvoiceItemConverter invoiceItemConverter;

    public InvoiceServiceImpl(EntityManager entityManager, InvoiceRepository invoiceRepository,
                              InvoiceConverter invoiceConverter, CompanyRepository companyRepository,
                              InvoiceItemRepository invoiceItemRepository,
                              EngagementRepository engagementRepository,
                              InvoiceItemConverter invoiceItemConverter) {
        this.entityManager = entityManager;
        this.invoiceRepository = invoiceRepository;
        this.invoiceConverter = invoiceConverter;
        this.companyRepository = companyRepository;
        this.invoiceItemRepository = invoiceItemRepository;
        this.engagementRepository = engagementRepository;
        this.invoiceItemConverter = invoiceItemConverter;
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

    private Engagement getEngagement(InvoiceItem invoiceItem){
        EngagementId engagementId =
                new EngagementId(invoiceItem.getEngagement().getId().getProjectId(),
                        invoiceItem.getEngagement().getId().getEmployeeId(),
                        invoiceItem.getEngagement().getId().getMonth(),
                        invoiceItem.getEngagement().getId().getYear());
        Optional<Engagement> engagementOptional = engagementRepository.findById(engagementId);
        if(engagementOptional.isEmpty()){
            throw new EntityNotFoundException("Engagement for project ID: "
                    + invoiceItem.getEngagement().getId().getProjectId() + " and employee ID: " +
                    invoiceItem.getEngagement().getId().getEmployeeId() + " for month: " +
                    invoiceItem.getEngagement().getId().getMonth() + " and year: " +
                    invoiceItem.getEngagement().getId().getYear() + " does not exist!");
        }
        return engagementOptional.get();
    }

    @Override
    @Transactional
    public InvoiceDto save(InvoiceDto invoiceDto) {
        Invoice invoice = invoiceConverter.toEntity(invoiceDto);
        Optional<Company> companyOpt = companyRepository.findById(invoiceDto.getPib());
        if(companyOpt.isEmpty()) {
            throw new EntityNotFoundException("Company with pib = " + invoiceDto.getPib() + " is not found");
        }
        Company company = companyOpt.get();
        if(invoiceDto.getId() == 0){
            Long generatedId = generateNewId();
            invoiceDto.setId(generatedId);
        }
        invoice.setCompany(company);
        invoice = invoiceRepository.save(invoice);

        List<InvoiceItem> invoiceItems = invoiceDto.getInvoiceItems()
                .stream().map(dto -> invoiceItemConverter.toEntity(dto))
                .toList();
        for(InvoiceItem invoiceItem : invoiceItems){
            Optional<InvoiceItem> invoiceItemOpt = invoiceItemRepository.findById(invoiceItem.getId());
            if(invoiceItemOpt.isEmpty()){
                Engagement engagement = getEngagement(invoiceItem);
                invoiceItem.setEngagement(engagement);

                InvoiceItemId invoiceItemId = new InvoiceItemId(1L, invoice.getId());
                invoiceItem.setId(invoiceItemId);
                invoiceItem.setInvoice(invoice);
                invoiceItemRepository.save(invoiceItem);
            }
        }
//        invoice.setInvoiceItems(invoiceItems);
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

    @Override
    public List<Object> findInvoicesByCompanyPib(int companyPib) throws EntityNotFoundException{
        Optional<Company> companyOptional = companyRepository.findById(companyPib);
        if(companyOptional.isPresent()){
            return invoiceRepository.findInvoicesByCompanyPib(companyPib);
        }
        else{
            throw new EntityNotFoundException("Company with pib = " + companyPib + " is not found");
        }
    }

    @Override
    public List<Object> findUnpaidInvoicesByCompanyPib(int companyPib) throws EntityNotFoundException {
        Optional<Company> companyOptional = companyRepository.findById(companyPib);
        if(companyOptional.isPresent()){
            return invoiceRepository.findUnpaidInvoicesByCompanyPib(companyPib);
        }
        else{
            throw new EntityNotFoundException("Company with pib = " + companyPib + " is not found");
        }
    }

    @Override
    public Object getTotalDeptByCompanyPib(int companyPib) throws EntityNotFoundException {
        Optional<Company> companyOptional = companyRepository.findById(companyPib);
        if(companyOptional.isPresent()){
            return invoiceRepository.getTotalDeptByCompanyPib(companyPib);
        }
        else{
            throw new EntityNotFoundException("Company with pib = " + companyPib + " is not found");
        }
    }

    @Override
    public Object findProjectsForCertainCompanyInvoice(int companyPib, Long invoiceId) throws EntityNotFoundException {
        doesCompanyExist(companyPib);
        Object list =  invoiceRepository.findProjectsForCertainCompanyInvoice(invoiceId, companyPib);
        if(list == null){
            throw new EntityNotFoundException("Invoice with id = " + invoiceId +
                    " for certain company = " + companyPib + " is not found");
        }
        return list;
    }

    @Override
    public Object findEmployeesForCertainCompanyInvoice(int pib, Long invoiceId) throws EntityNotFoundException {
        doesCompanyExist(pib);
        Object list =  invoiceRepository.findEmployeesForCertainCompanyInvoice(pib, invoiceId);
        if(list == null){
            throw new EntityNotFoundException("Invoice with id = " + invoiceId +
                    " for certain company = " + pib + " is not found");
        }
        return list;
    }

    @Override
    public Object findEmployeeRoleOnProjectForCompanyInvoiceItem(int pib, Long invoiceId, int seqNum) throws EntityNotFoundException {
        doesCompanyExist(pib);
        Object list =  invoiceRepository.findEmployeeRoleOnProjectForCompanyInvoiceItem(pib, invoiceId, seqNum);
        if(list == null){
            throw new EntityNotFoundException("Invoice with id = " + invoiceId +
                    " or seqNum = "+ seqNum + " for certain company = " + pib + " is not found");
        }
        return list;
    }

}
