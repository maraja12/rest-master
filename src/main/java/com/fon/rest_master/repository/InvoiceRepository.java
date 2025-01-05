package com.fon.rest_master.repository;

import com.fon.rest_master.domain.Invoice;
import com.fon.rest_master.domain.InvoiceId;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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

    //find name of projects for certain company invoice
    @Query("select c.name AS companyName, " +
            "       STRING_AGG(p.name, ',') AS projectNames  " +
            "from InvoiceItem as it " +
            "join it.invoice i " +
            "join i.company c " +
            "join it.engagement e " +
            "join e.project p " +
            "where i.id.id = :invoice_id and i.id.pib = :pib " +
            "GROUP BY c.name, c.pib, c.email")
    Object findProjectsForCertainCompanyInvoice(@Param("invoice_id") Long invoiceId, @Param("pib") int pib);

    //find employees id, name and surname for certain company invoice
    @Query("select c.name AS companyName, " +
            "string_agg(concat('id: ', e.id, ' ', e.name, ' ', e.surname), ', ') AS employees " +
            "from InvoiceItem as it " +
            "join it.invoice i " +
            "join i.company c " +
            "join it.engagement eng " +
            "join eng.employee e " +
            "where i.id.id = :invoice_id and i.id.pib = :pib " +
            "GROUP BY c.name")
    Object findEmployeesForCertainCompanyInvoice(@Param("pib") int pib, @Param("invoice_id") Long invoiceId);

    //find role of employee on project for certain company invoice (invoice item)
    @Query("select c.name AS companyName, " +
            "eng.id.projectId as project_id, " +
            "eng.id.employeeId as employee_id, " +
            "eng.role as employee_role " +
            "from InvoiceItem as it " +
            "join it.invoice i " +
            "join i.company c " +
            "join it.engagement eng " +
            "where it.id.invoiceId.id = :invoice_id and it.id.invoiceId.pib = :pib and it.id.seqNo = :seq_num")
    Object findEmployeeRoleOnProjectForCompanyInvoiceItem(
            @Param("pib") int pib, @Param("invoice_id") Long invoiceId, @Param("seq_num") int seqNum);
}
