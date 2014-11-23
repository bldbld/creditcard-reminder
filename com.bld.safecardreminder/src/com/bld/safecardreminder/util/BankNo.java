package com.bld.safecardreminder.util;

import android.content.Context;

import com.bld.safecardreminder.R;
import com.bld.safecardreminder.bean.BankInfo;

/**
 * 银行序号列表
 * 
 * @author ballad
 * @version 1.0 beta
 */
public class BankNo {

	public static final int ICBC = 11;

	public static final int CCB = 12;

	public static final int BOC = 13;

	public static final int ABC = 14;

	public static final int BANKCOMM = 21;

	public static final int CMB = 22;

	public static final int HXB = 23;

	public static final int CMBC = 24;

	public static final int PINGAN = 31;

	public static BankInfo getBankInfoByNo(Context ctx, int bankNo) {
		String strKey = new StringBuilder("bank_str_").append(String.valueOf(bankNo)).toString();
		String titleKey = new StringBuilder("bank_title_").append(String.valueOf(bankNo)).toString();

		BankInfo bankInfo = new BankInfo();
		bankInfo.bankNo = bankNo;
		switch (bankNo) {
		case BANKCOMM:
			bankInfo.bankStr = ctx.getResources().getString(R.string.bank_str_bankcomm);
			bankInfo.bankTitle = ctx.getResources().getString(R.string.bank_title_bankcomm);
			break;
		case CMB:
			bankInfo.bankStr = ctx.getResources().getString(R.string.bank_str_cmb);
			bankInfo.bankTitle = ctx.getResources().getString(R.string.bank_title_cmb);
			break;
		}
		return bankInfo;
	}
}
