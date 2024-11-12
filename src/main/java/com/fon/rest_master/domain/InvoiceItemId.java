package com.fon.rest_master.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class InvoiceItemId implements Serializable {

    @Column(name = "seq_no")
    private Long seqNo;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "invoice_id")),
            @AttributeOverride(name = "pib", column = @Column(name = "invoice_company_pib"))
    })
    private InvoiceId invoiceId;
}
