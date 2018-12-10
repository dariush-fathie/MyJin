package myjin.pro.ahoora.myjin.activities;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import myjin.pro.ahoora.myjin.R;
import myjin.pro.ahoora.myjin.fragments.loginAndSign.Login_Fragment;
import myjin.pro.ahoora.myjin.utils.Utils;

public class Login2Activity extends AppCompatActivity {
    private static FragmentManager fragmentManager;

    String vc,number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, new Login_Fragment(),
                            Utils.INSTANCE.getLogin_Fragment()).commit();
        }

        findViewById(R.id.iv_goback).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        onBackPressed();

                    }
                });

    }

    public void replaceLoginFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.frameContainer, new Login_Fragment(),
                        Utils.INSTANCE.getLogin_Fragment()).commit();
    }

    @Override
    public void onBackPressed() {
        Fragment SignUp_Fragment = fragmentManager
                .findFragmentByTag(Utils.INSTANCE.getVerification_Fragment());
        Fragment YourName_Fragment = fragmentManager
                .findFragmentByTag(Utils.INSTANCE.getYourName_Fragment());

        if (SignUp_Fragment != null|| YourName_Fragment!= null)
            replaceLoginFragment();

        else
            super.onBackPressed();
    }


}
