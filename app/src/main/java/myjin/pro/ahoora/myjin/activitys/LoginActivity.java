package myjin.pro.ahoora.myjin.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;

import myjin.pro.ahoora.myjin.R;
import myjin.pro.ahoora.myjin.utils.LoginClass;
import myjin.pro.ahoora.myjin.utils.ServerStatus;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatEditText et_user, et_pass;
    private ImageView iv_goback;
    AppCompatTextView tv_login;
    private LoginClass loginClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ABC();
        init_();
    }

    private void ABC() {
        et_user = findViewById(R.id.et_user);
        et_pass = findViewById(R.id.et_pass);
        tv_login = findViewById(R.id.tv_login);
        iv_goback = findViewById(R.id.iv_goback);
    }

    private void init_() {
        tv_login.setOnClickListener(this);
        iv_goback.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                loginClass = new LoginClass(et_user.getText().toString(), et_pass.getText().toString(), LoginActivity.this);
                loginClass.goToLogin();
                break;
            case R.id.iv_goback:
                finish();


                break;
        }
    }


}
