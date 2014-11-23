package com.bld.safecardreminder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bld.safecardreminder.adapter.RemindListAdapter;
import com.bld.safecardreminder.bean.CreditCard;
import com.bld.safecardreminder.bean.PaidInfo;
import com.bld.safecardreminder.bean.RemindInfo;
import com.bld.safecardreminder.bean.SettingInfo;
import com.bld.safecardreminder.data.DataProvider;
import com.bld.safecardreminder.data.PaidInfoProvider;
import com.bld.safecardreminder.service.NotificationService;

/**
 * ������-��ʾ������Ϣ
 * 
 * @author ballad
 * @version 0.1 beta
 */
public class RemindActivity extends Activity {

	/**
	 * �˵���-�˵�ID-��Ƭ�б�
	 */
	public static final int MENU_ID_CARD_LIST = 3;

	/**
	 * �˵���-�˵�ID-����
	 */
	public static final int MENU_ID_SETTING = 1;

	/**
	 * �����Ĳ˵�-�˵�ID-ɾ��
	 */
	public static final int CTXMENU_ID_DELETE = 11;

	/**
	 * �������-ˢ�������б�
	 */
	public static final int RESULT_CODE_REFRESH_REMIND_LIST = 2;

	/**
	 * ListView��Item��MAP-KEY
	 */
	public static final String LIST_VIEW_MAP_KEY_TITLE = "textViewTitle";

	/**
	 * ListView��Item��MAP-KEY
	 */
	public static final String LIST_VIEW_MAP_KEY_DESC = "textViewDesc";

	/**
	 * ListView��Item��MAP-KEY
	 */
	public static final String LIST_VIEW_MAP_KEY_CARD = "card";

	/**
	 * ListView��Item��MAP-KEY
	 */
	public static final String LIST_VIEW_MAP_KEY_REMINDINFO = "remindInfo";

	/**
	 * �����б�
	 */
	private ListView list;

	/**
	 * �����б������
	 */
	private ArrayList<HashMap<String, Object>> listItem;

	/**
	 * �����б�ؼ�������
	 */
	private SimpleAdapter listItemAdapter;

	/**
	 * ѡ��������
	 */
	private int selectIndex = -1;

	/**
	 * ��ʷ������Ϣ
	 */
	private PaidInfo historyRemindInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.activity_remind, null);
		layout.setBackgroundColor(Color.BLUE);
		setContentView(R.layout.activity_remind);
		setTitle(getResources().getString(R.string.title_activity_remind_activitytitle));
		initListViewReminder();
		startService();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// ���Ը��ݶ���������������Ӧ�Ĳ���
		if (RESULT_CODE_REFRESH_REMIND_LIST == resultCode) {
			initListViewReminder();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.remind, menu);
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_ID_CARD_LIST, 1, getResources().getString(R.string.title_activity_card_list_activitytitle));
		menu.add(0, MENU_ID_SETTING, 2, getResources().getString(R.string.title_activity_setting_activitytitle));
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ID_CARD_LIST:
			Intent intent = new Intent(RemindActivity.this, CardListActivity.class);
			startActivity(intent);
			// BaseApplication appState =
			// (BaseApplication)BaseActivity.this.getApplication();
			// appState.stopService();
			// int pid = android.os.Process.myPid();
			// android.os.Process.killProcess(pid);
			break;
		case MENU_ID_SETTING:
			Intent setIntent = new Intent(RemindActivity.this, SettingActivity.class);
			startActivity(setIntent);
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	/**
	 * ��ʼ�������б�
	 */
	public void initListViewReminder() {
		list = (ListView) findViewById(R.id.listViewReminder);

		// ���ɶ�̬���飬��������
		listItem = new ArrayList<HashMap<String, Object>>();
		// �������
		List<CreditCard> cardList = DataProvider.getCardList(this.getSharedPreferences(DataProvider.CONFIG_FILE_NAME, MODE_PRIVATE), this);
		SettingInfo settingInfo = DataProvider.getSetting(this.getSharedPreferences(DataProvider.CONFIG_FILE_NAME, MODE_PRIVATE), this);
		List<String> cardIdList = DataProvider.getIdList(this.getSharedPreferences(DataProvider.CONFIG_FILE_NAME, MODE_PRIVATE));
		// �����ʷ������Ϣ
		historyRemindInfo = PaidInfoProvider.getPaidInfo(this.getSharedPreferences(PaidInfoProvider.CONFIG_FILE_NAME, MODE_PRIVATE), cardIdList);
		for (CreditCard card : cardList) {
			RemindInfo remindinfo = card.getRemindInfo(settingInfo);
			if (historyRemindInfo.isCardPay(card.id, remindinfo.billingYear, remindinfo.billingMonth)) {
				// ����Ѿ����������
				continue;
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(LIST_VIEW_MAP_KEY_REMINDINFO, remindinfo);
			map.put(LIST_VIEW_MAP_KEY_CARD, card);

			switch (remindinfo.remindLevel) {
			case RemindInfo.REMIND_LEVEL_OVERTIME:
			case RemindInfo.REMIND_LEVEL_REMIND_NORMAL:
			case RemindInfo.REMIND_LEVEL_TODAY:
				map.put(LIST_VIEW_MAP_KEY_TITLE, RemindInfo.REMIND_TITLE);
				map.put(LIST_VIEW_MAP_KEY_DESC, remindinfo.remindText);
				listItem.add(map);
				break;
			case RemindInfo.REMIND_LEVEL_NONE:
				break;
			default:
				;
			}
		}

		// ������������Item�Ͷ�̬�����Ӧ��Ԫ��
		listItemAdapter = new RemindListAdapter(this, listItem,// ����Դ
				R.layout.reminder_text,// ListItem��XMLʵ��
				// ��̬������ImageItem��Ӧ������
				new String[] { LIST_VIEW_MAP_KEY_TITLE, LIST_VIEW_MAP_KEY_DESC },
				// ImageItem��XML�ļ������һ��ImageView,����TextView ID
				new int[] { R.id.textViewTitle, R.id.textViewDesc });

		// ��Ӳ�����ʾ
		list.setAdapter(listItemAdapter);

		// ��ӳ������
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selectIndex = arg2;
				return false;
			}
		});

		// ������������Ϣ
		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				menu.setHeaderTitle(getResources().getString(R.string.title_activity_remind_contexttitle));
				menu.add(0, CTXMENU_ID_DELETE, 1, getResources().getString(R.string.title_activity_remind_context_paid));
			}
		});
	}

	/**
	 * �����Ĳ˵���Ӧ
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		HashMap<String, Object> removedItem = listItem.get(selectIndex);
		CreditCard card = (CreditCard) removedItem.get(LIST_VIEW_MAP_KEY_CARD);
		RemindInfo remindinfo = (RemindInfo) removedItem.get(LIST_VIEW_MAP_KEY_REMINDINFO);
		PaidInfoProvider.makePay(this.getSharedPreferences(PaidInfoProvider.CONFIG_FILE_NAME, MODE_PRIVATE), card.id, remindinfo.billingYear,
				remindinfo.billingMonth, true);
		listItem.remove(selectIndex);
		listItemAdapter.notifyDataSetChanged();
		selectIndex = -1;
		return super.onContextItemSelected(item);
	}

	/**
	 * �������ѷ���
	 */
	public void startService() {
		// ָ������NotificationService���
		Intent intent = new Intent(RemindActivity.this, NotificationService.class);
		// ����PendingIntent����
		final PendingIntent pi = PendingIntent.getService(RemindActivity.this, 0, intent, 0);
		// ����ִ��pi��������һ��
		AlarmManager aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
		// aManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 5 * 60 * 60 * 1000,
		// pi);
		aManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 60 * 1000, pi);
	}

	// @SuppressWarnings("deprecation")
	// public void testNotification() {
	// // ���ѹ���������
	// NotificationManager notificationManager = (NotificationManager)
	// getSystemService(NOTIFICATION_SERVICE);
	// Notification n = new Notification(R.drawable.ic_launcher, "Hello,there!",
	// System.currentTimeMillis());
	// n.flags = Notification.FLAG_AUTO_CANCEL;
	// Intent i = new Intent(this, RemindActivity.class);
	// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
	// Intent.FLAG_ACTIVITY_NEW_TASK);
	// // PendingIntent
	// PendingIntent contentIntent = PendingIntent.getActivity(this,
	// R.string.app_name, i, PendingIntent.FLAG_UPDATE_CURRENT);
	//
	// n.setLatestEventInfo(this, "Hello,there!", "Hello,there,I'm john.",
	// contentIntent);
	// notificationManager.notify(R.string.app_name, n);
	// }

}
