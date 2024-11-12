package com.fon.rest_master.service.impl;

import com.fon.rest_master.converter.impl.InvoiceItemConverter;
import com.fon.rest_master.domain.Invoice;
import com.fon.rest_master.domain.InvoiceId;
import com.fon.rest_master.domain.InvoiceItem;
import com.fon.rest_master.domain.InvoiceItemId;
import com.fon.rest_master.dto.InvoiceItemDto;
import com.fon.rest_master.repository.InvoiceItemRepository;
import com.fon.rest_master.repository.InvoiceRepository;
import com.fon.rest_master.service.InvoiceItemService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceItemServiceImpl implements InvoiceItemService {

    private InvoiceItemRepository invoiceItemRepository;
    private InvoiceItemConverter invoiceItemConverter;
    private InvoiceRepository invoiceRepository;

    public InvoiceItemServiceImpl(InvoiceItemRepository invoiceItemRepository,
                                  InvoiceItemConverter invoiceItemConverter,
                                  InvoiceRepository invoiceRepository) {
        this.invoiceItemRepository = invoiceItemRepository;
        this.invoiceItemConverter = invoiceItemConverter;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public List<InvoiceItemDto> getAll() {
        return invoiceItemRepository.findAll().stream()
                .map(entity -> invoiceItemConverter.toDto(entity))
                .collect(Collectors.toList());
    }

    public void doesInvoiceExist(InvoiceId invoiceId){
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);
        if(invoiceOpt.isEmpty()){
            throw new EntityNotFoundException("Invoice with id = " + invoiceId.getId()
                    + " and company pib = "
                    + invoiceId.getPib() + " is not found");
        }
    }

    @Override
    public InvoiceItemDto getById(Long seqNum, Long invoiceId, int companyPib) throws EntityNotFoundException {
        InvoiceId invId = new InvoiceId(invoiceId,companyPib);
        doesInvoiceExist(invId);
        InvoiceItemId invoiceItemId = new InvoiceItemId(seqNum, invId);
        Optional<InvoiceItem> invoiceItemOpt = invoiceItemRepository.findById(invoiceItemId);
        if(invoiceItemOpt.isEmpty()){
            throw new EntityNotFoundException("Invoice item with sequence number = " + seqNum + " and invoice id = "
                    + invId + " is not found");
        }
        InvoiceItem invoiceItem = invoiceItemOpt.get();
        return invoiceItemConverter.toDto(invoiceItem);
    }

    @Override
    public InvoiceItemDto save(InvoiceItemDto invoiceItemDto) {
        InvoiceId invoiceId = new InvoiceId(invoiceItemDto.getInvoiceId(),
                invoiceItemDto.getCompanyPib());
        Optional<Invoice> invoiceOpt = invoiceRepository.findById(invoiceId);
        if(invoiceOpt.isEmpty()){
            throw new EntityNotFoundException("Invoice with id = " + invoiceItemDto.getInvoiceId()
                    + " and company pib = "
                    + invoiceItemDto.getCompanyPib() + " is not found");
        }
        Invoice invoice = invoiceOpt.get();
        Long seqNo = invoiceItemRepository.findMaxSeqNoForInvoice(invoiceItemDto.getInvoiceId(),
                invoiceItemDto.getCompanyPib()) + 1;
        InvoiceItemId invoiceItemId = new InvoiceItemId(seqNo, invoiceId);
        InvoiceItem invoiceItem = invoiceItemConverter.toEntity(invoiceItemDto);
        invoiceItem.setId(invoiceItemId);
        invoiceItem.setInvoice(invoice);
        invoiceItem = invoiceItemRepository.save(invoiceItem);
        return invoiceItemConverter.toDto(invoiceItem);
    }

    @Override
    public InvoiceItemDto update(InvoiceItemDto invoiceItemDto) throws EntityNotFoundException {
        InvoiceId invoiceId = new InvoiceId(invoiceItemDto.getInvoiceId(),
                invoiceItemDto.getCompanyPib());
        doesInvoiceExist(invoiceId);
        InvoiceItemId invoiceItemId = new InvoiceItemId(invoiceItemDto.getSeqNum(), invoiceId);
        Optional<InvoiceItem> invoiceItemOpt = invoiceItemRepository.findById(invoiceItemId);
        if(invoiceItemOpt.isEmpty()){
            throw new EntityNotFoundException("Invoice item with sequence number = " + invoiceItemDto.getSeqNum()
                    + " and invoice id = "
                    + invoiceId + " is not found");
        }
        InvoiceItem invoiceItem = invoiceItemOpt.get();
        invoiceItem.setDescription(invoiceItemDto.getDescription());
        invoiceItem.setPricePerHour(invoiceItemDto.getPricePerHour());
        invoiceItem = invoiceItemRepository.save(invoiceItem);
        return invoiceItemConverter.toDto(invoiceItem);
    }

    @Override
    public void delete(Long seqNum, Long invoiceId, int companyPib) throws EntityNotFoundException {
        InvoiceId invId = new InvoiceId(invoiceId,companyPib);
        doesInvoiceExist(invId);
        InvoiceItemId invoiceItemId = new InvoiceItemId(seqNum, invId);
        Optional<InvoiceItem> invoiceItemOpt = invoiceItemRepository.findById(invoiceItemId);
        if(invoiceItemOpt.isEmpty()){
            throw new EntityNotFoundException("Invoice item with sequence number = " + seqNum + " and invoice id = "
                    + invId + " is not found");
        }
        InvoiceItem invoiceItem = invoiceItemOpt.get();
        invoiceItemRepository.delete(invoiceItem);
    }
}
