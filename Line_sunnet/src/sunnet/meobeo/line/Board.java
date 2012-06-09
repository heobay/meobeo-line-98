package sunnet.meobeo.line;

import java.util.Random;

import android.graphics.Point;

public class Board {
	static final int ROW = 7;
	static final int COL = 7;
	static final int INTIAL_BALL = 5;
	static final int NEXT_BALL = 3;
	static final int MAX_COLOR = 5;
	static final int EAT_BALL = 5;

	static Random r;
	int i, j, stop, remain, temp, vertical, countTemp, size;
	public int countPoint;
	int[][] listPoint;
	public int[][] board;
	public int[][] undoBoard;
	public boolean undo;

	public static Point[][] dad;

	private int[] u = { 1, 0, -1, 0 };
	private int[] v = { 0, 1, 0, -1 };
	private int[] OX = { -1, 0, 1, 1 };
	private int[] OY = { 1, 1, 1, 0 };
	private int[] queueX;
	private int[] queueY;
	private int[][] color;
	private int qStart, qEnd;
	private int rowTemp, colTemp, x, y;
	public boolean findWay, gameOver;
	public int[][] tinyBalls;
	public int[][] undoTinyBalls;

	private int nextBall;;

	public Board() {
		r = new Random();
		board = new int[ROW][COL];
		undoBoard = new int[ROW][COL];
		dad = new Point[ROW][COL];
		for (i = 0; i < ROW; i++)
			for (j = 0; j < COL; j++) {
				dad[i][j] = new Point();
			}
		queueX = new int[ROW * COL];
		queueY = new int[ROW * COL];
		color = new int[ROW][COL];
		tinyBalls = new int[3][3];
		undoTinyBalls = new int[3][3];
		listPoint = new int[2][30];
		creatBoard();
		addNextBall();
	}

	public void creatBoard() {
		for (i = 0; i < ROW; i++)
			for (j = 0; j < COL; j++)
				board[i][j] = 0;

		size = ROW * COL;
		int count2 = size - INTIAL_BALL;
		do {
			remain = r.nextInt(size--) + 1;
			stop = 0;
			for (i = 0; i < ROW; i++) {
				if (stop == 1)
					break;
				for (j = 0; j < COL; j++) {
					if (board[i][j] == 0) {
						remain--;
						if (remain == 0) {
							board[i][j] = r.nextInt(MAX_COLOR) + 1;
							stop = 1;
							break;
						}
					}
				}
			}

		} while (size > count2);

		gameOver = false;

		Settings.score = 0;
		Settings.time = 0;
		Settings.startTime = System.currentTimeMillis();
	}

	public void addNextBall() {
		size = 0;
		for (i = 0; i < ROW; i++)
			for (j = 0; j < COL; j++)
				if (board[i][j] == 0)
					size++;
		if (size >= NEXT_BALL)
			nextBall = NEXT_BALL;
		else {
			nextBall = size;
			for (i = nextBall; i < NEXT_BALL; i++) {
				tinyBalls[2][i] = 1;
			}
		}
		
		for (temp = 0; temp < nextBall; temp++) {
			stop = 0;
			remain = r.nextInt(size--) + 1;
			for (i = 0; i < ROW; ++i) {
				if (stop == 1)
					break;
				for (j = 0; j < COL; j++) {
					if (board[i][j] == 0) {
						remain--;
						if (remain == 0) {
							board[i][j] = -(r.nextInt(MAX_COLOR) + 1);
							tinyBalls[0][temp] = i;
							tinyBalls[1][temp] = j;
							tinyBalls[2][temp] = 0;
							stop = 1;
							break;
						}
					}
				}
			}
		}
	}

	// kiem tra xem co an diem hay khong
	public boolean checkPoint(int rowCenter, int colCenter) {
		if (board[rowCenter][colCenter] == 0)
			return false;

		countPoint = 1;
		listPoint[0][countPoint] = rowCenter;
		listPoint[1][countPoint] = colCenter;

		for (temp = 0; temp < 4; temp++) {
			countTemp = 0;
			x = rowCenter;
			y = colCenter;
			while (true) {
				x += OX[temp];
				y += OY[temp];
				if (!isBound(x, y)) {
					break;
				}
				if (board[x][y] != board[rowCenter][colCenter]) {
					break;
				}

				countTemp++;
				countPoint++;
				listPoint[0][countPoint] = x;
				listPoint[1][countPoint] = y;
			}
			x = rowCenter;
			y = colCenter;
			while (true) {
				x -= OX[temp];
				y -= OY[temp];
				if (!isBound(x, y))
					break;
				if (board[x][y] != board[rowCenter][colCenter])
					break;

				countTemp++;
				countPoint++;
				listPoint[0][countPoint] = x;
				listPoint[1][countPoint] = y;
			}
			if (countTemp < (EAT_BALL - 1))
				countPoint -= countTemp;
			// Log.d("countTemp", "countTemp = " + countTemp);
		}

		if (countPoint >= EAT_BALL) {
			Settings.score += countPoint * 10;
			return true;
		} else
			return false;
	}

	public void checkPath(int x1, int y1, int x2, int y2) {
		temp = board[x2][y2];
		board[x2][y2] = 0;

		findWay = false;
		qStart = 0;
		qEnd = 0;
		for (i = 0; i < ROW; i++)
			for (j = 0; j < COL; j++)
				color[i][j] = -1;
		color[x1][y1] = 0;
		queueX[0] = x1;
		queueY[0] = y1;
		while (qEnd >= qStart) {
			if (findWay)
				break;
			x = queueX[qStart];
			y = queueY[qStart];
			qStart++;
			for (i = 0; i < 4; i++) {
				rowTemp = x + u[i];
				colTemp = y + v[i];
				if (rowTemp == x2 && colTemp == y2) {
					findWay = true;
					dad[x2][y2].x = x;
					dad[x2][y2].y = y;
					break;
				} else {
					if (0 <= rowTemp && rowTemp < ROW && 0 <= colTemp
							&& colTemp < COL && board[rowTemp][colTemp] <= 0
							&& color[rowTemp][colTemp] == -1) {
						qEnd++;
						queueX[qEnd] = rowTemp;
						queueY[qEnd] = colTemp;
						color[rowTemp][colTemp] = 0;
						dad[rowTemp][colTemp].x = x;
						dad[rowTemp][colTemp].y = y;
					}
				}
			}
			color[x][y] = 1;
		}
		board[x2][y2] = temp;
	}

	public void insertMiniBall(int color, int desRow, int desCol) {
		size = 0;
		for (i = 0; i < ROW; i++)
			for (j = 0; j < COL; j++) {
				if (board[i][j] == 0)
					size++;
			}
		remain = r.nextInt(size) + 1;
		stop = 0;
		for (i = 0; i < ROW; i++) {
			if (stop == 1)
				break;
			for (j = 0; j < COL; j++) {
				if (board[i][j] == 0) {
					remain--;
					if (remain == 0) {
						board[i][j] = color;
						// chen ball moi vao trong tinyBalls
						for (int k = 0; k < NEXT_BALL; k++) {
							if (tinyBalls[0][k] == desRow && tinyBalls[1][k] == desCol) {
								tinyBalls[0][k] = i;
								tinyBalls[1][k] = j;
								tinyBalls[2][k] = 0;
							}
						}
						stop = 1;
						break;
					}
				}
			}
		}
	}

	public boolean isBound(int x, int y) {
		if (0 <= x && x < ROW && 0 <= y && y < COL)
			return true;
		else
			return false;
	}

	public void checkGameOver(boolean insertMiniBall) {
		size = 0;
		for (i = 0; i < ROW; i++)
			for (j = 0; j < COL; j++)
				if (board[i][j] > 0) {
					size++;
				}
		if (size == ROW * COL)
			gameOver = true;
		else
			gameOver = false;
	}

	public void backup() {
		for (i = 0; i < ROW; i++)
			for (j = 0; j < COL; j++) {
				undoBoard[i][j] = board[i][j];
			}
		for (i = 0; i < 3; i++)
			for (j = 0; j < NEXT_BALL; j++) {
				undoTinyBalls[i][j] = tinyBalls[i][j];
			}
		Settings.undoScore = Settings.score;
	}

	public void restore() {
		for (i = 0; i < ROW; i++)
			for (j = 0; j < COL; j++) {
				board[i][j] = undoBoard[i][j];
			}
		for (i = 0; i < 3; i++)
			for (j = 0; j < NEXT_BALL; j++)
				tinyBalls[i][j] = undoTinyBalls[i][j];

		Settings.score = Settings.undoScore;
	}
}
