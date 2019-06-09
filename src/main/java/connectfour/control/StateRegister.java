package connectfour.control;

import javax.swing.JOptionPane;

import connectfour.logic.GameAnalysis;

/* This is essentially a parameter object with a few methods for accessing and
 * altering the Match and Game states. */

public class StateRegister
	{
	public Match match;
	public Game game;

	public StateRegister()
		{
		super();
		this.match = null;
		this.game = null;
		}

	public boolean isMatchEstablished()
		{
		return !(match == null);
		}

	public boolean isGameEstablished()
		{
		return !(game == null);
		}

	public boolean isStateEstablished()
		{
		return (isMatchEstablished() && isGameEstablished());
		}

	public boolean isCPUMove()
		{
		if (isStateEstablished() && game.isActive())
			{
			int controller = match.getPlayerController(game.getPlayer());
			return (controller >= 1);
			}
		return false;
		}

	public boolean isHumanMove() throws RuntimeException
		{
		return !(isCPUMove());
		}

	//------------------------------------------------------------------------------------------

	public void newMatch(Match match)
		{
		this.match = match;
		this.game = null;
		}

	public void newGame()
		{
		if (isMatchEstablished())
			{
			game = new Game(match);
			}
		}

	public void updateMatch()
		{
		if (isStateEstablished())
			{
			if (game.isWon())
				{
				match.incWins(game.getWinner());
				}
			else if (game.isDraw())
				{
				match.incDraws();
				}
			}
		}

	public void makeMove(int col)
		{
		if (isStateEstablished())
			{
			try
				{
				game.insertPiece(col);
				boolean completed = GameAnalysis.checkForCompletion(game);
				if (completed)
					{
					updateMatch();
					}
				else
					{
					game.nextTurn();
					}
				}
			catch (IllegalArgumentException ex)
				{
				String message = ex.getMessage();
				if (isCPUMove())
					{
					message += " by the CPU.  Exiting. . .";
					JOptionPane.showMessageDialog(null, message, "Error:", JOptionPane.ERROR_MESSAGE);
					System.exit(1);
					}
				else
					{
					JOptionPane.showMessageDialog(null, message, "Error:", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	}
