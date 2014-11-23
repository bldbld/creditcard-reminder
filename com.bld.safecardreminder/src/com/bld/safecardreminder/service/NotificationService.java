package com.bld.safecardreminder.service;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.bld.safecardreminder.R;
import com.bld.safecardreminder.RemindActivity;
import com.bld.safecardreminder.bean.CreditCard;
import com.bld.safecardreminder.bean.RemindInfo;
import com.bld.safecardreminder.bean.PaidInfo;
import com.bld.safecardreminder.bean.SettingInfo;
import com.bld.safecardreminder.data.DataProvider;
import com.bld.safecardreminder.data.PaidInfoProvider;

/**
 * 提醒服务
 * 
 * @author ballad
 * @version 1.0 beta
 */
public class NotificationService extends Service {

	public IBinder binder = new NotificationService.LocalBinder();

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}

	/**
	 * 定义内容类继承Binder
	 * 
	 * @author ballad
	 * 
	 */
	public class LocalBinder extends Binder {
		// 返回本地服务
		NotificationService getService() {
			return NotificationService.this;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		NotificationBuilder builder = new NotificationBuilder();
		// 获得数据
		List<CreditCard> cardList = DataProvider.getCardList(this.getSharedPreferences(DataProvider.CONFIG_FILE_NAME, MODE_PRIVATE), this);
		SettingInfo settingInfo = DataProvider.getSetting(this.getSharedPreferences(DataProvider.CONFIG_FILE_NAME, MODE_PRIVATE), this);
		List<String> cardIdList = DataProvider.getIdList(this.getSharedPreferences(DataProvider.CONFIG_FILE_NAME, MODE_PRIVATE));
		// 获得历史提醒信息
		PaidInfo historyRemindInfo = PaidInfoProvider
				.getPaidInfo(this.getSharedPreferences(PaidInfoProvider.CONFIG_FILE_NAME, MODE_PRIVATE), cardIdList);
		for (CreditCard card : cardList) {
			RemindInfo remindinfo = card.getRemindInfo(settingInfo);
			if (historyRemindInfo.isCardPay(card.id, remindinfo.billingYear, remindinfo.billingMonth)) {
				// 如果已经还款，则不提醒
				break;
			}
			switch (remindinfo.remindLevel) {
			case RemindInfo.REMIND_LEVEL_OVERTIME:
				builder.buildNotification(this, RemindInfo.REMIND_TITLE, RemindInfo.REMIND_TITLE, remindinfo.remindText);
				break;
			case RemindInfo.REMIND_LEVEL_REMIND_NORMAL:
				builder.buildNotification(this, RemindInfo.REMIND_TITLE, RemindInfo.REMIND_TITLE, remindinfo.remindText);
				break;
			case RemindInfo.REMIND_LEVEL_TODAY:
				builder.buildNotification(this, RemindInfo.REMIND_TITLE, RemindInfo.REMIND_TITLE, remindinfo.remindText);
				break;
			case RemindInfo.REMIND_LEVEL_NONE:
				break;
			default:
				;
			}
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	class NotificationBuilder {
		@SuppressWarnings("deprecation")
		public void buildNotification(Context ctx, String noticeInfoStr, String title, String body) {
			// 获得提醒管理器
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			Notification n = new Notification(R.drawable.ic_launcher, noticeInfoStr, System.currentTimeMillis());
			n.flags = Notification.FLAG_AUTO_CANCEL;
			Intent i = new Intent(ctx, RemindActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			// PendingIntent
			PendingIntent contentIntent = PendingIntent.getActivity(ctx, R.string.app_name, i, PendingIntent.FLAG_UPDATE_CURRENT);

			n.setLatestEventInfo(ctx, title, body, contentIntent);
			notificationManager.notify(R.string.app_name, n);
		}
	}
}
