package com.atividade.comeia.model.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Request {

    @SerializedName("total_count")
    private Long totalCount;
    private List<Repository> items;

    public Request(){}

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<Repository> getItems() {
        return items;
    }

    public void setItems(List<Repository> items) {
        this.items = items;
    }
}
