package com.krazytop.teamfighttactics.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.teamfighttactics.entity.Match;
import com.krazytop.teamfighttactics.exception.ApiErrorEnum;
import com.krazytop.teamfighttactics.exception.CustomException;
import com.krazytop.teamfighttactics.mapper.MatchMapper;
import com.krazytop.teamfighttactics.model.generated.MatchDTO;
import com.krazytop.teamfighttactics.nomenclature.QueueEnum;
import com.krazytop.teamfighttactics.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MatchService {

    @Value("${spring.data.web.pageable.default-page-size:5}")
    private int pageSize;
    @Value("${teamfight-tactics.api-key:api-key}")
    private String apiKey;
    private final MatchRepository matchRepository;
    private final SummonerService summonerService;
    private final MatchMapper matchMapper;

    public List<MatchDTO> getMatches(String puuid, Integer pageNb, String queue, Integer set) {
        return getMatches(puuid, pageNb, QueueEnum.fromName(queue), set).stream().map(matchMapper::toDTO).toList();
    }

    public Integer getMatchesCount(String puuid, String queue, Integer set) {
        return getMatchesCount(puuid, QueueEnum.fromName(queue), set);
    }

    public void updateMatches(String puuid) {
        try {
            boolean moreMatchToRecovered = true;
            int firstIndex = 0;
            while (moreMatchToRecovered) {// TODO faire un appel pour chaque region (europe, americas, asia, sea)
                String url = String.format("https://europe.api.riotgames.com/tft/match/v1/matches/by-puuid/%s/ids?start=%d&count=%d&api_key=%s", puuid, firstIndex, 100, apiKey);
                ObjectMapper mapper = new ObjectMapper();
                List<String> matchIds = mapper.convertValue(mapper.readTree(new URI(url).toURL()), new TypeReference<>() {});
                for (String matchId : matchIds) {
                    Optional<Match> existingMatch = this.matchRepository.findFirstById(matchId);
                    if (existingMatch.isEmpty()) {
                        String stringUrl = String.format("https://europe.api.riotgames.com/tft/match/v1/matches/%s?api_key=%s", matchId, apiKey);
                        JsonNode node = mapper.readTree(new URI(stringUrl).toURL());
                        Match match = mapper.convertValue(node.get("info"), Match.class);
                        match.setId(node.get("metadata").get("match_id").asText());
                        saveMatch(match, puuid);
                        Thread.sleep(2000);
                    } else if (!existingMatch.get().getOwners().contains(puuid)) {
                        saveMatch(existingMatch.get(), puuid);
                    } else {
                        moreMatchToRecovered = false;
                        break;
                    }
                }
                Thread.sleep(2000);
                firstIndex += 100;
            }
        } catch (IOException | URISyntaxException | InterruptedException ex) {
            throw new CustomException(ApiErrorEnum.MATCH_UPDATE_ERROR, ex);
        }
    }

    private void saveMatch(Match match, String puuid) {
        match.getOwners().add(puuid);
        log.info("Saving match : {}", match.getId());
        matchRepository.save(match);
        summonerService.updateSpentTimeAndPlayedSeasonsOrSets(puuid, match.getDuration(), match.getSet());
    }

    public List<Match> getMatches(String puuid, Integer pageNb, QueueEnum queue, Integer set) {
        PageRequest pageRequest = PageRequest.of(pageNb, pageSize);
        if (queue == QueueEnum.ALL_QUEUES) {
            if (set == -1) {
                return this.matchRepository.findAll(puuid, pageRequest).getContent();
            } else {
                return this.matchRepository.findAllBySet(puuid, set, pageRequest).getContent();
            }
        } else {
            if (set == -1) {
                return this.matchRepository.findAllByQueue(puuid, queue.getIds(), pageRequest).getContent();
            } else {
                return this.matchRepository.findAllByQueueAndBySet(puuid, queue.getIds(), set, pageRequest).getContent();
            }
        }
    }

    public Integer getMatchesCount(String puuid, QueueEnum queue, Integer set) {
        if (queue == QueueEnum.ALL_QUEUES) {
            if (set == -1) {
                return this.matchRepository.countAll(puuid);
            } else {
                return this.matchRepository.countAllBySet(puuid, set);
            }
        } else {
            if (set == -1) {
                return this.matchRepository.countAllByQueue(puuid, queue.getIds());
            } else {
                return this.matchRepository.countAllByQueueAndBySet(puuid, queue.getIds(), set);
            }
        }
    }

}
