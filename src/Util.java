import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Util {

	static void shuffle(char[][] a) {
		Random random = new Random();
		for (int i = a.length - 1; i > 0; i--) {
			for (int j = a[i].length - 1; j > 0; j--) {
				int m = random.nextInt(i + 1);
				int n = random.nextInt(j + 1);
				char temp = a[i][j];
				a[i][j] = a[m][n];
				a[m][n] = temp;
			}
		}
	}

	public static void transpose(char[][] theArray) {
		for (int i = 0; i < (theArray.length / 2); i++) {
			char[] temp = theArray[i];
			theArray[i] = theArray[theArray.length - i - 1];
			theArray[theArray.length - i - 1] = temp;
		}
	}

	static void merge(char A[][], char B[][], final int M, final int N) {
		char[][] C = new char[M][2 * N];
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				C[i][j] = A[i][j];
				C[i][j + N] = B[i][j];
			}
		}
	}
	
	static boolean inBounds(Point pos, final int ROWS, final int COLUMNS) {
		if(pos.x < ROWS && pos.x >= 0 && pos.y < COLUMNS && pos.y >= 0)
			return true;
		return false;
	}
	
	enum Directions {
		UP(0, -1),
		DOWN(0, 1),
		LEFT(-1, 0),
		RIGHT(1, 0),
		STOP(0, 0);
		
		Point point;
		Directions(int i, int j) {
			point = new Point(i, j);
		}
	}
	
	/**
	 * Check if the move will stay on the board
	 * ...Later we can check for collision (need to pass in the game state eventually)
	 */
	static List<Point> getLegalActions(Point pos, final int ROWS, final int COLUMNS) { 
		List<Point> actions = new ArrayList<Point>();
		for (Util.Directions dir : Util.Directions.values()) { 
			Point action = new Point(pos.x + dir.point.x, pos.y + dir.point.y);
			if(Util.inBounds(action, ROWS, COLUMNS))
				actions.add(action);
		}
		//System.out.println("Actions: " + Arrays.toString(actions.toArray()));
		return actions;
	}

}
