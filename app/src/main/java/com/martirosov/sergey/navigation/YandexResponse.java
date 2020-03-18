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

    public List<FeatureMember> getFeatureMember() {
        return response.getGeoObjectCollection().getFeatureMembers();
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

    public GeoObject getGeoObject(int index){
        return response.geoObjectCollection.getFeatureMembers().get(index).getGeoObject();
    }

    private class GeoObjectCollection {
        private MetaDataProperty metaDataProperty;
        @SerializedName("featureMember")
        private List<FeatureMember> featureMembers;

        public MetaDataProperty getMetaDataProperty() {
            return metaDataProperty;
        }

        public List<FeatureMember> getFeatureMembers() {
            return featureMembers;
        }
    }


}
