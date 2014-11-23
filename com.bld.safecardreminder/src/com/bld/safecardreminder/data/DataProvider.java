package com.bld.safecardreminder.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bld.safecardreminder.R;
import com.bld.safecardreminder.bean.CreditCard;
import com.bld.safecardreminder.bean.SettingInfo;

/**
 * 数据提供器
 * 
 * @author ballad
 * @version 1.0 beta
 */
public class DataProvider {

	/**
	 * 持久化数据文件-卡片信息
	 */
	public static final String CONFIG_FILE_NAME = "bld-scm-data";

	/**
	 * 卡片列表
	 */
	protected static final String KEY_CARD_ID_LIST = "CARD_ID_LIST";

	/**
	 * 配置
	 */
	protected static final String KEY_SETTING = "SETTING";

	/**
	 * 存储卡片标题，前面补卡片ID
	 */
	protected static final String KEY_TITLE = "_TITLE";

	/**
	 * 存储卡片账单日，前面补卡片ID
	 */
	protected static final String KEY_BILLING_DAY = "_BILLING_DAY";

	/**
	 * 存储卡片还款期，前面补卡片ID
	 */
	protected static final String KEY_PAY_PERIOD = "_PAY_PERIOD";

	/**
	 * 新增一张卡片
	 * 
	 * @param sp
	 * @param card
	 *            卡片
	 */
	public static void newCard(SharedPreferences sp, CreditCard card) {
		String cardIdList = sp.getString("CARD_ID_LIST", null);
		String id = card.id;

		Editor editor = sp.edit();
		editor.putString("CARD_ID_LIST", cardIdList == null ? id : (cardIdList + ";" + id));
		editor.putString(id + KEY_TITLE, card.title);
		editor.putInt(id + KEY_BILLING_DAY, card.billingDay);
		editor.putInt(id + KEY_PAY_PERIOD, card.payPeriod);
		editor.commit();
	}

	/**
	 * 删除卡片
	 * 
	 * @param sp
	 * @param card
	 */
	public static void delCard(SharedPreferences sp, CreditCard card) {
		List<String> idList = getIdList(sp);
		StringBuilder idListStrBuilder = new StringBuilder();
		for (String id : idList) {
			if (!card.id.equals(id)) {
				idListStrBuilder.append(id + ";");
			}
		}
		String id = card.id;

		Editor editor = sp.edit();
		editor.putString("CARD_ID_LIST", idListStrBuilder.toString());
		editor.remove(id + KEY_TITLE);
		editor.remove(id + KEY_BILLING_DAY);
		editor.remove(id + KEY_PAY_PERIOD);
		editor.commit();
	}

	/**
	 * 更新卡片
	 * 
	 * @param sp
	 * @param card
	 */
	public static void updateCard(SharedPreferences sp, CreditCard card) {
		String id = card.id;

		Editor editor = sp.edit();
		editor.putString(id + KEY_TITLE, card.title);
		editor.putInt(id + KEY_BILLING_DAY, card.billingDay);
		editor.putInt(id + KEY_PAY_PERIOD, card.payPeriod);
		editor.commit();
	}

	/**
	 * 获取卡片列表
	 * 
	 * @param sp
	 * @return
	 */
	public static List<CreditCard> getCardList(SharedPreferences sp, Context ctx) {
		String defalutCard_nName = ctx.getResources().getString(R.string.default_card_name);
		int defaultBillingDay = Integer.parseInt(ctx.getResources().getString(R.string.default_card_billingday));
		int defaultPayPeriod = Integer.parseInt(ctx.getResources().getString(R.string.default_card_payperiod));

		List<String> idList = getIdList(sp);
		List<CreditCard> cardList = new ArrayList<CreditCard>();
		for (String id : idList) {
			CreditCard card = new CreditCard();
			card.id = id;
			card.title = sp.getString(id + KEY_TITLE, defalutCard_nName);
			card.billingDay = sp.getInt(id + KEY_BILLING_DAY, defaultBillingDay);
			card.payPeriod = sp.getInt(id + KEY_PAY_PERIOD, defaultPayPeriod);
			cardList.add(card);
		}
		return cardList;
	}

	/**
	 * 保存配置
	 * 
	 * @param sp
	 * @param setinfo
	 */
	public static void saveSetting(SharedPreferences sp, SettingInfo setinfo) {
		Editor editor = sp.edit();
		editor.putString(KEY_SETTING, setinfo.toString());
		editor.commit();
	}

	/**
	 * 读取配置
	 * 
	 * @param sp
	 * @return
	 */
	public static SettingInfo getSetting(SharedPreferences sp, Context ctx) {
		String setstr = sp.getString(KEY_SETTING, ctx.getResources().getString(R.string.default_setting_string));
		SettingInfo setinfo = new SettingInfo();
		setinfo.initSettingInfo(setstr);
		return setinfo;
	}

	/**
	 * 获得卡片ID列表
	 * 
	 * @param sp
	 * @return
	 */
	public static List<String> getIdList(SharedPreferences sp) {
		String cardIdList = sp.getString(KEY_CARD_ID_LIST, null);
		List<String> idList = new ArrayList<String>();
		if (cardIdList != null) {
			String[] cardIdListSplit = cardIdList.split(";");
			for (String id : cardIdListSplit) {
				if (!id.equals("")) {
					idList.add(id);
				}
			}
		}
		return idList;
	}
}
