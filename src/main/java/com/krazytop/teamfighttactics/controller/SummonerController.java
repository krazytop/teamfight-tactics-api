package com.krazytop.teamfighttactics.controller;

import com.krazytop.teamfighttactics.api.generated.SummonerApi;
import com.krazytop.teamfighttactics.model.generated.SummonerDTO;
import com.krazytop.teamfighttactics.service.SummonerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
public class SummonerController implements SummonerApi {

    private final SummonerService summonerService;

    @Override
    public ResponseEntity<SummonerDTO> getSummonerByTagAndName(String tag, String name) {
        log.info("Retrieving summoner with name and tag");
        SummonerDTO summoner = summonerService.getSummonerDTO(tag, name);
        log.info("Summoner retrieved");
        return new ResponseEntity<>(summoner, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SummonerDTO> getSummonerByPuuid(String puuid) {
        log.info("Retrieving summoner with puuid");
        SummonerDTO summoner = summonerService.getSummonerDTO(puuid);
        log.info("Summoner retrieved");
        return new ResponseEntity<>(summoner, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<SummonerDTO> updateSummoner(String puuid) {
        log.info("Updating summoner");
        SummonerDTO summoner = summonerService.updateSummoner(puuid);
        log.info("Summoner updated");
        return new ResponseEntity<>(summoner, HttpStatus.OK);
    }
}