package com.bld.safecardreminder.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bld.safecardreminder.bean.PaidInfo;

/**
 * 还款信息数据提供器
 * 
 * @author ballad
 * @version 1.0 beta
 */
public class PaidInfoProvider {

	/**
	 * 配置文件名
	 */
	public static final String CONFIG_FILE_NAME = "bld-scm-remind-info";

	/**
	 * 读取最新的历史还款信息
	 * 
	 * @param sp
	 * @param cardIdList
	 *            卡片列表
	 * @return
	 */
	public static PaidInfo getPaidInfo(SharedPreferences sp, List<String> cardIdList) {
		PaidInfo remidnInfo = new PaidInfo();

		Map<String, Map<String, String>> remindInfoMap = new HashMap<String, Map<String, String>>();

		for (String cardId : cardIdList) {
			Map<String, String> remindInfoMapByCard = new HashMap<String, String>();

			// remindInfoStr: {YEAR+MONTH-ISPAY,...}
			String remindInfoStrByCard = sp.getString(cardId, "");
			String[] remindInfosByCard = remindInfoStrByCard.split(",");
			for (String remindInfoByCard : remindInfosByCard) {
				if (!remindInfoByCard.equals("")) {
					String[] remindInfos = remindInfoByCard.split("-");
					if (remindInfos.length == 2) {
						String billingTime = remindInfos[0];
						String ispay = remindInfos[1];
						remindInfoMapByCard.put(billingTime, ispay);
					}
				}
			}
			remindInfoMap.put(cardId, remindInfoMapByCard);
		}
		remidnInfo.remindInfoMap = remindInfoMap;
		return remidnInfo;

	}

	/**
	 * 设置制定年月已经还款
	 * 
	 * @param sp
	 * @param cardId
	 * @param billingYear
	 * @param billingMonth
	 * @param isPay
	 *            目前只支持TRUE
	 */
	public static void makePay(SharedPreferences sp, String cardId, int billingYear, int billingMonth, boolean isPay) {

		// Prepare the string
		String remindInfoStrByCard = sp.getString(cardId, "");
		String modifiedRemindInfoStrByCard = String.valueOf(billingYear) + String.valueOf(billingMonth) + "-" + String.valueOf(isPay) + ","
				+ remindInfoStrByCard;

		// Update Data
		Editor editor = sp.edit();
		editor.putString(cardId, modifiedRemindInfoStrByCard);
		editor.commit();
	}

}
