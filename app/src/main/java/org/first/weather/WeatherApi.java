package org.first.weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("weather?appid=2c74cdc7e0acc25023a13ad990009b46&units=metric")
    Call<OpenWeatherMap> getWeatherWithLocation(@Query("lat") double lat , @Query("lon") double lon);

    @GET("weather?appid=2c74cdc7e0acc25023a13ad990009b46&units=metric")
    Call<OpenWeatherMap> getWeatherWithName(@Query("q") String name );
}
