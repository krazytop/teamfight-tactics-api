package com.krazytop.teamfighttactics.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.teamfighttactics.entity.Rank;
import com.krazytop.teamfighttactics.entity.RankInformations;
import com.krazytop.teamfighttactics.exception.ApiErrorEnum;
import com.krazytop.teamfighttactics.exception.CustomException;
import com.krazytop.teamfighttactics.mapper.RankMapper;
import com.krazytop.teamfighttactics.model.generated.RankDTO;
import com.krazytop.teamfighttactics.repository.RankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RankService {

    @Value("${teamfight-tactics.api-key:api-key}")
    private String apiKey;
    private final RankRepository rankRepository;
    private final MetadataService metadataService;
    private final SummonerService summonerService;
    private final RankMapper rankMapper;

    public Optional<Rank> getRanks(String puuid) {
        return rankRepository.findByPuuid(puuid);
    }

    public RankDTO getRanksDTO(String puuid) {
        return rankMapper.toDTO(getRanks(puuid).orElseThrow(() -> new CustomException(ApiErrorEnum.SUMMONER_NEED_IMPORT_FIRST)));
    }

    public void updateRanks(String puuid) {
        try {
            int currentSeasonOrSet = metadataService.getMetadataDTO().getCurrentSeasonOrSet();
            String region = summonerService.getLocalSummoner(puuid).orElseThrow(() -> new CustomException(ApiErrorEnum.SUMMONER_NEED_IMPORT_FIRST)).getRegion();
            String url = String.format("https://%s.api.riotgames.com/tft/league/v1/by-puuid/%s?api_key=%s", region, puuid, apiKey);
            ObjectMapper mapper = new ObjectMapper();
            List<JsonNode> nodes = mapper.convertValue(mapper.readTree(new URI(url).toURL()), new TypeReference<>() {});
            Rank rank = getRanks(puuid).orElse(new Rank(puuid));
            for (JsonNode node : nodes) {
                RankInformations rankInformations = mapper.convertValue(node, RankInformations.class);
                rankInformations.setDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
                rank.joinRanks(List.of(rankInformations), currentSeasonOrSet, node.get("queueType").asText());//TODO je ne sais plus quoi, mettre la fonction dans la class rank
            }
            rankRepository.save(rank);
        } catch (URISyntaxException | IOException ex) {
            throw new CustomException(ApiErrorEnum.RANK_UPDATE_ERROR, ex);
        }
    }
}
