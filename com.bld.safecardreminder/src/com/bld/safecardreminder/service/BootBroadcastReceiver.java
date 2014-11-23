package com.bld.safecardreminder.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 设备开机启动注册服务
 * 
 * @author ballad
 * @version 1.0 beta
 * 
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		Intent intent = new Intent(arg0, NotificationService.class);
		arg0.startService(intent);
		Log.v("BLD-SCM", "CreditCardReminder is start......");
	}

}
