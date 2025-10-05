package com.krazytop.teamfighttactics.nomenclature;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemNomenclature {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    private String name;
    @JsonAlias("icon")
    private String image;
    @JsonAlias("desc")
    private String description;
    @JsonAlias("effects")
    private Map<String, Float> variables;
    private List<String> composition;

    @JsonProperty("apiName")
    private void unpackId(String id) {
        this.id = id;
    }
}
