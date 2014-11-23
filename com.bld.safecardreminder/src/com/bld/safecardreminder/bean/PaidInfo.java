package com.bld.safecardreminder.bean;

import java.util.Map;

/**
 * ��ʷ�ѻ�����Ϣ
 * 
 * @author ballad
 * @version 1.0 beta
 */
public class PaidInfo {

	/**
	 * ���ݼ� <��ƬID,<����,�Ƿ񻹿�>>
	 */
	public Map<String, Map<String, String>> remindInfoMap;

	/**
	 * �ж��Ƿ������ƬID
	 * 
	 * @param cardId
	 * @return
	 */
	public boolean containsCardId(String cardId) {
		return remindInfoMap.containsKey(cardId);
	}

	/**
	 * �ж��Ƿ񸶿�
	 * 
	 * @param cardId
	 * @param billingYear
	 * @param billingMonth
	 * @return
	 */
	public boolean isCardPay(String cardId, int billingYear, int billingMonth) {
		Map<String, String> cardPayInfo = remindInfoMap.get(cardId);
		if (cardPayInfo == null) {
			return false;
		}
		if (cardPayInfo.containsKey(String.valueOf(billingYear) + String.valueOf(billingMonth))) {
			return true;
		}
		return false;
	}
}
