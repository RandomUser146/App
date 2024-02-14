package com.example.app.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListItem {
    private String itemName = "Loading......";
    private ArrayList<String> x = new ArrayList<String>();
    private ArrayList<String> y = new ArrayList<String>();

    public ListItem() {
    }

    public ListItem setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public ListItem setX(ArrayList<String> map) {
        this.x = map;
        return this;
    }

    public ListItem setY(ArrayList<String> map) {
        this.y = map;
        return this;
    }

    public String getItemName() {
        return itemName;
    }

    public ArrayList<String> getX() {
        return x;
    }

    public ArrayList<String> getY() {
        return y;
    }
}
