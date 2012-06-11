package sunnet.meobeo.line;

import java.util.List;
import sunnet.meobeo.framework.Game;
import sunnet.meobeo.framework.Graphics;
import sunnet.meobeo.framework.Input.KeyEvent;
import sunnet.meobeo.framework.Input.TouchEvent;
import sunnet.meobeo.framework.Screen;
import sunnet.meobeo.framework.impl.AndroidSprite;
import sunnet.meobeo.framework.impl.AndroidGame;

public class GameScreen extends Screen {
	enum GameState {
		Reading, Running, Paused, GameOver
	}

	final int SIZE_WIDTH = 109;
	int OX = 0;
	int OY = 130;
	int width_ball, height_ball, width_board, height_board;

	private AndroidSprite chooseBall;

	private boolean firstClick;
	private int srcRow, srcCol;
	public static int desCol, desRow;
	GameState state;
	public static Board mainBoard;

	private int i, j;
	private int tempRow, tempCol;
	private String string;

	public static boolean point;
	public int running_press;
	public int gameOver_press;

	int color;
	private boolean bong_no = false;
	private int delayBongNo = 0;
	private int[][] listPoint;
	private int countPoint;

	public int color_miniBall;
	public boolean insertMiniBall;

	List<TouchEvent> touchEvents;
	List<KeyEvent> keyEvents;
	TouchEvent event;
	KeyEvent eventKey;
	Graphics g;

	public GameScreen(Game game) {
		super(game);
		state = GameState.Running;
		running_press = 0;
		firstClick = false;
		mainBoard = new Board();
		srcRow = srcCol = -1;
		chooseBall = new AndroidSprite(game, this);
		width_ball = SIZE_WIDTH;
		height_ball = (int) (width_ball * ((AndroidGame) game).getScaleY() / ((AndroidGame) game)
				.getScaleX());
		width_board = SIZE_WIDTH * 7;
		height_board = height_ball * 7;
		// Log.d("height_board",""+height_board);
		// Log.d("height_ball",""+height_ball);

		listPoint = new int[2][30];

		g = game.getGraphics();
	}

	@Override
	public void update(long deltaTime) {
		touchEvents = game.getInput().getTouchEvents();
		keyEvents = game.getInput().getKeyEvents();

		if (state == GameState.Running) {
			updateRunning(touchEvents);
			// Log.d("update", "time");
		}
		if (state == GameState.GameOver)
			updateGameOver(touchEvents);

		// int lenKey = keyEvents.size();
		// for (i = 0; i < lenKey; i++) {
		// eventKey = keyEvents.get(i);
		// if (event.type == KeyEvent.KEY_DOWN) {
		// if (eventKey.keyCode == android.view.KeyEvent.KEYCODE_BACK) {
		// // saveSettings();
		// System.gc();
		// return;
		// }
		// }
		// }

	}

	public void updateRunning(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		// TouchEvent event;
		for (i = 0; i < len; i++) {
			event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {
				if (inBounds(event, OX, OY, width_board, height_board)
						&& !chooseBall.sliding) {
					if (!firstClick) {
						tempRow = (event.y - OY) / height_ball;
						tempCol = (event.x - OX) / width_ball;
						if (mainBoard.board[tempRow][tempCol] > 0) {
							srcCol = tempCol;
							srcRow = tempRow;
							firstClick = true;
							// chooseBall.initaliseFPS(100);
							chooseBall
									.setColor(mainBoard.board[srcRow][srcCol]);
							chooseBall.setXPos(OX + srcCol * width_ball);
							chooseBall.setYPos(OY + srcRow * height_ball);
							if (Settings.soundEnable) {
								Assets.touch.play(1);
							}
						}
					} else {
						tempCol = (event.x - OX) / width_ball;
						tempRow = (event.y - OY) / height_ball;

						if (mainBoard.board[tempRow][tempCol] <= 0) {
							mainBoard.checkPath(tempRow, tempCol, srcRow,
									srcCol);
							if (mainBoard.findWay) {
								chooseBall.sliding = true;
								firstClick = false;

								mainBoard.undo = true;
								mainBoard.backup();
								desRow = tempRow;
								desCol = tempCol;

								if (mainBoard.board[desRow][desCol] < 0) {
									insertMiniBall = true;
									color_miniBall = mainBoard.board[desRow][desCol];
								} else
									insertMiniBall = false;

								// chooseBall.initaliseFPS(100);
								chooseBall.setSlide(srcRow, srcCol);
								chooseBall
										.setColor(mainBoard.board[srcRow][srcCol]);

								if (Settings.soundEnable)
									Assets.move.play(1);

							}
						} else {
							srcCol = tempCol;
							srcRow = tempRow;
							desCol = desRow = -1;
							// chooseBall.initaliseFPS(100);
							// Log.d("src", "" + srcRow + " " + srcCol);
							chooseBall
									.setColor(mainBoard.board[srcRow][srcCol]);
							// Log.d("src", "" + srcRow + " " + srcCol);
							chooseBall.setXPos(OX + srcCol * width_ball);
							chooseBall.setYPos(OY + srcRow * height_ball);
							if (Settings.soundEnable) {
								Assets.touch.play(1);
							}
						}
					}
				}

				if (inBounds(event, 234, 900, 300, 110)) {
					running_press = 1;
					return;
				}
			}

			if (event.type == TouchEvent.TOUCH_UP) {
				if (running_press == 1) {
					running_press = 0;
					if (mainBoard.undo) {
						mainBoard.restore();
						mainBoard.undo = false;
						firstClick = false;
						srcRow = -1;
						srcCol = -1;
					}
					return;
				}
			}
		}
	}

	public void updateGameOver(List<TouchEvent> touchEvents) {
		int len = touchEvents.size();
		// TouchEvent event;
		for (i = 0; i < len; i++) {
			event = touchEvents.get(i);
			if (event.type == TouchEvent.TOUCH_DOWN) {
				if (inBounds(event, 121, 890, 102, 104)) {
					gameOver_press = 1;
					return;
				}
				if (inBounds(event, 233, 880, 301, 114)) {
					gameOver_press = 2;
					return;
				}
				if (inBounds(event, 544, 890, 102, 104)) {
					gameOver_press = 3;
					return;
				}
				gameOver_press = 0;
			}

			if (event.type == TouchEvent.TOUCH_UP) {
				if (gameOver_press == 2) {
					Settings.time = 0;
					game.setScreen(new MainMenuScreen(game));
					return;
				}
				gameOver_press = 0;

			}
		}
	}

	@Override
	public void present(long deltaTime) {
		/* dem thoi gian */
		// Graphics g = game.getGraphics();
		if (state == GameState.Running) {
			Settings.countTimer(deltaTime);
			drawRunningUI(g, deltaTime);
		}

		if (state == GameState.GameOver)
			drawGameOverUI(g);
	}

	public void drawRunningUI(Graphics g, long deltaTime) {
		g.drawPixmap(Assets.background, 0, 0);
		g.drawPixmap(Assets.bg_game, OX, OY, 0, 0, 768, 767, width_board,
				height_board);
		if (running_press == 1)
			g.drawPixmap(Assets.undo_click, 234, 900);
		else
			g.drawPixmap(Assets.undo, 234, 900);

		if (firstClick)
			drawAnimation(deltaTime);

		drawBoard();

		if (chooseBall.sliding)
			drawSlidingBall(g, deltaTime);

		string = "" + Settings.score;
		i = string.length();
		while (i++ != 6) {
			string = "0" + string;
		}
		drawNumber(string, 528, 50);

		drawTime(0, 50);

		// TODO
		if (bong_no && delayBongNo < 4) {
			drawDHBall();
			drawBongNo();
			delayBongNo++;
		} else {
			delayBongNo = 0;
			bong_no = false;
		}
	}

	public void drawGameOverUI(Graphics g) {
		g.drawPixmap(Assets.background, 0, 0);
		g.drawPixmap(Assets.bg_game, OX, OY, 0, 0, 768, 767, width_board,
				height_board);
		drawBoard();
		g.drawPixmap(Assets.bg_menu, 52, OY);

		string = "" + Settings.score;
		i = string.length();
		while (i++ != 6) {
			string = "0" + string;
		}
		drawNumber(string, 264, 522);

		drawTime(224, 452);

		switch (gameOver_press) {
		case 1:
			g.drawPixmap(Assets.left_click, 121, 920);
			g.drawPixmap(Assets.menu, 233, 908);
			g.drawPixmap(Assets.right, 544, 920);
			break;
		case 2:
			g.drawPixmap(Assets.left, 121, 920);
			g.drawPixmap(Assets.menu_click, 233, 908);
			g.drawPixmap(Assets.right, 544, 920);
			break;
		case 3:
			g.drawPixmap(Assets.left, 121, 920);
			g.drawPixmap(Assets.menu, 233, 908);
			g.drawPixmap(Assets.right_click, 544, 920);
			break;
		default:
			g.drawPixmap(Assets.left, 121, 920);
			g.drawPixmap(Assets.menu, 233, 908);
			g.drawPixmap(Assets.right, 544, 920);
			break;
		}

		drawDHDiem();

	}

	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	public void drawBoard() {
		for (i = 0; i < Board.ROW; i++)
			for (j = 0; j < Board.COL; j++) {
				if (i != srcRow || j != srcCol)
					switch (mainBoard.board[i][j]) {
					case 1:
						g.drawPixmap(Assets.bong, OX + j * width_ball, OY + i
								* height_ball, 109, 0, SIZE_WIDTH, SIZE_WIDTH,
								width_ball, height_ball);
						break;
					case 2:
						g.drawPixmap(Assets.bong, OX + j * width_ball, OY + i
								* height_ball, 218, 0, SIZE_WIDTH, SIZE_WIDTH,
								width_ball, height_ball);
						break;
					case 3:
						g.drawPixmap(Assets.bong, OX + j * width_ball, OY + i
								* height_ball, 327, 0, SIZE_WIDTH, SIZE_WIDTH,
								width_ball, height_ball);
						break;
					case 4:
						g.drawPixmap(Assets.bong, OX + j * width_ball, OY + i
								* height_ball, 436, 0, SIZE_WIDTH, SIZE_WIDTH,
								width_ball, height_ball);
						break;
					case 5:
						g.drawPixmap(Assets.bong, OX + j * width_ball, OY + i
								* height_ball, 545, 0, SIZE_WIDTH, SIZE_WIDTH,
								width_ball, height_ball);
						break;
					case -1:
						g.drawPixmap(Assets.bong_nho, OX + j * width_ball, OY
								+ i * height_ball, 109, 0, SIZE_WIDTH,
								SIZE_WIDTH, width_ball, height_ball);
						break;
					case -2:
						g.drawPixmap(Assets.bong_nho, OX + j * width_ball, OY
								+ i * height_ball, 218, 0, SIZE_WIDTH,
								SIZE_WIDTH, width_ball, height_ball);
						break;
					case -3:
						g.drawPixmap(Assets.bong_nho, OX + j * width_ball, OY
								+ i * height_ball, 327, 0, SIZE_WIDTH,
								SIZE_WIDTH, width_ball, height_ball);
						break;
					case -4:
						g.drawPixmap(Assets.bong_nho, OX + j * width_ball, OY
								+ i * height_ball, 436, 0, SIZE_WIDTH,
								SIZE_WIDTH, width_ball, height_ball);
						break;
					case -5:
						g.drawPixmap(Assets.bong_nho, OX + j * width_ball, OY
								+ i * height_ball, 545, 0, SIZE_WIDTH,
								SIZE_WIDTH, width_ball, height_ball);
						break;
					}
			}
	}

	public void drawBall(int number, int x, int y) {
		// Graphics g = game.getGraphics();
		switch (number) {
		case 1:
			g.drawPixmap(Assets.bong, x, y, 109, 0, SIZE_WIDTH, SIZE_WIDTH,
					width_ball, height_ball);
			break;
		case 2:
			g.drawPixmap(Assets.bong, x, y, 218, 0, SIZE_WIDTH, SIZE_WIDTH,
					width_ball, height_ball);
			break;
		case 3:
			g.drawPixmap(Assets.bong, x, y, 327, 0, SIZE_WIDTH, SIZE_WIDTH,
					width_ball, height_ball);
			break;
		case 4:
			g.drawPixmap(Assets.bong, x, y, 436, 0, SIZE_WIDTH, SIZE_WIDTH,
					width_ball, height_ball);
			break;
		case 5:
			g.drawPixmap(Assets.bong, x, y, 545, 0, SIZE_WIDTH, SIZE_WIDTH,
					width_ball, height_ball);
			break;
		}
	}

	public void drawAnimation(long deltaTime) {
		chooseBall.update(deltaTime);
		chooseBall.draw(width_ball, height_ball);
	}

	public void drawSlidingBall(Graphics g, long deltaTime) {
		chooseBall.updateSlidingBall(deltaTime, width_ball, height_ball);
		chooseBall.draw(width_ball, height_ball);
	}

	public void drawNumber(String line, int x, int y) {
		int len = line.length();
		int srcX = 0;
		for (i = 0; i < len; i++) {
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

	public void drawTime(int x, int y) {
		if (Settings.hour < 10)
			string = "0" + Settings.hour;
		else
			string = "" + Settings.hour;
		drawNumber(string, x, y);

		g.drawPixmap(Assets.number, x + 80, y, 400, 0, 40, 50);

		if (Settings.minute < 10)
			string = "0" + Settings.minute;
		else
			string = "" + Settings.minute;
		drawNumber(string, x + 120, y);

		g.drawPixmap(Assets.number, x + 200, y, 400, 0, 40, 50);

		if (Settings.second < 10)
			string = "0" + Settings.second;
		else
			string = "" + Settings.second;
		drawNumber(string, x + 240, y);
	}

	public void drawBongNo() {
		switch (color) {
		case 1:
			for (i = 1; i <= countPoint; i++) {
				g.drawPixmap(Assets.bong_no, OX + listPoint[1][i] * width_ball,
						OY + listPoint[0][i] * height_ball, 109, 0, SIZE_WIDTH,
						SIZE_WIDTH, width_ball, height_ball);
			}
			break;
		case 2:
			for (i = 1; i <= countPoint; i++) {
				g.drawPixmap(Assets.bong_no, OX + listPoint[1][i] * width_ball,
						OY + listPoint[0][i] * height_ball, 218, 0, SIZE_WIDTH,
						SIZE_WIDTH, width_ball, height_ball);
			}
			break;
		case 3:
			for (i = 1; i <= countPoint; i++) {
				g.drawPixmap(Assets.bong_no, OX + listPoint[1][i] * width_ball,
						OY + listPoint[0][i] * height_ball, 327, 0, SIZE_WIDTH,
						SIZE_WIDTH, width_ball, height_ball);
			}
			break;
		case 4:
			for (i = 1; i <= countPoint; i++) {
				g.drawPixmap(Assets.bong_no, OX + listPoint[1][i] * width_ball,
						OY + listPoint[0][i] * height_ball, 436, 0, SIZE_WIDTH,
						SIZE_WIDTH, width_ball, height_ball);
			}
			break;
		case 5:
			for (i = 1; i <= countPoint; i++) {
				g.drawPixmap(Assets.bong_no, OX + listPoint[1][i] * width_ball,
						OY + listPoint[0][i] * height_ball, 545, 0, SIZE_WIDTH,
						SIZE_WIDTH, width_ball, height_ball);
			}
			break;
		}

	}

	public void drawDHDiem() {
		if (Settings.score < 50) {
			g.drawPixmap(Assets.ga_dich, 284, 300);
			return;
		}
		if (Settings.score < 150) {
			g.drawPixmap(Assets.ga_toi, 284, 300);
			return;
		}
		if (Settings.score < 250) {
			g.drawPixmap(Assets.ga_que, 284, 300);
			return;
		}
		if (Settings.score < 350) {
			g.drawPixmap(Assets.ga_thien, 284, 300);
			return;
		}
		if (Settings.score < 450) {
			g.drawPixmap(Assets.ga_mo, 284, 300);
			return;
		}

	}

	public void drawDHBall() {
		switch (countPoint) {
		case 7:
			g.drawPixmap(Assets.xuat_sac, 134, 900);
			break;
		case 9:
			g.drawPixmap(Assets.ngac_nhien, 134, 900);
			break;
		case 11:
			g.drawPixmap(Assets.tuyet_voi, 134, 900);
			break;
		case 13:
			g.drawPixmap(Assets.tot_dinh, 134, 900);
			break;
		case 17:
			g.drawPixmap(Assets.kiet_xuat, 134, 900);
			break;
		case 18:
			g.drawPixmap(Assets.phi_thuong, 134, 900);
			break;
		default:
			if (countPoint >= 19)
				g.drawPixmap(Assets.gioi_vai, 134, 900);
			break;
		}
	}

	public void deleteTinyDes() {
		for (j = 0; j < 3; j++) {
			if ((mainBoard.tinyBalls[0][j] == desRow)
					&& (mainBoard.tinyBalls[1][j] == desCol)) {
				mainBoard.tinyBalls[2][j] = 1;
			}
		}
	}

	public void deleteAllTiny() {
		for (j = 0; j < 3; j++) {
			mainBoard.tinyBalls[2][j] = 1;
		}
	}

	public void deleteListBall() {
		for (i = 1; i <= mainBoard.countPoint; i++) {
			mainBoard.board[mainBoard.listPoint[0][i]][mainBoard.listPoint[1][i]] = 0;
		}
	}

	public void switchBall() {
		mainBoard.board[desRow][desCol] = mainBoard.board[srcRow][srcCol];
		mainBoard.board[srcRow][srcCol] = 0;
	}

	public void tinyToBig() {

	}

	public void checkPoint() {
		srcRow = -1;
		srcCol = -1;
		if (mainBoard.checkPoint(desRow, desCol)) {
			if (Settings.soundEnable)
				soundPoint();
			bong_no = true;
			color = mainBoard.board[desRow][desCol];
			for (i = 0; i <= mainBoard.countPoint; i++) {
				listPoint[0][i] = mainBoard.listPoint[0][i];
				listPoint[1][i] = mainBoard.listPoint[1][i];
			}
			this.countPoint = mainBoard.countPoint;
			deleteListBall();
			if (insertMiniBall) {
				mainBoard.board[desRow][desCol] = color_miniBall;
			}
		} else {
			if (insertMiniBall) {
				mainBoard.insertMiniBall(color_miniBall, desRow, desCol);
			}
			for (j = 0; j < 3; j++) {
				if (mainBoard.tinyBalls[2][j] == 0) {
					tempRow = mainBoard.tinyBalls[0][j];
					tempCol = mainBoard.tinyBalls[1][j];
					mainBoard.board[tempRow][tempCol] *= -1;
					if (mainBoard.checkPoint(tempRow, tempCol)) {
						if (Settings.soundEnable)
							soundPoint();
						bong_no = true;
						color = mainBoard.board[tempRow][tempCol];
						for (i = 0; i <= mainBoard.countPoint; i++) {
							listPoint[0][i] = mainBoard.listPoint[0][i];
							listPoint[1][i] = mainBoard.listPoint[1][i];
						}
						this.countPoint = mainBoard.countPoint;
						deleteListBall();
					}
				}
			}

			mainBoard.checkGameOver(insertMiniBall);
			if (mainBoard.gameOver) {
				if (Settings.soundEnable)
					Assets.gameOver.play(1);
				state = GameState.GameOver;
				// saveSettings();
				return;
			} else {
				mainBoard.addNextBall();
			}

		}
	}

	public void soundPoint() {
		if (countPoint < 7) {
			Assets.boom1.play(1);
			return;
		}
		if (countPoint < 10) {
			Assets.boom2.play(1);
			Assets.clap.play(1);
			return;
		}
		if (countPoint < 14) {
			Assets.boom3.play(1);
			Assets.clap.play(1);
			return;
		}
		Assets.boom4.play(1);
		Assets.clap.play(1);
	}

	@Override
	public void pause() {
		if (state == GameState.Running)
			state = GameState.Paused;
	}

	public void saveSettings() {
		Settings.addScore(Settings.score);
		Settings.save(game.getFileIO());
	}

	@Override
	public void resume() {
		state = GameState.Running;
		Settings.startTime = System.currentTimeMillis();
	}

	@Override
	public void dispose() {

	}

}
