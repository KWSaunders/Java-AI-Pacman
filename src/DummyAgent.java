import java.awt.Point;
import java.util.List;
import java.util.Random;

public class DummyAgent extends Agent {

	public DummyAgent(Game.Team side, Point position) {
		super(side, position);
	}
	
	Point chooseAction(char[][] state) {
		List<Point> actions = Util.getLegalActions(position, 10, 20);
		Random rand = new Random();
		Point action = actions.get(rand.nextInt(actions.size()));
		//System.out.println("Dummy Agent chose action: " + action);
		moves.add(action);
		return action;
	}

}
