package com.bld.safecardreminder.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bld.safecardreminder.CardListActivity;
import com.bld.safecardreminder.R;
import com.bld.safecardreminder.bean.CreditCard;

public class CardListAdapter extends SimpleAdapter {

	LayoutInflater viewInflator;

	public CardListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
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
			holder.textViewCardTitle = ((TextView) convertView.findViewById(R.id.textViewCardTitle));
			holder.textViewCardinfo = ((TextView) convertView.findViewById(R.id.textViewCardinfo));
			convertView.setTag(holder);
		}
		@SuppressWarnings("unchecked")
		HashMap<String, Object> item = (HashMap<String, Object>) this.getItem(position);
		CreditCard card = (CreditCard) item.get(CardListActivity.LIST_VIEW_MAP_KEY_CARD);

		holder.textViewCardTitle.setText(card.title);
		holder.textViewCardTitle.setTextColor(Color.BLACK);
		// TextPaint tp = holder.textViewCardTitle.getPaint();
		// tp.setFakeBoldText(true);

		holder.textViewCardinfo.setText(card.getDesc());

		return convertView;
	}

	private class ViewHolder {
		ImageView imageViewCardIcon;
		TextView textViewCardTitle;
		TextView textViewCardinfo;
	}
}
