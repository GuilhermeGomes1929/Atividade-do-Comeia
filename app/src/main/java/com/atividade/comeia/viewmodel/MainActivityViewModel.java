package com.atividade.comeia.viewmodel;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.atividade.comeia.model.entity.Repository;
import com.atividade.comeia.model.entity.Request;
import com.atividade.comeia.model.service.RequestService;
import com.atividade.comeia.model.service.RetrofitService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityViewModel extends ViewModel {

    private MutableLiveData<List<Repository>> request;
    private MutableLiveData<String> textFromSearch;
    private MutableLiveData<Integer> firstPage;
    private MutableLiveData<Integer> nextPage;
    private MutableLiveData<Integer> prevPage;
    private MutableLiveData<Integer> lastPage;
    private MutableLiveData<String> message;
    private MutableLiveData<Long> resultsCount;

    private RequestService requestService;

    public MainActivityViewModel(){
        request = new MutableLiveData<>(new ArrayList<>());
        textFromSearch = new MutableLiveData<>();
        requestService = new RetrofitService().getRequestService();
        firstPage = new MutableLiveData<>(1);
        nextPage= new MutableLiveData<>(1);
        prevPage= new MutableLiveData<>(0);
        lastPage= new MutableLiveData<>(2);
        message = new MutableLiveData<>();
        resultsCount = new MutableLiveData<>(0l);
    }

    public void setTextFromSearch(String text){
        textFromSearch.setValue(text);
    }

    public void doRequest(String page){
        if (nextPage.getValue() < lastPage.getValue()){

            Call<Request> requestCall = requestService.getRequest(textFromSearch.getValue(), page);
            requestCall.enqueue(new Callback<Request>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<Request> call, Response<Request> response) {
                    if (response.isSuccessful()){
                        Headers headers = response.headers();
                        headers.values("link").forEach(bigLink -> {

                            List<String> links = Arrays.asList(bigLink.split(","));
                            links.forEach(link -> {

                                Integer startsIn = link.lastIndexOf("page") + 5;
                                Integer endsIn = link.lastIndexOf(">");
                                Integer value  = Integer.parseInt(link.substring(startsIn, endsIn));

                                if (link.contains("prev")){
                                    prevPage.postValue(value);
                                } if(link.contains("next")){
                                    nextPage.postValue(value);
                                }if (link.contains("last")){
                                    lastPage.postValue(value);
                                }if (link.contains("first")){
                                    firstPage.postValue(value);
                                }
                            });
                        });
                        List<Repository> list = request.getValue();
                        list.addAll(response.body().getItems());
                        request.postValue(list);
                        resultsCount.postValue(response.body().getTotalCount());
                    }else{
                        message.postValue("Limite de requisições atingido!");
                    }
                }

                @Override
                public void onFailure(Call<Request> call, Throwable t) {

                }
            });
        }else{
            message.postValue("Não existem mais intens.");
        }
    }

    public void clearAllItems(){
        request.getValue().clear();
        resultsCount.setValue(0l);
        nextPage.setValue(1);
        lastPage.setValue(2);
        firstPage.setValue(1);
        prevPage.setValue(1);
    }

    public LiveData<List<Repository>> getResult(){ return request;}

    public LiveData<Long> getResultsCount(){return resultsCount;}

    public LiveData<String> getMessage(){ return  message;}

    public LiveData<Integer> getNextPage(){return nextPage;}

    public LiveData<Integer> getLastPage(){return lastPage;}

}
