package com.bld.safecardreminder.bean;

import java.util.Map;

/**
 * 历史已还款信息
 * 
 * @author ballad
 * @version 1.0 beta
 */
public class PaidInfo {

	/**
	 * 数据集 <卡片ID,<年月,是否还款>>
	 */
	public Map<String, Map<String, String>> remindInfoMap;

	/**
	 * 判断是否包含卡片ID
	 * 
	 * @param cardId
	 * @return
	 */
	public boolean containsCardId(String cardId) {
		return remindInfoMap.containsKey(cardId);
	}

	/**
	 * 判断是否付款
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
