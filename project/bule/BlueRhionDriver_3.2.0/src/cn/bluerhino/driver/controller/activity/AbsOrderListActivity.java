package cn.bluerhino.driver.controller.activity;

import com.umeng.analytics.MobclickAgent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bluerhino.driver.R;

public abstract class AbsOrderListActivity extends BaseParentActivity {
	
    @InjectView(R.id.left_img)
    ImageView mBackImageView;
    @InjectView(R.id.center_title)
    TextView mTitleTextView;
    
    @Override
    protected void onCreate(Bundle bundle) {
    	super.onCreate(bundle);
    	if(super.checkUserIsLogin()){
    		initView();
    		mobEventStatistic();
    	}
    }

	private void initView() {
    	setContentView(setContentViewId());
    	ButterKnife.inject(this);
        mBackImageView.setVisibility(View.VISIBLE);
        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	AbsOrderListActivity.this.onBackPressed();
            }
        });
        mTitleTextView.setText(getCenterTitle());
	}
	
	
	private void mobEventStatistic(){
		MobclickAgent.onEvent(this, getString(getStatisticParam()));
	}
	
	protected abstract int setContentViewId();
	
	protected abstract int getCenterTitle();
	
	protected abstract int getStatisticParam();

}
