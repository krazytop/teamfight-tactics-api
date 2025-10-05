package com.krazytop.teamfighttactics.repository;

import com.krazytop.teamfighttactics.nomenclature.Patch;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PatchRepository extends MongoRepository<Patch, String> {

    Optional<Patch> findFirstByPatchIdAndLanguage(String patchId, String language);
}
