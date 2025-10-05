package com.krazytop.teamfighttactics.nomenclature;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TraitNomenclature {

    @JsonAlias("apiName")
    private String id;
    @JsonAlias("desc")
    private String description;
    private String name;
    @JsonAlias("icon")
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