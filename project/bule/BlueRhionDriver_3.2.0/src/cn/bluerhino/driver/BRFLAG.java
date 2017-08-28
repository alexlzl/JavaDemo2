package cn.bluerhino.driver;

import android.content.Intent;

public interface BRFLAG {
	
	int PUSH_ACTIVITY_FLAG = Intent.FLAG_ACTIVITY_SINGLE_TOP
			| Intent.FLAG_ACTIVITY_CLEAR_TOP
			| Intent.FLAG_ACTIVITY_NEW_TASK;

}
