package com.krazytop.teamfighttactics.service;

import com.krazytop.teamfighttactics.exception.ApiErrorEnum;
import com.krazytop.teamfighttactics.exception.CustomException;
import com.krazytop.teamfighttactics.mapper.PatchMapper;
import com.krazytop.teamfighttactics.model.generated.PatchDTO;
import com.krazytop.teamfighttactics.nomenclature.*;
import com.krazytop.teamfighttactics.repository.PatchRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class PatchService {

    private final PatchRepository patchNomenclatureRepository;
    private final PatchMapper patchMapper;

    public Optional<Patch> getPatch(String patchId, String language) {
        return patchNomenclatureRepository.findFirstByPatchIdAndLanguage(patchId, language);
    }

    public PatchDTO getPatchDTO(String patchId, String language) {
        return patchMapper.toDTO(getPatch(patchId, language).orElseThrow(() -> new CustomException(ApiErrorEnum.PATCH_NOT_FOUND)));
    }

}