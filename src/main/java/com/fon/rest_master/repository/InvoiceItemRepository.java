package com.fon.rest_master.repository;

import com.fon.rest_master.domain.InvoiceItem;
import com.fon.rest_master.domain.InvoiceItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, InvoiceItemId> {

    @Query("select coalesce(max(i.id.seqNo), 0) from InvoiceItem i where i.id.invoiceId.id = :id " +
            "and i.id.invoiceId.pib =:pib")
    Long findMaxSeqNoForInvoice(@Param("id") Long id, @Param("pib") int pib);
}
