package com.martirosov.sergey.navigation;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YandexResponse {
    private Response response;

    private class Response {
        @SerializedName("GeoObjectCollection")
        private GeoObjectCollection geoObjectCollection;

        public GeoObjectCollection getGeoObjectCollection() {
            return geoObjectCollection;
        }
    }

    private class GeoObjectCollection {
        private MetaDataProperty metaDataProperty;
        private List<FeatureMember> featureMember;

        public MetaDataProperty getMetaDataProperty() {
            return metaDataProperty;
        }

        public List<FeatureMember> getFeatureMember() {
            return featureMember;
        }
    }


    private class MetaDataProperty {
        @SerializedName("GeocoderResponseMetaData")
        private GeocoderResponseMetaData geocoderResponseMetaData;
    }

    private class GeocoderResponseMetaData {
        private String request;
        int found;
        int results;
    }

    private class FeatureMember{
        @SerializedName("GeoObject")
        private GeoObject geoObject;

        public GeoObject getGeoObject() {
            return geoObject;
        }
    }




    public List<FeatureMember> getFeatureMember() {
        return response.getGeoObjectCollection().getFeatureMember();
    }
    public GeoObject getGeoObject(int index){
        return response.geoObjectCollection.getFeatureMember().get(index).getGeoObject();
    }


}
