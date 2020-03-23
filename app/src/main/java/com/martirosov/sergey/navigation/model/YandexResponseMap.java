package com.martirosov.sergey.navigation.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YandexResponseMap {
    @GET("1.x/")
    Call<YandexResponse> getAddressInfo(@Query("apikey") String apiKey, @Query("format") String format, @Query("geocode") String s);
}
