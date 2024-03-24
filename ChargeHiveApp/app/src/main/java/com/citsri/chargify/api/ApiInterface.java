package com.citsri.chargify.api;


import com.citsri.chargify.api.body.EVStation;
import com.citsri.chargify.api.body.LatLon;
import com.citsri.chargify.api.body.LatLonWithRoute;

import java.util.ArrayList;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.Call;

public interface ApiInterface {
    @GET("/map/locate")
    Call<LatLon> getLocation(@Query("place") String namePlace);

    @POST("/ev/reg")
    Call<String> registerEvStation(@Body EVStation evStation);

    @POST("/map/locate_with_evs")
    Call<LatLonWithRoute> getAllEvsNear(@Query("place")String place, @Query("lat1") double lat1,@Query("lon1") double lon1);

    @GET("/ev/update_stat")
    Call<String> updateStat(@Query("id")int id,@Query("stat")String status);

}
