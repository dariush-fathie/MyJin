package myjin.pro.ahoora.myjin.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import myjin.pro.ahoora.myjin.activitys.DetailActivity;
import myjin.pro.ahoora.myjin.activitys.LoginActivity;
import myjin.pro.ahoora.myjin.activitys.ServerStatusActivity;
import myjin.pro.ahoora.myjin.interfaces.TempListener;
import myjin.pro.ahoora.myjin.models.TempModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginClass {
    String user;
    String pass;
    String yekta;
    private String val = "";
    private Context mContaxt;
    boolean ch;
    private TempListener tempListener;

    public LoginClass(Context context, TempListener tempListener) {
        this.mContaxt = context;
        pass = "*";
        user = "*";
        ch = false;
        this.tempListener = tempListener;
        callBachServer();
    }


    public LoginClass(String user, String pass, Context context) {
        this.user = user;
        this.pass = pass;
        this.mContaxt = context;
        ch = true;
    }


    public void goToLogin() {
        if (user.trim().length() < 4 || pass.trim().length() < 4) {
            Toast.makeText(mContaxt, "نام کاربری یا رمز عبور موجود نیست", Toast.LENGTH_SHORT).show();
        } else if (user.trim().equals("")) {

            Toast.makeText(mContaxt, "نام کاربری خالی است", Toast.LENGTH_SHORT).show();

        } else if (pass.trim().equals("")) {

            Toast.makeText(mContaxt, "رمز عبور خالی است", Toast.LENGTH_SHORT).show();
        } else {
            callBachServer();
        }


    }

    private void callBachServer() {
        Utils.INSTANCE.setYekta();
        yekta = VarableValues.INSTANCE.getYekta();

        try {
            KotlinApiClient.INSTANCE.getClient().create(ApiInterface.class).login(user, pass, yekta).enqueue(new Callback<TempModel>() {
                @Override
                public void onResponse(@NonNull Call<TempModel> call, @NonNull Response<TempModel> response) {
                    TempModel tempModel = response.body();
                    assert tempModel != null;
                    val = tempModel.getVal();
                    Log.e("val", val + "");

                    if (val.equals("noserver")) {
                        mContaxt.startActivity(new Intent(mContaxt, ServerStatusActivity.class));
                        if (!ch) {
                            ((DetailActivity) mContaxt).finish();
                        } else {
                            ((LoginActivity) mContaxt).finish();
                        }
                    } else {
                        if (val.equals("noUser") || val.equals("wPass")) {
                            if (ch) {
                                Toast.makeText(mContaxt, "نام کاربری یا رمز عبور موجود نیست", Toast.LENGTH_SHORT).show();
                            }
                        } else if (val.equals("okL")) {
                            if (ch) {
                                Toast.makeText(mContaxt, "ورود انجام شد", Toast.LENGTH_SHORT).show();
                                ((LoginActivity) mContaxt).finish();
                            } else {
                                tempListener.IsOk();
                            }

                        } else if (val.equals("empty")) {
                            tempListener.IsNotOk();
                        }
                    }


                }

                @Override
                public void onFailure(@NonNull Call<TempModel> call, @NonNull Throwable t) {

                    if (ch) {
                        Toast.makeText(mContaxt, "خطایی رخ داده است", Toast.LENGTH_SHORT).show();

                    } else {
                        tempListener.IsNotOk();
                    }

                }
            });

        } catch (Exception e) {
            if (ch) {
                Toast.makeText(mContaxt, "خطایی رخ داده است", Toast.LENGTH_SHORT).show();

            } else {
                tempListener.IsNotOk();
            }
        }

    }
}
