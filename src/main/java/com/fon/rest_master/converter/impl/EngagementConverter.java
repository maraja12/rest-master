package com.fon.rest_master.converter.impl;

import com.fon.rest_master.converter.DtoEntityConverter;
import com.fon.rest_master.domain.Engagement;
import com.fon.rest_master.domain.EngagementId;
import com.fon.rest_master.dto.EngagementDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EngagementConverter implements DtoEntityConverter<EngagementDto, Engagement> {


    @Override
    public EngagementDto toDto(Engagement engagement) {
        return EngagementDto.builder()
                .projectId(engagement.getId().getProjectId())
                .employeeId(engagement.getId().getEmployeeId())
                .month(engagement.getId().getMonth())
                .year(engagement.getId().getYear())
                .role(engagement.getRole())
                .numOfHours(engagement.getNumOfHours())
                .build();
    }

    @Override
    public Engagement toEntity(EngagementDto engagementDto) {
        return Engagement.builder()
                .id(EngagementId.builder()
                        .projectId(engagementDto.getProjectId())
                        .employeeId(engagementDto.getEmployeeId())
                        .month(engagementDto.getMonth())
                        .year(engagementDto.getYear())
                        .build())
                .role(engagementDto.getRole())
                .numOfHours(engagementDto.getNumOfHours())
                .build();
    }
}
