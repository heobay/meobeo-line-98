package sunnet.meobeo.framework;

import sunnet.meobeo.framework.Graphics.PixmapFormat;

public interface Pixmap {
	public int getWidth();
	public int getHeight();
	public PixmapFormat getFormat();
	public void dispose();
}
