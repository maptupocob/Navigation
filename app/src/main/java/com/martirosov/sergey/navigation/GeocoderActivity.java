package com.martirosov.sergey.navigation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.LocationListener;
import android.location.LocationManager;
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

import com.martirosov.sergey.navigation.model.FeatureMember;
import com.martirosov.sergey.navigation.model.YandexResponse;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GeocoderActivity extends AppCompatActivity {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private YandexResponse yandexResponse;
    private ListView suggestResultView;
    private ArrayAdapter resultAdapter;
    private List<String> suggestResult;
    private List<FeatureMember> featureMemberList;
    LocationManager locationManager;
    Context context;
    private String currentPos;

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String msg = "Current Latitude: " + latitude + "\nCurrent Longitude: " + longitude;
            currentPos = longitude + " " + latitude;
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geokoder);
        context = this;
        EditText addressText = findViewById(R.id.enter_address);
        addressText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 3) {
                    getYandexResponse(s.toString());
                }

            }
        });
        suggestResultView = findViewById(R.id.suggest_result);
        suggestResult = new ArrayList<>();
        resultAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_2,
                android.R.id.text1,
                suggestResult);
        suggestResultView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, featureMemberList.get(position).getGeoObject().getPos(), Toast.LENGTH_SHORT).show();
                startYandexNavi(currentPos, featureMemberList.get(position).getGeoObject().getPos());
            }
        });
        suggestResultView.setAdapter(resultAdapter);

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if ((ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                && (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                10, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                2000,
                10, locationListener);
    }

    public void startYandexNavi(String from, String to) {
        if (from == null) {
            Toast.makeText(context, R.string.no_current_coordinates, Toast.LENGTH_SHORT).show();
            return;
        }
        float latFrom = Float.parseFloat(from.split("\\s")[1]);
        float lonFrom = Float.parseFloat(from.split("\\s")[0]);
        float latTo = Float.parseFloat(to.split("\\s")[1]);
        float lonTo = Float.parseFloat(to.split("\\s")[0]);

        Intent intent = new Intent("ru.yandex.yandexnavi.action.BUILD_ROUTE_ON_MAP");
        intent.setPackage("ru.yandex.yandexnavi");

        PackageManager pm = getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);

        // Проверяем, установлен ли Яндекс.Навигатор
        if (infos == null || infos.size() == 0) {
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

    private void getYandexResponse(String s) {
        suggestResultView.setVisibility(View.INVISIBLE);
        MyCustomTask customTask = new MyCustomTask(yandexResponse, this);
        customTask.execute(s);
    }

    public void showSearchResults(YandexResponse yandexResponse) {
        featureMemberList = yandexResponse.getFeatureMember();
        suggestResult.clear();
        for (int i = 0; i < featureMemberList.size(); i++) {
            suggestResult.add(featureMemberList.get(i).getGeoObject().getFormattedAddress());
        }
        resultAdapter.notifyDataSetChanged();
        suggestResultView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if ((ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        && (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            2000,
                            10, locationListener);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            2000,
                            10, locationListener);
                }

            } else {
                Toast.makeText(context, R.string.app_will_not_work, Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.removeUpdates(locationListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    2000,
                    10, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    2000,
                    10, locationListener);
        }
    }
}
