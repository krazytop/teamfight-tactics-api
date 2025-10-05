package com.krazytop.teamfighttactics.mapper;

import com.krazytop.teamfighttactics.entity.Rank;
import com.krazytop.teamfighttactics.model.generated.RankDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = DateMapper.class)
public interface RankMapper {

    RankDTO toDTO(Rank rank);
}
