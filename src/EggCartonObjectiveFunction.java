
public class EggCartonObjectiveFunction implements ObjectiveFunction
{
	private static enum Direction { UL_LR, UR_LL, LR_UL, LL_UR };
	
	@Override
	public int evaluate(State state) 
	{	
		boolean[][] occupiedSpaces = convertStateToBooleanArray(state);
		int horizontalEggsCounter = 0;
		int verticalEggsCounter = 0;
		int diagonalEggsCounter = 0;
		//Non-diagonals
		for(int i = 0; i < state.n; i++)
		{
			diagonalEggsCounter += evaluateDiagonals(0, i, state.k, state.n, Direction.UL_LR, occupiedSpaces);
			diagonalEggsCounter += evaluateDiagonals(0, i, state.k, state.n, Direction.LL_UR, occupiedSpaces);
			diagonalEggsCounter += evaluateDiagonals(state.n-1, i, state.k, state.n, Direction.UR_LL, occupiedSpaces);
			diagonalEggsCounter += evaluateDiagonals(state.n-1, i, state.k, state.n, Direction.LR_UL, occupiedSpaces);
			
			/*
			 * Checks if there are an appropriate amount of eggs in the vertical 
			 * and horizontal lines
			 */
			int tmpHorEggCounter = 0;
			int tmpVerEggCounter = 0;
			for(int j = 0; j < state.m; j++)
			{
				if(occupiedSpaces[i][j] == true)
					tmpVerEggCounter++;
				if(occupiedSpaces[j][i] == true)
					tmpHorEggCounter++;
			}
			
			if(tmpVerEggCounter > state.k)
				verticalEggsCounter += tmpVerEggCounter - state.k;
			if(tmpHorEggCounter > state.k)
				horizontalEggsCounter += tmpHorEggCounter - state.k;
		}
		return verticalEggsCounter + horizontalEggsCounter 
				+ diagonalEggsCounter;
	}
	
	/**
	 * returns the amount of illegal eggs in the diagonals 
	 * @param x
	 * @param y
	 * @param k
	 * @param n if -n, then inverse
	 * @param occupiedSpaces
	 * @return
	 */
	private int evaluateDiagonals(int x, int y, int k, int n, Direction d,
			boolean[][] occupiedSpaces)
	{
		return evaluateDiagonals(x, y, k, n, d, occupiedSpaces, 0);
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param k
	 * @param n
	 * @param occupiedSpaces
	 * @param eggCounter
	 * @return
	 */
	private int evaluateDiagonals(int x, int y, int k, int n, Direction d,
			boolean[][] occupiedSpaces, int eggCounter)
	{
		if(eggCounter > k)
			return eggCounter - k;
		else if(x >= 0 && x < n && y >= 0 && y < n)
		{
			if(occupiedSpaces[x][y])
				eggCounter++;
			if(d == Direction.UL_LR) //upper left to lower right
				return(evaluateDiagonals(x + 1, y - 1, k, n, d, occupiedSpaces, eggCounter));
			else if(d == Direction.LL_UR) //lower left to upper right
				return(evaluateDiagonals(x + 1, y + 1, k, n, d, occupiedSpaces, eggCounter));
			else if(d == Direction.UR_LL) //upper_right to lower left
				return(evaluateDiagonals(x - 1, y + 1, k, n, d, occupiedSpaces, eggCounter));
			else //lower right to upper left
				return(evaluateDiagonals(x - 1, y - 1, k, n, d, occupiedSpaces, eggCounter));
			
		}
		else
			return 0;	
	}
	
	private boolean[][] convertStateToBooleanArray(State state)
	{
		boolean[][] spacesOccupied = new boolean[state.n][state.m];
		for(int i = 0; i < state.n; i++)
			for(int j = 0; j < state.m; j++)
			{
				spacesOccupied[i][j] = false;
				for(int[] egg : state.eggs)
					if(egg[0] == i && egg[1] == j)
						spacesOccupied[i][j] = true;
			}
		return spacesOccupied;
	}
}
