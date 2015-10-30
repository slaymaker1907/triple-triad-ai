package com.dyllongagnier.triad.gui.view;

import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.function.Consumer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import com.dyllongagnier.triad.card.DeployedCard;
import com.dyllongagnier.triad.card.Player;
import com.dyllongagnier.triad.card.UndeployedCard;
import com.dyllongagnier.triad.core.AscensionField;
import com.dyllongagnier.triad.core.BoardState;
import com.dyllongagnier.triad.core.Field;
import com.dyllongagnier.triad.core.PossibleMove;
import com.dyllongagnier.triad.core.TriadGame;
import com.dyllongagnier.triad.deckbuilder.view.DeckBuilderWindow;
import com.dyllongagnier.triad.gui.controller.GameController;
import com.dyllongagnier.triad.gui.controller.InvalidPlayerException;
import com.dyllongagnier.triad.deckbuilder.view.QuickBuilderWindow;

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	private final ButtonGroup playerAIButtons = new ButtonGroup();
	private final ButtonGroup opponentAIButtons = new ButtonGroup();
	private CardCollection selfHand, opponentHand, currentField;
	private static final JFileChooser explorerWindow = new JFileChooser(System.getProperty("user.dir"));
	private static MainWindow mainWindow;
	public CurrentTurnIndicator currentTurnIndicatorSelf, currentTurnIndicatorOpponent;
	
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
		this.setCurrentTurn(Player.NONE);
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
		this.setSize(1000, 437);
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
		MainWindow.setAIButtonEvents(playerAI, playerManual, Player.SELF);
		
		JRadioButton opponentAI = new JRadioButton("AI");
		JRadioButton opponentManual = new JRadioButton("Manual");
		opponentAIButtons.add(opponentAI);
		opponentAIButtons.add(opponentManual);
		opponentManual.setSelected(true);
		MainWindow.setAIButtonEvents(opponentAI, opponentManual, Player.OPPONENT);
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener((act) ->
		{
			TriadGame toDisplay = GameController.getController(false).startNewGame();
			this.displayBoardState(toDisplay);
		});
		
		JButton btnDeckBuilder = new JButton("Deck Builder");
		btnDeckBuilder.addActionListener((act) -> new DeckBuilderWindow().setVisible(true));
		
		currentTurnIndicatorSelf = new CurrentTurnIndicator(Player.SELF);
		
		currentTurnIndicatorOpponent = new CurrentTurnIndicator(Player.OPPONENT);
		
		JButton quickLoadSelf = new JButton("QuickLoad");
		quickLoadSelf.addActionListener((act) -> new QuickBuilderWindow(this.getDeckConsumer(Player.SELF)));
		
		JButton quickLoadOpp = new JButton("QuickLoad");
		quickLoadOpp.addActionListener((act) -> new QuickBuilderWindow(this.getDeckConsumer(Player.OPPONENT)));
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(selfHand, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(quickLoadSelf, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(selfLoadDeck, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(playerAI)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(playerManual)
							.addGap(18)
							.addComponent(currentTurnIndicatorSelf, GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)))
					.addGap(30)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(currentField, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnGameSettings)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnStart)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDeckBuilder)))
					.addGap(25)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(opponentHand, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(quickLoadOpp, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(opponentLoadDeck, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(opponentAI, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(opponentManual)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(currentTurnIndicatorOpponent, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
					.addContainerGap(19, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(29, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(quickLoadOpp)
						.addComponent(quickLoadSelf))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(currentTurnIndicatorSelf, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
							.addComponent(currentTurnIndicatorOpponent, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
							.addComponent(opponentManual)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(selfLoadDeck)
								.addComponent(opponentLoadDeck)
								.addComponent(btnGameSettings)
								.addComponent(playerAI)
								.addComponent(playerManual)
								.addComponent(btnStart)
								.addComponent(btnDeckBuilder))
							.addComponent(opponentAI)))
					.addPreferredGap(ComponentPlacement.RELATED)
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
	
	private Consumer<String[]> getDeckConsumer(Player player)
	{
		return (deck) ->
		{
			Iterable<UndeployedCard> result = GameController.getController(false).setPlayerDeck(player, deck);
			setHand(player, result);
		};
	}
	
	private void loadDeck(Player player)
	{
		int opened = MainWindow.explorerWindow.showOpenDialog(MainWindow.mainWindow);
		if (opened == JFileChooser.APPROVE_OPTION)
		{
			File file = MainWindow.explorerWindow.getSelectedFile();
			try
			{
				Iterable<UndeployedCard> newHand = GameController.getController(false).setPlayerDeck(player, file.getAbsolutePath());
				this.setHand(player, newHand);
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(MainWindow.explorerWindow, "Error, could not load deck from: " + file.getName() + ". " + e.getMessage());
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public void displayBoardState(TriadGame game)
	{
		this.setCurrentTurn(game.getCurrentPlayer());
		BoardState state = game.getCurrentState();
		this.displayField(state.playedCards);
		this.setHand(Player.SELF, state.getHand(Player.SELF));
		this.setHand(Player.OPPONENT, state.getHand(Player.OPPONENT));
		this.setCanDropToField(false);
		this.allowDraggingFromHand(Player.SELF, false);
		this.allowDraggingFromHand(Player.OPPONENT, false);
		
		PossibleMove lastMove = game.getLastMove();
		if (lastMove != null)
		{
			this.currentField.setLastPlayed(lastMove.row, lastMove.col);
		}
	}
	
	public CardCollection getHand(Player player)
	{
		switch(player)
		{
			case SELF:
				return this.selfHand;
			case OPPONENT:
				return this.opponentHand;
			default:
				throw new InvalidPlayerException();
		}
	}
	
	private void displayField(Field field)
	{
		for(int row = 0; row < 3; row++)
		{
			for(int col = 0; col < 3; col++)
			{
				DeployedCard card = field.getCard(row, col);
				if (card != null)
				{
					if (field instanceof AscensionField)
					{
						AscensionField asField = (AscensionField)field;
						this.currentField.setCard(new CardWindow(card.card, asField.getAscensionNumber(card.card.cardType)), row, col);
					}
					else
					{
						this.currentField.setCard(new CardWindow(card.card, 0), row, col);
					}
				}
				else
					this.currentField.setCard(new CardWindow(), row, col);
			}
		}
	}
	
	public void setHand(Player player, Iterable<UndeployedCard> hand)
	{
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
		for(int i = 0; i < 9; i++)
			col.setCard(new CardWindow(), i);
		
		for(UndeployedCard card : hand)
		{
			assert card.isVisible();
			// Use the hashcode to determine position (kind of hacky fix).
			col.setCard(new CardWindow(card.deploy(), 0), card.hashCode());
		}
		this.validate();
		this.repaint();
	}
	
	public void setCurrentTurn(Player player)
	{
		switch(player)
		{
			case SELF:
				this.currentTurnIndicatorSelf.setVisible(true);
				this.currentTurnIndicatorOpponent.setVisible(false);
				break;
			case OPPONENT:
				this.currentTurnIndicatorSelf.setVisible(false);
				this.currentTurnIndicatorOpponent.setVisible(true);
				break;
			default:
				this.currentTurnIndicatorSelf.setVisible(false);
				this.currentTurnIndicatorOpponent.setVisible(false);
				break;
		}
	}
	
	private static void setAIButtonEvents(JRadioButton ai, JRadioButton manual, Player player)
	{
		ActionListener handler = (act) -> GameController.getController(false).setAgent(player, ai.isSelected());
		ai.addActionListener(handler);
		manual.addActionListener(handler);
	}
	
	public void allowDraggingFromHand(Player player, boolean canDrag)
	{
		switch(player)
		{
			case SELF:
				this.selfHand.setCanDragFrom(canDrag);
				break;
			case OPPONENT:
				this.opponentHand.setCanDragFrom(canDrag);
				break;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	public void setCanDropToField(boolean choice)
	{
		this.currentField.setCanDropTo(choice);
	}
}
