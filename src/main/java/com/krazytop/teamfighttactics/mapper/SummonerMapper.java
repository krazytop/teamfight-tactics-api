package com.krazytop.teamfighttactics.mapper;

import com.krazytop.teamfighttactics.entity.Summoner;
import com.krazytop.teamfighttactics.model.generated.SummonerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = DateMapper.class)
public interface SummonerMapper {

    SummonerDTO toDTO(Summoner summoner);
}
