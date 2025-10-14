package com.krazytop.teamfighttactics.nomenclature;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TraitNomenclature {

    private String id;
    private String description;
    private String name;
    private String image;
    private List<EffectEntity> effects;

    @Data
    public static class EffectEntity {

        private Integer maxUnits;
        private Integer minUnits;
        private Integer style;
        private Map<String, Float> variables;
    }
}