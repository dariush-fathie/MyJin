package myjin.pro.ahoora.myjin.customClasses;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import myjin.pro.ahoora.myjin.R;
import myjin.pro.ahoora.myjin.models.KotlinProvCityModel;

public class SpinnerDialog {
    private ArrayList<KotlinProvCityModel> items;
    private Activity context;
    private String dTitle;
    private String closeTitle;
    private OnSpinerItemClick onSpinerItemClick;
    private AlertDialog alertDialog;
    private int provId;
    private int cityId;
    private int style;
    private Realm realm;
    private String name ;

    public SpinnerDialog(Activity activity, String dialogTitle, String closeTitle) {
        this.context = activity;
        this.dTitle = dialogTitle;
        this.closeTitle = closeTitle;
        items = new ArrayList<>();
    }


    public void bindOnSpinerListener(OnSpinerItemClick onSpinerItemClick1) {
        this.onSpinerItemClick = onSpinerItemClick1;
    }

    public void fillBuffer(String t) {
        realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        RealmResults<KotlinProvCityModel> res = realm.where(KotlinProvCityModel.class)
                .contains("nameC",t).or().contains("nameP",t).findAll();


        realm.commitTransaction();

        items.clear();
        for (KotlinProvCityModel x : res) {
            items.add(x);
        }
    }

    public void showSpinerDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this.context);
        View v = this.context.getLayoutInflater().inflate(R.layout.dialog_layout, (ViewGroup) null);
        AppCompatTextView rippleViewClose = v.findViewById(R.id.close);
        AppCompatTextView title = v.findViewById(R.id.spinerTitle);
        rippleViewClose.setText(this.closeTitle);
        title.setText(this.dTitle);
        final RecyclerView listView = v.findViewById(R.id.list);
        final AppCompatEditText searchBox = v.findViewById(R.id.searchBox);

        String t = searchBox.getText().toString();
        fillBuffer(t);

        final LinearLayoutManager layoutmanager = new LinearLayoutManager(this.context);
        listView.setLayoutManager(layoutmanager);
        final AdapterSpiner adapter = new AdapterSpiner();
        listView.setAdapter(adapter);
        adb.setView(v);
        this.alertDialog = adb.create();
        this.alertDialog.getWindow().getAttributes().windowAnimations = this.style;
        this.alertDialog.setCancelable(true);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String t = searchBox.getText().toString();
                fillBuffer(t);

                listView.setAdapter(null);
                final AdapterSpiner adapter = new AdapterSpiner();
                listView.setAdapter(adapter);
            }
        });

        rippleViewClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SpinnerDialog.this.alertDialog.dismiss();
            }
        });
        this.alertDialog.show();
    }

    public class AdapterSpiner extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            view = LayoutInflater.from(context).inflate(R.layout.items_view, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            if (items.get(position).getCityId()!=0){
                SpinnerDialog.this.name=items.get(position).getNameP()+" ، "+items.get(position).getNameC();
            }else {
                SpinnerDialog.this.name=items.get(position).getNameP();
            }


            ((ViewHolder) holder).text1.setText(SpinnerDialog.this.name);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView text1;

            ViewHolder(View itemView) {
                super(itemView);
                text1 = itemView.findViewById(R.id.text1);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {

                SpinnerDialog.this.provId = items.get(getAdapterPosition()).getProvId();
                SpinnerDialog.this.cityId= items.get(getAdapterPosition()).getCityId();


                SpinnerDialog.this.onSpinerItemClick.onClick(text1.getText().toString(), SpinnerDialog.this.provId,SpinnerDialog.this.cityId);
                SpinnerDialog.this.alertDialog.dismiss();
            }
        }
    }
}


