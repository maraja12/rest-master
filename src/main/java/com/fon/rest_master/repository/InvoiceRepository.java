package com.fon.rest_master.repository;

import com.fon.rest_master.domain.Invoice;
import com.fon.rest_master.domain.InvoiceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, InvoiceId> {

//    find all invoices for particular company and show company details like name, pib and email
    @Query("select i.id.id, i.issueDate, i.paymentDate, i.status, c.name, c.pib, c.email from Invoice i" +
            " join Company c on c.pib = i.id.pib where c.pib = :pib")
    List<Object> findInvoicesByCompanyPib(@Param("pib") int companyPib);

//    find all UNPAID invoices for particular company and show company details like name, pib and email
    @Query("select i.id.id, i.issueDate, i.paymentDate, i.status, c.name, c.pib, c.email from Invoice i" +
            " join Company c on c.pib = i.id.pib where c.pib = :pib and i.status = 'UNPAID'")
    List<Object> findUnpaidInvoicesByCompanyPib(@Param("pib") int companyPib);

//    find total debt for particular company
    @Query("select c.name, c.pib, c.email, sum(it.pricePerHour) as total_dept " +
            "from InvoiceItem as it " +
            "join it.invoice i " +
            "join i.company c " +
            "where i.status = 'UNPAID' and c.pib = :pib " +
            "GROUP BY c.name, c.pib, c.email")
    Object getTotalDeptByCompanyPib(@Param("pib") int companyPib);
}
