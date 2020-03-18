package com.martirosov.sergey.navigation;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class GeokoderActivity extends AppCompatActivity {
    private YandexResponse yandexResponse;
    private ListView suggestResultView;
    private ArrayAdapter resultAdapter;
    private List<String> suggestResult;
    private List<FeatureMember> featureMemberList;
    private String currentPos = "37.299656 55.493959";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geokoder);
        EditText addressText = findViewById(R.id.enter_address);
        suggestResultView = findViewById(R.id.suggest_result);
        suggestResult = new ArrayList<>();
        resultAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                suggestResult);
        suggestResultView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GeokoderActivity.this, featureMemberList.get(position).getGeoObject().getPos(), Toast.LENGTH_SHORT).show();
                startYandexNavi(currentPos, featureMemberList.get(position).getGeoObject().getPos());
            }
        });

        suggestResultView.setAdapter(resultAdapter);

        addressText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 5) {
                    getYandexResponse(s.toString());
//                    startYandexMap(s.toString());
                }

            }
        });
    }

    private void getYandexResponse(String s) {
        suggestResultView.setVisibility(View.INVISIBLE);
        MyCustomTask customTask = new MyCustomTask(yandexResponse, this);
        customTask.execute(s);
//        String name = yandexResponse.getFeatureMember().get(0).getName();
//        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
    }

    public void startYandexMap(String s) {
        Uri uri = Uri.parse("yandexmaps://maps.yandex.ru/?ll=" + s + "&z=12");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void startYandexMapRoute(String s) {
        Uri uri = Uri.parse("yandexmaps://maps.yandex.ru/?rtext=55.492914,37.300647~" + s.split(",")[1] + "," + s.split(",")[0] + "&rtt=mt");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void startYandexNavi(String from, String to) {
        float latFrom = Float.parseFloat(from.split("\\s")[1]);
        float lonFrom = Float.parseFloat(from.split("\\s")[0]);
        float latTo = Float.parseFloat(from.split("\\s")[1]);
        float lonTo = Float.parseFloat(from.split("\\s")[0]);
        // Создаем интент для построения маршрута
        Intent intent = new Intent("ru.yandex.yandexnavi.action.BUILD_ROUTE_ON_MAP");
        intent.setPackage("ru.yandex.yandexnavi");

        PackageManager pm = getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);

        // Проверяем, установлен ли Яндекс.Навигатор
        if (infos == null || infos.size() == 0) {
            // Если нет - будем открывать страничку Навигатора в Google Play
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=ru.yandex.yandexnavi"));
        } else {
            intent.putExtra("lat_from", latFrom);
            intent.putExtra("lon_from", lonFrom);
            intent.putExtra("lat_to", latTo);
            intent.putExtra("lon_to", lonTo);
        }
        startActivity(intent);
    }

    public void showSearchResults(YandexResponse yandexResponse) {
        featureMemberList = yandexResponse.getFeatureMember();
        suggestResult.clear();
        for (int i = 0; i < featureMemberList.size(); i++) {
            suggestResult.add(featureMemberList.get(i).getGeoObject().getName());
        }
        resultAdapter.notifyDataSetChanged();
        suggestResultView.setVisibility(View.VISIBLE);
    }
}
