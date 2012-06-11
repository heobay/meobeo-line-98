package sunnet.meobeo.line;

import java.util.List;

import android.content.Intent;
import sunnet.meobeo.framework.Game;
import sunnet.meobeo.framework.Graphics;
import sunnet.meobeo.framework.Input.KeyEvent;
import sunnet.meobeo.framework.Input.TouchEvent;
import sunnet.meobeo.framework.Screen;
import sunnet.meobeo.framework.impl.AndroidGame;

public class MainMenuScreen extends Screen {
	private int button_press;

	public MainMenuScreen(Game game) {
		super(game);
	}

	@Override
	public void update(long deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		List<KeyEvent> keyEvents = game.getInput().getKeyEvents();

		int lenTouch = touchEvents.size();
		for (int i = 0; i < lenTouch; i++) {
			TouchEvent event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {
				if (inBounds(event, 430, 410, 192, 96)) {
					button_press = 1;
					return;
				}
				if (inBounds(event, 375, 506, 305, 96)) {
					button_press = 2;
					return;
				}
				if (inBounds(event, 430, 602, 192, 96)) {
					button_press = 3;
					return;
				}
			}

			if (event.type == TouchEvent.TOUCH_UP) {
				if (button_press == 1) {
					button_press = 0;
					game.setScreen(new GameScreen(game));
					return;
				}
				if (button_press == 2) {
					button_press = 0;
					game.setScreen(new HighscoreScreen(game));
					return;
				}
				if (button_press == 3) {
					button_press = 0;
					Intent intent = new Intent((AndroidGame)this.game, Option.class);
					((AndroidGame)this.game).startActivity(intent);
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

	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	@Override
	public void present(long deltaTime) {
		Graphics g = game.getGraphics();

		g.drawPixmap(Assets.bg, 0, 0);

		if (button_press == 1)
			g.drawPixmap(Assets.bt_play_ck, 430, 410);
		else
			g.drawPixmap(Assets.bt_play, 430, 410);

		if (button_press == 2)
			g.drawPixmap(Assets.bt_achievement_ck, 375, 506);
		else
			g.drawPixmap(Assets.bt_achievement, 375, 506);

		if (button_press == 3)
			g.drawPixmap(Assets.bt_option_ck, 430, 602);
		else
			g.drawPixmap(Assets.bt_option, 430, 602);
	}

	public void saveSettings() {
		Settings.addScore(Settings.score);
		Settings.save(game.getFileIO());
	}
	
	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {

	}

}
