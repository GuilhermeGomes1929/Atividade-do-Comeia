package com.atividade.comeia.model.service;

import com.atividade.comeia.model.entity.Request;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RequestService {

    @GET("repositories?per_page=10")
    Call<Request> getRequest(@Query("q") String q,
                             @Query("page") String page);

}
