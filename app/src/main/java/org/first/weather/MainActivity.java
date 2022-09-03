package org.first.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
        private TextView city, tempreture,weatherContition,humidity,maxTempreture,minTempreture,pressure,wind;
        private ImageView imageView;
        private FloatingActionButton fab;
        LocationManager locationManager;
        LocationListener locationListener;
        double lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.purple_500));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = findViewById(R.id.textViewCty);
        tempreture =findViewById(R.id.textViewtemp);
        weatherContition = findViewById(R.id.textViewWeatherCondition);
        humidity = findViewById(R.id.Humidity);
        maxTempreture = findViewById(R.id.textViewMaxTemp);
        minTempreture = findViewById(R.id.textViewMinTemp);
        pressure = findViewById(R.id.textViewPressure);
        wind = findViewById(R.id.textVieWind);
        imageView = findViewById(R.id.imageView);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,WeatherActivity.class);
                startActivity(intent);

            }
        });
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onLocationChanged(@NonNull Location location) {
                    lat = location.getLatitude();
                    lon = location.getLongitude();

                Log.e("lat :" , String.valueOf(lat));
                Log.e("lon :" , String.valueOf(lon));
                getWeatherDate(lat,lon);
            }
        };
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION} ,1);
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500, 50,locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1 && permissions.length>0 && ContextCompat.checkSelfPermission(this
        , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500, 50,locationListener);
        }
    }
    public void getWeatherDate(double lat , double lon){
        WeatherApi weatherApi = RetrofitWeather.getClient().create(WeatherApi.class);
        Call<OpenWeatherMap> call = weatherApi.getWeatherWithLocation(lat, lon);
        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {
                city.setText(response.body().getName()+" , "+response.body().getSys().getCountry());
                tempreture.setText(response.body().getMain().getTemp()+" °C");
                weatherContition.setText(response.body().getWeather().get(0).getDescription());
                humidity.setText(" : "+response.body().getMain().getHumidity()+"%");
                maxTempreture.setText(" : "+response.body().getMain().getTempMax()+" °C");
                minTempreture.setText(" : "+response.body().getMain().getTempMin()+" °C");
                pressure.setText(" : "+response.body().getMain().getPressure());
                wind.setText(" : "+response.body().getWind().getSpeed());

                String iconcode = response.body().getWeather().get(0).getIcon();
                Picasso.get().load("https://openweathermap.org/img/wn/"+iconcode+"@2x.png")
                        .placeholder(R.drawable.ic_launcher_background)
                        .into(imageView);
            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {

            }
        });
    }
}