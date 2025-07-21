package com.fon.rest_master.converter.impl;

import com.fon.rest_master.converter.DtoEntityConverter;
import com.fon.rest_master.domain.Place;
import com.fon.rest_master.dto.PlaceDto;
import org.springframework.stereotype.Component;

@Component
public class PlaceConverter implements DtoEntityConverter<PlaceDto, Place> {

    @Override
    public PlaceDto toDto(Place place) {
        return PlaceDto.builder()
                .zipCode(place.getZipCode())
                .name(place.getName())
                .build();
    }

    @Override
    public Place toEntity(PlaceDto placeDto) {
        return Place.builder()
                .zipCode(placeDto.getZipCode())
                .name(placeDto.getName())
                .build();
    }
}
