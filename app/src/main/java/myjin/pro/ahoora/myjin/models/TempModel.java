package myjin.pro.ahoora.myjin.models;


import com.google.gson.annotations.SerializedName;

public class TempModel {

    @SerializedName("x")
    String val;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;

    }
}
