package connectfour.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;

import connectfour.control.Match;

public class MatchOptionsPanel extends javax.swing.JPanel
	{
	private String[] p1ColorArray;
	private String[] p2ColorArray;

	private ActionListener p1ColorSelect = new P1ColorSelect();
	private ActionListener p2ColorSelect = new P2ColorSelect();
	private ButtonGroup turnSelectButtons = new ButtonGroup();

	public MatchOptionsPanel()
		{
		p1ColorArray = generateColorArray(Match.COLORS[1]);
		p2ColorArray = generateColorArray(Match.COLORS[0]);
		initComponents();
		}

	private void initComponents()
		{
		firstTurnPanel = new javax.swing.JPanel();
		firstTurnLable = new javax.swing.JLabel();
		p1TurnSelector = new javax.swing.JRadioButton("", true);
		p2TurnSelector = new javax.swing.JRadioButton();
		playerPropertiesPanel = new javax.swing.JPanel();
		player1Properties = new javax.swing.JPanel();
		p1ColorLabel = new javax.swing.JLabel();
		p1ColorSelector = new javax.swing.JComboBox();
		p1ControllerLabel = new javax.swing.JLabel();
		p1ControllerSelector = new javax.swing.JComboBox();
		player2Properties = new javax.swing.JPanel();
		p2ColorLabel = new javax.swing.JLabel();
		p2ColorSelector = new javax.swing.JComboBox();
		p2ControllerLabel = new javax.swing.JLabel();
		p2ControllerSelector = new javax.swing.JComboBox();

		setLayout(new java.awt.BorderLayout());

		firstTurnPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

		firstTurnLable.setText("The first turn goes to:      ");
		firstTurnPanel.add(firstTurnLable);

		p1TurnSelector.setText("Player 1           ");
		firstTurnPanel.add(p1TurnSelector);

		p2TurnSelector.setText("Player 2");
		firstTurnPanel.add(p2TurnSelector);

		add(firstTurnPanel, java.awt.BorderLayout.PAGE_START);

		playerPropertiesPanel.setLayout(new java.awt.GridLayout(2, 1));

		player1Properties.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

		p1ColorLabel.setText("Player 1       Color:");
		player1Properties.add(p1ColorLabel);

		p1ColorSelector.setModel(new javax.swing.DefaultComboBoxModel(p1ColorArray));
		p1ColorSelector.addActionListener(p1ColorSelect);
		player1Properties.add(p1ColorSelector);

		p1ControllerLabel.setText("       Controller:");
		player1Properties.add(p1ControllerLabel);

		p1ControllerSelector.setModel(new javax.swing.DefaultComboBoxModel(Match.CONTROLLERS));
		player1Properties.add(p1ControllerSelector);

		playerPropertiesPanel.add(player1Properties);

		player2Properties.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

		p2ColorLabel.setText("Player 2       Color:");
		player2Properties.add(p2ColorLabel);

		p2ColorSelector.setModel(new javax.swing.DefaultComboBoxModel(p2ColorArray));
		p2ColorSelector.addActionListener(p2ColorSelect);
		player2Properties.add(p2ColorSelector);

		p2ControllerLabel.setText("       Controller:");
		player2Properties.add(p2ControllerLabel);

		p2ControllerSelector.setModel(new javax.swing.DefaultComboBoxModel(Match.CONTROLLERS));
		player2Properties.add(p2ControllerSelector);
		p2ControllerSelector.setSelectedIndex(Match.CONTROLLERS.length - 1);

		playerPropertiesPanel.add(player2Properties);

		add(playerPropertiesPanel, java.awt.BorderLayout.CENTER);

		turnSelectButtons.add(p1TurnSelector);
		turnSelectButtons.add(p2TurnSelector);
		}// </editor-fold>

	// Variables declaration - do not modify
	private javax.swing.JLabel firstTurnLable;
	private javax.swing.JPanel firstTurnPanel;
	private javax.swing.JLabel p1ColorLabel;
	private javax.swing.JComboBox p1ColorSelector;
	private javax.swing.JLabel p1ControllerLabel;
	private javax.swing.JComboBox p1ControllerSelector;
	private javax.swing.JRadioButton p1TurnSelector;
	private javax.swing.JLabel p2ColorLabel;
	private javax.swing.JComboBox p2ColorSelector;
	private javax.swing.JLabel p2ControllerLabel;
	private javax.swing.JComboBox p2ControllerSelector;
	private javax.swing.JRadioButton p2TurnSelector;
	private javax.swing.JPanel player1Properties;
	private javax.swing.JPanel player2Properties;
	private javax.swing.JPanel playerPropertiesPanel;

	// End of variables declaration

	private class P1ColorSelect implements ActionListener
		{
		public void actionPerformed(ActionEvent e)
			{
			JComboBox cb = (JComboBox) e.getSource();
			String selection = (String) cb.getSelectedItem();

			p2ColorArray = generateColorArray(selection);
			String p2selection = (String) p2ColorSelector.getSelectedItem();
			p2ColorSelector.setModel(new javax.swing.DefaultComboBoxModel(p2ColorArray));
			p2ColorSelector.setSelectedItem(p2selection);
			}
		}

	private class P2ColorSelect implements ActionListener
		{
		public void actionPerformed(ActionEvent e)
			{
			JComboBox cb = (JComboBox) e.getSource();
			String selection = (String) cb.getSelectedItem();

			p1ColorArray = generateColorArray(selection);
			String p1selection = (String) p1ColorSelector.getSelectedItem();
			p1ColorSelector.setModel(new javax.swing.DefaultComboBoxModel(p1ColorArray));
			p1ColorSelector.setSelectedItem(p1selection);
			}
		}

	private static String[] generateColorArray(String exclusion)
		{
		LinkedList<String> colorList = new LinkedList<String>();
		for (int i = 0; i < Match.COLORS.length; i++)
			{
			if (!Match.COLORS[i].equals(exclusion))
				{
				colorList.add(Match.COLORS[i]);
				}
			}
		String[] colorArray = new String[colorList.size()];
		colorList.toArray(colorArray);
		return colorArray;
		}

	public Match generateMatchState()
		{
		int firstMover = 1;
		int p1Controller = p1ControllerSelector.getSelectedIndex();
		int p2Controller = p2ControllerSelector.getSelectedIndex();
		String p1ColorName = p1ColorSelector.getSelectedItem().toString();
		String p2ColorName = p2ColorSelector.getSelectedItem().toString();

		if (p1TurnSelector.isSelected())
			{
			firstMover = 1;
			}
		else
			{
			firstMover = 2;
			}
		return new Match(firstMover, p1Controller, p2Controller, p1ColorName, p2ColorName);
		}
	}
