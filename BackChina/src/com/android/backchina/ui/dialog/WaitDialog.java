package com.android.backchina.ui.dialog;

import com.android.backchina.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class WaitDialog extends Dialog {

	private TextView _messageTv;
	private Context mContext;
	
	public WaitDialog(Context context) {
		super(context);
		mContext = context;
		init(context);
	}

	public WaitDialog(Context context, int defStyle) {
		super(context, defStyle);
		mContext = context;
		init(context);
	}

	protected WaitDialog(Context context, boolean cancelable,DialogInterface.OnCancelListener listener) {
		super(context, cancelable, listener);
		mContext = context;
		init(context);
	}

	public static boolean dismiss(WaitDialog dialog) {
		if (dialog != null) {
			dialog.dismiss();
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.dismiss();
	}

	public static void hide(Context context) {
		if (context instanceof DialogControl)
			((DialogControl) context).hideWaitDialog();
	}

	public static boolean hide(WaitDialog dialog) {
		if (dialog != null) {
			dialog.hide();
			return false;
		} else {
			return true;
		}
	}

	private void init(Context context) {
		setCancelable(false);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_wait, null);
		_messageTv = (TextView) view.findViewById(R.id.waiting_tv);
		setContentView(view);
	}

	public static void show(Context context) {
		if (context instanceof DialogControl)
			((DialogControl) context).showWaitDialog();
	}

	public static boolean show(WaitDialog waitdialog) {
		boolean flag;
		if (waitdialog != null) {
			waitdialog.show();
			flag = false;
		} else {
			flag = true;
		}
		return flag;
	}

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	}

	public void setMessage(int message) {
		_messageTv.setText(message);
	}

	public void setMessage(String message) {
		_messageTv.setText(message);
	}
	
	public void hideMessage() {
	    _messageTv.setVisibility(View.GONE);
	}
}
