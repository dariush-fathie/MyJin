package myjin.pro.ahoora.myjin.adapters;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import io.realm.Realm;
import io.realm.RealmResults;
import myjin.pro.ahoora.myjin.R;
import myjin.pro.ahoora.myjin.models.KotlinNotificationModel;
import myjin.pro.ahoora.myjin.utils.DateConverter;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity context;
    private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();
    private RealmResults<KotlinNotificationModel> buffer;
    private DateConverter converter;
    private Realm realm;

    public NotificationAdapter(Activity context, RealmResults<KotlinNotificationModel> buffer, Realm realm) {
        expansionsCollection.openOnlyOne(true);
        this.context = context;
        this.buffer = buffer;
        this.realm = realm;
        converter = new DateConverter(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.notification_item_layout, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        KotlinNotificationModel b = buffer.get(position);

        try {
            assert b != null;
            String d = converter.convert2(b.getRegDate());
            ((ViewHolder) holder).tv_messageTitle.setText(b.getTitle());
            ((ViewHolder) holder).tv_notification_context.setText(b.getMessage());
            ((ViewHolder) holder).tv_messageDate.setText(d);


        } catch (Exception e) {

        }


        expansionsCollection.add(((ViewHolder) holder).expansionLayout);
    }

    @Override
    public int getItemCount() {
        return buffer.size();
    }


    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        AppCompatTextView tv_messageTitle, tv_messageDate, tv_notification_context;
        AppCompatImageView iv_delete, iv_messageImage;
        ExpansionLayout expansionLayout;

        ViewHolder(View itemView) {
            super(itemView);
            tv_messageTitle = itemView.findViewById(R.id.tv_messageTitle);
            tv_notification_context = itemView.findViewById(R.id.tv_notification_context);
            tv_messageDate = itemView.findViewById(R.id.tv_messageDate);
            iv_delete = itemView.findViewById(R.id.iv_delete);
            iv_messageImage = itemView.findViewById(R.id.iv_messageImage);
            expansionLayout = itemView.findViewById(R.id.el_services);

            iv_delete.setOnClickListener(this);




        }

        @Override
        public void onClick(View v) {
            try {
                realm.beginTransaction();
                KotlinNotificationModel messageobj = realm.where(KotlinNotificationModel.class)
                        .equalTo("messageId", buffer.get(getAdapterPosition()).getMessageId())
                        .findFirst();
                if (messageobj != null) {
                    messageobj.deleteFromRealm();
                }
                realm.commitTransaction();

                notifyItemRemoved(getAdapterPosition());
            } catch (Exception e) {
                Log.e("delete", e.toString());
            }

        }


    }


}
