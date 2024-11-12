package com.fon.rest_master.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invoice_item")
public class InvoiceItem {

    @NotNull
    @EmbeddedId
    private InvoiceItemId id;

    @MapsId("invoiceId")
    @ManyToOne
    private Invoice invoice;

    @Column(name = "description")
    private String description;
    @NotNull(message = "Price per hour is mandatory!")
    @Column(name = "price_per_hour", precision = 18, scale = 2)
    private BigDecimal pricePerHour;
}
