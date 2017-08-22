package com.boscotec.andelachallenge.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Johnbosco on 15-Jul-17.
 */

public class User {

    @SerializedName("items")
    @Expose
    private List<UserDetail> items = null;

    public List<UserDetail> getItems() {
        return items;
    }

    public void setItems(List<UserDetail> items) {
        this.items = items;
    }

}
