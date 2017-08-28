package com.example.testviewlocation;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv=(TextView) findViewById(R.id.test);
		
	}

	public void test(View view){
		int[] windowLocation=new int[2];
		int[] screenLocation=new int[2];
		view.getLocationInWindow(windowLocation);
		view.getLocationOnScreen(screenLocation);
		tv.setText("坐标"+"windowLocation=="+windowLocation[0]+"==="+windowLocation[1]+"screenLocation=="+screenLocation[0]+"==="+screenLocation[1]);
	}
}
