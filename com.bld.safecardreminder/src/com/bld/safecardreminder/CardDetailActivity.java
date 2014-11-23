package com.bld.safecardreminder;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bld.safecardreminder.bean.CreditCard;
import com.bld.safecardreminder.data.DataProvider;

/**
 * 卡片详细界面
 * 
 * @author ballad
 * @version 1.0 beta
 */
public class CardDetailActivity extends Activity {

	/**
	 * Intent 参数-所选卡片，非必选
	 */
	public static final String INTENT_EXTRA_KEY_CARD = "card";
	/**
	 * Intent 参数-是否新增
	 */
	public static final String INTENT_EXTRA_KEY_ISCREATE = "isCreate";

	/**
	 * 是否为新增卡片
	 */
	public boolean isCreate = true;

	/**
	 * 卡片标题
	 */
	public EditText editorCardTitle;

	/**
	 * 账单日
	 */
	public EditText editorBillingDay;

	/**
	 * 还款期
	 */
	public EditText editorPayPeriod;

	/**
	 * 被操作卡片
	 */
	public CreditCard cardForUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_detail);
		setTitle(getResources().getString(R.string.title_activity_card_detail));
		// Show the Up button in the action bar.
		setupActionBar();

		// Initial the UI
		editorCardTitle = (EditText) findViewById(R.id.editTextCardTitle);
		editorBillingDay = (EditText) findViewById(R.id.editTextCardBillingDay);
		editorPayPeriod = (EditText) findViewById(R.id.editTextPayPeriod);

		// Get Extra
		Intent intent = getIntent();
		isCreate = intent.getBooleanExtra(INTENT_EXTRA_KEY_ISCREATE, true);
		if (!isCreate) {
			cardForUpdate = (CreditCard) intent.getSerializableExtra(INTENT_EXTRA_KEY_CARD);
			editorCardTitle.setText(cardForUpdate.title);
			editorBillingDay.setText(String.valueOf(cardForUpdate.billingDay));
			editorPayPeriod.setText(String.valueOf(cardForUpdate.payPeriod));
		}

		initCreateButton();
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

	/**
	 * 初始化按钮
	 */
	public void initCreateButton() {
		Button button1 = (Button) findViewById(R.id.buttonCreate);

		// 增加事件响应
		button1.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (isCreate) {
					CreditCard newCard = new CreditCard();
					newCard.title = editorCardTitle.getText().toString();
					newCard.billingDay = Integer.parseInt(editorBillingDay.getText().toString());
					newCard.payPeriod = Integer.parseInt(editorPayPeriod.getText().toString());
					newCard.generateId();
					DataProvider.newCard(CardDetailActivity.this.getSharedPreferences(DataProvider.CONFIG_FILE_NAME, MODE_PRIVATE), newCard);
				} else {
					cardForUpdate.title = editorCardTitle.getText().toString();
					cardForUpdate.billingDay = Integer.parseInt(editorBillingDay.getText().toString());
					cardForUpdate.payPeriod = Integer.parseInt(editorPayPeriod.getText().toString());
					DataProvider.updateCard(CardDetailActivity.this.getSharedPreferences(DataProvider.CONFIG_FILE_NAME, MODE_PRIVATE), cardForUpdate);
				}
				Intent intent = new Intent(CardDetailActivity.this, CardListActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivityForResult(intent, CardListActivity.RESULT_CODE_REFRESH_CARD_LIST);
				CardDetailActivity.this.finish();
			}
		});
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(CardDetailActivity.this, CardListActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivityForResult(intent, CardListActivity.RESULT_CODE_REFRESH_CARD_LIST);
		CardDetailActivity.this.finish();
	}
}
