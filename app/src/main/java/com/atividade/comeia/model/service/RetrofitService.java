package com.atividade.comeia.model.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private Retrofit retrofit;

    public RetrofitService(){
        this.retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.github.com/search/")
                .build();
    }

    public RequestService getRequestService(){
        return retrofit.create(RequestService.class);

    }
}
