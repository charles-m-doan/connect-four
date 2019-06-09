package connectfour.control;

import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.JOptionPane;

import connectfour.ai.MoveCalculation;
import connectfour.ui.InterfaceFrame;
import connectfour.ui.MatchOptionsPanel;

public class Mediator extends Thread
	{
	public static final long INTRO_SCREEN_DURATION = 1500;
	private StateRegister states;
	private BufferedReader commandIn;
	private InterfaceFrame gameInterfaceFrame;

	public Mediator(StateRegister states, BufferedReader commandIn, InterfaceFrame gameInterfaceFrame)
		{
		super();
		this.states = states;
		this.commandIn = commandIn;
		this.gameInterfaceFrame = gameInterfaceFrame;
		}

	public void run()
		{
		gameInterfaceFrame.lockInterface();
		gameInterfaceFrame.updateDisplay(states);
		sleep(INTRO_SCREEN_DURATION);
		states = new StateRegister();
		gameInterfaceFrame.updateDisplay(states);
		try
			{
			gameLoop();
			}
		catch (IOException ex)
			{
			ex.printStackTrace();
			System.exit(0);
			}
		}

	private void gameLoop() throws IOException
		{
		String command;
		while (true)
			{
			gameInterfaceFrame.lockInterface();
			//DebugMessages.printVals(states);
			if (states.isCPUMove())
				{
				//System.out.println("CPU turn!  Interface locked!");
				cpuTurn();
				}
			else
				{
				if (states.match == null)
					{
					//System.out.println("No match determined! \"New Match\" button unlocked!");
					gameInterfaceFrame.unlockNewMatchButton();
					}
				else if (states.game == null || !states.game.isActive())
					{
					//System.out.println("No game determined! \"New Match\" and \"New Game\" buttons unlocked!");
					gameInterfaceFrame.unlockNewMatchButton();
					gameInterfaceFrame.unlockNewGameButton();
					}
				else if (states.isHumanMove())
					{
					//System.out.println("Human turn!  Interface unlocked!");
					//System.out.println(states.game.gridToString());
					gameInterfaceFrame.unlockInterface();
					}
				command = commandIn.readLine();
				parseCommand(command);
				}
			gameInterfaceFrame.updateDisplay(states);
			}
		}

	private void parseCommand(String command)
		{
		if (command.indexOf(InterfaceFrame.NEW_MATCH_COMMAND) != -1)
			{
			showNewMatchDialogue();
			}
		else if (command.indexOf(InterfaceFrame.NEW_GAME_COMMAND) != -1)
			{
			states.newGame();
			}
		else
			{
			states.makeMove(Integer.parseInt(command));
			}
		}

	private void showNewMatchDialogue()
		{
		MatchOptionsPanel matchOptionsDialogue = new MatchOptionsPanel();
		int result = JOptionPane.showConfirmDialog(null, matchOptionsDialogue, "New Connect Four Match", JOptionPane.OK_CANCEL_OPTION); //0 = OK, 2 = Cancel
		if (result == 0)
			{
			states.newMatch(matchOptionsDialogue.generateMatchState());
			}
		}

	private void cpuTurn()
		{
		int col = MoveCalculation.determineMove(states);
		states.makeMove(col);
		}

	public static void sleep(long durationMillis)
		{
		try
			{
			Thread.sleep(durationMillis);
			}
		catch (InterruptedException ex)
			{
			ex.printStackTrace();
			System.exit(0);
			}
		}
	}
