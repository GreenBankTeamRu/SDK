package ru.sberbank.sbbol.sberbusinessapi.model.clientinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String area;
    private String building;
    private String city;
    private String comment;
    private String country;
    private String flat;
    private String fullAddress;
    private String house;
    private String region;
    private String settlement;
    private String settlementType;
    private String street;
    private String type;
    private String zip;
}
