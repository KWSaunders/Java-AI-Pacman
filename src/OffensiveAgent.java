import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OffensiveAgent extends Agent {

	public OffensiveAgent(Game.Team side, Point position) {
		super(side, position);
		
	}
	
	Point chooseAction(char[][] state) {
		List<Point> actions = Util.getLegalActions(position, 10, 20);
		Point nearestFood = getMinimumDistancePoint(getFoodLocations(state));
		if(position.y != nearestFood.y) {
			int diff = (nearestFood.y - position.y) / Math.abs(nearestFood.y - position.y);
			return new Point(position.x, diff + position.y);
		}
		if(position.x != nearestFood.x) {
			int diff = (nearestFood.x - position.x) / Math.abs(nearestFood.x - position.x);
			return new Point(diff + position.x, position.y);
		}
		return actions.get(new Random().nextInt(actions.size()));
	}
	
	//implement an A* search algorithm	
	
	Point getMinimumDistancePoint(List<Point> distances) {
		if(distances.isEmpty()) {
			return new Point(0,0);
		}
		int min = getManhattanDistance(position, distances.get(0));
		Point minPoint = distances.get(0);
		for(int i = 1; i < distances.size(); i++) {
			if(getManhattanDistance(position, distances.get(i)) < min) {
				min = getManhattanDistance(position, distances.get(i));
				minPoint = distances.get(i);
			}
		}
		return minPoint;
	}
	
	int getMinimumDistance(List<Point> distances) {
		int min = getManhattanDistance(position, distances.get(0));
		for(int i = 1; i < distances.size(); i++) {
			if(getManhattanDistance(position, distances.get(i)) < min) 
				min = getManhattanDistance(position, distances.get(i));
		}
		return min;
	}
	
	int getManhattanDistance(Point a, Point b) {
		return Math.abs(a.y-a.x) + Math.abs(b.y-b.x);
	}
	
	List<Point> getFoodLocations(char[][] state) {
		List<Point> foodLocations = new ArrayList<Point>();
		
		int column = 0;
		if(this.team == Game.Team.BLUE) {
			column = 10;
		}
		for(int i = 0; i < 10; i++) {
			for(int j = 0 + column; j < 10 + column; j++) {
				if(state[i][j] == Game.Assets.FOOD_PELLET.type)
					foodLocations.add(new Point(i, j));
			}
		}
		
		return foodLocations;
	}

}
