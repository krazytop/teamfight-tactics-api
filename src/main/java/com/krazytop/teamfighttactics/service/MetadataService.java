package com.krazytop.teamfighttactics.service;

import com.krazytop.teamfighttactics.entity.Metadata;
import com.krazytop.teamfighttactics.exception.ApiErrorEnum;
import com.krazytop.teamfighttactics.exception.CustomException;
import com.krazytop.teamfighttactics.mapper.MetadataMapper;
import com.krazytop.teamfighttactics.model.generated.MetadataDTO;
import com.krazytop.teamfighttactics.repository.MetadataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class MetadataService {

    private final MetadataRepository metadataRepository;
    private final MetadataMapper metadataMapper;

    public MetadataDTO getMetadataDTO() {
        return getMetadata()
                .map(this.metadataMapper::toDTO)
                .orElseThrow(() -> new CustomException(ApiErrorEnum.METADATA_NOT_FOUND));
    }

    public Optional<Metadata> getMetadata() {
        return metadataRepository.findAll().stream().findFirst();
    }

}
