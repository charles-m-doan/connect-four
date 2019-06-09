package connectfour.control;

import java.awt.Color;

public class Match
	{
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static final String[] CONTROLLERS = { "Human", "CPU - Very Easy", "CPU - Easy", "CPU - Normal", "CPU - Hard", "CPU - Very Hard", "CPU - Insane" };
	public static final String[] COLORS = { "Red", "Black", "Green", "Blue", "Orange", "Yellow", "Purple", "White" };

	//Match Options
	public final int firstMover; // 1 or 2.
	public final int p1Controller; //0 = human. 1+ = AI of increasing difficulty.
	public final int p2Controller; //0 = human. 1+ = AI of increasing difficulty.
	public final Color p1Color;
	public final Color p2Color;
	public final String p1ColorName;
	public final String p2ColorName;

	//Match Statistics
	private int gamesCompleted = 0;
	private int draws = 0;
	private int p1Wins = 0;
	private int p2Wins = 0;

	public Match(int firstMover, int p1Controller, int p2Controller, String p1ColorName, String p2ColorName)
		{
		super();
		this.firstMover = firstMover;
		this.p1Controller = p1Controller;
		this.p2Controller = p2Controller;
		this.p1Color = generateColor(p1ColorName);
		this.p2Color = generateColor(p2ColorName);
		this.p1ColorName = p1ColorName;
		this.p2ColorName = p2ColorName;
		}

	//Accessors ----------------------------------------------------------

	public int getGamesCompleted()
		{
		return gamesCompleted;
		}

	public int getDraws()
		{
		return draws;
		}

	public int getP1Wins()
		{
		return p1Wins;
		}

	public int getP2Wins()
		{
		return p2Wins;
		}

	public int getPlayerController(int player)
		{
		if (player == 1)
			{
			return p1Controller;
			}
		else
			{
			return p2Controller;
			}
		}

	//Mutators ----------------------------------------------------------

	public void incDraws()
		{
		this.draws++;
		this.gamesCompleted++;
		}

	public void incP1Wins()
		{
		this.p1Wins++;
		this.gamesCompleted++;
		}

	public void incP2Wins()
		{
		this.p2Wins++;
		this.gamesCompleted++;
		}

	public void incWins(int winner)
		{
		if (winner == 1)
			{
			incP1Wins();
			}
		else
			{
			incP2Wins();
			}
		}

	//Helpers -----------------------------------------------------------

	public static Color generateColor(String colorName)
		{
		int r = 0;
		int g = 0;
		int b = 0;

		if (colorName.toLowerCase().compareTo(COLORS[0].toLowerCase()) == 0)
			{
			r = 255;
			}
		else if (colorName.toLowerCase().compareTo(COLORS[1].toLowerCase()) == 0)
			{
			r = 0;
			g = 0;
			b = 0;
			}
		else if (colorName.toLowerCase().compareTo(COLORS[2].toLowerCase()) == 0)
			{
			g = 255;
			}
		else if (colorName.toLowerCase().compareTo(COLORS[3].toLowerCase()) == 0)
			{
			b = 255;
			}
		else if (colorName.toLowerCase().compareTo(COLORS[4].toLowerCase()) == 0)
			{
			r = 255;
			g = 125;
			}
		else if (colorName.toLowerCase().compareTo(COLORS[5].toLowerCase()) == 0)
			{
			r = 255;
			g = 255;
			}
		else if (colorName.toLowerCase().compareTo(COLORS[6].toLowerCase()) == 0)
			{
			r = 255;
			b = 255;
			}
		else if (colorName.toLowerCase().compareTo(COLORS[7].toLowerCase()) == 0)
			{
			r = 255;
			g = 255;
			b = 255;
			}
		return new Color(r, g, b);
		}

	public String toString()
		{
		String outputStr = "Player 1 (" + p1ColorName + ", " + CONTROLLERS[p1Controller] + ") vs Player 2 (" + p2ColorName + ", " + CONTROLLERS[p2Controller] + ")";
		outputStr += LINE_SEPARATOR + "Games Played: " + gamesCompleted + "  Draws:" + draws + "  Player 1 Wins: " + p1Wins + "  Player 2 Wins: " + p2Wins;
		return outputStr;
		}
	}
