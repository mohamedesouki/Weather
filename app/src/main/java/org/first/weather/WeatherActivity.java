package org.first.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {
    private TextView cityWeather, tempretureWeather,weatherContitionWeather,humidityWeather,maxTempretureWeather,minTempretureWeather
            ,pressureWeather,windWeather;
    private ImageView imageViewWeather;
    private Button search;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setStatusBarColor(ContextCompat.getColor(WeatherActivity.this,R.color.purple_500));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        cityWeather = findViewById(R.id.textViewCtyWeather);
        tempretureWeather = findViewById(R.id.textViewtempWeather);
        weatherContitionWeather = findViewById(R.id.textViewWeatherConditionWeather);
        humidityWeather = findViewById(R.id.textViewHumidityWeather);
        maxTempretureWeather = findViewById(R.id.textViewMaxTempWeather);
        minTempretureWeather = findViewById(R.id.textViewMinTempWeather);
        pressureWeather = findViewById(R.id.textViewPressureWeather);
        windWeather = findViewById(R.id.textViewWindWeather);
        imageViewWeather = findViewById(R.id.imageViewWeather);
        search = findViewById(R.id.searchbtn);
        editText = findViewById(R.id.editTextCityName);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = editText.getText().toString();
                getWeatherDate(cityName);
                editText.setText("");

            }
        });



    }
    public void getWeatherDate(String name){
        WeatherApi weatherApi = RetrofitWeather.getClient().create(WeatherApi.class);
        Call<OpenWeatherMap> call = weatherApi.getWeatherWithName(name);
        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {
                if(response.isSuccessful()){
                    cityWeather.setText(response.body().getName()+" , "+response.body().getSys().getCountry());
                    tempretureWeather.setText(response.body().getMain().getTemp()+" °C");
                    weatherContitionWeather.setText(response.body().getWeather().get(0).getDescription());
                    humidityWeather.setText(" : "+response.body().getMain().getHumidity()+"%");
                    maxTempretureWeather.setText(" : "+response.body().getMain().getTempMax()+" °C");
                    minTempretureWeather.setText(" : "+response.body().getMain().getTempMin()+" °C");
                    pressureWeather.setText(" : "+response.body().getMain().getPressure());
                    windWeather.setText(" : "+response.body().getWind().getSpeed());

                    String iconcode = response.body().getWeather().get(0).getIcon();
                    Picasso.get().load("https://openweathermap.org/img/wn/"+iconcode+"@2x.png")
                            .placeholder(R.drawable.ic_launcher_background)
                            .into(imageViewWeather);
                }else {
                    Toast.makeText(WeatherActivity.this, "City not found,try again", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable t) {

            }
        });
    }
}