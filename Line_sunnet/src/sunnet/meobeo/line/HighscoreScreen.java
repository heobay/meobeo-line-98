package sunnet.meobeo.line;

import java.util.List;

import sunnet.meobeo.framework.Game;
import sunnet.meobeo.framework.Graphics;
import sunnet.meobeo.framework.Screen;
import sunnet.meobeo.framework.Input.KeyEvent;
import sunnet.meobeo.framework.Input.TouchEvent;

public class HighscoreScreen extends Screen {
	String line[] = new String[5];
	int button_press;

	public HighscoreScreen(Game game) {
		super(game);
		for (int i = 0; i < 5; i++)
			line[i] = "" + Settings.highscores[i];
		button_press = 0;
	}

	public void update(long deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		List<KeyEvent> keyEvents = game.getInput().getKeyEvents();

		int len = touchEvents.size();
		for (int i = 0; i < len; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {
				if (inBounds(event, 230, 860, 302, 113)) {
					button_press = 1;
					return;
				}
			}

			if (event.type == TouchEvent.TOUCH_UP) {
				if (button_press == 1) {
					button_press = 0;
					game.setScreen(new MainMenuScreen(game));
					return;
				}
			}

		}

		int lenKey = keyEvents.size();
		for (int i = 0; i < lenKey; i++) {
			KeyEvent event = keyEvents.get(i);
			if (event.type == KeyEvent.KEY_DOWN) {
				if (event.keyCode == android.view.KeyEvent.KEYCODE_BACK) {
//					saveSettings();
					System.gc();
					return;
				}
			}
		}
	}

	public void present(long deltaTime) {
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.hight_score, 0, 0);

		drawText(g, line[0], 250);
		drawText(g, line[1], 380);
		drawText(g, line[2], 480);
		drawText(g, line[3], 610);
		drawText(g, line[4], 740);

		if (button_press == 1)
			g.drawPixmap(Assets.ok_click, 230, 860);

	}

	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	public void drawText(Graphics g, String line, int y) {
		int len = line.length();
		int x = 384 - len * 20;
		int srcX = 0;
		for (int i = 0; i < len; i++) {
			char character = line.charAt(i);
			if (character - '0' == 0) {
				srcX = 360;
			} else {
				srcX = (character - '1') * 40;
			}
			g.drawPixmap(Assets.number, x, y, srcX, 0, 40, 50);
			x += 40;
		}
	}

	public void saveSettings() {
		Settings.addScore(Settings.score);
		Settings.save(game.getFileIO());
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
