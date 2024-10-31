package com.fon.rest_master.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionDto implements Serializable {

    private Long id;
    @NotEmpty(message = "Name of profession is mandatory!")
    @Size(max = 30, message = "Maximum length of name is 30 characters!")
    private String name;

}
