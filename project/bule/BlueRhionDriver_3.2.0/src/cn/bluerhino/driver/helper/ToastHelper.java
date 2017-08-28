package cn.bluerhino.driver.helper;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class ToastHelper {
	
    /**
     * 弹出toast
     * @param toast 要弹出的内容
     * @param context 
     */
    public static void showToast(final String toast, final Context context)
    {
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}).start();
    }

}
