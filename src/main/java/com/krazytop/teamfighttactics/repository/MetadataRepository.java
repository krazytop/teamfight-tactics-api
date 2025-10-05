package com.krazytop.teamfighttactics.repository;

import com.krazytop.teamfighttactics.entity.Metadata;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MetadataRepository extends MongoRepository<Metadata, String> {

}
