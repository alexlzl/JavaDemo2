package com.example.testview;

import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	LinearLayout ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ll=(LinearLayout) findViewById(R.id.parent);
	}
	
	public void ontest(View view){
		/**
		 * 插入1
		 */
		
		TextView tv1=new TextView(this);
		tv1.setTextColor(Color.RED);
		tv1.setText("add==1");
		ll.addView(tv1, 1);
		Log.e("tag", ll.toString());
		/**
		 * 插入2
		 */
		TextView tv2=new TextView(this);
		tv2.setTextColor(Color.RED);
		tv2.setText("add==2");
		ll.addView(tv2, 2);
		Log.e("tag", ll.toString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
