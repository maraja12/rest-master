package com.fon.rest_master.repository;

import com.fon.rest_master.domain.Invoice;
import com.fon.rest_master.domain.InvoiceId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, InvoiceId> {
}
