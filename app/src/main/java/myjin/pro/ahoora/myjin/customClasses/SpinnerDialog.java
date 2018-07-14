package myjin.pro.ahoora.myjin.customClasses;


import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import myjin.pro.ahoora.myjin.R;
public class SpinnerDialog {
    private ArrayList<String> items;
    private   Activity context;
    private String dTitle;
    private String closeTitle ;
    private OnSpinerItemClick onSpinerItemClick;
    private AlertDialog alertDialog;
    private int pos;
    private int style;



    public SpinnerDialog(Activity activity, ArrayList<String> items, String dialogTitle, String closeTitle) {
        this.items = items;
        this.context = activity;
        this.dTitle = dialogTitle;
        this.closeTitle = closeTitle;
    }



    public void bindOnSpinerListener(OnSpinerItemClick onSpinerItemClick1) {
        this.onSpinerItemClick = onSpinerItemClick1;
    }

    public void showSpinerDialog() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this.context);
        View v = this.context.getLayoutInflater().inflate(R.layout.dialog_layout, (ViewGroup) null);
        AppCompatTextView rippleViewClose = v.findViewById(R.id.close);
        AppCompatTextView title = v.findViewById(R.id.spinerTitle);
        rippleViewClose.setText(this.closeTitle);
        title.setText(this.dTitle);
        ListView listView =v.findViewById(R.id.list);
        final AppCompatEditText searchBox = v.findViewById(R.id.searchBox);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.context, R.layout.items_view, this.items);
        listView.setAdapter(adapter);
        adb.setView(v);
        this.alertDialog = adb.create();
        this.alertDialog.getWindow().getAttributes().windowAnimations = this.style;
        this.alertDialog.setCancelable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView t = view.findViewById(R.id.text1);

                for (int j = 0; j < SpinnerDialog.this.items.size(); ++j) {
                    if (t.getText().toString().equalsIgnoreCase((SpinnerDialog.this.items.get(j)))) {
                        SpinnerDialog.this.pos = j;
                    }
                }

                SpinnerDialog.this.onSpinerItemClick.onClick(t.getText().toString(), SpinnerDialog.this.pos);
                SpinnerDialog.this.alertDialog.dismiss();
            }
        });
        searchBox.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                adapter.getFilter().filter(searchBox.getText().toString());
            }
        });
        rippleViewClose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SpinnerDialog.this.alertDialog.dismiss();
            }
        });
        this.alertDialog.show();
    }
}

