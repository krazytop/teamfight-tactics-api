package com.krazytop.teamfighttactics.nomenclature;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnitNomenclature {

    @JsonAlias("apiName")
    private String id;
    private String name;
    @JsonAlias("tileIcon")
    private String image;
    private Integer cost;
    @JsonAlias("traits")
    private List<String> traits;
    private AbilityEntity ability;
    @Transient
    @JsonProperty(value = "icon", access = JsonProperty.Access.WRITE_ONLY)
    private String oldImage;

    @Data
    public static class AbilityEntity {

        private String name;
        @JsonAlias("icon")
        private String image;
        @JsonAlias("desc")
        private String description;
        private List<VariableEntity> variables;

        @Data
        public static class VariableEntity {

            private String name;
            private List<Float> value;
        }
    }
}