import java.util.ArrayList;


public class NonRecursiveEggCartonPuzzleSolver implements PuzzleSolver
{
	private final int MAX_ATTEMPTS = 20;
	@Override
	public State solve(State state) 
	{
		int attempts = 0;
		boolean solved = false;
		while(!solved)
		{
			int heuristic = state.evaluate();
			if(heuristic == 0)
				solved = true;
			else
			{
				ArrayList<State> neighbors = state.generateNeighbors();
				boolean stateReplaced = false;
				for(State neighbor : neighbors)
					if(neighbor.evaluate() < heuristic)
					{
						state = neighbor;
						stateReplaced = true;
						break;
					}
				if(!stateReplaced && attempts < MAX_ATTEMPTS)
				{
					/*
					 * Before we generate a new map. We try out one neighbor with the same
					 * heuristic value as this
					 */
					for(State neighbor : neighbors)
						if(neighbor.evaluate() == heuristic)
						{
							state = neighbor;
							stateReplaced = true;
							attempts++;
							break;
						}	
				}
				
				/*
				 * if we still have not yet found a valid move, it is time to randomize
				 * and start over. 
				 */
				if(!stateReplaced)
				{
					state.randomizeEggs();
					heuristic = state.evaluate();
					attempts = 0;
				}	
			}
		}
		return state;
	}

}
