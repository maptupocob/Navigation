package com.martirosov.sergey.navigation;

import com.google.gson.annotations.SerializedName;

public class FeatureMember {
    @SerializedName("GeoObject")
    private GeoObject geoObject;

    public GeoObject getGeoObject() {
        return geoObject;
    }

}
