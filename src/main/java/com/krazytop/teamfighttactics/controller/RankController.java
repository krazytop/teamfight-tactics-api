package com.krazytop.teamfighttactics.controller;

import com.krazytop.teamfighttactics.api.generated.RankApi;
import com.krazytop.teamfighttactics.model.generated.RankDTO;
import com.krazytop.teamfighttactics.service.RankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RankController implements RankApi {

    private final RankService rankService;

    @Autowired
    public RankController(RankService rankService) {
        this.rankService = rankService;
    }

    @Override
    public ResponseEntity<RankDTO> getRanks(String puuid) {
        log.info("Retrieving ranks");
        RankDTO rank = rankService.getRanksDTO(puuid);
        log.info("Ranks retrieved");
        return new ResponseEntity<>(rank, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateRanks(String puuid) {
        log.info("Updating ranks");
        rankService.updateRanks(puuid);
        log.info("Ranks updated");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}