package com.bld.safecardreminder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bld.safecardreminder.bean.CreditCard;
import com.bld.safecardreminder.data.DataProvider;

public class CardListActivity extends Activity {

	/**
	 * �˵���-�˵�ID-������Ƭ
	 */
	public static final int MENU_ID_CREATE_CARD = 1;

	/**
	 * �����Ĳ˵�-�˵�ID-ɾ��
	 */
	public static final int CTXMENU_ID_DELETE = 11;

	/**
	 * �������-ˢ�¿�Ƭ�б�
	 */
	public static final int RESULT_CODE_REFRESH_CARD_LIST = 1;

	/**
	 * ListView��Item��MAP-KEY
	 */
	public static final String LIST_VIEW_MAP_KEY_CARD = "card";

	/**
	 * ListView��Item��MAP-KEY
	 */
	public static final String LIST_VIEW_MAP_KEY_CARD_TITLE = "textViewCardTitle";

	/**
	 * ListView��Item��MAP-KEY
	 */
	public static final String LIST_VIEW_MAP_KEY_CARD_INFO = "textViewCardinfo";

	/**
	 * ��Ƭ�б�
	 */
	public ListView list;

	/**
	 * ��Ƭ�б�ؼ�������
	 */
	private SimpleAdapter listItemAdapter;

	/**
	 * ��Ƭ�б�
	 */
	public List<CreditCard> cardList;

	/**
	 * �����б������
	 */
	public ArrayList<HashMap<String, Object>> listItem;

	/**
	 * ѡ��������
	 */
	private int selectIndex = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_list);
		setTitle(getResources().getString(R.string.title_activity_card_list_activitytitle));
		// Show the Up button in the action bar.
		setupActionBar();
		initCardListView();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(CardListActivity.this, RemindActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivityForResult(intent, RemindActivity.RESULT_CODE_REFRESH_REMIND_LIST);
		CardListActivity.this.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// ���Ը��ݶ���������������Ӧ�Ĳ���
		if (RESULT_CODE_REFRESH_CARD_LIST == resultCode) {
			initCardListView();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.card_list, menu);
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_ID_CREATE_CARD, 1, getResources().getString(R.string.title_activity_create_card_activitytitle));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ID_CREATE_CARD:
			Intent intent = new Intent(CardListActivity.this, CardDetailActivity.class);
			intent.putExtra(CardDetailActivity.INTENT_EXTRA_KEY_ISCREATE, true);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * ��ʼ����Ƭ�б�
	 */
	public void initCardListView() {
		list = (ListView) findViewById(R.id.listViewCardList);

		// ���ɶ�̬���飬��������
		listItem = new ArrayList<HashMap<String, Object>>();
		cardList = DataProvider.getCardList(CardListActivity.this.getSharedPreferences(DataProvider.CONFIG_FILE_NAME, MODE_PRIVATE), this);
		for (CreditCard card : cardList) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(LIST_VIEW_MAP_KEY_CARD, card);
			map.put(LIST_VIEW_MAP_KEY_CARD_TITLE, card.title);
			map.put(LIST_VIEW_MAP_KEY_CARD_INFO, card.getDesc());

			listItem.add(map);
		}

		// ������������Item�Ͷ�̬�����Ӧ��Ԫ��
		listItemAdapter = new SimpleAdapter(this, listItem,// ����Դ
				R.layout.card_text,// ListItem��XMLʵ��
				// ��̬������ImageItem��Ӧ������
				new String[] { LIST_VIEW_MAP_KEY_CARD_TITLE, LIST_VIEW_MAP_KEY_CARD_INFO },
				// ImageItem��XML�ļ������һ��ImageView,����TextView ID
				new int[] { R.id.textViewCardTitle, R.id.textViewCardinfo });

		// ��Ӳ�����ʾ
		list.setAdapter(listItemAdapter);

		// ��ӵ��
		list.setOnItemClickListener(new OnItemClickListener() {

			/**
			 * parent The AdapterView where the click happened. view The view
			 * within the AdapterView that was clicked (this will be a view
			 * provided by the adapter). position The position of the view in
			 * the adapter. id The row id of the item that was clicked.
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Intent intent = new Intent(CardListActivity.this, CardDetailActivity.class);
				intent.putExtra(CardDetailActivity.INTENT_EXTRA_KEY_ISCREATE, false);
				Log.i("SCM", "card pos :" + id);
				// HashMap<String, Object> item = (HashMap<String, Object>)
				// parent
				// .getItemAtPosition((int) id);
				HashMap<String, Object> item = listItem.get((int) id);
				CreditCard card = (CreditCard) item.get(LIST_VIEW_MAP_KEY_CARD);
				intent.putExtra(CardDetailActivity.INTENT_EXTRA_KEY_CARD, card);
				startActivity(intent);
			}
		});

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
		DataProvider.delCard(CardListActivity.this.getSharedPreferences(DataProvider.CONFIG_FILE_NAME, MODE_PRIVATE), card);
		listItem.remove(selectIndex);
		listItemAdapter.notifyDataSetChanged();
		selectIndex = -1;
		return super.onContextItemSelected(item);
	}
}
