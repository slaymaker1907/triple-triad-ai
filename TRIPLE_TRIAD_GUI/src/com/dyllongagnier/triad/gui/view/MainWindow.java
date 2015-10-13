package com.dyllongagnier.triad.gui.view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.TriadGame;
import com.dyllongagnier.triad.gui.controller.Players;

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	private final ButtonGroup playerAIButtons = new ButtonGroup();
	private final ButtonGroup opponentAIButtons = new ButtonGroup();
	private CardCollection selfHand, opponentHand, currentField;
	private static final JFileChooser explorerWindow = new JFileChooser(System.getProperty("user.dir"));
	private static MainWindow mainWindow;
	
	private final TriadSettings settings = new TriadSettings();
	
	public static MainWindow getMainWindow()
	{
		return MainWindow.mainWindow;
	}

	public static void main(String[] args)
	{
		mainWindow = new MainWindow();
		mainWindow.setVisible(true);
	}
	
	public MainWindow()
	{
		setTitle("Triple Triad Simulator");
		this.init();
	}
	
	private static class WindowExit implements WindowListener
	{
		@Override
		public void windowOpened(WindowEvent e)
		{
		}

		@Override
		public void windowClosing(WindowEvent e)
		{
			System.exit(0);
		}

		@Override
		public void windowClosed(WindowEvent e)
		{
		}

		@Override
		public void windowIconified(WindowEvent e)
		{
		}

		@Override
		public void windowDeiconified(WindowEvent e)
		{
		}

		@Override
		public void windowActivated(WindowEvent e)
		{
		}

		@Override
		public void windowDeactivated(WindowEvent e)
		{
		}
	}
	
	private void init()
	{
		this.setSize(1000, 402);
		this.addWindowListener(new WindowExit());
		
		selfHand = new CardCollection();
		
		opponentHand = new CardCollection();
		
		currentField = new CardCollection();
		
		JButton selfLoadDeck = new JButton("Load Deck");
		
		JButton opponentLoadDeck = new JButton("Load Deck");
		
		JButton btnGameSettings = new JButton("Game Settings");
		btnGameSettings.addActionListener((act) -> this.settings.setVisible(true));
		
		JRadioButton playerAI = new JRadioButton("AI");
		JRadioButton playerManual = new JRadioButton("Manual");
		playerAIButtons.add(playerManual);
		playerAIButtons.add(playerAI);
		playerAI.setSelected(true);
		
		JRadioButton opponentAI = new JRadioButton("AI");
		JRadioButton opponentManual = new JRadioButton("Manual");
		opponentAIButtons.add(opponentAI);
		opponentAIButtons.add(opponentManual);
		opponentManual.setSelected(true);
		
		JButton btnStart = new JButton("Start");
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(selfHand, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(selfLoadDeck)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(playerAI)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(playerManual)))
					.addGap(31)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(currentField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnGameSettings)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnStart)))
					.addPreferredGap(ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(opponentLoadDeck, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(opponentAI, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(opponentManual, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE))
						.addComponent(opponentHand, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(115, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(selfLoadDeck)
							.addComponent(opponentLoadDeck)
							.addComponent(btnGameSettings)
							.addComponent(playerAI)
							.addComponent(playerManual)
							.addComponent(btnStart))
						.addComponent(opponentAI)
						.addComponent(opponentManual))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(currentField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
						.addComponent(opponentHand, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
						.addComponent(selfHand, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
		
		selfLoadDeck.addActionListener((arg) -> this.loadDeck(Player.SELF));
		opponentLoadDeck.addActionListener((arg) -> this.loadDeck(Player.OPPONENT));
	}
	
	private void loadDeck(Player player)
	{
		int opened = MainWindow.explorerWindow.showOpenDialog(MainWindow.mainWindow);
		if (opened == JFileChooser.APPROVE_OPTION)
		{
			File file = MainWindow.explorerWindow.getSelectedFile();
			try
			{
				Iterable<UndeployedCard> newHand = Players.setPlayerDeck(player, file.getAbsolutePath());
				this.setHand(player, newHand);
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(MainWindow.explorerWindow, "Error, could not load deck from: " + file.getName());
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public void displayBoardState(TriadGame game)
	{
	}
	
	public void setHand(Player player, Iterable<UndeployedCard> hand)
	{
		int i = 0;
		for(UndeployedCard card : hand)
		{
			assert card.isVisible();
			CardCollection col;
			switch(player)
			{
				case SELF:
					col = this.selfHand;
					break;
				case OPPONENT:
					col = this.opponentHand;
					break;
				default:
					throw new IllegalArgumentException();
			}
			col.setCard(new CardWindow(card.deploy()), i++);
		}
		this.validate();
		this.repaint();
	}
}
