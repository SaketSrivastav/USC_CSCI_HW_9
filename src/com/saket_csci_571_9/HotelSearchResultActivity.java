package com.saket_csci_571_9;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class HotelSearchResultActivity extends Activity {

	Facebook facebook = new Facebook("272508239499899");

	ListView hotelListView;

	ArrayList<Hotel> hotels = new ArrayList<Hotel>();
	JSONObject jsonResults;
	JSONArray jsonHotels;
	JSONObject jsonHotel;
	Hotel hotel;

	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String searchResults = getIntent().getExtras().getString("results");
		System.out.println("Results Obtained: "+searchResults);
		try{
			JSONObject testResult = new JSONObject(searchResults);

			testResult = testResult.getJSONObject("hotels");
			JSONArray testArray = testResult.getJSONArray("hotel");

			if(testArray.length() == 0){
				setContentView(R.layout.no_hotels);
			}else{
				setContentView(R.layout.hotel_list);

				prepareHotels(searchResults);

				hotelListView = (ListView) findViewById(R.id.hotelList);
				hotelListView.setAdapter(new HotelListAdapter(this, getHotels()));

				hotelListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> a, View v, int position, long id) {
						Object o = hotelListView.getItemAtPosition(position);
						final Hotel hotel = (Hotel)o;
						//Toast.makeText(HotelSearchResultActivity.this, "You have chosen: " + " " + hotel.getHotelName(), Toast.LENGTH_LONG).show();

						final Dialog dialog = new Dialog(HotelSearchResultActivity.this);
						dialog.setContentView(R.layout.hotel_details);
						dialog.setTitle("Hotel Info");

						final TextView txtHotelName,txtHotelLocation,txtHotelReviews;
						final String txtReviewUrl;
						ImageView imgHotelImage;
						RatingBar rbHotelRatings;
						Button btnPost;

						txtHotelName = (TextView) dialog.findViewById(R.id.hotelName);
						txtHotelLocation = (TextView) dialog.findViewById(R.id.hotelLocation);
						txtHotelReviews = (TextView) dialog.findViewById(R.id.hotelReviews);
						imgHotelImage = (ImageView) dialog.findViewById(R.id.hotelImage);
						rbHotelRatings =  (RatingBar) dialog.findViewById(R.id.hotelRatingBar);

						btnPost = (Button) dialog.findViewById(R.id.postToFacebook);

						txtHotelName.setText(hotel.getHotelName());
						if(hotel.getHotelLocation() == null||hotel.getHotelLocation().equals("")){
							txtHotelLocation.setText("Location Not Found");
						}else{
							txtHotelLocation.setText(hotel.getHotelLocation());
						}
						
						if(hotel.getHotelReviewNo() == null||hotel.getHotelReviewNo().equals("")){
							txtHotelReviews.setText("No Reviews");
						}else{
							txtHotelReviews.setText(hotel.getHotelReviewNo());
						}
						
						rbHotelRatings.setNumStars(5);
						rbHotelRatings.setIsIndicator(true);
						
						if(hotel.getHotelRatings() == null ||hotel.getHotelRatings().equals("")){
							Log.e("MYAPP", "NO Ratings");
						}else{
							rbHotelRatings.setRating(Float.parseFloat(hotel.getHotelRatings()));
						}
						
						if(hotel.getHotelImageUrl() == null||hotel.getHotelImageUrl().equals("")){
							imgHotelImage.setImageResource(R.drawable.ic_launcher);
						}else{
							imgHotelImage.setImageBitmap(Utility.getBitmap(hotel.getHotelImageUrl()));
						}
						
						txtReviewUrl = hotel.getHotelReviewUrl();
						btnPost.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								Bundle params = new Bundle();
								if(hotel.getHotelRatings().equals("")){
									params.putString("description", "This hotel is in "+txtHotelLocation.getText().toString()+" and has no ratings");
								}else{
									params.putString("description", "This hotel is in "+txtHotelLocation.getText().toString()+" and has ratings of "+hotel.getHotelRatings()+" out of 5");
								}
								params.putString("picture", hotel.getHotelImageUrl());
								
								params.putString("name", hotel.getHotelName());
								params.putString("link", txtReviewUrl);
								try{
									if(txtReviewUrl.equals("http://www.tripadvisor.com")){
										Log.e("MYAPP", "NO ReviewURL Found");
									}else{
										JSONObject properties = new JSONObject();
										JSONObject prop = new JSONObject();
										prop.put("text", "here");
										prop.put("href", txtReviewUrl);
										properties.put("Find Reviews Of hotel ", prop);
										params.putString("properties", properties.toString());
									}
									
								}catch (Exception e) {
									e.printStackTrace();
								}


								//params.putString("properties", "{\"Hotel Reviews can be found\":{\"text\":\"here\",\"href\"\":"+txtReviewUrl+"}}");
								//post on user's wall.
								facebook.dialog(HotelSearchResultActivity.this, "feed",params, new DialogListener(){

									@Override
									public void onComplete(Bundle values) {
										Object post_id = values.get("post_id");
										System.out.println(post_id);
										if(values != null && post_id != null){
											Toast.makeText(HotelSearchResultActivity.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
											dialog.dismiss();
										}else{
											Toast.makeText(HotelSearchResultActivity.this, "Posted Cancelled", Toast.LENGTH_SHORT).show();
										}
									}

									@Override
									public void onFacebookError(FacebookError e) {
										Toast.makeText(HotelSearchResultActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
										dialog.dismiss();
									}

									@Override
									public void onError(DialogError e) {
										Toast.makeText(HotelSearchResultActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
										dialog.dismiss();
									}

									@Override
									public void onCancel() {
										Toast.makeText(HotelSearchResultActivity.this, "Post cancelled", Toast.LENGTH_SHORT).show();
										dialog.dismiss();
										return;

									}});
							}
						});
						dialog.show();
					}

				});
			}
		}catch(Exception E){

		}
	}

	public void prepareHotels(String searchResults){
		try{
			jsonResults = new JSONObject(searchResults);

			jsonResults = jsonResults.getJSONObject("hotels");

			System.out.println("jsonresult names: "+jsonResults.names());
			jsonHotels = jsonResults.getJSONArray("hotel");

			for (int i = 0; i < jsonHotels.length(); i++) {
				hotel = new Hotel();
				jsonHotel = jsonHotels.getJSONObject(i);

				hotel.setHotelImageUrl(jsonHotel.getString("image_url"));
				hotel.setHotelName(jsonHotel.getString("name"));
				hotel.setHotelRatings(jsonHotel.getString("no_of_stars"));
				hotel.setHotelReviewNo(jsonHotel.getString("no_of_reviews"));
				hotel.setHotelReviewUrl(jsonHotel.getString("reviews_url"));
				hotel.setHotelLocation(jsonHotel.getString("location"));

				addHotel(hotel);

				System.out.println(hotel.getHotelName());

			}

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Hotel> getHotels (){
		return hotels;
	}

	public void addHotel(Hotel hotel){
		hotels.add(hotel);
	}
}
