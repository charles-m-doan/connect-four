package connectfour.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import connectfour.control.StateRegister;

public class InterfaceFrame extends JFrame
	{
	//CONSTANTS
	public static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(); //Set to the current screen width in pixels.
	public static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight(); //Set to the current screen height in pixels.
	public static final String[] INFO_LABELS = { "Turn:", "Move:", "Games Completed:", "Player 1 Wins:", "Player 2 Wins:" };
	public static final String INITIAL_INFO_VALUE = "---";
	public static final String NEW_MATCH_COMMAND = "New Match";
	public static final String NEW_GAME_COMMAND = "New Game";

	//STATE VARIABLES
	private PrintWriter commandOut;

	//Positioning and Sizing variables
	private final int gameDisplayPanelHeight = SCREEN_HEIGHT * 3 / 4; //Initial height of the display panel.
	private final int gameDisplayPanelWidth = SCREEN_HEIGHT * 7 / 8; //Initial width of the display panel.
	private final int moveButtonPanelHeight = gameDisplayPanelHeight / 20; //Height of the move button panel
	private final int interfacePanelWidth = gameDisplayPanelWidth / 5; //Width of the interface Panel (the panel on the right)
	private final int frameXPosition = (SCREEN_WIDTH - (gameDisplayPanelWidth + interfacePanelWidth)) / 2; //x position of the frame's upper left corner
	private final int frameYPosition = (SCREEN_HEIGHT - (gameDisplayPanelHeight + moveButtonPanelHeight)) / 2; //y position of the frame's upper left corner

	//Components
	//LEFT-SIDE panel containing the game canvas and the move column buttons.
	private JPanel gameplayPanel = new JPanel();
	private GameDisplayPanel gameDisplayPanel = new GameDisplayPanel();
	private JPanel moveButtonPanel = new JPanel();
	private JButton[] moveButtons = new JButton[7];

	//RIGHT-SIDE panel containing "new match" and "new game" buttons, and the info labels.
	private JPanel interfacePanel = new JPanel();
	private JPanel labelPanel = new JPanel();
	private JLabel[] labels = { new JLabel(INFO_LABELS[0]), new JLabel(INFO_LABELS[1]), new JLabel(INFO_LABELS[2]), new JLabel(INFO_LABELS[3]), new JLabel(INFO_LABELS[4]) };
	private JLabel[] values = { new JLabel(INITIAL_INFO_VALUE), new JLabel(INITIAL_INFO_VALUE), new JLabel(INITIAL_INFO_VALUE), new JLabel(INITIAL_INFO_VALUE), new JLabel(INITIAL_INFO_VALUE) };
	private JPanel newMatchOrGamePanel = new JPanel();
	private JButton newMatch = new JButton(NEW_MATCH_COMMAND);
	private JButton newGame = new JButton(NEW_GAME_COMMAND);

	//Event Listeners
	private ActionListener moveButtonPress = new MoveButtonPress();
	private ActionListener newMatchOrGamePress = new NewMatchOrGamePress();

	public InterfaceFrame(PrintWriter commandOut)
		{
		this.commandOut = commandOut;
		this.setBounds(frameXPosition, frameYPosition, (gameDisplayPanelWidth + interfacePanelWidth), (gameDisplayPanelHeight + moveButtonPanelHeight));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.add(gameplayPanel, BorderLayout.CENTER);
		this.add(interfacePanel, BorderLayout.EAST);

		//Gameplay Panel (stores the Game Display panel and Button panel)
		gameplayPanel.setLayout(new BorderLayout());
		gameplayPanel.setPreferredSize(new Dimension(gameDisplayPanelWidth, (gameDisplayPanelHeight + moveButtonPanelHeight)));
		gameplayPanel.add(gameDisplayPanel, BorderLayout.CENTER);
		gameplayPanel.add(moveButtonPanel, BorderLayout.SOUTH);

		//Game Display Panel
		gameDisplayPanel.setBackground(new Color(255, 255, 255));
		gameDisplayPanel.setPreferredSize(new Dimension(gameDisplayPanelWidth, gameDisplayPanelHeight));

		//Button Panel
		moveButtonPanel.setBackground(new Color(200, 200, 200));
		moveButtonPanel.setPreferredSize(new Dimension(gameDisplayPanelWidth, moveButtonPanelHeight));
		moveButtonPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		moveButtonPanel.setLayout(new GridLayout(1, 7, (int) (1.0 + ((gameDisplayPanelWidth + gameDisplayPanelHeight) / 350)), 0));
		for (int i = 0; i < 7; i++) //Initialize arrays.
			{
			moveButtons[i] = new JButton("" + i);
			moveButtons[i].addActionListener(moveButtonPress);
			moveButtonPanel.add(moveButtons[i]);
			}

		//Interface Panel
		interfacePanel.setBackground(new Color(200, 200, 200));
		interfacePanel.setPreferredSize(new Dimension(interfacePanelWidth, gameDisplayPanelHeight + moveButtonPanelHeight));
		interfacePanel.setBorder(BorderFactory.createRaisedBevelBorder());
		interfacePanel.setLayout(new BorderLayout());
		interfacePanel.add(newMatchOrGamePanel, BorderLayout.NORTH);
		interfacePanel.add(labelPanel, BorderLayout.CENTER);

		//New Match or Game Panel
		newMatchOrGamePanel.setPreferredSize(new Dimension(interfacePanelWidth, gameDisplayPanelHeight / 4));
		newMatchOrGamePanel.setBorder(BorderFactory.createLoweredBevelBorder());
		newMatchOrGamePanel.setLayout(new GridLayout(2, 1, 0, 1));
		newMatchOrGamePanel.add(newMatch);
		newMatchOrGamePanel.add(newGame);
		newMatch.addActionListener(newMatchOrGamePress);
		newGame.addActionListener(newMatchOrGamePress);

		//Label Panel
		labelPanel.setLayout(new GridLayout(10, 1, 0, 0));
		for (int i = 0; i < 5; i++)
			{
			labels[i].setHorizontalAlignment(JLabel.CENTER);
			labels[i].setFont(new Font("Arial", Font.BOLD, interfacePanelWidth / 10));
			values[i].setBorder(BorderFactory.createLoweredBevelBorder());
			values[i].setHorizontalAlignment(JLabel.CENTER);
			values[i].setFont(new Font("Arial", Font.BOLD, interfacePanelWidth / 10));
			labelPanel.add(labels[i]);
			labelPanel.add(values[i]);
			}
		}

	private class MoveButtonPress implements ActionListener
		{
		public void actionPerformed(ActionEvent e)
			{
			String command = e.getActionCommand();
			commandOut.println(command);
			commandOut.flush();
			}
		}

	private class NewMatchOrGamePress implements ActionListener
		{
		public void actionPerformed(ActionEvent e)
			{
			String command = e.getActionCommand();
			commandOut.println(command);
			commandOut.flush();
			}
		}

	public void updateDisplay(StateRegister states)
		{
		updateValueLabels(states);
		gameDisplayPanel.renderGameState(states);
		this.repaint();
		}

	public void updateValueLabels(StateRegister states)
		{
		if (states == null || states.match == null || states.game == null)
			{
			if (states != null && (states.match == null || states.game == null))
				{
				for (int i = 0; i < 5; i++)
					{
					values[i].setText(INITIAL_INFO_VALUE);
					}
				}
			return;
			}
		String playerTurn = "---";
		if (states.game.isActive())
			{
			if (states.game.getPlayer() == 1)
				{
				playerTurn = "Player One";
				}
			else
				{
				playerTurn = "Player Two";
				}
			}
		values[0].setText("" + states.game.getTurn());
		values[1].setText(playerTurn);
		values[2].setText("" + states.match.getGamesCompleted());
		values[3].setText("" + states.match.getP1Wins());
		values[4].setText("" + states.match.getP2Wins());
		}

	public void lockInterface()
		{
		newGame.setEnabled(false);
		newMatch.setEnabled(false);
		lockMoveButtons();
		}

	public void unlockInterface()
		{
		newGame.setEnabled(true);
		newMatch.setEnabled(true);
		unlockMoveButtons();
		}

	public void lockNewMatchButton()
		{
		newMatch.setEnabled(false);
		}

	public void unlockNewMatchButton()
		{
		newMatch.setEnabled(true);
		}

	public void lockNewGameButton()
		{
		newGame.setEnabled(false);
		}

	public void unlockNewGameButton()
		{
		newGame.setEnabled(true);
		}

	public void lockMoveButtons()
		{
		for (int i = 0; i < 7; i++)
			{
			moveButtons[i].setEnabled(false);
			}
		}

	public void unlockMoveButtons()
		{
		for (int i = 0; i < 7; i++)
			{
			moveButtons[i].setEnabled(true);
			}
		}
	}
