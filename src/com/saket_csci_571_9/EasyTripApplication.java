/**
 * 
 */
package com.saket_csci_571_9;

import com.facebook.android.Facebook;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Handler;

/**
 * @author saket
 *
 */
public class EasyTripApplication extends Application {
	
	Handler mFacebookHandler = new Handler();
	String FACEBOOK_APPID = "272508239499899";
	String FACEBOOK_PERMISSION = "publish_stream";
	Facebook facebook = null;
	
	/**
	 * @return the mFacebookHandler
	 */
	public Handler getmFacebookHandler() {
		return mFacebookHandler;
	}

	/**
	 * @param mFacebookHandler the mFacebookHandler to set
	 */
	public void setmFacebookHandler(Handler mFacebookHandler) {
		this.mFacebookHandler = mFacebookHandler;
	}

	/**
	 * @return the fACEBOOK_APPID
	 */
	public String getFACEBOOK_APPID() {
		return FACEBOOK_APPID;
	}

	/**
	 * @param fACEBOOK_APPID the fACEBOOK_APPID to set
	 */
	public void setFACEBOOK_APPID(String fACEBOOK_APPID) {
		FACEBOOK_APPID = fACEBOOK_APPID;
	}

	/**
	 * @return the fACEBOOK_PERMISSION
	 */
	public String getFACEBOOK_PERMISSION() {
		return FACEBOOK_PERMISSION;
	}

	/**
	 * @param fACEBOOK_PERMISSION the fACEBOOK_PERMISSION to set
	 */
	public void setFACEBOOK_PERMISSION(String fACEBOOK_PERMISSION) {
		FACEBOOK_PERMISSION = fACEBOOK_PERMISSION;
	}

	/**
	 * @return the facebook
	 */
	public Facebook getFacebook() {
		//facebook = getFacebookConnector().getFacebook();
		return facebook;
	}

	/**
	 * @param facebook the facebook to set
	 */
	public void setFacebook(Facebook facebook) {
		this.facebook = facebook;
	}

	/* (non-Javadoc)
	 * @see android.app.Application#onConfigurationChanged(android.content.res.Configuration)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		facebook = null;
	}
	
	/* (non-Javadoc)
	 * @see android.app.Application#onLowMemory()
	 */
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	/* (non-Javadoc)
	 * @see android.app.Application#onTerminate()
	 */
	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}
}
