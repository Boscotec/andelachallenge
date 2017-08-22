package com.boscotec.andelachallenge.rest;

import com.boscotec.andelachallenge.model.User;
import com.boscotec.andelachallenge.model.UserDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiInterface {

//    @GET("users?since=xxx")
//    Call<List<User>> getUser(@Query("q") String q);

    @GET("search/users")
    Call<User> getUser(@Query("q") String filter);

}
