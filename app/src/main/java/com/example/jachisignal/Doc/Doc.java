package com.example.jachisignal.Doc;

public class Doc {
    String item;
    int money;

    public Doc(String item, int money){
        this.item = item;
        this.money = money;
    }
    public Doc(){

    }

    public String getItem() {
        return item;
    }

    public int getMoney() {
        return money;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}

