package com.martirosov.sergey.navigation;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Address {
    @SerializedName("country_code")
    private String countryCode;
    @SerializedName("postal_code")
    private String postalCode;
    private String formatted;
    @SerializedName("Components")
    private List<Component> components;

    private class Component{
        private String kind;
        private String name;
    };


}
