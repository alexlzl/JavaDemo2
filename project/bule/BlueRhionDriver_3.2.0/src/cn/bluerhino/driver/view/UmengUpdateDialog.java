package cn.bluerhino.driver.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.bluerhino.driver.R;

public class UmengUpdateDialog extends Dialog {
	
	private static final String TAG = UmengUpdateDialog.class.getSimpleName(); 

	@InjectView(R.id.umeng_update_content)
	TextView mMessageTextView;

	@InjectView(R.id.umeng_update_id_ok)
	Button mOkayButton;

	@InjectView(R.id.umeng_update_id_cancel)
	Button mCancelButton;

	@InjectView(R.id.umeng_update_id_check)
	CheckBox mUpdateIgnoreCheckBox;
	
	private final Delegate mDelegate;
	private final DataSource mDataSource;
	
	public UmengUpdateDialog(Context context, Delegate delegate, DataSource dataSource) {
		super(context, R.style.umeng_update_dialog_background_custom);
		mDelegate = delegate;
		mDataSource = dataSource;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
		setCancelable(false);

		setContentView(R.layout.umeng_update_dialog);
		ButterKnife.inject(this);
	}
	
	@Override
    protected void onStart() {
	    super.onStart();
		viewDidload();
    }

	private void viewDidload() {
		String msg = mDataSource.getMessage();
		mMessageTextView.setText(msg);
		
		boolean isForceUpdate = mDataSource.isForceUpdate();
		if (isForceUpdate) {
			mCancelButton.setText(R.string.UMCLOSE);
			mUpdateIgnoreCheckBox.setVisibility(View.GONE);
		} else {
			mCancelButton.setText(R.string.UMNotNow);
			mUpdateIgnoreCheckBox.setVisibility(View.VISIBLE);
		}
	}

	@OnClick(R.id.umeng_update_id_ok)
    void downLoadApk() {
		Log.i(TAG, "downloadApk");
		dismiss();
		mDelegate.downloadApk();
    }

    @OnClick(R.id.umeng_update_id_cancel)
	void cancelUpdate() {
		Log.i(TAG, "cancelUpdete");
		dismiss();

		if (mDataSource.isForceUpdate()) {
			mDelegate.cancelUpdate();
		}
	}
    
	@OnCheckedChanged(R.id.umeng_update_id_check)
	void onIgonreCheckUpdate(boolean checked) {
		Log.i(TAG, "onIgonreCheckUpdate");
		mUpdateIgnoreCheckBox.setChecked(checked);
		mDelegate.ignoreCheckUpdate(checked);
	}

	public interface Delegate {
		void downloadApk();
		
		void cancelUpdate();
		
		void ignoreCheckUpdate(boolean isIgonre);
	}
	
	public interface DataSource {
		boolean isForceUpdate();
		boolean isIgonreUpdate();
		String getMessage();
	}
}
