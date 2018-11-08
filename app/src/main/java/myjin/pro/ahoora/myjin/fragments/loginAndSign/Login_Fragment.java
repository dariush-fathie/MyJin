package myjin.pro.ahoora.myjin.fragments.loginAndSign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import myjin.pro.ahoora.myjin.R;
import myjin.pro.ahoora.myjin.customClasses.CustomToast;
import myjin.pro.ahoora.myjin.models.TempModel;
import myjin.pro.ahoora.myjin.utils.ApiInterface;
import myjin.pro.ahoora.myjin.utils.KotlinApiClient;
import myjin.pro.ahoora.myjin.utils.NetworkUtil;
import myjin.pro.ahoora.myjin.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login_Fragment extends Fragment implements OnClickListener {

    @SuppressLint("StaticFieldLeak")
    private static View view;

    @SuppressLint("StaticFieldLeak")
    private static AppCompatEditText etPhonebox;

    private static FragmentManager fragmentManager;
    private AppCompatTextView tv_continue;
    private RelativeLayout rl_hbf;
    protected IActivityEnabledListener aeListener;


    public Login_Fragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_layout2, container, false);
        initViews();
        setListeners();
        return view;
    }


    protected interface IActivityEnabledListener {
        void onActivityEnabled(FragmentActivity activity);
    }

    protected void getAvailableActivity(IActivityEnabledListener listener) {
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
        getAvailableActivity(new IActivityEnabledListener() {
            @Override
            public void onActivityEnabled(FragmentActivity activity) {
                fragmentManager = activity.getSupportFragmentManager();
                etPhonebox = view.findViewById(R.id.et_phonebox);
                tv_continue = view.findViewById(R.id.tv_continue);
                rl_hbf=view.findViewById(R.id.rl_hbf);
            }
        });


    }

    private void setListeners() {
        tv_continue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        checkValidation();
    }

    private void checkValidation() {
        final String getPhoneNumber = "0" + etPhonebox.getText();

        if (getPhoneNumber.equals("0")) {

            new CustomToast().Show_Toast(getActivity(), view,
                    getString(R.string.shhkhrvk));
            makeVibrate();

        } else if (getPhoneNumber.length()!= 11) {
            new CustomToast().Show_Toast(getActivity(), view,
                    getString(R.string.shhen));
            makeVibrate();
        } else if (getPhoneNumber.charAt(1) == '0') {
            new CustomToast().Show_Toast(getActivity(), view,
                    getString(R.string.shhtovsh));
            makeVibrate();
        } else {

            getAvailableActivity(new IActivityEnabledListener() {
                @Override
                public void onActivityEnabled(FragmentActivity activity) {
                    if ((new NetworkUtil()).isNetworkAvailable(activity)) {
                        sendSms(getPhoneNumber);
                    } else {
                        new CustomToast().Show_Toast(getActivity(), view,
                                getString(R.string.checkYourConnection));
                        makeVibrate();
                    }
                }
            });


        }


    }



    private void showCPV() {
        rl_hbf.setVisibility(View.VISIBLE);
        tv_continue.setEnabled(false);
    }

    private void hideCPV() {
        rl_hbf.setVisibility(View.GONE);
        tv_continue.setEnabled(true);
    }

    private void sendSms(final String phoneNumber) {
        showCPV();

        ApiInterface apiInterface = KotlinApiClient.INSTANCE.getClient().create(ApiInterface.class);

        apiInterface.sendSms(phoneNumber).enqueue(new Callback<TempModel>() {
            @Override
            public void onResponse(@NonNull Call<TempModel> call, @NonNull Response<TempModel> response) {
                hideCPV();
                if (response.isSuccessful()) {
                    String val;
                    TempModel tempModel = response.body();
                    assert tempModel != null;
                    val = tempModel.getVal();


                    if (val.substring(0, 3).equals("yes")) {

                        Bundle bundle = new Bundle();
                        bundle.putString("vc", val.substring(3));
                        bundle.putString("number", phoneNumber);

                        Fragment vfr = new Verification_Fragment();
                        vfr.setArguments(bundle);

                        fragmentManager
                                .beginTransaction()
                                .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                                .replace(R.id.frameContainer, vfr,
                                        Utils.INSTANCE.getVerification_Fragment()).commit();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TempModel> call, @NonNull Throwable t) {

                getAvailableActivity(new IActivityEnabledListener() {
                    @Override
                    public void onActivityEnabled(FragmentActivity activity) {
                        hideCPV();
                        makeVibrate();
                        Toast.makeText(activity, R.string.khrda, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void makeVibrate(){

        getAvailableActivity(new IActivityEnabledListener() {
            @Override
            public void onActivityEnabled(FragmentActivity activity) {
                Vibrator v = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(500);
                }
            }
        });

    }

}
