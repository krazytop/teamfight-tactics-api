package com.krazytop.teamfighttactics.mapper;

import com.krazytop.teamfighttactics.entity.Match;
import com.krazytop.teamfighttactics.model.generated.MatchDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = DateMapper.class)
public interface MatchMapper {

    MatchDTO toDTO(Match board);
}
