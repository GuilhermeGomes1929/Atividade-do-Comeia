package com.atividade.comeia.viewmodel;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.atividade.comeia.model.entity.Message;
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
    private Integer firstPage;
    private Integer nextPage;
    private Integer prevPage;
    private Integer lastPage;
    private MutableLiveData<Message> message;
    private Long resultsCount;

    private RequestService requestService;

    public MainActivityViewModel(){
        request = new MutableLiveData<>(new ArrayList<>());
        textFromSearch = new MutableLiveData<>();
        requestService = new RetrofitService().getRequestService();
        firstPage = 1;
        nextPage= 1;
        prevPage= 0;
        lastPage= 2;
        message = new MutableLiveData<>();
    }

    public void setTextFromSearch(String text){
        textFromSearch.setValue(text);
    }

    public void doRequest(){

        if (nextPage <= lastPage){

            Call<Request> requestCall = requestService.getRequest(textFromSearch.getValue(), nextPage.toString());
            requestCall.enqueue(new Callback<Request>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<Request> call, Response<Request> response) {
                    if (response.isSuccessful()){
                        Headers headers = response.headers();
                        String bigLink = headers.values("link").get(0);

                        if (bigLink.contains("next")){

                            List<String> links = Arrays.asList(bigLink.split(","));
                            links.forEach(link -> {

                                Integer startsIn = link.lastIndexOf("page") + 5;
                                Integer endsIn = link.lastIndexOf(">");
                                Integer value  = Integer.parseInt(link.substring(startsIn, endsIn));

                                if (link.contains("prev")){
                                    prevPage = value;
                                }if(link.contains("next")){
                                    nextPage = value;
                                }if (link.contains("last")){
                                    lastPage = value;
                                }if (link.contains("first")){
                                    firstPage = value;
                                }
                            });
                        }else{
                            nextPage = lastPage + 1;
                        }

                        List<Repository> list = request.getValue();
                        list.addAll(response.body().getItems());
                        request.postValue(list);
                        resultsCount = response.body().getTotalCount();
                    }else{
                        Message msg = new Message();
                        msg.setTitleAndMessage("Limite de requisições atingido",
                                "Aguarde um momento e tente novamente");
                        message.postValue(msg);
                    }
                }

                @Override
                public void onFailure(Call<Request> call, Throwable t) {
                    Message msg = new Message();
                    msg.setTitleAndMessage("Conexão não estabelecida", "Por favor, verifique a sua conexão com a internet.");
                    message.postValue(msg);
                    resultsCount = null;
                }
            });
        }else{
            Message msg = new Message();
            msg.setTitleAndMessage("Não há mais resultados",
                    "Você chegou ao final da lista.");
            message.postValue(msg);
        }
    }

    public void clearAllItems(){
        request.getValue().clear();
        message.setValue(null);
        resultsCount = 0l;
        nextPage= 1;
        lastPage = 2;
        firstPage = 1;
        prevPage = 0;
    }

    public LiveData<List<Repository>> getResult(){ return request;}

    public Long getResultsCount(){return resultsCount;}

    public LiveData<Message> getMessage(){ return  message;}

    public Integer getNextPage(){return nextPage;}


}
