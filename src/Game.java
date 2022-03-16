import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Game {
	
	/**
	 * Game Configurations
	 */
	final static int ROWS = 10;
	
	final static int COLUMNS = 10 * 2;
	
	final int MAX_PELLETS = 5;
	
	final int MAX_CAPSULES = 2;
	
	/**
	 * Game attributes
	 */
	
	private char[][] gameBoard;
	
	static List<Agent> agents = new ArrayList<Agent>();
	
	static Agent dummyAgent1 = new OffensiveAgent(Team.BLUE, new Point(0, 0));
	static Agent dummyAgent2 = new OffensiveAgent(Team.BLUE, new Point(0, 0));
	static Agent dummyAgent3 = new DummyAgent(Team.RED, new Point(ROWS - 1, COLUMNS - 1));
	static Agent dummyAgent4 = new DummyAgent(Team.RED, new Point(ROWS - 1, COLUMNS - 1));
	
	/** 
	 * Creates a 10x10 Game Board.
	 * Super imposes 5x5 of the board and then transposes opposing half of board.
	 */
	void generateBoard() {
		
		char[][] blueSide = new char[ROWS][COLUMNS / 2];
		char[][] redSide = new char[ROWS][COLUMNS / 2];
		
		gameBoard = new char[ROWS][COLUMNS];
		
		int pellets = MAX_PELLETS;
		int capsules = MAX_CAPSULES;
		
		for(int i = 0 ; i < ROWS; i++) {
			for(int j = 0 ; j < COLUMNS / 2; j++) {
				if(pellets > 0) {
					blueSide[i][j] = Assets.FOOD_PELLET.type;
					pellets--;
				} else if(capsules > 0) {
					blueSide[i][j] = Assets.CAPSULE.type;
					capsules--;
				} else {
					blueSide[i][j] = ' ';
				}
			}
		}
		
		Util.shuffle(blueSide);
		
		//copy blue to red
		for(int i = 0; i < blueSide.length; i++)
			redSide[i] = blueSide[i].clone();
		
		//transpose
		Util.transpose(redSide);
		
		//merge
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLUMNS / 2; j++) {
				gameBoard[i][j] = blueSide[i][j];
				gameBoard[i][j + (COLUMNS / 2)] = redSide[i][j];
			}
		}
		
	}
	
	enum Assets {
		
		FOOD_PELLET('*'),
		CAPSULE('O');
		
		char type;
		
		Assets(char type) {
			this.type = type;
		}
		
	}
	
	enum Team {
		BLUE(0),
		RED(1); 
		
		int value;
		Team(int value) {
			this.value = value;
		}
	}

	
	void printBoard() {
		//check if point is in set
		Set<Point> agentPositions = new HashSet<Point>();
		for(Agent agent : agents) {
			agentPositions.add(agent.position);
		}
		for(int i = 0 ; i < ROWS; i++) {
			for(int j = 0 ; j < COLUMNS; j++) {
				Point pos = new Point(i, j);
				if(agentPositions.contains(pos)) {
					System.out.print('@'); //if the agent is on pacman then
				} else {
					System.out.print(gameBoard[i][j]);
				}
			}
			System.out.println();
		}
	}
	
	boolean inBlueSide(Agent agent) {
		return Util.inBounds(agent.position, ROWS, COLUMNS / 2);
	}
	
	boolean inRedSide(Agent agent) {
		return !inBlueSide(agent);
	}

	Set<Point> getFoodPositions() {
		Set<Point> foodPositions = new HashSet<Point>();
		for(int i = 0; i < ROWS; i++) {
			for(int j = 0; j < COLUMNS; j++) {
				if(gameBoard[i][j] == Assets.FOOD_PELLET.type) {
					foodPositions.add(new Point(i, j));
				}
			}
		}
		return foodPositions;
	}
	
	boolean onOppositeSide(Agent agent) {
		if(agent.getTeam() == Team.BLUE && inRedSide(agent)) {
			return true;
		}
		if(agent.getTeam() == Team.RED && inBlueSide(agent)) {
			return true;
		}
		return false;
	}
	
	boolean isPacman(Agent agent) {
		return onOppositeSide(agent) && agent.capsuleTicks == 0;
	}
	
	boolean isGhost(Agent agent) {
		return onOppositeSide(agent) && agent.capsuleTicks > 0;
	}
	
	Team getOppositeTeam(Agent agent) {
		return agent.getTeam() == Team.RED ? Team.BLUE : Team.RED;
	}
	
	public Game() {
		super();
		generateBoard();
	}
	
	public static void main(String[] args) throws InterruptedException {
		Game ctf = new Game();
		
		agents.add(dummyAgent1);
		agents.add(dummyAgent2);
		agents.add(dummyAgent3);
		agents.add(dummyAgent4);
		
		int redScore = 0;
		int blueScore = 0;
		
		while(redScore != ctf.MAX_PELLETS && blueScore != ctf.MAX_PELLETS) {
			Thread.sleep(500);
			ctf.printBoard();
			for(Agent agent : agents) {
				
				
				/**
				 * Handle killing and suicide here
				 */
				HashMap<Point, Agent> enemyMap = new HashMap<Point, Agent>();
				ArrayList<Agent> enemyAgents = new ArrayList<Agent>();
				HashSet<Point> enemyPositions = new HashSet<Point>();
				for(Agent a : agents) {
					if(a.getTeam() == ctf.getOppositeTeam(agent)) {
						enemyAgents.add(a);
						enemyPositions.add(a.position);
						enemyMap.put(a.position, a);
					}
				}
				
				if(enemyPositions.contains(agent.position) && ctf.onOppositeSide(agent) && !ctf.isGhost(agent)) {
					agent.position = agent.startPos;
				} else if(enemyPositions.contains(agent.position) && !ctf.onOppositeSide(agent) && !ctf.isGhost(enemyMap.get(agent.position))) {
					for(Agent enemy : agents) {
						if(enemy.position == agent.position) {
							enemy.position = enemy.startPos;
						}
					}
				}
				
				/**
				 * Agent must choose a legal action
				 */
				
				agent.position = agent.chooseAction(ctf.gameBoard);
				
				/**
				 * Handle food and score here
				 */
				
				if(ctf.getFoodPositions().contains(agent.position) && ctf.isPacman(agent)) {
					ctf.gameBoard[agent.position.x][agent.position.y] = ' ';
					if(agent.getTeam() == Team.RED)
						redScore++;
					else 
						blueScore++;
				}
				
				/**
				 * Handle capsule killing and capsule ticks down here
				 */
				if(agent.capsuleTicks > 0)
					agent.capsuleTicks--;
				
			}
		}
		ctf.printBoard();
		/**
		 * Handle results
		 */
		if(redScore > blueScore)
			System.out.println("Red Team Wins!");
		else if(blueScore > redScore)
			System.out.println("Blue Team Wins!");
		else 
			System.out.println("Tie! redScore: " + redScore + " bluescore: " + blueScore);
	}

}
