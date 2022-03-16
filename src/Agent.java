import java.awt.Point;
import java.util.ArrayList;

public class Agent {
	
	Point position;
	Point startPos;
	Game.Team team;
	
	int foodEaten;
	int capsuleTicks = 0;
	
	ArrayList<Point> moves;
	
	
	public Agent(Game.Team team, Point position) {
		this.team = team;
		this.startPos = position;
		this.position = position;
		this.foodEaten = 0;
		this.capsuleTicks = 0;
		moves = new ArrayList<Point>();
	}
	
	Point chooseAction(char[][] state) {
		return null;
	}
	
	Game.Team getTeam() {
		return team;
	}

}
