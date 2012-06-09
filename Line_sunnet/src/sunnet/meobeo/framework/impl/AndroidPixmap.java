package sunnet.meobeo.framework.impl;

import sunnet.meobeo.framework.Graphics.PixmapFormat;
import sunnet.meobeo.framework.Pixmap;
import android.graphics.Bitmap;

public class AndroidPixmap implements Pixmap {
	public Bitmap bitmap;
	PixmapFormat format;

	public AndroidPixmap(Bitmap bitmap, PixmapFormat format) {
		this.bitmap = bitmap;
		this.format = format;
	}

	public int getWidth() {
		return bitmap.getWidth();
	}

	public int getHeight() {
		return bitmap.getHeight();
	}

	public PixmapFormat getFormat() {
		return format;
	}
	
	public Bitmap getBitmap(){
		return bitmap;
	}

	public void dispose() {
		bitmap.recycle();
	}

}
