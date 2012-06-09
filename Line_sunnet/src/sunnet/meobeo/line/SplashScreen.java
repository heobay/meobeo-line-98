/*
 * @(#)SplasScreen.java     1.0 2011/10/01
 * Copyright (C) 2011 Sunnet ITC Solution
 */
package sunnet.meobeo.line;

import vn.sunnet.util.ui.SplashView;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class SplashScreen extends Activity {
	public static int SPLASH_TIME = 3200;
	public static int MSG_START_ANIM = 0;
	public static int MSG_STOP_ANIM = 1;
	
	private FrameLayout mlayout;
	private SplashView mView;
	private Handler hRefresh;
	private boolean blnIsStopAnim = false;

	/**
	 * On create
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		//		if (qplay_sound == null)
		//			qplay_sound = new Audio(this, R.raw.backgournd_sound);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//qplay_sound.play();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		setContentView(R.layout.splash);

		// init animation
		mlayout = (FrameLayout) findViewById(R.id.splash_view);
		mView = new SplashView(this);
		mlayout.addView(mView);
		
//		slide = AnimationUtils.loadAnimation(this, R.anim.animation);
		blnIsStopAnim = false;

//		// Create handler
		hRefresh = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch(msg.what){
				case 0:
					/*Refresh UI*/
					runAnimation();
					break;
				case 1:
					stopAnimation();
				}
			}
		};

		// post msg for handle
		hRefresh.sendEmptyMessage(MSG_START_ANIM);
		hRefresh.sendEmptyMessageDelayed(MSG_STOP_ANIM, SPLASH_TIME);
	}

	/**
	 * Run animation
	 */
	public void runAnimation() {
		blnIsStopAnim = false;
		mView.startAnim();
//		mlayout.startAnimation(slide);
	}
	
	/**
	 * Stop animation
	 */
	public void stopAnimation() {
//		mView
		if (blnIsStopAnim) return;
		mView.stopAnim();
		blnIsStopAnim = true;
		
		Intent i = new Intent(SplashScreen.this, LineGame.class);
		startActivity(i);
		finish();
	}

	/**
	 * Stop animation on touch event
	 */
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) stopAnimation();
		return true;
	}
}
