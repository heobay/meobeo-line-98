package sunnet.meobeo.line;

import sunnet.meobeo.framework.Screen;
import sunnet.meobeo.framework.impl.AndroidGame;

public class LineGame extends AndroidGame {
	@Override
	public Screen getStartScreen() {
		return new LoadingScreen(this);
	}
}
