package com.martirosov.sergey.navigation;

import com.google.gson.annotations.SerializedName;

class GeoObject {
    private MetaDataProperty metaDataProperty;
    private String name;
    private String description;
    @SerializedName("Point")
    private Point point;

    public String getPos() {
        return point.pos;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    private class Point {
        private String pos;

        public String getPos() {
            return pos;
        }
    }

    private class MetaDataProperty {
        @SerializedName("GeocoderMetaData")
        private GeocoderMetaData geocoderMetaData;
    }

    private class GeocoderMetaData {
        private String kind;
        private String text;
        private String precision;
        @SerializedName("Address")
        private Address address;
    }
}


