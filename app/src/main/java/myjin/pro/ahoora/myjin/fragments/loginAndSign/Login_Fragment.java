package myjin.pro.ahoora.myjin.fragments.loginAndSign;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import myjin.pro.ahoora.myjin.R;
import myjin.pro.ahoora.myjin.customClasses.CustomToast;
import myjin.pro.ahoora.myjin.utils.Utils;

public class Login_Fragment extends Fragment implements OnClickListener {

    private static View view;

    private static AppCompatEditText et_phonebox;

    private static FragmentManager fragmentManager;
    private AppCompatTextView tv_continue;

    public Login_Fragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_layout2, container, false);
        initViews();
        setListeners();
        return view;
    }

    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();
        et_phonebox = view.findViewById(R.id.et_phonebox);
        tv_continue=view.findViewById(R.id.tv_continue);
    }

    private void setListeners() {
        tv_continue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        checkValidation();
    }
    private void checkValidation() {
        String getPhoneNumber = et_phonebox.getText().toString();

        if (getPhoneNumber.equals("")) {

            new CustomToast().Show_Toast(getActivity(), view,
                    "گزینه مورد نظر خالی است");

        } else if (getPhoneNumber.length() < 10) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "شماره همراه کمتر از 10 رقم نباشد");
        } else if (getPhoneNumber.length() > 10) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "شماره همراه بیشتر از 10 رقم نباشد");
        } else


              fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                .replace(R.id.frameContainer, new Verification_Fragment(),
                        Utils.INSTANCE.getVerification_Fragment()).commit();


    }
}
