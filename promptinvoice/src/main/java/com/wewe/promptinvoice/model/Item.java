package com.wewe.promptinvoice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

    @JsonProperty("desc")
    private String description;

    private double price;

    private int quantity;
}

