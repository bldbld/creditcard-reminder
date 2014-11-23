package com.bld.safecardreminder.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bld.safecardreminder.bean.PaidInfo;

/**
 * ������Ϣ�����ṩ��
 * 
 * @author ballad
 * @version 1.0 beta
 */
public class PaidInfoProvider {

	/**
	 * �����ļ���
	 */
	public static final String CONFIG_FILE_NAME = "bld-scm-remind-info";

	/**
	 * ��ȡ���µ���ʷ������Ϣ
	 * 
	 * @param sp
	 * @param cardIdList
	 *            ��Ƭ�б�
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
	 * �����ƶ������Ѿ�����
	 * 
	 * @param sp
	 * @param cardId
	 * @param billingYear
	 * @param billingMonth
	 * @param isPay
	 *            Ŀǰֻ֧��TRUE
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
