package myjin.pro.ahoora.myjin.fragments.loginAndSign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import myjin.pro.ahoora.myjin.R;
import myjin.pro.ahoora.myjin.activitys.ProfileActivity;
import myjin.pro.ahoora.myjin.customClasses.CustomToast;
import myjin.pro.ahoora.myjin.models.TempModel;
import myjin.pro.ahoora.myjin.utils.ApiInterface;
import myjin.pro.ahoora.myjin.utils.KotlinApiClient;
import myjin.pro.ahoora.myjin.utils.NetworkUtil;
import myjin.pro.ahoora.myjin.utils.Utils;
import myjin.pro.ahoora.myjin.utils.VarableValues;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourName_Fragment extends Fragment implements OnClickListener {

    @SuppressLint("StaticFieldLeak")
    private static View view;
    @SuppressLint("StaticFieldLeak")
    private static AppCompatEditText etFn, etLn;
    private AppCompatTextView tv_continue;
    private RelativeLayout rl_hbf;
    private IActivityEnabledListener aeListener;
    private String number, fn, ln;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();

        if (args != null) {

            number = args.getString("number");
        }

        view = inflater.inflate(R.layout.yourname_layout, container, false);
        initViews();
        setListeners();

        return view;
    }


    protected interface IActivityEnabledListener {
        void onActivityEnabled(FragmentActivity activity);
    }

    private void getAvailableActivity(IActivityEnabledListener listener) {
        if (getActivity() == null) {
            aeListener = listener;

        } else {
            listener.onActivityEnabled(getActivity());
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (aeListener != null) {
            aeListener.onActivityEnabled((FragmentActivity) context);
            aeListener = null;
        }
    }

    private void initViews() {
        etFn = view.findViewById(R.id.et_fn);
        etLn = view.findViewById(R.id.et_ln);
        tv_continue = view.findViewById(R.id.tv_continue);
        rl_hbf = view.findViewById(R.id.rl_hbf);
    }

    private void setListeners() {
        tv_continue.setOnClickListener(this);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_continue:
                checkValidation();
                break;

        }
    }

    private void checkValidation() {
        getAvailableActivity(new IActivityEnabledListener() {
            @Override
            public void onActivityEnabled(FragmentActivity activity) {

                if (etFn.getText() != null && etLn.getText() != null) {
                    if (!etFn.getText().toString().trim().equals("") && !etLn.getText().toString().trim().equals("")) {
                        fn = etFn.getText().toString().trim();
                        ln = etLn.getText().toString().trim();

                        if ((new NetworkUtil()).isNetworkAvailable(activity)) {
                            signIn(activity);
                        } else {
                            Toast.makeText(activity, "noConnect", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        new CustomToast().Show_Toast(activity, view,
                                getString(R.string.tmpsh));
                    }
                }
            }
        });


    }

    private void showCPV() {
        rl_hbf.setVisibility(View.VISIBLE);
        tv_continue.setEnabled(false);
    }

    private void hideCPV() {
        rl_hbf.setVisibility(View.GONE);
        tv_continue.setEnabled(true);
    }

    private void signIn(final FragmentActivity activity) {
        showCPV();
        ApiInterface apiInterface = KotlinApiClient.INSTANCE.getClient().create(ApiInterface.class);
        Utils.INSTANCE.setYekta();
        String yekta = VarableValues.INSTANCE.getYekta();

        apiInterface.signIn(number, fn, ln, "pr","1","1",yekta).enqueue(new Callback<TempModel>() {
            @Override
            public void onResponse(@NonNull Call<TempModel> call, @NonNull Response<TempModel> response) {
                hideCPV();
                if (response.isSuccessful()) {
                    String val;
                    TempModel tempModel = response.body();
                    assert tempModel != null;
                    val = tempModel.getVal();

                    switch (val) {

                        case "U": {

                            Intent intent=new Intent(activity,ProfileActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("number", number);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            activity.finish();

                            break;
                        }
                        case "no": {
                            new CustomToast().Show_Toast(getActivity(), view,
                                    getString(R.string.vbkhmsh));
                            break;
                        }
                        case "empty": {
                            new CustomToast().Show_Toast(getActivity(), view,
                                    getString(R.string.ltmkhshesh));
                            break;
                        }
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<TempModel> call, @NonNull Throwable t) {

                getAvailableActivity(new IActivityEnabledListener() {
                    @Override
                    public void onActivityEnabled(FragmentActivity activity) {
                        hideCPV();
                        Toast.makeText(activity, R.string.khrda, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}


