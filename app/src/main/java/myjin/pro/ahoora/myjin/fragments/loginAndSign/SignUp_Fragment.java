package myjin.pro.ahoora.myjin.fragments.loginAndSign;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import myjin.pro.ahoora.myjin.R;
import myjin.pro.ahoora.myjin.activitys.Login2Activity;
import myjin.pro.ahoora.myjin.customClasses.CustomToast;
import myjin.pro.ahoora.myjin.models.events.SetTitleEvent;
import myjin.pro.ahoora.myjin.utils.Utils;

public class SignUp_Fragment extends Fragment implements OnClickListener {
	private static View view;
	private static EditText alias, emailId, password, confirmPassword;
	private static TextView already_user;
	private static AppCompatTextView tv_signUp;

	public SignUp_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.signup_layout, container, false);
		initViews();
		setListeners();
		return view;
	}

	// Initialize all views
	private void initViews() {
		alias = view.findViewById(R.id.et_alias);
		emailId = view.findViewById(R.id.userEmailId);
		password = view.findViewById(R.id.et_pass);
		confirmPassword = view.findViewById(R.id.et_confirmPassword);
		tv_signUp = view.findViewById(R.id.tv_signUp);
		already_user= view.findViewById(R.id.already_user);

	}

	// Set Listeners
	private void setListeners() {
		tv_signUp.setOnClickListener(this);
		already_user.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_signUp:

			// Call checkValidation method
			checkValidation();
			break;

		case R.id.already_user:

			// Replace login fragment
			EventBus.getDefault().post(new SetTitleEvent("ورود"));
			new Login2Activity().replaceLoginFragment();
			break;
		}

	}

	// Check Validation Method
	private void checkValidation() {

		// Get all edittext texts
		String getFullName = alias.getText().toString();
		String getEmailId = emailId.getText().toString();
		String getPassword = password.getText().toString();
		String getConfirmPassword = confirmPassword.getText().toString();

		// Pattern match for email id
		Pattern p = Pattern.compile(Utils.INSTANCE.getRegEx());
		Matcher m = p.matcher(getEmailId);

		// Check if all strings are null or not
		if (getFullName.equals("") || getFullName.length() == 0
				|| getEmailId.equals("") || getEmailId.length() == 0
				|| getPassword.equals("") || getPassword.length() == 0
				|| getConfirmPassword.equals("")
				|| getConfirmPassword.length() == 0)

			new CustomToast().Show_Toast(getActivity(), view,
					"تمام موارد خواسته شده را وارد کنید");

		// Check if email id valid or not
		else if (!m.find())
			new CustomToast().Show_Toast(getActivity(), view,
					"ایمیل اعتبار ندارد.");

		// Check if both password should be equal
		else if (!getConfirmPassword.equals(getPassword))
			new CustomToast().Show_Toast(getActivity(), view,
					"رمز های وارد شده با هم تطابق ندارند");

		// Else do signup or do your stuff
		else
			Toast.makeText(getActivity(), "Do SignUp.", Toast.LENGTH_SHORT)
					.show();

	}
}
