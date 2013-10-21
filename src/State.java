import java.util.ArrayList;
import java.util.Random;


public class State 
{
	private final char TAG_OCCUPIED = '*';
	private final char TAG_EMPTY = '.';
	
	private final int M = 5;
	private final int N = 5;
	private final int K = 2;
	
	private ObjectiveFunction ws;
	private Random rand;
	public int[][] eggs;
	public int heuristic = -1;
	
	public int m;
	public int n;
	public int k;
	
	/**
	 * Empty constructor, sets m = 5, n = 5, k = 2
	 */
	public State()
	{
		m = M;
		n = N;
		k = K;
		
		init();
	}
	
	/**
	 * This constructor has not been fully testet yet
	 * @param m
	 * @param n
	 * @param k
	 */
	public State(int m, int n, int k)
	{
		this.m = m;
		this.n = n;
		this.k = k;
		
		init();
	}
	
	/**
	 * Sets m = n = mn, k = k
	 * @param nm
	 * @param k
	 */
	public State(int mn, int k)
	{
		this.m = mn;
		this.n = mn;
		this.k = k;
		
		init();
	}
	
	/**
	 * Uses the chosen winningstate algorithm. in this case, an 
	 * instance of EggCartonWinningState
	 * @return
	 */
	public int evaluate()
	{
		if(heuristic == -1)
			heuristic = ws.evaluate(this);
		return heuristic;
	}
	
	public State move(char x, char y, char direction)
	{
		int posX = Character.getNumericValue(x);
		int posY = Character.getNumericValue(y);
		return move(posX, posY, direction);
	}
	/**
	 * moves the egg at position x,y in either one of the directions
	 * n, s, w, e.
	 * If the egg does not exist, nothing happens
	 * @param x
	 * @param y
	 * @param direction
	 */
	public State move(int x, int y, char direction)
	{	
		int indexOfEgg = eggExists(x, y);
		if(indexOfEgg != -1)
		{
			switch(direction)
			{
			case 'n':
				y--;
				break;
			case 's':
				y++;
				break;
			case 'e':
				x++;
				break;
			case 'w':
				x--;
				break;
			}
		}
		
		if(x >= 0 && x < n && y >= 0 && y < m && eggExists(x, y) == -1)
		{
			State newState = new State(n, m, k);
			newState.eggs = eggs.clone();
			newState.eggs[indexOfEgg] = new int[] { x, y };
			return newState;
		} else
			return null;
	}
	
	public ArrayList<State> generateNeighbors()
	{
		ArrayList<State> neighbors = new ArrayList<>();
		for(int[] egg : eggs)
		{
			State[] immediateNeighbors = new State[]{ 
					move(egg[0], egg[1], 'n'),
					move(egg[0], egg[1], 's'),
					move(egg[0], egg[1], 'e'),
					move(egg[0], egg[1], 'w'),
			};
			for(State s : immediateNeighbors)
				if(s != null)
					neighbors.add(s);
		}
		return neighbors;
	}
	
	/**
	 * Prints a map of the state
	 */
	public void printGrid()
	{
		for(int i = 0; i < m; i++)
		{
			for(int j = 0; j < n; j++)
				if(eggExists(j,i) != -1)
					System.out.print(TAG_OCCUPIED);
				else
					System.out.print(TAG_EMPTY);
			System.out.println();
		}
	}
	
	/**
	 * Clears the current grid and replaces it with input
	 * Only used for testing
	 * @param newEggs
	 */
	public void setGrid(int[][] newEggs)
	{
		eggs = newEggs;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return index of the egg, -1 if not
	 */
	private int eggExists(int x, int y)
	{
		for(int i = 0; i < eggs.length; i++)
			if(eggs[i][0] == x && eggs[i][1] == y)
				return i;
		//if we have come this far, the egg does not exists.
		return -1;
	}
	
	/**
	 * called in every constructor
	 */
	private void init()
	{
		rand = new Random();
		ws = new EggCartonObjectiveFunction();
		
		randomizeEggs();
	}
	
	/**
	 * places K * N random eggs in grid
	 */
	public void randomizeEggs()
	{
		//inserts eggs
		eggs = new int[n*k][2];
		
		int eggsCount = 0;
		while(eggsCount != n*k)
		{
			int x = rand.nextInt(n);
			int y = rand.nextInt(m);
			
			if(eggExists(x, y) == -1)
			{
				eggs[eggsCount][0] = x;
				eggs[eggsCount][1] = y;
				eggsCount++;
			}
		}
	}
}
