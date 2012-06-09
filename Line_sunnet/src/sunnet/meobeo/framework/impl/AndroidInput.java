package sunnet.meobeo.framework.impl;

import java.util.List;

import sunnet.meobeo.framework.Input;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

public class AndroidInput implements Input {
	KeyboardHandler keyHandler;
	TouchHandler touchHandler;

	public AndroidInput(Context context, View view, float scaleX, float scaleY) {
		keyHandler = new KeyboardHandler(view);
		if (Integer.parseInt(VERSION.SDK) < 5)
			touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
		else
			touchHandler = new MultiTouchHandler(view, scaleX, scaleY);
	}

	public boolean isKeyPressed(int keyCode) {
		return keyHandler.isKeyPressed(keyCode);
	}

	public boolean isTouchDown(int pointer) {
		return touchHandler.isTouchDown(pointer);
	}

	public int getTouchX(int pointer) {
		return touchHandler.getTouchX(pointer);
	}

	public int getTouchY(int pointer) {
		return touchHandler.getTouchY(pointer);
	}

	public List<TouchEvent> getTouchEvents() {
		return touchHandler.getTouchEvents();
	}

	public List<KeyEvent> getKeyEvents() {
		return keyHandler.getKeyEvents();
	}
}
