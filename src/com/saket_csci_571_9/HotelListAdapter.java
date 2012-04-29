/**
 * 
 */
package com.saket_csci_571_9;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author saket
 *
 */
public class HotelListAdapter extends BaseAdapter {

	private static ArrayList<Hotel> hotelList;
	private LayoutInflater mInflater;

	public HotelListAdapter(Context context, ArrayList<Hotel> results) {
		hotelList = results;
		mInflater = LayoutInflater.from(context);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return hotelList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return hotelList.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.hotel_list_item, null);
			holder = new ViewHolder();
			holder.txtHotelName = (TextView) convertView.findViewById(R.id.hotelName);
			holder.txtHotelLocation = (TextView) convertView.findViewById(R.id.hotelLocation);
			holder.imgHotelImage = (ImageView) convertView.findViewById(R.id.hotelImage);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtHotelName.setText(hotelList.get(position).getHotelName());
		holder.txtHotelLocation.setText(hotelList.get(position).getHotelLocation());
		holder.imgHotelImage = (ImageView) convertView.findViewById(R.id.hotelImage);
		
		

		Bitmap bmp = Utility.getBitmap(hotelList.get(position).getHotelImageUrl());
		if(bmp == null){
			holder.imgHotelImage.setImageResource(R.drawable.ic_launcher);
		}else{
			holder.imgHotelImage.setImageBitmap(bmp);
		}

		return convertView;
	}

	static class ViewHolder {
		TextView txtHotelName;
		TextView txtHotelLocation;
		ImageView imgHotelImage;
	}
}
