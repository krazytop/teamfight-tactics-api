package com.krazytop.teamfighttactics.mapper;

import com.krazytop.teamfighttactics.entity.Metadata;
import com.krazytop.teamfighttactics.model.generated.MetadataDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MetadataMapper {

    @Mapping(source = "currentSet", target = "currentSeasonOrSet")
    MetadataDTO toDTO(Metadata metadata);
}
