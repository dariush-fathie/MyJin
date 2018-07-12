package myjin.pro.ahoora.myjin.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import myjin.pro.ahoora.myjin.R;

public class HeaIncAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    Activity context;

    public ArrayList<String> buffer = new ArrayList<>();

    public HeaIncAdapter(Activity context) {
        this.context = context;
        fillBuffer();
    }

    private void fillBuffer() {
        buffer.add("استعلام بیمه شده");
        buffer.add("صدور دفترچه / کارت بیمه");
        buffer.add("مشاهده نسخ الکترونیکی");
        buffer.add("آگهی و پیام ها");
        buffer.add("لیست دفاتر ICT ژین");
        buffer.add("لیست مراکز بهداشتی درمانی");
        buffer.add("لیست دفاتر پیشخوان");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.hea_inc_item_layout, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).tv_hea_inc.setText(buffer.get(position));
    }

    @Override
    public int getItemCount() {
        return buffer.size();
    }

    @Override
    public void onClick(View view) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_hea_inc;

        ViewHolder(View itemView) {
            super(itemView);
            tv_hea_inc = itemView.findViewById(R.id.tv_hea_inc);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (getAdapterPosition()) {
                case 0:
                    inquiry_Mth();
                    break;
                case 1:
                    request_mth();
                    break;
                case 2:
                    elcCopies_Mth();
                    break;
                case 3:
                    message_Mth();
                    break;
                case 4:
                    ict_Mth();
                    break;
                case 5:
                    ict_Mth();
                    break;
                case 6:
                    ict_Mth();
                    break;
            }
        }

        private void ict_Mth() {
            View view1;
            view1 = LayoutInflater.from(context).inflate(R.layout.ict, null, false);

            alertDialog_mth(view1,"");
        }
        private void message_Mth() {
            View view1;
            view1 = LayoutInflater.from(context).inflate(R.layout.message_layout, null, false);

            alertDialog_mth(view1,"");
        }

        private void elcCopies_Mth() {
            View view1;
            view1 = LayoutInflater.from(context).inflate(R.layout.elc_copies_layout, null, false);

            alertDialog_mth(view1,"");
        }

        private void request_mth() {
           /* View view1;
            view1 = LayoutInflater.from(context).inflate(R.layout.request_layout, null, false);

            alertDialog_mth(view1,"نه");*/
            Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("http://myjin.ir/service/healthInsuranceCard/"));
            context.startActivity(intent);
           
        }

        private void inquiry_Mth() {

            View view1;
            view1 = LayoutInflater.from(context).inflate(R.layout.inquiry_layout, null, false);

            alertDialog_mth(view1,"نه");
        }


        private void alertDialog_mth(View view1, String no) {
            DialogInterface.OnClickListener d = null;

            if (!no.equals("")) {
                d = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                };
            }

            TextView tv_costom_title;
            View view2 = LayoutInflater.from(context).inflate(R.layout.hea_inc_services_title, null, false);
            tv_costom_title = view2.findViewById(R.id.tv_customTitle);
            tv_costom_title.setText(buffer.get(getAdapterPosition()));

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCustomTitle(view2);
            builder.setPositiveButton("باشه", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).setNegativeButton(no, d);
            builder.setView(view1);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }


}
