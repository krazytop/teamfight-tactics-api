package com.krazytop.teamfighttactics.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Unit {

    @JsonAlias("character_id")
    private String id;
    private int rarity;
    private int tier;
    @JsonAlias("itemNames")
    private List<String> items;
}
