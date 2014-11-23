package com.bld.safecardreminder.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bld.safecardreminder.R;
import com.bld.safecardreminder.RemindActivity;
import com.bld.safecardreminder.bean.RemindInfo;

/**
 * Ã·–—¡–±Ì  ≈‰∆˜
 * 
 * @author ballad
 * @version 1.0 beta
 */
public class RemindListAdapter extends SimpleAdapter {

	LayoutInflater viewInflator;

	public RemindListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		viewInflator = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView != null)
			holder = (ViewHolder) convertView.getTag();
		else {
			convertView = viewInflator.inflate(R.layout.reminder_text, null);
			holder = new ViewHolder();
			holder.textViewTitle = ((TextView) convertView.findViewById(R.id.textViewTitle));
			holder.textViewDesc = ((TextView) convertView.findViewById(R.id.textViewDesc));
			convertView.setTag(holder);
		}
		@SuppressWarnings("unchecked")
		HashMap<String, Object> item = (HashMap<String, Object>) this.getItem(position);
		RemindInfo rmdInfo = (RemindInfo) item.get(RemindActivity.LIST_VIEW_MAP_KEY_REMINDINFO);

		holder.textViewTitle.setText(item.get(RemindActivity.LIST_VIEW_MAP_KEY_TITLE).toString());
		holder.textViewTitle.setTextColor(Color.BLACK);
		// TextPaint tp = holder.textViewTitle.getPaint();
		// tp.setFakeBoldText(true);

		holder.textViewDesc.setText(item.get(RemindActivity.LIST_VIEW_MAP_KEY_DESC).toString());
		if (rmdInfo.remindLevel == RemindInfo.REMIND_LEVEL_TODAY) {
			holder.textViewDesc.setTextColor(Color.RED);
		} else {
			holder.textViewDesc.setTextColor(Color.GRAY);
		}

		return convertView;
	}

	private class ViewHolder {
		TextView textViewTitle;
		TextView textViewDesc;
	}
}
