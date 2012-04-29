package com.saket_csci_571_9;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;

public class HotelSearchActivity extends Activity {

	//Facebook facebook = new Facebook("272508239499899");
	Facebook facebook;
	
	EditText mTxtCityName;
	Spinner mTxtHotelChain;
	Button mBtnSearch;
	ImageButton mBtnLogout;
	
	String cityName;
	String hotelchain;

	/* Important Objects */
	
	Hotel hotel;
	
	StringBuffer results = new StringBuffer();
	
	public EasyTripApplication getMyApplication(){
		return (EasyTripApplication)getApplication();
	}
	
	public void setFacebookObjects(){
		//facebookConnector = getMyApplication().getFacebookConnector();
		facebook = getMyApplication().getFacebook();
	}
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotelsearch);

		//Initialize facebook Objects
		setFacebookObjects();
		
		mTxtCityName = (EditText) findViewById(R.id.cityName);
		
		mTxtHotelChain = (Spinner) findViewById(R.id.hotelChain);
		mBtnSearch = (Button) findViewById(R.id.btnSearch);

		mBtnLogout = (ImageButton) findViewById(R.id.logoutBtn);
		mBtnLogout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					logout();
					Toast.makeText(HotelSearchActivity.this, "Logged Out Of Facebook", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		mBtnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cityName = mTxtCityName.getText().toString();
				hotelchain = mTxtHotelChain.getSelectedItem().toString();
				if(cityName.equals("")){
					Toast.makeText(HotelSearchActivity.this, "City Name Is Empty", Toast.LENGTH_LONG).show();
				}else if(hotelchain.equals("---None---")){
					Toast.makeText(HotelSearchActivity.this, "Hotel Chain cannot be ---None---", Toast.LENGTH_LONG).show();
				}else{
					GetHotels getHotels = new GetHotels();
					getHotels.execute(cityName,hotelchain);				
				}
			}
		});
	}

	/**
	 * @author saket
	 * Class to handle the asynchronous call to webserver to get search results
	 */
	private class GetHotels extends AsyncTask<String, Integer, String>{

		private StringBuffer usefulString = new StringBuffer();
		ProgressDialog dialog;

		@Override
		protected String doInBackground(String... params) {
			publishProgress(); 
			
			HttpClient client = new DefaultHttpClient();
			
			String encodedCityName = URLEncoder.encode(params[0].toLowerCase());
			String encodedHotelChain = URLEncoder.encode(params[1].toLowerCase());
			String searchUrl = "http://cs-server.usc.edu:29141/Assignment8/HotelSearchServlet?city="+encodedCityName+"&hotelChain="+encodedHotelChain;
			System.out.println("SEARCHURL:"+searchUrl);
			
			HttpGet request = new HttpGet(searchUrl);
			HttpResponse response;
			BufferedReader rd = null;
			
			try {
				response = client.execute(request);
				rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));

				if(rd == null){
					//Toast.makeText(HotelSearchActivity.this, "No Response", Toast.LENGTH_LONG).show();
					System.out.println("READ OBJECT NULL");
				}else{
					String line = "";
					while ((line = rd.readLine()) != null) {
						usefulString.append(line);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(HotelSearchActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
			}finally{
				System.out.println("Results: "+usefulString.toString());
				return usefulString.toString();
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			dialog = ProgressDialog.show(HotelSearchActivity.this, "Easy Trip", "Searching Hotels...", true);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setCancelable(false);
		}

		@Override
		protected void onPostExecute(String result) {
			if(result == null||result.equals("")){
				Toast.makeText(HotelSearchActivity.this, "No Response Obtained: Check For Connectivity", Toast.LENGTH_LONG).show();
			}else{
				try {
					Intent intent = new Intent(HotelSearchActivity.this, HotelSearchResultActivity.class);
					intent.putExtra("results", result);
					startActivity(intent);
				} catch (Exception e) {
					Toast.makeText(HotelSearchActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
			dialog.dismiss();
		}
	}
	
	public void logout() {
        AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(facebook);
        asyncRunner.logout(HotelSearchActivity.this, new LogoutRequestListener());
	}
	
	public void gotoMainActivity(){
		Intent intent = new Intent(HotelSearchActivity.this,FacebookLogin.class);
		startActivity(intent);
		finish();
	}
	
	public class LogoutRequestListener extends BaseRequestListener {

		@Override
		public void onComplete(String response, Object state) {
			//Toast.makeText(getApplicationContext(), "Logged Out Of Facebook", Toast.LENGTH_SHORT).show();
			gotoMainActivity();
		}
    }
}