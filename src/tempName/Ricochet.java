package tempName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Point;

public class Ricochet {

	public static void main(String[] args) throws IOException {
		Board board = new Ricochet().loadBoard();

		board.computeSolution();
	}

	public class Board {
		private static final char GOAL_CHAR = 'G';
		private static final char WALL_CHAR = '#';
		private static final char EMPTY_CHAR = ' ';

		private char[][] board;
		private int size;
		private Point[] robots;
		private Point goal;
		private ArrayList<Point> vP = new ArrayList<Point>();
		private ArrayList<Point> correctPoints = new ArrayList<Point>();
		private ArrayList<String> syso = new ArrayList<String>();
		private int loopDeadEnd;
		private Point startPosition;
		
		public Board(char[][] board, Point[] robots, Point goal) {
			this.board = board;
			this.size = board.length;
			this.robots = robots;
			this.goal = goal;
		}

		public void computeSolution() {
			startPosition = robots[0];
			correctPoints.add(robots[0]);
			vP.add(robots[0]);
			while(!(robots[0].equals(goal))){

				if (!(moveUp())){
					if (!(moveDown())){
						if (!(moveLeft())){
							moveRight();	
						}
					}
				}

				if (checkDeadEnd() && !(robots[0].equals(goal))){
					for (int i = 1; i < vP.size(); i++){
						if (vP.get(i).equals(robots[0])){
							for (int j = 0; j < correctPoints.size(); j++){
								if (correctPoints.get(j).equals(robots[0])){
									correctPoints.remove(j);
									break;
								}
							}
							if (!(robots[0].equals(startPosition))){
								try{
									moveBackRobot(0, vP.get(i - 1));								
								}catch(ArrayIndexOutOfBoundsException e){
									System.out.println(robots[0]);
								}
							} else {
								System.out.println("Haeae");
							}
							break;
						}
					}
				}
			}
			for (int i = 0; i < correctPoints.size(); i++){
				if (correctPoints.get(i) != correctPoints.get(correctPoints.size() - 1)){
					if(correctPoints.get(i).x < correctPoints.get(i+1).x){
						syso.add("0D");
					} else if(correctPoints.get(i).y < correctPoints.get(i+1).y){
						syso.add("0R");
					} else if(correctPoints.get(i).x > correctPoints.get(i+1).x){
						syso.add("0U");
					} else if(correctPoints.get(i).y > correctPoints.get(i+1).y){
						syso.add("0L");
					}
				}
			}
			for (int i = 0; i < syso.size() - 1; i++){
				if(syso.get(i).equals("0D")){
					if (syso.get(i+1).equals("0U")){
						syso.remove(i);
					}
				} else if(syso.get(i).equals("0U")){
					if (syso.get(i+1).equals("0D")){
						syso.remove(i);
					}
				} else if(syso.get(i).equals("0L")){
					if (syso.get(i+1).equals("0R")){
						syso.remove(i);
					}
				} else if(syso.get(i).equals("0R")){
					if (syso.get(i+1).equals("0L")){
						syso.remove(i);
					}
				}
			}
			for (int i = 0; i < syso.size(); i++){
				System.out.println(syso.get(i));
			}
		}

		private boolean moveRight() {
			if(!(pointAfterMovingRobot(0, Direction.Right).equals(robots[0]))){
				boolean move = true;
				for (int i = 0; i < vP.size(); i++){
					if (vP.get(i).equals(pointAfterMovingRobot(0, Direction.Right))){
						move = false;
						break;
					}
				}
				if (move){
					moveRobot(0, Direction.Right);
					vP.add(robots[0]);
					correctPoints.add(robots[0]);
				}
				return move;
			}
			return false;
		}

		private boolean moveLeft() {
			if(!(pointAfterMovingRobot(0, Direction.Left).equals(robots[0]))){
				boolean move = true;
				for (int i = 0; i < vP.size(); i++){
					if (vP.get(i).equals(pointAfterMovingRobot(0, Direction.Left))){
						move = false;
						break;
					}
				}
				if (move){
					moveRobot(0, Direction.Left);
					vP.add(robots[0]);
					correctPoints.add(robots[0]);
				}
				return move;
			}
			return false;
		}

		private boolean moveDown() {
			if(!(pointAfterMovingRobot(0, Direction.Down).equals(robots[0]))){
				boolean move = true;
				for (int i = 0; i < vP.size(); i++){
					if (vP.get(i).equals(pointAfterMovingRobot(0, Direction.Down))){
						move = false;
						break;
					}
				}
				if (move){
					moveRobot(0, Direction.Down);
					vP.add(robots[0]);
					correctPoints.add(robots[0]);
				}
				return move;
			}
			return false;
		}

		private boolean moveUp() {
			if (!(pointAfterMovingRobot(0, Direction.Up).equals(robots[0]))){
				boolean move = true;
				for (int i = 0; i < vP.size(); i++){
					if (vP.get(i).equals(pointAfterMovingRobot(0, Direction.Up))){
						move = false;
						break;
					}
				}
				if (move){
					moveRobot(0, Direction.Up);
					vP.add(robots[0]);
					correctPoints.add(robots[0]);
				}
				return move;
			}
			return false;
		}

		private boolean checkDeadEnd() {
			loopDeadEnd = 0;
			Endpoints ep = possibleEndpointsForRobot(0);
			for (int i = 0; i < vP.size(); i++){
				if (ep.down.equals(vP.get(i))){
					loopDeadEnd++;
				}
				if (ep.left.equals(vP.get(i))){
					loopDeadEnd++;
				}
				if (ep.right.equals(vP.get(i))){
					loopDeadEnd++;
				}
				if (ep.up.equals(vP.get(i))){
					loopDeadEnd++;
				}
				if (loopDeadEnd == 4){
					break;
				}
			}
			if (loopDeadEnd == 4){
				return true;
			} else{
				return false;
			}
		}

		public Endpoints possibleEndpointsForRobot(int robot) {
			assert robot < robots.length;

			Point up = pointAfterMovingRobot(robot, Direction.Up);
			Point down = pointAfterMovingRobot(robot, Direction.Down);
			Point left = pointAfterMovingRobot(robot, Direction.Left);
			Point right = pointAfterMovingRobot(robot, Direction.Right);

			return new Endpoints(up, down, left, right);
		}

		public Point pointAfterMovingRobot(int robot, Direction m) {
			assert robot < robots.length;

			Point pos = new Point(robots[robot]);
			int drow = 0, dcol = 0;

			if (m == Direction.Up) {
				drow = -1;
			} else if (m == Direction.Down) {
				drow = 1;
			}

			if (m == Direction.Left) {
				dcol = -1;
			} else if (m == Direction.Right) {
				dcol = 1;
			}

			while (withinBoard(pos.x+drow, pos.y+dcol) &&
					(board[pos.x+drow][pos.y+dcol] == EMPTY_CHAR
					|| board[pos.x+drow][pos.y+dcol] == GOAL_CHAR)) {
				pos.translate(drow, dcol);
			}

			return pos;
		}

		public void moveRobot(int robot, Direction d) {
			assert robot < robots.length;

			Point from = robots[robot];
			Point to = pointAfterMovingRobot(robot, d);
			board[from.x][from.y] = EMPTY_CHAR;
			robots[robot] = to;
			board[to.x][to.y] = (char) ('0' + robot);
		}
		public void moveBackRobot(int robot, Point d) {
			if (!(startPosition.equals(robots[0]))){
				assert robot < robots.length;
				
				Point from = robots[robot];
				Point to = d;
				board[from.x][from.y] = EMPTY_CHAR;
				robots[robot] = to;
				board[to.x][to.y] = (char) ('0' + robot);
			}
		}

		private boolean withinBoard(int x, int y) {
			return x >= 0 && x < size && y >= 0 && y < size;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder(size * size);

			for (int i = 0; i < board.length; i++) {
				sb.append(board[i]);
				sb.append(System.getProperty("line.separator"));
			}

			return sb.toString();
		}
	}

	public enum Direction {
		Up, Down, Left, Right;

		public String toString() {
			switch (this) {
			case Up: return "U";
			case Down: return "D";
			case Left: return "L";
			case Right: return "R";
			default: throw new AssertionError();
			}
		}
	}

	public class Endpoints {
		public final Point up, down, left, right;

		public Endpoints(Point up, Point down, Point left, Point right) {
			this.up = up;
			this.down = down;
			this.left = left;
			this.right = right;
		}

		public String toString() {
			return "Endpoints (Up: " + up + ", Down: " + down + ", Left: " + left + ", Right: " + right + ")";
		}
	}


	private Board loadBoard() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		int size = Integer.parseInt(in.readLine());
		int numberOfRobots = Integer.parseInt(in.readLine());

		char[][] b = new char[size][size];
		Point[] robots = new Point[numberOfRobots];

		Point goal = null;

		for (int i = 0; i < size; i++) {
			char[] line = in.readLine().toCharArray();
			for (int j = 0; j < line.length; j++) {
				if (line[j] == Board.GOAL_CHAR) {
					goal = new Point(i, j);
				} else if (Character.isDigit(line[j])) {
					int number = Character.getNumericValue(line[j]);
					robots[number] = new Point(i, j);
				} else if (line[j] != Board.EMPTY_CHAR &&
						line[j] != Board.WALL_CHAR) {
					System.err.println("Error: Unknown character '" + line[j] + "' on line " + (i+1) + ", column " + (j+1));
				}

				b[i][j] = line[j];
			}
		}

		if (goal == null) {
			System.err.println("Error: No goal found on board");
		}

		return new Board(b, robots, goal);
	}
}