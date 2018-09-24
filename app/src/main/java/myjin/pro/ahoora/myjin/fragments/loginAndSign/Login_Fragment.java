package myjin.pro.ahoora.myjin.fragments.loginAndSign;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import myjin.pro.ahoora.myjin.R;
import myjin.pro.ahoora.myjin.customClasses.CustomToast;
import myjin.pro.ahoora.myjin.models.events.SetTitleEvent;
import myjin.pro.ahoora.myjin.utils.Utils;

public class Login_Fragment extends Fragment implements OnClickListener {

    private static View view;

    private static AppCompatEditText emailid, password;
	private static AppCompatTextView forgotPassword, signUp,loginButton,LoginWithG;
	//private static CheckBox show_hide_password;
	private static CardView cv_login;
	private static Animation shakeAnimation;
	private static FragmentManager fragmentManager;

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

	// Initiate Views
	private void initViews() {
		fragmentManager = getActivity().getSupportFragmentManager();

		emailid = view.findViewById(R.id.login_emailid);
		password =view.findViewById(R.id.login_password);
		loginButton = view.findViewById(R.id.loginBtn);
		LoginWithG=view.findViewById(R.id.tv_loginWithG);
		forgotPassword = view.findViewById(R.id.forgot_password);
		signUp = view.findViewById(R.id.createAccount);
		/*show_hide_password = view
				.findViewById(R.id.show_hide_password);*/
		cv_login = view.findViewById(R.id.cv_login);

		// Load ShakeAnimation
		shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.shake);


	}

	// Set Listeners
	private void setListeners() {
		loginButton.setOnClickListener(this);
		forgotPassword.setOnClickListener(this);
		signUp.setOnClickListener(this);
		LoginWithG.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.loginBtn:
				checkValidation();
				break;

			case R.id.forgot_password:

				// Replace forgot password fragment with animation
				EventBus.getDefault().post(new SetTitleEvent("بازیابی"));
				fragmentManager
						.beginTransaction()
						.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
						.replace(R.id.frameContainer,
								new ForgotPassword_Fragment(),
								Utils.INSTANCE.getForgotPassword_Fragment()).commit();
				break;
			case R.id.createAccount:

				// Replace signup frgament with animation
				EventBus.getDefault().post(new SetTitleEvent("ثبت نام"));
				fragmentManager
						.beginTransaction()
						.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
						.replace(R.id.frameContainer, new SignUp_Fragment(),
								Utils.INSTANCE.getSignUp_Fragment()).commit();
				break;

			case R.id.tv_loginWithG:
				new CustomToast().Show_Toast(getActivity(), view,
						"ورود با گوگل");

		}

	}

	// Check Validation before login
	private void checkValidation() {
		// Get email id and password
		String getEmailId = emailid.getText().toString();
		String getPassword = password.getText().toString();

		// Check patter for email id
		Pattern p = Pattern.compile(Utils.INSTANCE.getRegEx());

		Matcher m = p.matcher(getEmailId);

		// Check for both field is empty or not
		if (getEmailId.equals("") || getEmailId.length() == 0
				|| getPassword.equals("") || getPassword.length() == 0) {
			cv_login.startAnimation(shakeAnimation);
			new CustomToast().Show_Toast(getActivity(), view,
					"هر دو مورد را وارد کنید");

		}
		// Check if email id is valid or not
		else if (!m.find())
			new CustomToast().Show_Toast(getActivity(), view,
					"ایمیل اعتبار ندارد.");
			// Else do login and do your stuff
		else
			Toast.makeText(getActivity(), "Do Login.", Toast.LENGTH_SHORT)
					.show();

	}
}
