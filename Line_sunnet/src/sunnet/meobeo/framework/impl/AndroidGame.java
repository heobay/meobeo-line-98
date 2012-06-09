package sunnet.meobeo.framework.impl;

import sunnet.meobeo.framework.Audio;
import sunnet.meobeo.framework.FileIO;
import sunnet.meobeo.framework.Game;
import sunnet.meobeo.framework.Graphics;
import sunnet.meobeo.framework.Input;
import sunnet.meobeo.framework.Screen;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

public class AndroidGame extends Activity implements Game {
	AndroidFastRenderView renderView;
	Graphics graphics;
	Audio audio;
	Input input;
	FileIO fileIO;
	Screen screen;
	WakeLock wakeLock;
	Typeface mFace;
	public float scaleX;
	public float scaleY;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE); // ko có title trên màn
														// hình hinh
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		boolean isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
		int frameBufferWidth = isLandscape ? 1024 : 768;
		int frameBufferHeight = isLandscape ? 768 : 1024;
		Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
				frameBufferHeight, Config.RGB_565);

		scaleX = (float) frameBufferWidth
				/ getWindowManager().getDefaultDisplay().getWidth();
		scaleY = (float) frameBufferHeight
				/ getWindowManager().getDefaultDisplay().getHeight();

		renderView = new AndroidFastRenderView(this, frameBuffer);
		graphics = (Graphics) new AndroidGraphics(getAssets(), frameBuffer, this);
		
		fileIO = new AndroidFileIO(getAssets());
		audio = new AndroidAudio(this);
		input = new AndroidInput(this, renderView, scaleX, scaleY);
		screen = getStartScreen();
		setContentView(renderView);

		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"GLGame");
	}

	public void onResume() {
		super.onResume();
		wakeLock.acquire();
		screen.resume();
		renderView.resume();
	}

	public void onPause() {
		super.onPause();
		wakeLock.release();
		renderView.pause();
		screen.pause();
		if (isFinishing())
			screen.dispose();
	}	

	public Input getInput() {
		return input;
	}

	public FileIO getFileIO() {
		return fileIO;
	}

	public Graphics getGraphics() {
		return graphics;
	}

	public Audio getAudio() {
		return audio;
	}

	public void setScreen(Screen screen) {
		if (screen == null)
			throw new IllegalArgumentException("Screen must not be null");

		this.screen.pause();
		this.screen.dispose();
		screen.resume();
		screen.update(0);
		this.screen = screen;
	}

	public Screen getCurrentScreen() {
		return screen;
	}

	public Screen getStartScreen() {
		return null;
	}
	
	public float getScaleX() {
		return this.scaleX;
	}
	
	public float getScaleY() {
		return this.scaleY;
	}
	
//	public void disPlayToast(String string) {
//		Toast.makeText(getApplication(), string, Toast.LENGTH_SHORT).show();
//	}
}