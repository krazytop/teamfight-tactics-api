package com.krazytop.teamfighttactics.controller;

import com.krazytop.teamfighttactics.api.generated.PatchApi;
import com.krazytop.teamfighttactics.model.generated.PatchDTO;
import com.krazytop.teamfighttactics.service.PatchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
public class PatchController implements PatchApi {

    private final PatchService patchService;

    @Override
    public ResponseEntity<PatchDTO> getPatch(String patchId, String language) {
        log.info("Retrieving patch {} with {} language", patchId,  language);
        PatchDTO patch = patchService.getPatchDTO(patchId, language);
        log.info("Patch retrieved");
        return new ResponseEntity<>(patch, HttpStatus.OK);
    }
}