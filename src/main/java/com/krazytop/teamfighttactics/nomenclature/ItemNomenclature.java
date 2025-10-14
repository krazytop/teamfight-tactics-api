package com.krazytop.teamfighttactics.nomenclature;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ItemNomenclature {

    private String id;
    private String name;
    private String image;
    private String description;
    private Map<String, Float> variables;
    private List<String> composition;
}
