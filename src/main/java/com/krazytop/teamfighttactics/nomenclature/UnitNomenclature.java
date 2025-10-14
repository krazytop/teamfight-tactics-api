package com.krazytop.teamfighttactics.nomenclature;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnitNomenclature {

    private String id;
    private String name;
    private String image;
    private Integer cost;
    private List<String> traits;
    private AbilityEntity ability;

    @Data
    public static class AbilityEntity {

        private String name;
        private String image;
        private String description;
        private List<VariableEntity> variables;

        @Data
        public static class VariableEntity {

            private String name;
            private List<Float> value;
        }
    }
}