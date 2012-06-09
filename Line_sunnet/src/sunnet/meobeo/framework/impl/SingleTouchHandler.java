package sunnet.meobeo.framework.impl;

import java.util.ArrayList;
import java.util.List;

import sunnet.meobeo.framework.Input.TouchEvent;
import sunnet.meobeo.framework.Pool;
import sunnet.meobeo.framework.Pool.PoolObjectFactory;

import android.view.MotionEvent;
import android.view.View;

public class SingleTouchHandler implements TouchHandler {
	boolean isTouched;
	int touchX;
	int touchY;
	Pool<TouchEvent> touchEventPool; // xem lai class Pool
	List<TouchEvent> touchEvents = new ArrayList<TouchEvent>(); /*
																 * tương tự
																 * class
																 * KeyboardHandler
																 */
	List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
	float scaleX;
	float scaleY;

	/*
	 * Khởi tạo class SingleTouchHandler, không có gì đặc biệt
	 */
	public SingleTouchHandler(View view, float scaleX, float scaleY) {
		PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
			public TouchEvent createObject() {
				return new TouchEvent();
			}
		};
		touchEventPool = new Pool<TouchEvent>(factory, 100);
		view.setOnTouchListener(this);

		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	/*
	 * tương ứng với mỗi event touch DOWN, UP, CANCEL, MOVE thì thiết lập
	 * touchEvent.type tương ứng thiết lập isTouched true nếu touch, và false
	 * nếu ko touch
	 */

	public boolean onTouch(View v, MotionEvent event) {
		synchronized (this) {
			TouchEvent touchEvent = touchEventPool.newObject(); // tạo mới 1
																// instance của
																// TouchEvent
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touchEvent.type = TouchEvent.TOUCH_DOWN;
				isTouched = true;
				break;
			case MotionEvent.ACTION_MOVE:
				touchEvent.type = TouchEvent.TOUCH_DRAGGED;
				isTouched = true;
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				touchEvent.type = TouchEvent.TOUCH_UP;
				isTouched = false;
				break;
			}

			touchEvent.x = touchX = (int) (event.getX() * scaleX); // t�?a độ
																	// touch
			touchEvent.y = touchY = (int) (event.getY() * scaleY);
			touchEventsBuffer.add(touchEvent);

			return true;
		}
	}

	public boolean isTouchDown(int pointer) {
		synchronized (this) {
			if (pointer == 0)
				return isTouched;
			else
				return false;
		}
	}

	public int getTouchX(int pointer) {
		synchronized (this) {
			return touchX;
		}
	}

	public int getTouchY(int pointer) {
		synchronized (this) {
			return touchY;
		}
	}

	public List<TouchEvent> getTouchEvents() {
		synchronized (this) {
			int len = touchEvents.size();
			for (int i = 0; i < len; i++)
				touchEventPool.free(touchEvents.get(i));
			touchEvents.clear();
			touchEvents.addAll(touchEventsBuffer);
			touchEventsBuffer.clear();
			return touchEvents;
		}
	}
}
