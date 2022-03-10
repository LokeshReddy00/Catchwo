package com.buddies.catchwo.Support;

import com.buddies.catchwo.Model.Users;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("single.php")
    Call<ArrayList<Users>> getUsers (@Query("key") String Keyword );

}
