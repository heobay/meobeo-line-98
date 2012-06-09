package sunnet.meobeo.framework.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
//import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
	AndroidGame game;
	Bitmap framebuffer;
	Thread thread = null;
	SurfaceHolder holder;
	volatile boolean running = false;
	long deltaTime;

	public AndroidFastRenderView(AndroidGame game, Bitmap framebuffer) {
		super(game);
		this.game = game;
		this.framebuffer = framebuffer;
		this.holder = getHolder();
	}

	public void resume() {
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public void run() {
		Rect dstRect = new Rect();
		while (running) {
			if (!holder.getSurface().isValid())
				continue;
			deltaTime = System.currentTimeMillis();
			game.getCurrentScreen().update(deltaTime);
			game.getCurrentScreen().present(deltaTime);

			Canvas canvas = holder.lockCanvas();
			canvas.getClipBounds(dstRect);
			canvas.drawBitmap(framebuffer, null, dstRect, null);
			holder.unlockCanvasAndPost(canvas);
		}
	}

	public void pause() {
		running = false;
		while (true) {
			try {
				thread.join();
				break;
			} catch (InterruptedException e) {

			}
		}
	}
}
