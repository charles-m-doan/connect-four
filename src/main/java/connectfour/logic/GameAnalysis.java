package connectfour.logic;

import connectfour.control.Game;

public class GameAnalysis
	{
	private GameAnalysis()
		{
		}

	public static boolean checkForCompletion(Game game)
		{
		int player = game.getPlayer();
		int turn = game.getTurn();
		//At least 7 turns are necessary for a win to have occurred.
		if (turn < 7)
			{
			return false;
			}
		int check = 0;
		int count = 0;
		boolean done = false;
		while (!done)
			{
			switch (check)
				{
				case 0: {
				count = checkRows(game);
				break;
				}
				case 1: {
				count = checkColumns(game);
				break;
				}
				case 2: {
				count = checkUpslopes(game);
				break;
				}
				case 3: {
				count = checkDownslopes(game);
				break;
				}
				default: {
				done = true;
				break; //All possibilities have been checked with no win.
				}
				} //End switch
			if (count >= 4) //If 4 pieces in a row were found.
				{
				game.specifyWinner(player); //Sets the winner to the current player.
				return true; //Breaks from the method since 4 in a row were found. The game is won.
				}
			check++; //Increment switch statement condition.
			} //End While Loop.
		if (turn >= 42)
			{
			game.confirmDraw(); //Sets this game to a draw.
			return true; //Game complete, but no winner declared.
			}
		else
			{
			return false; //Game incomplete and no winner at this time.
			}
		}

	private static int checkRows(Game game)
		{
		int[][] grid = game.getGrid();
		int player = game.getPlayer();
		int connect;
		for (int y = 0; y < game.getMaxHeight(); y++)
			{
			game.clearWinNodes();
			connect = 0;
			for (int x = 0; x < 7; x++)
				{
				if (grid[x][y] == player)
					{
					game.getWinNodes().add(new Node(x, y));
					connect++;
					}
				else
					{
					game.clearWinNodes();
					connect = 0;
					}
				if (connect >= 4)
					{
					return connect;
					}
				}
			}
		return 0;
		}

	private static int checkColumns(Game game)
		{
		int[][] grid = game.getGrid();
		int[] columnHeights = game.getColumnHeights();
		int player = game.getPlayer();
		//no sense in checking anything if there isn't at least one column greater than or equal to 4.
		if (game.getMaxHeight() < 4)
			{
			return 0;
			}
		int connect;
		for (int x = 0; x < 7; x++)
			{
			game.clearWinNodes();
			connect = 0;
			for (int y = 0; y < columnHeights[x]; y++)
				{
				//If the number of remaining positions in the given column are less than 4 minus the current number already connected,
				//then there is no need to check the rest.
				if ((columnHeights[x] - y) < (4 - connect))
					{
					game.clearWinNodes();
					connect = 0;
					break;
					}
				if (grid[x][y] == player)
					{
					game.getWinNodes().add(new Node(x, y));
					connect++;
					}
				else
					{
					game.clearWinNodes();
					connect = 0;
					}
				if (connect >= 4)
					{
					return connect;
					}
				}
			}
		return 0;
		}

	private static int checkUpslopes(Game game)
		{
		int[][] grid = game.getGrid();
		int player = game.getPlayer();
		//no sense in checking anything if there isn't at least one column greater than or equal to 4.
		if (game.getMaxHeight() < 4)
			{
			return 0;
			}
		int connect;
		int x;
		int y;
		//left side 0 to 2
		for (int i = 0; i < 3; i++)
			{
			connect = 0;
			game.clearWinNodes();
			for (y = i; y < 6; y++)
				{
				x = y - i;
				if (grid[x][y] == player)
					{
					game.getWinNodes().add(new Node(x, y));
					connect++;
					}
				else
					{
					game.clearWinNodes();
					connect = 0;
					}
				if (connect >= 4)
					{
					return connect;
					}
				}
			}
		//bottom 1 to 3
		for (int i = 1; i < 4; i++)
			{
			connect = 0;
			game.clearWinNodes();
			for (x = i; x < 7; x++)
				{
				y = x - i;
				if (grid[x][y] == player)
					{
					game.getWinNodes().add(new Node(x, y));
					connect++;
					}
				else
					{
					game.clearWinNodes();
					connect = 0;
					}
				if (connect >= 4)
					{
					return connect;
					}
				}
			}
		return 0;
		}

	private static int checkDownslopes(Game game)
		{
		int[][] grid = game.getGrid();
		int player = game.getPlayer();
		//no sense in checking anything if there isn't at least one column greater than or equal to 4.
		if (game.getMaxHeight() < 4)
			{
			return 0;
			}
		int connect;
		int x;
		int y;
		//top left 3 to 5
		for (int i = 3; i < 6; i++)
			{
			connect = 0;
			game.clearWinNodes();
			for (x = 0; x < i + 1; x++)
				{
				y = i - x;
				if (grid[x][y] == player)
					{
					game.getWinNodes().add(new Node(x, y));
					connect++;
					}
				else
					{
					game.clearWinNodes();
					connect = 0;
					}
				if (connect >= 4)
					{
					return connect;
					}
				}
			}
		//top 1 to 3 			
		for (int i = 1; i < 4; i++)
			{
			connect = 0;
			game.clearWinNodes();
			for (y = 5; y > i - 2; y--)
				{
				x = i + 5 - y;
				if (grid[x][y] == player)
					{
					game.getWinNodes().add(new Node(x, y));
					connect++;
					}
				else
					{
					game.clearWinNodes();
					connect = 0;
					}
				if (connect >= 4)
					{
					return connect;
					}
				}
			}
		return 0;
		}

	}
