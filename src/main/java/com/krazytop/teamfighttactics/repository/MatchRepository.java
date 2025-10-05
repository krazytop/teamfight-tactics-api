package com.krazytop.teamfighttactics.repository;

import com.krazytop.teamfighttactics.entity.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MatchRepository extends MongoRepository<Match, String> {

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}}", sort = "{'datetime': -1}")
    Page<Match> findAll(String puuid, PageRequest pageRequest);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}, 'queue':  {$in: ?1}}", sort = "{'datetime': -1}")
    Page<Match> findAllByQueue(String puuid, List<String> queueIds, PageRequest pageRequest);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}, 'set': ?1}", sort = "{'datetime': -1}")
    Page<Match> findAllBySet(String puuid, int set, PageRequest pageRequest);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}, 'set': ?2, 'queue':  {$in: ?1}}", sort = "{'datetime': -1}")
    Page<Match> findAllByQueueAndBySet(String puuid, List<String> queueIds, int set, PageRequest pageRequest);

    Optional<Match> findFirstById(String matchId);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}}", count = true)
    Integer countAll(String puuid);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}, 'queue': {$in: ?1}}", count = true)
    Integer countAllByQueue(String puuid, List<String> queueIds);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}, 'set': ?1}", count = true)
    Integer countAllBySet(String puuid, int set);

    @Query(value = "{'participants': {$elemMatch: {'summoner.puuid': ?0}}, 'set': ?2, 'queue':  {$in: ?1}}", count = true)
    Integer countAllByQueueAndBySet(String puuid, List<String> queueIds, int set);

}
