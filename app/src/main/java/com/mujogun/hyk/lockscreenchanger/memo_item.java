package com.mujogun.hyk.lockscreenchanger;

/**
 * Created by hyk on 2016-07-19.
 */
public class memo_item {
    String Data1;
    String Data2;
    String Data3;
    public memo_item(String data1, String data2, String data3) {
        this.Data1 = data1;
        this.Data2 = data2;
        this.Data3 = data3;
    }
    public void setData(String data1, String data2, String data3){
        this.Data1 = data1;
        this.Data2 = data2;
        this.Data3 = data3;
    }
    public String getData1() {
        return this.Data1;
    }
    public String getData2() {
        return this.Data2;
    }
    public String getData3() {
        return this.Data3;
    }
}
