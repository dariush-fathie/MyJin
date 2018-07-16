package myjin.pro.ahoora.myjin.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import myjin.pro.ahoora.myjin.models.KotlinItemModel;
import myjin.pro.ahoora.myjin.models.KotlinSlideMainModel;

public class SharedPer {

    Context mContext;
    private SharedPreferences.Editor editor1;
    private SharedPreferences sharedPreference;
    private ArrayList<Integer> filter;

    private int filterC=0;
    public void setFilterC(int filterC) {
        this.filterC = filterC;
    }

    public int getFilterC() {
        return filterC;
    }


    public ArrayList<Integer> getFilter() {
        filter.clear();
        filterC=sharedPreference.getInt("filterC" , 0);
        for (int i = 0; i <filterC ; i++) {
            filter.add(sharedPreference.getInt("f"+i ,0));
        }

        return filter;
    }

    public void setFilter(ArrayList<Integer> filter) {
        editor1.putInt("filterC", filter.size());
        editor1.apply();
        for (int i = 0; i <filter.size() ; i++) {
            editor1.putInt("f"+i, filter.get(i));
            editor1.apply();
        }
    }


    public SharedPer(Context mContext) {
        this.mContext = mContext;
        sharedPreference =  mContext.getSharedPreferences("spz", 0);
        editor1 = sharedPreference.edit();

    }

}
