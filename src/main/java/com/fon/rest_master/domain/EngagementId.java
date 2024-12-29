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
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EngagementId implements Serializable {

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "employee_id")
    private Long employeeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "month")
    private Month month;

    @Column(name = "year")
    private int year;
}
