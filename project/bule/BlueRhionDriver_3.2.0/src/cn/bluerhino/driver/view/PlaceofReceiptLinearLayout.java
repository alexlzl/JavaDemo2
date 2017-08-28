package cn.bluerhino.driver.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.bluerhino.driver.R;

public class PlaceofReceiptLinearLayout extends LinearLayout implements View.OnClickListener{

	private Context mContext;
	
	private TextView mTakeManView;
	private Button mTakeManPhone;
	private TextView mAddressTitleView;
	private TextView mAddressView;
	private TextView mConsignee_name;
	private TextView mConsignee_phone;
	
	private String mTitle;
	private String mTitleFormat;

	public PlaceofReceiptLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		viewDidLoad();
	}
	

	private void viewDidLoad(){
		Resources res = mContext.getResources();
		//mTitle = 收货地点
		mTitle = res.getString(R.string.place_of_receipt);
		//mTitleFormat = 收货地点%1$d
		mTitleFormat = res.getString(R.string.place_of_receipt_format); 
	}

	@Override
    protected void onFinishInflate() {
	    super.onFinishInflate();
	    loadChildView();
    }
	
	private void loadChildView() {
		//收货人
		mTakeManView = (TextView) findViewById(R.id.placeofreceipt_takeman);
		//收货人手机号
		mTakeManPhone = (Button) findViewById(R.id.placeofreceipt_takemanphone);
		
		mAddressTitleView = (TextView) findViewById(R.id.placeofreceipt_addresstitleview);
		
		mAddressView = (TextView) findViewById(R.id.placeofreceipt_address);
		mConsignee_name = (TextView) findViewById(R.id.orderinfo_consignee_name);
		mConsignee_phone = (TextView) findViewById(R.id.orderinfo_consignee_phone);
		
		//使得收货人手机号可以点击
		if(null != mTakeManPhone){
			mTakeManPhone.setOnClickListener(this);
		}
	}

	public void updateTakeMan(String takeman){
		if(null != takeman){
			mTakeManView.setText(takeman);
		}
	}
	
	public void updateTakeManPhone(String takemanphone){
		if(null != takemanphone){
			mTakeManPhone.setText(takemanphone);
		}
	}
	/**
	 * 直接设置为'收获地点'
	 */
	public void updateAddressTitle(){
		mAddressTitleView.setText(mTitle);
	}
	
	/**
	 * 设置收货地点并加上标号
	 * @param index
	 */
	public void updateAddressTitleIndex(int index){
		mAddressTitleView.setText(String.format(mTitleFormat, index));
	}
	
	public void updateAddress(String address){
		if(null != address){
			mAddressView.setText(" " + address);
		}
	}
	
	public void updateConsigneeName(String name){
		if(null != name){
			mConsignee_name.setVisibility(View.VISIBLE);
			mConsignee_name.setText(name);
		}
	}
	public void updateConsigneePhone(String phone){
		if(null != phone){
			mConsignee_phone.setVisibility(View.VISIBLE);
			mConsignee_phone.setText(phone);
			mConsignee_phone.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mContext.startActivity(initCallIntent(mConsignee_phone.getText()));
				}
			});
		}
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(R.id.placeofreceipt_takemanphone == id){
			contactTakeMan();
		}
	}
	
	private void contactTakeMan(){
		mContext.startActivity(initCallIntent(mTakeManPhone.getText()));
	}
	
	private Intent initCallIntent(CharSequence phoneNum){
		Intent intent =  new Intent(Intent.ACTION_CALL,
		        Uri.parse("tel:"+phoneNum));
       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       return intent;
	}
}
