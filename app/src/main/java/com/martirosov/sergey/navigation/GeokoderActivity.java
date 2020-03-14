package com.martirosov.sergey.navigation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

public class GeokoderActivity extends AppCompatActivity {
    YandexResponse yandexResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geokoder);
        EditText addressText = findViewById(R.id.enter_address);
        addressText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Toast.makeText(GeokoderActivity.this, s.length()+"", Toast.LENGTH_SHORT).show();
                if (s.length() > 5) {
                    getYandexResponse(s.toString());
//                    startYandexMap(s.toString());
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getYandexResponse(String s) {
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
        Uri uri = Uri.parse("yandexmaps://maps.yandex.ru/?ll=" + s + "&z=12");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
