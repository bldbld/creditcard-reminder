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
 * 主界面-显示提醒信息
 * 
 * @author ballad
 * @version 0.1 beta
 */
public class RemindActivity extends Activity {

	/**
	 * 菜单键-菜单ID-卡片列表
	 */
	public static final int MENU_ID_CARD_LIST = 3;

	/**
	 * 菜单键-菜单ID-设置
	 */
	public static final int MENU_ID_SETTING = 1;

	/**
	 * 上下文菜单-菜单ID-删除
	 */
	public static final int CTXMENU_ID_DELETE = 11;

	/**
	 * 请求代码-刷新提醒列表
	 */
	public static final int RESULT_CODE_REFRESH_REMIND_LIST = 2;

	/**
	 * ListView中Item的MAP-KEY
	 */
	public static final String LIST_VIEW_MAP_KEY_TITLE = "textViewTitle";

	/**
	 * ListView中Item的MAP-KEY
	 */
	public static final String LIST_VIEW_MAP_KEY_DESC = "textViewDesc";

	/**
	 * ListView中Item的MAP-KEY
	 */
	public static final String LIST_VIEW_MAP_KEY_CARD = "card";

	/**
	 * ListView中Item的MAP-KEY
	 */
	public static final String LIST_VIEW_MAP_KEY_REMINDINFO = "remindInfo";

	/**
	 * 提醒列表
	 */
	private ListView list;

	/**
	 * 提醒列表的数据
	 */
	private ArrayList<HashMap<String, Object>> listItem;

	/**
	 * 提醒列表控件适配器
	 */
	private SimpleAdapter listItemAdapter;

	/**
	 * 选择项索引
	 */
	private int selectIndex = -1;

	/**
	 * 历史提醒信息
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
		// 可以根据多个请求代码来作相应的操作
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
	 * 初始化提醒列表
	 */
	public void initListViewReminder() {
		list = (ListView) findViewById(R.id.listViewReminder);

		// 生成动态数组，加入数据
		listItem = new ArrayList<HashMap<String, Object>>();
		// 获得数据
		List<CreditCard> cardList = DataProvider.getCardList(this.getSharedPreferences(DataProvider.CONFIG_FILE_NAME, MODE_PRIVATE), this);
		SettingInfo settingInfo = DataProvider.getSetting(this.getSharedPreferences(DataProvider.CONFIG_FILE_NAME, MODE_PRIVATE), this);
		List<String> cardIdList = DataProvider.getIdList(this.getSharedPreferences(DataProvider.CONFIG_FILE_NAME, MODE_PRIVATE));
		// 获得历史提醒信息
		historyRemindInfo = PaidInfoProvider.getPaidInfo(this.getSharedPreferences(PaidInfoProvider.CONFIG_FILE_NAME, MODE_PRIVATE), cardIdList);
		for (CreditCard card : cardList) {
			RemindInfo remindinfo = card.getRemindInfo(settingInfo);
			if (historyRemindInfo.isCardPay(card.id, remindinfo.billingYear, remindinfo.billingMonth)) {
				// 如果已经还款，则不提醒
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

		// 生成适配器的Item和动态数组对应的元素
		listItemAdapter = new RemindListAdapter(this, listItem,// 数据源
				R.layout.reminder_text,// ListItem的XML实现
				// 动态数组与ImageItem对应的子项
				new String[] { LIST_VIEW_MAP_KEY_TITLE, LIST_VIEW_MAP_KEY_DESC },
				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] { R.id.textViewTitle, R.id.textViewDesc });

		// 添加并且显示
		list.setAdapter(listItemAdapter);

		// 添加长按点击
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selectIndex = arg2;
				return false;
			}
		});

		// 创建上下文信息
		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				menu.setHeaderTitle(getResources().getString(R.string.title_activity_remind_contexttitle));
				menu.add(0, CTXMENU_ID_DELETE, 1, getResources().getString(R.string.title_activity_remind_context_paid));
			}
		});
	}

	/**
	 * 上下文菜单响应
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
	 * 启动提醒服务
	 */
	public void startService() {
		// 指定启动NotificationService组件
		Intent intent = new Intent(RemindActivity.this, NotificationService.class);
		// 创建PendingIntent对象
		final PendingIntent pi = PendingIntent.getService(RemindActivity.this, 0, intent, 0);
		// 设置执行pi代表的组件一次
		AlarmManager aManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
		// aManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 5 * 60 * 60 * 1000,
		// pi);
		aManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, 60 * 1000, pi);
	}

	// @SuppressWarnings("deprecation")
	// public void testNotification() {
	// // 提醒管理器测试
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
