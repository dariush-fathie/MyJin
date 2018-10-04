package myjin.pro.ahoora.myjin.fragments.loginAndSign;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import myjin.pro.ahoora.myjin.R;
import myjin.pro.ahoora.myjin.activitys.Login2Activity;

public class Verification_Fragment extends Fragment implements OnClickListener {

	private static View view;
	private static AppCompatEditText et_veriftybox;
	private static AppCompatTextView tv_edit_phone_number,tv_signUp;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.verification_layout, container, false);
		initViews();
		setListeners();
		return view;
	}

	private void initViews() {
		et_veriftybox = view.findViewById(R.id.et_veriftybox);
		tv_edit_phone_number = view.findViewById(R.id.tv_edit_phone_number);
		tv_signUp = view.findViewById(R.id.tv_signUp);
	}

	private void setListeners() {
		tv_signUp.setOnClickListener(this);
		tv_edit_phone_number.setOnClickListener(this);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		fillEt_veriftybox();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_signUp:
			checkValidation();
			break;

		case R.id.tv_edit_phone_number:
			t.cancel();
			new Login2Activity().replaceLoginFragment();
			break;
		}
	}

	private void checkValidation() {
		Toast.makeText(getActivity(), "Go To Profile", Toast.LENGTH_SHORT).show();
	}
	int v=0;
	final Timer t = new Timer();
	public void fillEt_veriftybox() {
		final Handler handler = new Handler();
		t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					@SuppressLint("SetTextI18n")
					@Override
					public void run() {
						v++;
						et_veriftybox.setText(et_veriftybox.getText()+(v+""));
						if (v>5){
							t.cancel();
						}
					}
				});
			}
		}, 1, 300);
	}
}
