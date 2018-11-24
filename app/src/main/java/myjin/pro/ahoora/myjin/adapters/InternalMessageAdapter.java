package myjin.pro.ahoora.myjin.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import myjin.pro.ahoora.myjin.R;
import myjin.pro.ahoora.myjin.models.KotlinInternalMessegeModel;

public class InternalMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity context;
    private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();
    private ArrayList<KotlinInternalMessegeModel> buffer = new ArrayList<>();

    public InternalMessageAdapter(Activity context) {
        expansionsCollection.openOnlyOne(true);
        this.context = context;
        fillBuffer();
    }

    private void fillBuffer() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<KotlinInternalMessegeModel> res = realm.where(KotlinInternalMessegeModel.class)
                .findAll().sort("id", Sort.DESCENDING);
        realm.commitTransaction();
        buffer.addAll(res);

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.services_item_layout, parent, false);
        return new InternalMessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       /* val bg = "#ff" + messageItem.bgColor
        try {
            ((ViewHolder) holder).tv_service_title.setBackgroundColor(Color.parseColor(bg))
        }catch (e:Exception){
            holder.message_cl.setBackgroundColor(Color.WHITE)
        }*/

        String bg = "#ff" + buffer.get(position).getColor_();
        try {
            ((ViewHolder) holder).tv_service_title.setTextColor(Color.parseColor(bg));
        } catch (Exception e) {
            ((ViewHolder) holder).tv_service_title.setTextColor(Color.WHITE);
        }

        ((ViewHolder) holder).tv_service_title.setText(buffer.get(position).getTitle());
        ((ViewHolder) holder).tv_service_context.setText(buffer.get(position).getContext());
        expansionsCollection.add(((ViewHolder) holder).expansionLayout);
    }

    @Override
    public int getItemCount() {
        return buffer.size();
    }


    private class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tv_service_title;
        AppCompatTextView tv_service_context;
        ExpansionLayout expansionLayout;

        ViewHolder(View itemView) {
            super(itemView);
            tv_service_title = itemView.findViewById(R.id.tv_service_title);
            tv_service_context = itemView.findViewById(R.id.tv_service_context);
            expansionLayout = itemView.findViewById(R.id.expansionLayout);
        }

    }


}
