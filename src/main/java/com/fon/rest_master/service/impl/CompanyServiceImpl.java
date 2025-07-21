package com.fon.rest_master.service.impl;

import com.fon.rest_master.converter.impl.CompanyConverter;
import com.fon.rest_master.converter.impl.PlaceConverter;
import com.fon.rest_master.domain.Company;
import com.fon.rest_master.domain.Place;
import com.fon.rest_master.dto.CompanyDto;
import com.fon.rest_master.repository.CompanyRepository;
import com.fon.rest_master.repository.PlaceRepository;
import com.fon.rest_master.service.CompanyService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;
    private CompanyConverter companyConverter;
    private PlaceRepository placeRepository;
    private PlaceConverter placeConverter;

    public CompanyServiceImpl(CompanyRepository companyRepository, CompanyConverter companyConverter,
                              PlaceRepository placeRepository, PlaceConverter placeConverter) {
        this.companyRepository = companyRepository;
        this.companyConverter = companyConverter;
        this.placeRepository = placeRepository;
        this.placeConverter = placeConverter;
    }

    @Override
    public List<CompanyDto> getAll() {
        return companyRepository.findAll().stream()
                .map(entity -> companyConverter.toDto(entity))
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDto getById(int id) throws EntityNotFoundException {
        Optional<Company> companyOpt = companyRepository.findById(id);
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();
            return companyConverter.toDto(company);
        }
        else{
            throw new EntityNotFoundException("Company with id = " + id + " is not found");
        }
    }

    @Override
    public CompanyDto save(CompanyDto companyDto) {
        Optional<Place> placeOptional = placeRepository.findById(companyDto.getPlaceDto().getZipCode());
        if(placeOptional.isPresent()){
            Company company = companyConverter.toEntity(companyDto);
            company = companyRepository.save(company);
            return companyConverter.toDto(company);
        }
        throw new EntityNotFoundException("Place with zip_code = " + companyDto.getPlaceDto().getZipCode() + " is not found");
    }

    @Override
    public CompanyDto update(CompanyDto companyDto) throws EntityNotFoundException {
        Optional<Company> companyOptional = companyRepository.findById(companyDto.getPib());
        Optional<Place> placeOptional = placeRepository.findById(companyDto.getPlaceDto().getZipCode());
        if (companyOptional.isPresent()) {
            if(placeOptional.isPresent()) {
                Company company = companyOptional.get();
                company.setPib(companyDto.getPib());
                company.setName(companyDto.getName());
                company.setAddress(companyDto.getAddress());
                company.setEmail(companyDto.getEmail());
                company = companyRepository.save(company);
                return companyConverter.toDto(company);
            }
            throw new EntityNotFoundException("Place with zip_code = " + companyDto.getPlaceDto().getZipCode() + " is not found");
        }
        else{
            throw new EntityNotFoundException("Company with pib = " + companyDto.getPib() + " is not found");
        }
    }

    @Override
    public void delete(int id) throws EntityNotFoundException {
        Optional<Company> companyOpt = companyRepository.findById(id);
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();
            companyRepository.delete(company);
        }
        else{
            throw new EntityNotFoundException("Company with id = " + id + " is not found");
        }
    }
}
