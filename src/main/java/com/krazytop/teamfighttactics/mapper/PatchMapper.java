package com.krazytop.teamfighttactics.mapper;

import com.krazytop.teamfighttactics.model.generated.PatchDTO;
import com.krazytop.teamfighttactics.nomenclature.Patch;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PatchMapper {

    PatchDTO toDTO(Patch patch);
}
