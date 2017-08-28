package com.kale.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

	DialogManager dm;
	String msg = "内容";
	String[] str = new String[] { "android", "java", "ios" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dm = new DialogManager(this);

		
	}

	public void buttonListener(View v) {
		dm = new DialogManager(this);
		switch (v.getId()) {
		case R.id.simple_button_id:
			dm.simpleDialog("最简单的对话框", msg);
			break;
		case R.id.list_button_id:
			dm.listDialog("列表对话框", str);
			break;
		case R.id.singleChoice_button_id:
			dm.singleChoiceDialog("单选对话框", str);
			break;
		case R.id.multiChoice_button_id:
			dm.MultiChoiceDialog("多选对话框", str);
			break;
		case R.id.adapter_button_id:
			dm.adapterDialog("用适配器建立的对话框", str);
			break;
		case R.id.view_button_id:
			dm.viewDialog("采用自定义视图的对话框");
			break;
		case R.id.progress_button_id:
			dm.progressDialog("含进度条的对话框",msg);
			break;
		case R.id.activity_button_id:
			startActivity(new Intent(MainActivity.this,DialogActivity.class));
			break;
		case R.id.popup_button_id:
			dm.popupWindowDialog("PopupWindows对话框", v);
			break;
		case R.id.date_button_id:
			dm.dateDialog();
			break;
		case R.id.time_button_id:
			dm.timeDialog();
			break;			
		default:
			break;
		}
	}

}
