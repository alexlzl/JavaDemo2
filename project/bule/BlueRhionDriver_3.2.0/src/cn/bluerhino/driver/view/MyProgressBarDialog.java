package cn.bluerhino.driver.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import cn.bluerhino.driver.R;

/**
 * 
* @Description: 自定义对话框
* @author: sunliang
* @date 2015年8月31日 下午6:06:29
*
 */
public class MyProgressBarDialog extends Dialog {
	private NumberProgressBar numberBars;
	Context context;
	String title;

	public MyProgressBarDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.my_progressbar_dialog);
		numberBars = (NumberProgressBar) findViewById(R.id.numberbars);
		this.setCancelable(true);
		setTitle("正在上传,请稍后...");
	}

	public MyProgressBarDialog showDialog(Context context) {
		MyProgressBarDialog dialog = new MyProgressBarDialog(context);
		dialog.show();
		return dialog;
	}

	public void setProgress(int progress) {
		numberBars.setProgress(progress);
	}

	public void incrementProgressBy(int by) {
		numberBars.incrementProgressBy(by);
	}

	public void setTitle(String dialogTitle) {
		if (dialogTitle == null) {
			return;
		}
		((TextView) findViewById(R.id.dialog_title)).setText(dialogTitle);
	}

	public void setTitle(int dialogTitle) {
		String title = context.getResources().getString(dialogTitle);
		if (title == null) {
			return;
		}
		((TextView) findViewById(R.id.dialog_title)).setText(title);
	}

}
