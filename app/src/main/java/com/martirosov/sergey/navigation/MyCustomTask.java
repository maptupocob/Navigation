package com.martirosov.sergey.navigation;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyCustomTask extends AsyncTask<String, String, String> {

    private Context context;
    private YandexResponse yandexResponse;
    private Retrofit retrofit;
    private final String apiKey = "e554b4ae-cdd6-4990-9c48-7f9dd5947412";
    private final String format = "json";

    public MyCustomTask(YandexResponse yandexResponse, Context context) {
        this.yandexResponse = yandexResponse;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String pos=null;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://geocode-maps.yandex.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        YandexResponseMap yandexResponseMap = retrofit.create(YandexResponseMap.class);
        for (String s : strings) {
            try {
                Call<YandexResponse> call = yandexResponseMap.getAddressInfo(apiKey, format, s);
                Response<YandexResponse> response = call.execute();
//                Toast.makeText(context,response.message(), Toast.LENGTH_SHORT).show();
                if (response.code() != 200) {
                    publishProgress(response.message());
                }
                yandexResponse = response.body();
                 pos = yandexResponse.getGeoObject(0).getPos();
                 //. getName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pos;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        Toast.makeText(context, values[0], Toast.LENGTH_SHORT).show();
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        ((GeokoderActivity) context).startYandexMapRoute(s.replace(" ", ","));
        super.onPostExecute(s);
    }

}
