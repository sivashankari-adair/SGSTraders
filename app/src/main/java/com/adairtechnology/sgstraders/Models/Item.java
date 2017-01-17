package com.adairtechnology.sgstraders.Models;

/**
 * Created by Android-Team1 on 12/29/2016.
 */

public class Item {

    public String name;
    public String id;
    public String qty;
    public String itemcode;
    public String font;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getQty() {
        return qty;
    }
    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getItemcode() {
        return itemcode;
    }
    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getFont() {
        return font;
    }
    public void setFont(String font) {
        this.font = font;
    }

}