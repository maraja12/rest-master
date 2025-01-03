package com.fon.rest_master.dto;

import com.fon.rest_master.domain.Month;
import com.fon.rest_master.domain.Role;
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
public class EngagementDto implements Serializable {

    @NotNull(message = "Project ID is mandatory!")
    private Long projectId;
    @NotNull(message = "Employee ID is mandatory!")
    private Long employeeId;
    @NotNull(message = "Month is mandatory!")
    private Month month;
    @NotNull(message = "Year is mandatory!")
    private int year;
    @NotNull(message = "Employee role is mandatory!")
    private Role role;
    @NotNull(message = "Number of hours is mandatory!")
    private int numOfHours;
}
