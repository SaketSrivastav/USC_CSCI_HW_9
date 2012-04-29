package com.saket_csci_571_9;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class FacebookLogin extends Activity {
	/** Called when the activity is first created. */

	Facebook facebook;
	Handler mFacebookHandler = new Handler();
	String FACEBOOK_APPID = "272508239499899";
	String FACEBOOK_PERMISSION = "publish_stream";

	ImageButton loginBtn;
	//String FILENAME = "AndroidSSO_data";
	//SharedPreferences mPrefs;

	public EasyTripApplication getMyApplication(){
		return (EasyTripApplication)getApplication();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		//facebookConnector = new FacebookConnector(FACEBOOK_APPID, this, getApplicationContext(), new String[] {this.FACEBOOK_PERMISSION});
		//getMyApplication().setFacebookConnector(facebookConnector);
		facebook = new Facebook(FACEBOOK_APPID);
		
		//facebook = getMyApplication().getFacebook();
		getMyApplication().setFacebook(facebook);

		loginBtn = (ImageButton) findViewById(R.id.loginBtn);

		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//loginFacebook();
				//facebookConnector.login();
				try{
					if (facebook.isSessionValid()){		
						Toast.makeText(FacebookLogin.this, "Facebook Session Valid", Toast.LENGTH_SHORT).show();
						goToHotelSearchActivity();
					}else{
						loginFacebook();
						System.out.println("Login To Facebook");
					}
				}catch(Exception e){
					Toast.makeText(FacebookLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
				}
				
				//Intent intent = new Intent(FacebookLogin.this, HotelSearchActivity.class);
				//startActivity(intent);
				//finish();
			}	
		});
	}
	
	public void loginFacebook(){
		facebook.authorize(this, new String[]{FACEBOOK_PERMISSION} ,Facebook.FORCE_DIALOG_AUTH,new LoginDialogListener());
	}
	
	private void goToHotelSearchActivity(){
		Intent intent = new Intent(FacebookLogin.this, HotelSearchActivity.class);
        startActivity(intent);
	}
	
    private final class LoginDialogListener implements DialogListener {
        public void onComplete(Bundle values) {
        	Toast.makeText(FacebookLogin.this, "Logged In Facebook", Toast.LENGTH_SHORT).show();
            goToHotelSearchActivity();
        }

        public void onFacebookError(FacebookError error) {
           	Toast.makeText(FacebookLogin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
        
        public void onError(DialogError error) {
        	Toast.makeText(FacebookLogin.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }

        public void onCancel() {
        	Toast.makeText(FacebookLogin.this, "Action Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

}