package com.fon.rest_master.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invoice")
public class Invoice {

    @NotNull
    @EmbeddedId
    private InvoiceId id;

    @MapsId("pib")
    @ManyToOne
    private Company company;

    @NotNull(message = "Issue date is mandatory!")
    @PastOrPresent(message = "Issue date must be a current or past date.")
    @Column(name = "issue_date")
    private LocalDate issueDate;
    @Column(name = "payment_date")
    private LocalDate paymentDate;
    @NotNull(message = "Status of invoice is mandatory!")
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}
