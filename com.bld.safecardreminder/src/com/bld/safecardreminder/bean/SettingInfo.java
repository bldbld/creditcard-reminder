package com.bld.safecardreminder.bean;

/**
 * ������Ϣ
 * 
 * @author ballad
 * @version 1.0 beta
 */
public class SettingInfo {

	/**
	 * ��ǰ��������
	 */
	public int numberOfDaysInAdvance2remind = 5;

	/**
	 * ��������Ϣת��Ϊ�ַ���
	 */
	public String toString() {
		return String.valueOf(this.numberOfDaysInAdvance2remind);
	}

	/**
	 * ͨ���ַ�������������Ϣ
	 * 
	 * @param data
	 */
	public void initSettingInfo(String data) {
		this.numberOfDaysInAdvance2remind = Integer.parseInt(data);
	}
}
