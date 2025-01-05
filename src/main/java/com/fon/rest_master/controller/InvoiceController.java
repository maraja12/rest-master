package com.fon.rest_master.controller;

import com.fon.rest_master.dto.InvoiceDto;
import com.fon.rest_master.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<List<InvoiceDto>> getAll(){
        List<InvoiceDto> invoiceDtos = invoiceService.getAll();
        return new ResponseEntity<>(invoiceDtos, HttpStatus.OK);
    }
    @GetMapping("/{id}/{company-pib}")
    public ResponseEntity<InvoiceDto> getById(
            @PathVariable("id") Long id,
            @PathVariable("company-pib") int companyPib){
        InvoiceDto invoiceDto = invoiceService.getById(id, companyPib);
        return new ResponseEntity<>(invoiceDto, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<InvoiceDto> save(@RequestBody @Valid InvoiceDto invoiceDto){
        InvoiceDto invoiceDtoSaved = invoiceService.save(invoiceDto);
        return new ResponseEntity<>(invoiceDtoSaved, HttpStatus.CREATED);
    }
    @PutMapping
    public ResponseEntity<InvoiceDto> update(@RequestBody @Valid InvoiceDto invoiceDto){
        InvoiceDto invoiceDtoUpdated = invoiceService.update(invoiceDto);
        return new ResponseEntity<>(invoiceDtoUpdated, HttpStatus.OK);
    }
    @DeleteMapping("/{id}/{company-pib}")
    public ResponseEntity<String> delete(
            @PathVariable("id") Long id,
            @PathVariable("company-pib") int companyPib){
        invoiceService.delete(id, companyPib);
        return new ResponseEntity<>("Invoice with id = " + id + " and company pib " +
                companyPib + " is deleted.", HttpStatus.OK);
    }
    @GetMapping("/company/{company-pib}")
    public ResponseEntity<List<Object>> getAllInvoicesByCompanyPib(@PathVariable("company-pib") int companyPib){
        List<Object> details = invoiceService.findInvoicesByCompanyPib(companyPib);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }
    @GetMapping("/unpaid/company/{company-pib}")
    public ResponseEntity<List<Object>> getAllUnpaidInvoicesByCompanyPib(@PathVariable("company-pib") int companyPib){
        List<Object> details = invoiceService.findUnpaidInvoicesByCompanyPib(companyPib);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }
    @GetMapping("/total-dept/{company-pib}")
    public ResponseEntity<Object> getTotalDeptByCompanyPib(@PathVariable("company-pib") int companyPib){
        Object details = invoiceService.getTotalDeptByCompanyPib(companyPib);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }
    @GetMapping("/invoice-projects/{company-pib}/{invoice-id}")
    public ResponseEntity<Object> findProjectsForCertainCompanyInvoice(
            @PathVariable("company-pib") int pib,
            @PathVariable("invoice-id") Long invoiceId
    ){
        Object list = invoiceService.findProjectsForCertainCompanyInvoice(pib, invoiceId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
