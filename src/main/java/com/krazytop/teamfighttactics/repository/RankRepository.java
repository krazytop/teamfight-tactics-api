package com.krazytop.teamfighttactics.repository;

import com.krazytop.teamfighttactics.entity.Rank;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RankRepository extends MongoRepository<Rank, String> {

    Optional<Rank> findByPuuid(String puuid);

}
