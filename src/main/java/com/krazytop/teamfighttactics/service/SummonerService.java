package com.krazytop.teamfighttactics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krazytop.teamfighttactics.entity.Account;
import com.krazytop.teamfighttactics.entity.Summoner;
import com.krazytop.teamfighttactics.exception.ApiErrorEnum;
import com.krazytop.teamfighttactics.exception.CustomException;
import com.krazytop.teamfighttactics.mapper.SummonerMapper;
import com.krazytop.teamfighttactics.model.generated.SummonerDTO;
import com.krazytop.teamfighttactics.repository.SummonerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SummonerService {

    @Value("${teamfight-tactics.api-key:api-key}")
    private String apiKey;
    private final SummonerRepository summonerRepository;
    private final SummonerMapper summonerMapper;

    public Summoner getSummoner(String puuid) {
        return getLocalSummoner(puuid).orElse(getRemoteSummoner(puuid));
    }

    public SummonerDTO getSummonerDTO(String tag, String name) {
        return summonerMapper.toDTO(getLocalSummoner(tag, name).orElse(getRemoteSummoner(tag, name)));
    }

    public SummonerDTO getSummonerDTO(String puuid) {
        return summonerMapper.toDTO(getSummoner(puuid));
    }

    private String getRegion(String puuid) {
        return "euw1";//TODO implémenter l'API + appeler ça uniquement si la region est mauvaise pour eco la limit rate ?
    }

    public SummonerDTO updateSummoner(String puuid) {
        Summoner newSummoner = getRemoteSummoner(puuid);
        Optional<Summoner> oldSummoner = getLocalSummoner(puuid);
        if (oldSummoner.isPresent()) {
            newSummoner.setSpentTime(oldSummoner.get().getSpentTime());
            newSummoner.setPlayedSeasonsOrSets(oldSummoner.get().getPlayedSeasonsOrSets());
        }
        newSummoner.setUpdateDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        summonerRepository.save(newSummoner);
        return summonerMapper.toDTO(newSummoner);
    }

    public Optional<Summoner> getLocalSummoner(String puuid) {
        return summonerRepository.findFirstByPuuid(puuid);
    }

    private Optional<Summoner> getLocalSummoner(String tag, String name) {
        return summonerRepository.findFirstByTagAndName(tag, name);
    }

    private Summoner getRemoteSummoner(String puuid) {
        try {
            String region = getRegion(puuid);
            ObjectMapper mapper = new ObjectMapper();
            String summonerApiUrl = String.format("https://%s.api.riotgames.com/tft/summoner/v1/summoners/by-puuid/%s?api_key=%s", region, puuid, apiKey);
            Summoner summoner = mapper.convertValue(mapper.readTree(new URI(summonerApiUrl).toURL()), Summoner.class);
            Account account = getAccount(summoner.getPuuid());
            summoner.setName(account.getName());
            summoner.setTag(account.getTag());
            summoner.setRegion(region);
            return summoner;
        } catch (URISyntaxException | IOException ex) {
            throw new CustomException(ApiErrorEnum.SUMMONER_NOT_FOUND, ex);
        }
    }

    private Summoner getRemoteSummoner(String tag, String name) {
        try {
            name = name.replace(" ", "%20");
            ObjectMapper mapper = new ObjectMapper();
            Account account = getAccount(tag, name);
            String region = getRegion(account.getPuuid());
            String summonerApiUrl = String.format("https://%s.api.riotgames.com/tft/summoner/v1/summoners/by-puuid/%s?api_key=%s", region, account.getPuuid(), apiKey);
            Summoner summoner = mapper.convertValue(mapper.readTree(new URI(summonerApiUrl).toURL()), Summoner.class);
            summoner.setRegion(region);
            summoner.setName(account.getName());
            summoner.setTag(account.getTag());
            return summoner;
        } catch (URISyntaxException | IOException ex) {
            throw new CustomException(ApiErrorEnum.SUMMONER_NOT_FOUND, ex);
        }
    }

    private Account getAccount(String puuid) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String accountApiUrl = String.format("https://europe.api.riotgames.com/riot/account/v1/accounts/by-puuid/%s?api_key=%s", puuid, apiKey);
            return mapper.convertValue(mapper.readTree(new URI(accountApiUrl).toURL()), Account.class);
        } catch (URISyntaxException | IOException ex) {
            throw new CustomException(ApiErrorEnum.ACCOUNT_NOT_FOUND, ex);
        }
    }

    private Account getAccount(String tag, String name) throws URISyntaxException, IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String accountApiUrl = String.format("https://europe.api.riotgames.com/riot/account/v1/accounts/by-riot-id/%s/%s?api_key=%s", name, tag, apiKey);
            return mapper.convertValue(mapper.readTree(new URI(accountApiUrl).toURL()), Account.class);
        } catch (URISyntaxException | IOException ex) {
            throw new CustomException(ApiErrorEnum.ACCOUNT_NOT_FOUND, ex);
        }
    }

    public void updateSpentTimeAndPlayedSeasonsOrSets(String puuid, Long matchDuration, Integer season) {
        Optional<Summoner> summonerOpt = getLocalSummoner(puuid);
        if (summonerOpt.isPresent()) {
            Summoner summoner = summonerOpt.get();
            summoner.setSpentTime((summoner.getSpentTime() == null ? 0L : summoner.getSpentTime()) + matchDuration);
            if (summoner.getPlayedSeasonsOrSets() == null) summoner.setPlayedSeasonsOrSets(new HashSet<>());
            summoner.getPlayedSeasonsOrSets().add(season);
            summonerRepository.save(summoner);
        }
    }

}
