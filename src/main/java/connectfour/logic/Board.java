package connectfour.logic;

import java.util.ArrayList;

import connectfour.control.Match;

public class Board
	{
	/* Note: The player holding turn priority will always be referred to as the
	 * "player", while the player without turn priority will always be referred
	 * to as the "opponent". */
	public final int firstPlayer; //The id (1 or 2) of the player who played first.
	protected int turn = 1; //The turn number of this board state.
	protected int prevMove = -1; //Stores the column number where the last piece was placed. Initialized to -1.
	protected int maxHeight = 0; //The height of the tallest column.
	protected int[] columnHeights = new int[7]; //The heights of each column in the grid.
	protected int[][] grid = new int[7][6]; //7 columns by 6 rows. This 2D matrix represents the positions of player Pieces. 0 for no piece, 1's for player 1, and 2's for player 2.

	public Board()
		{
		this.firstPlayer = 1;
		for (int col = 0; col < 7; col++)
			{
			columnHeights[col] = 0;
			for (int row = 0; row < 6; row++)
				{
				grid[col][row] = 0;
				}
			}
		}

	public Board(int firstPlayer)
		{
		this.firstPlayer = firstPlayer;
		for (int col = 0; col < 7; col++)
			{
			columnHeights[col] = 0;
			for (int row = 0; row < 6; row++)
				{
				grid[col][row] = 0;
				}
			}
		}

	public int getTurn()
		{
		return turn;
		}

	public int getPrevMove()
		{
		return prevMove;
		}

	public int getMaxHeight()
		{
		return maxHeight;
		}

	public int[] getColumnHeights()
		{
		return columnHeights;
		}

	public int[][] getGrid()
		{
		return grid;
		}

	//Helper Methods -------------------------------------------------------------------------

	//Returns 0 if even, 1 if odd.
	public int getParity()
		{
		return (turn % 2);
		}

	//Returns the pID of the player.
	public int getPlayer()
		{
		if (getParity() == 1)
			{
			return firstPlayer;
			}
		else
			{
			return invertPID(firstPlayer);
			}
		}

	//Returns the pID of the opponent.
	public int getOpponent()
		{
		if (getParity() == 1)
			{
			return invertPID(firstPlayer);
			}
		else
			{
			return firstPlayer;
			}
		}

	//Returns a 1 for player 1, and a -1 for player 2.
	public int getPlayerSign(int player)
		{
		return (3 - (player * 2));
		}

	//Inserts a piece for the player on the specified column.  Relevant fields are then updated.
	public void insertPiece(int col) throws IllegalArgumentException
		{
		if (col > 6 || columnHeights[col] >= 6)
			{
			throw new IllegalArgumentException("Illegal Move Attempt");
			}
		grid[col][columnHeights[col]] = getPlayer();
		columnHeights[col]++;
		maxHeight = Math.max(maxHeight, columnHeights[col]);
		prevMove = col;
		}

	//Increments the turn counter.
	public void nextTurn()
		{
		turn++;
		}

	//Makes a move for the player on the specified column.
	public void makeMove(int col) throws IllegalArgumentException
		{
		insertPiece(col);
		nextTurn();
		}

	public void makeMove(Node node) throws IllegalArgumentException
		{
		insertPiece(node.getColumn());
		nextTurn();
		}

	public int getLegalMoveCount()
		{
		int count = 0;
		for (int i = 0; i < 7; i++)
			{
			if (columnHeights[i] < 6)
				{
				count++;
				}
			}
		return count;
		}

	public ArrayList<Node> getLegalNodes()
		{
		ArrayList<Node> nodes = new ArrayList<Node>();
		for (int i = 0; i < 7; i++)
			{
			if (columnHeights[i] < 6)
				{
				nodes.add(new Node(i, columnHeights[i]));
				}
			}
		return nodes;
		}

	//Static Helper Methods ------------------------------------------------------------------------

	//If pID = 1, return = 2.  If pID = 2, return = 1.
	public static int invertPID(int pID)
		{
		return (pID - (int) Math.pow(-1, pID));
		}

	public static void singleArrayCopy(int[] source, int[] destination)
		{
		System.arraycopy(source, 0, destination, 0, source.length);
		}

	public static void multiArrayCopy(int[][] source, int[][] destination)
		{
		for (int a = 0; a < source.length; a++)
			{
			System.arraycopy(source[a], 0, destination[a], 0, source[a].length);
			}
		}

	public String gridToString()
		{
		String gridStr = "";
		for (int row = 5; row >= 0; row--)
			{
			gridStr += Match.LINE_SEPARATOR;
			for (int col = 0; col < 7; col++)
				{
				gridStr += grid[col][row] + " ";
				}
			}
		return gridStr;
		}

	public String toString()
		{
		return gridToString();
		}
	}
