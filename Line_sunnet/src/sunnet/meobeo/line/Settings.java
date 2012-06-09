package sunnet.meobeo.line;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import sunnet.meobeo.framework.FileIO;

public class Settings {
	public static boolean soundEnable;
	public static int[] highscores = new int[] { 100, 80, 50, 30, 10 };
	public static int score;
	public static int undoScore;
	public static int time;
	public static long startTime;

	public static int second, minute, hour;

	public static void load(FileIO files) {
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					files.readFile(".line")));
			soundEnable = Boolean.parseBoolean(in.readLine());
//			for (int i = 0; i < 5; i++) {
//				highscores[i] = Integer.parseInt(in.readLine());
//			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {

		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {

			}
		}
	}

	public static void save(FileIO files) {
		BufferedWriter out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					files.writeFile(".line")));
			out.write(Boolean.toString(soundEnable));
//			out.write("\n");
//			for (int i = 0; i < 5; i++) {
//				out.write(Integer.toString(highscores[i]));
//				out.write("\n");
//			}
		} catch (IOException e) {

		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {

			}
		}
	}

	public static void addScore(int score) {
		for (int i = 0; i < 5; i++) {
			if (highscores[i] < score) {
				for (int j = 4; j > i; j--)
					highscores[j] = highscores[j - 1];
				highscores[i] = score;
				break;
			}
		}
	}

	public static void countTimer(long deltaTime) {
		if ((deltaTime - startTime) >= 1000) {
			time++;
			startTime = deltaTime;

			second = time % 60;
			minute = (time / 60) % 60;
			hour = (time / 60) / 60;

			if (time % 2 == 0)
				System.gc();
		}
	}
}
