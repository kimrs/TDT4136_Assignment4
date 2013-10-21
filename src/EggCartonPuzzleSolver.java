import java.util.ArrayList;


public class EggCartonPuzzleSolver implements PuzzleSolver
{

	@Override
	public State solve(State state) 
	{
		int heuristic = state.evaluate();
		if(heuristic == 0)
			return state;
		else
		{
			ArrayList<State> neighbors = state.generateNeighbors();
			for(State neighbor : neighbors)
				if(neighbor.evaluate() < heuristic)
					return solve(neighbor);
		}
		//If we reached this far, a pit might have been encountered
		state.randomizeEggs();
		return solve(state);
	}

}
