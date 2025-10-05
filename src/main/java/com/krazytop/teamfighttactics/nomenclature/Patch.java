package com.krazytop.teamfighttactics.nomenclature;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "Patch")
public class Patch {

    private String id;
    private String patchId;
    private String language;
    private Integer set;
    private List<UnitNomenclature> units;
    private List<TraitNomenclature> traits;
    private List<ItemNomenclature> items;
    private List<ItemNomenclature> augments;
    private List<QueueNomenclature> queues;
}
