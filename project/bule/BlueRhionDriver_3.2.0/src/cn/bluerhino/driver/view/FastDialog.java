package cn.bluerhino.driver.view;

import cn.bluerhino.driver.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class FastDialog extends Dialog {
	
	private TextView mDialog_title;
	private TextView mMoneyView;
	private TextView mMainDesc;
	private TextView mLeft;
	private TextView mRight;
	
	private String titleText;
	private String mainText;
	private String maindesc;
	private String leftDes;
	private String rightDes;
	
	public interface OnClick {
		void onLeftClick();
		void onRightClick();
	}
	private OnClick mOnClick;
	
	private final OnClick defaultOnClickListener = new FastDialog.OnClick() {
		@Override
		public void onLeftClick() {}
		@Override
		public void onRightClick() {}
	};
	
	public FastDialog(Context context, String titleText,String mainText, String maindesc, String leftDes, String rightDes) {
		this(context, titleText, mainText, maindesc, leftDes, rightDes, null);
	}
	
	public FastDialog(Context context, String titleText,String mainText, String maindesc, String leftDes, String rightDes, OnClick onClick) {
		this(context, R.style.payment_selected_dialog_theme, titleText, mainText, maindesc, leftDes, rightDes, onClick);
	}
	
	public FastDialog(Context context, int theme, String titleText,String mainText, String maindesc, String leftDes, String rightDes, OnClick onClick) {
		super(context, theme);
		this.titleText = titleText;
		if(mainText == null){
			mainText="";
		}
		if(maindesc == null){
			maindesc="";
		}
		this.mainText = mainText;
		this.maindesc = maindesc;
		this.leftDes = leftDes;
		this.rightDes = rightDes;
		if(onClick == null){
			mOnClick = defaultOnClickListener;
		}else{
			mOnClick = onClick;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payinfo_dialog_bg);
		mDialog_title = (TextView) findViewById(R.id.dialog_title);
		mMoneyView = (TextView) findViewById(R.id.money);
		mMainDesc = (TextView) findViewById(R.id.main_desc);
		mLeft = (TextView) findViewById(R.id.left);
		mRight = (TextView) findViewById(R.id.right);
		
		mDialog_title.setText(titleText);
		if(TextUtils.isEmpty(mainText)){
			mMoneyView.setVisibility(View.GONE);
		}else{
			mMoneyView.setText(mainText);
		}
		mMainDesc.setText(maindesc);
		mLeft.setText(leftDes);
		mRight.setText(rightDes);
		
		mLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				mOnClick.onLeftClick();
			}
		});
		mRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				mOnClick.onRightClick();	
			}
		});
	}
	
	public void setOnClickListener(OnClick onClick){
		if(onClick == null){
			onClick = defaultOnClickListener;
		}
		mOnClick = onClick;
	}
	
	public void setMainText(String mainText){
		mMainDesc.setText(mainText);
	}

}
