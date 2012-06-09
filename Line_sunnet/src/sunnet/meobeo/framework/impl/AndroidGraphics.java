package sunnet.meobeo.framework.impl;

import java.io.IOException;
import java.io.InputStream;

import sunnet.meobeo.framework.Graphics;
import sunnet.meobeo.framework.Pixmap;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;

public class AndroidGraphics implements Graphics {
	AssetManager assets;
	Bitmap frameBuffer;
	Canvas canvas;
	Paint paint;
	Typeface mFace;
	Rect srcRect = new Rect();
	Rect desRect = new Rect();
	
	InputStream in;
	Bitmap bitmap;
	
	public AndroidGraphics(AssetManager assets, Bitmap frameBuffer){
		this.assets = assets;
		this.frameBuffer = frameBuffer;
		this.canvas = new Canvas(frameBuffer);
		this.paint = new Paint();
	}
	
	public AndroidGraphics(AssetManager assets, Bitmap frameBuffer, Context context) {
		this.assets = assets;
		this.frameBuffer = frameBuffer;
		this.canvas = new Canvas(frameBuffer);
		this.paint = new Paint();
		
	}
	
	public Pixmap newPixmap(String fileName, PixmapFormat format){
		/*
		 * �?ịnh dạng ảnh xuất ra
		 */
		Config config = null;
		if(format == PixmapFormat.RGB565)
			config = Config.RGB_565;
		else if(format == PixmapFormat.ARGB4444)
			config = Config.ARGB_4444;
		else 
			config = Config.ARGB_8888;
		
		Options options = new Options();
		options.inPreferredConfig = config;
		
		in = null;
		bitmap = null;
		/*
		 * tạo ra 1 bitmap từ asset sử dụng pixmapFormat ở trên ^
		 */
		try{
			in = assets.open(fileName);
			bitmap = BitmapFactory.decodeStream(in);
			if(bitmap == null) {
				bitmap.recycle();
				throw new RuntimeException("Could not load bitmap from assets"+fileName+" ");
			}
		}catch (IOException e){
			throw new RuntimeException("Could not load bitmap from assets"+fileName+" ");
		}finally{
			if(in != null){
				try{
					in.close();
				}catch(IOException e){
					
				}
			}
		}
		/*
		 * format
		 */
		
		if(bitmap.getConfig() == Config.RGB_565)
			format = PixmapFormat.RGB565;
		else if(bitmap.getConfig() == Config.ARGB_4444)
			format = PixmapFormat.ARGB4444;
		else 
			format = PixmapFormat.ARGB8888;
		return (Pixmap)new AndroidPixmap(bitmap, format);
		
	}
	
	public void clear (int color){
		canvas.drawRGB((color & 0xff0000) >> 16,(color & 0xff00) >> 8, (color & 0xff));
	}
	
	public void drawPixel(int x, int y, int color){
		paint.setColor(color);
		canvas.drawPoint(x, y, paint);
	}
	
	public void drawLine(int x, int y, int x2, int y2, int color){
		paint.setColor(color);
		canvas.drawLine(x, y, x2, y2, paint);
	}
	
	public void drawRect(int x, int y, int width, int height, int color){
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawRect(x,y,x+width-1,y+height-1, paint);
	}
	
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight){
		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.right = srcX + srcWidth -1;
		srcRect.bottom = srcY + srcHeight -1;
		
		desRect.left = x;
		desRect.top = y;
		desRect.right = x + srcWidth -1;
		desRect.bottom = y + srcHeight -1;
		
		canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, srcRect, desRect, null);
	}
	
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight, int desWidth, int desHeight) {
		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.right = srcX + srcWidth -1;
		srcRect.bottom = srcY + srcHeight -1;
		
		desRect.left = x;
		desRect.top = y;
		desRect.right = x + desWidth - 1;
		desRect.bottom = y + desHeight - 1;
		
		canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, srcRect, desRect, null);
	}
	
	public void drawPixmap(Pixmap pixmap, int x, int y){
		canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, x, y, null);
	}

	public void drawText(String name, int x, int y, int color, int size) {
		paint.setTypeface(this.mFace);
		paint.setColor(color);
		paint.setTextSize(size);
		paint.setTextAlign(Paint.Align.CENTER);
		canvas.drawText(name, x, y, paint);
	}
	
	public int getWidth(){
		return frameBuffer.getWidth();
	}

	public int getHeight(){
		return frameBuffer.getHeight();
	}
}
