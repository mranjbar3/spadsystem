package com.spadsystem.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchRes {
    private String data;
    private String result;

    public SearchRes(String result) {
        this.result = result;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
