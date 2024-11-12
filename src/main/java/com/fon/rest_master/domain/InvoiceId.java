package com.fon.rest_master.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class InvoiceId implements Serializable {

    @Column(name = "id")
    private Long id;
    @Column(name = "company_pib")
    private int pib;
}
