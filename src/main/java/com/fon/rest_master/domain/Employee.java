package com.fon.rest_master.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @NotEmpty(message = "Employee name is mandatory!")
    @Size(max = 30, message = "Maximum length of name is 30 characters!")
    @Pattern(regexp = "^[A-Ž].*", message = "Name has to start with an uppercase letter from A to Ž!")
    @Column(name = "name")
    private String name;
    @NotEmpty(message = "Employee surname is mandatory!")
    @Size(max = 30, message = "Maximum length of surname is 30 characters!")
    @Pattern(regexp = "^[A-Ž].*", message = "Surname has to start with an uppercase letter from A to Ž!")
    @Column(name = "surname")
    private String surname;
    @NotNull(message = "Employee birthday is mandatory!")
    @Column(name = "birthday")
    private LocalDate birthday;
    @NotNull(message = "Employee address is mandatory!")
    @Size(max = 50, message = "Maximum length of address is 50 characters!")
    @Pattern(regexp = "^[A-Ž].*", message = "Address has to start with an uppercase letter from A to Ž!")
    @Column(name = "address")
    private String address;
    @NotNull(message = "Employee email is mandatory!")
    @Size(max = 30, message = "Maximum length of email is 30 characters!")
    @Pattern(regexp = "^.*@.*\\..*$", message = "Email has to match standard format!")
    @Column(name = "email", unique = true)
    private String email;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profession_id", nullable = false)
    private Profession profession;
}
