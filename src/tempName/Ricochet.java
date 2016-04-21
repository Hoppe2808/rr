package tempName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.Point;

public class Ricochet {

    public static void main(String[] args) throws IOException {
        Board board = new Ricochet().loadBoard();

        // Debug code - uncomment to test that robot 0 is moved as expected
        System.out.print(board);

        board.computeSolution();
        System.out.println("Possible endpoints for robot 0: " + board.possibleEndpointsForRobot(0));

    }

    public class Board {
        private static final char GOAL_CHAR = 'G';
        private static final char WALL_CHAR = '#';
        private static final char EMPTY_CHAR = ' ';

        private char[][] board;
        private int size;
        private Point[] robots;
        private Point goal;
        private Point previousPosition;
        private Point deadEnd;

        public Board(char[][] board, Point[] robots, Point goal) {
            this.board = board;
            this.size = board.length;
            this.robots = robots;
            this.goal = goal;
        }

        public void computeSolution() {
            
        	while(!(robots[0].equals(goal))){
            	if (!(pointAfterMovingRobot(0, Direction.Up).equals(robots[0])) && !(pointAfterMovingRobot(0, Direction.Up).equals(previousPosition))){
            		if (!(pointAfterMovingRobot(0, Direction.Up).equals(deadEnd))){
	            		previousPosition = robots[0];
	            		moveRobot(0, Direction.Up);
	            		System.out.println("Up");
            		}
            	} else if(!(pointAfterMovingRobot(0, Direction.Down).equals(robots[0])) && !(pointAfterMovingRobot(0, Direction.Down).equals(previousPosition))){
            		if (!(pointAfterMovingRobot(0, Direction.Up).equals(deadEnd))){
	            		previousPosition = robots[0];
	            		moveRobot(0, Direction.Down);
	            		System.out.println("Down");
            		}
            	} else if(!(pointAfterMovingRobot(0, Direction.Left).equals(robots[0])) && !(pointAfterMovingRobot(0, Direction.Left).equals(previousPosition))){
            		if (!(pointAfterMovingRobot(0, Direction.Up).equals(deadEnd))){
	            		previousPosition = robots[0];
	            		moveRobot(0, Direction.Left);
	            		System.out.println("Left");
            		}
            	} else if(!(pointAfterMovingRobot(0, Direction.Right).equals(robots[0])) && !(pointAfterMovingRobot(0, Direction.Right).equals(previousPosition))){
            		if (!(pointAfterMovingRobot(0, Direction.Up).equals(deadEnd))){
	            		previousPosition = robots[0];
	            		moveRobot(0, Direction.Right);
	            		System.out.println("Right");
            		}
            	} else{
            		deadEnd = robots[0];
            		robots[0] = previousPosition;
            		System.out.println("Dead end, going back");
            	}
        	}
        	System.out.println("Win");
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
