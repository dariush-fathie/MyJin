package myjin.pro.ahoora.myjin.activitys;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.View.OnClickListener;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import myjin.pro.ahoora.myjin.R;
import myjin.pro.ahoora.myjin.fragments.loginAndSign.Login_Fragment;
import myjin.pro.ahoora.myjin.models.events.SetTitleEvent;
import myjin.pro.ahoora.myjin.utils.Utils;

public class Login2Activity extends AppCompatActivity {
	private static FragmentManager fragmentManager;
	private AppCompatTextView setTile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login2);
		fragmentManager = getSupportFragmentManager();

		setTile=findViewById(R.id.tv_LoginTitle);
		// If savedinstnacestate is null then replace login fragment
		if (savedInstanceState == null) {
			fragmentManager
					.beginTransaction()
					.replace(R.id.frameContainer, new Login_Fragment(),
							Utils.INSTANCE.getLogin_Fragment()).commit();
		}

		// On close icon click finish activity
		findViewById(R.id.iv_goback).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						onBackPressed();

					}
				});

	}

	// Replace Login Fragment with animation
    public void replaceLoginFragment() {
		fragmentManager
				.beginTransaction()
				.setCustomAnimations(R.anim.left_enter, R.anim.right_out)
				.replace(R.id.frameContainer, new Login_Fragment(),
						Utils.INSTANCE.getLogin_Fragment()).commit();
	}

	@Override
	public void onBackPressed() {

		// Find the tag of signup and forgot password fragment
		Fragment SignUp_Fragment = fragmentManager
				.findFragmentByTag(Utils.INSTANCE.getSignUp_Fragment());
		Fragment ForgotPassword_Fragment = fragmentManager
				.findFragmentByTag(Utils.INSTANCE.getForgotPassword_Fragment());

		// Check if both are null or not
		// If both are not null then replace login fragment else do backpressed
		// task
		EventBus.getDefault().post(new SetTitleEvent("ورود"));
		if (SignUp_Fragment != null)
			replaceLoginFragment();
		else if (ForgotPassword_Fragment != null)
			replaceLoginFragment();
		else
			super.onBackPressed();
	}

	@Override
	protected void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
	}

	@Override
	protected void onStop() {
		EventBus.getDefault().unregister(this);
		super.onStop();
	}

	@Subscribe
	void setTitle(SetTitleEvent e){
		setTile.setText(e.getVal_());
	}
}
