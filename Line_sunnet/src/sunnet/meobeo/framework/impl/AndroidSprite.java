package sunnet.meobeo.framework.impl;

import sunnet.meobeo.framework.Game;
import sunnet.meobeo.framework.Graphics;
import sunnet.meobeo.line.Assets;
import sunnet.meobeo.line.Board;
import sunnet.meobeo.line.GameScreen;

public class AndroidSprite {
	private int mFPS;
	private long mFrameTimer;
	private int mXPos;
	private int mYPos;
	private int upAndDown;

	public boolean sliding;
	public int distance = 0;
	public int rowSlide, colSlide;
	public int tempRow, tempCol;
	public int color;

	private Game game;
	private GameScreen gameScreen;

	public AndroidSprite(Game game, GameScreen gameScreen) {
		this.game = game;
		this.gameScreen = gameScreen;
		upAndDown = 5;
		sliding = false;
		mFPS = 100;
	}

	public void initaliseFPS(int theFPS) {
		mFPS = theFPS;
		mFrameTimer = System.currentTimeMillis();
	}


	public void update(long deltaTime) {
		if (deltaTime > mFrameTimer + mFPS) {
			mFrameTimer = deltaTime;
			upAndDown = -(upAndDown);
		}
	}

	public void updateSlidingBall(long deltaTime, int width_ball, int height_ball) {
		if (distance == 0) {
			tempRow = Board.dad[rowSlide][colSlide].x;
			tempCol = Board.dad[rowSlide][colSlide].y;
		}
		if (deltaTime > mFrameTimer + mFPS) {
			
			mFrameTimer = deltaTime;
			if (tempRow == rowSlide && tempCol < colSlide) {
				distance += width_ball;
				mXPos -= width_ball;
			}
			if (tempRow == rowSlide && tempCol > colSlide) {
				distance += width_ball;
				mXPos += width_ball;
			}
			if (tempRow < rowSlide && tempCol == colSlide) {
				distance += height_ball;
				mYPos -= height_ball;
			}
			if (tempRow > rowSlide && tempCol == colSlide) {
				distance += height_ball;
				mYPos += height_ball;
			}
			if (tempRow == GameScreen.desRow && tempCol == GameScreen.desCol
					&& ((distance == width_ball) || (distance == height_ball))) {
				distance = 0;
				gameScreen.switchBall();
				gameScreen.checkPoint();
				sliding = false;
			}
			if ((distance == width_ball) || (distance == height_ball)) {
				distance = 0;
				rowSlide = tempRow;
				colSlide = tempCol;
			}
		}
	}


	public void draw(int width_ball, int height_ball) {
		Graphics g = game.getGraphics();
		switch (color) {
		case 1:
			g.drawPixmap(Assets.bong, mXPos, mYPos + upAndDown, 109, 0, 109, 109, width_ball, height_ball);
			break;
		case 2:
			g.drawPixmap(Assets.bong, mXPos, mYPos + upAndDown, 218, 0, 109, 109, width_ball, height_ball);
			break;
		case 3:
			g.drawPixmap(Assets.bong, mXPos, mYPos + upAndDown, 327, 0, 109, 109, width_ball, height_ball);
			break;
		case 4:
			g.drawPixmap(Assets.bong, mXPos, mYPos + upAndDown, 436, 0, 109, 109, width_ball, height_ball);
			break;
		case 5:
			g.drawPixmap(Assets.bong, mXPos, mYPos + upAndDown, 545, 0, 109, 109, width_ball, height_ball);
			break;
		}
	}

	public int getXPos() {
		return mXPos;
	}

	public int getYPos() {
		return mYPos;
	}

	public void setXPos(int xPos) {
		mXPos = xPos;
	}

	public void setYPos(int yPos) {
		mYPos = yPos;
	}

	public void setSlide(int row, int col) {
		this.rowSlide = row;
		this.colSlide = col;
	}

	public void setColor(int color) {
		this.color = color;
	}
}
