package com.fon.rest_master.controller;

import com.fon.rest_master.dto.InvoiceItemDto;
import com.fon.rest_master.service.InvoiceItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/invoice_item")
public class InvoiceItemController {

    private InvoiceItemService invoiceItemService;

    public InvoiceItemController(InvoiceItemService invoiceItemService) {
        this.invoiceItemService = invoiceItemService;
    }

    @GetMapping
    public ResponseEntity<List<InvoiceItemDto>> getAll(){
        List<InvoiceItemDto> invoiceItemDtos = invoiceItemService.getAll();
        return new ResponseEntity<>(invoiceItemDtos, HttpStatus.OK);
    }
    @GetMapping("/{seq-no}/{invoice-id}/{company-pib}")
    public ResponseEntity<InvoiceItemDto> getById
            (@PathVariable("seq-no") Long seqNo,
             @PathVariable("invoice-id") Long invoiceId,
             @PathVariable("company-pib") int companyPib){
        InvoiceItemDto invoiceItemDto = invoiceItemService.getById(seqNo, invoiceId, companyPib);
        return new ResponseEntity<>(invoiceItemDto, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<InvoiceItemDto> save(@RequestBody @Valid InvoiceItemDto invoiceItemDto){
        InvoiceItemDto savedInvoiceItemDto = invoiceItemService.save(invoiceItemDto);
        return new ResponseEntity<>(savedInvoiceItemDto, HttpStatus.CREATED);
    }
    @PutMapping
    public ResponseEntity<InvoiceItemDto> update(@RequestBody @Valid InvoiceItemDto invoiceItemDto){
        InvoiceItemDto updatedInvoiceItemDto = invoiceItemService.update(invoiceItemDto);
        return new ResponseEntity<>(updatedInvoiceItemDto, HttpStatus.OK);
    }
    @DeleteMapping("/{seq-no}/{invoice-id}/{company-pib}")
    public ResponseEntity<String> delete (@PathVariable("seq-no") Long seqNo,
                                          @PathVariable("invoice-id") Long invoiceId,
                                          @PathVariable("company-pib") int companyPib){
        invoiceItemService.delete(seqNo, invoiceId, companyPib);
        return new ResponseEntity<>("Invoice item is deleted.", HttpStatus.OK);
    }
}
