package com.dyllongagnier.triad.gui.view;

import javax.swing.ButtonModel;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	private final ButtonGroup playerAIButtons = new ButtonGroup();
	private final ButtonGroup opponentAIButtons = new ButtonGroup();

	public static void main(String[] args)
	{
		JFrame window = new MainWindow();
		window.setVisible(true);
	}
	
	public MainWindow()
	{
		setTitle("Triple Triad Simulator");
		this.init();
	}
	
	private void init()
	{
		this.setSize(1000, 402);
		
		CardCollection selfHand = new CardCollection();
		
		CardCollection opponentHand = new CardCollection();
		
		CardCollection currentField = new CardCollection();
		
		JButton selfLoadDeck = new JButton("Load Deck");
		
		JButton opponentLoadDeck = new JButton("Load Deck");
		
		JButton btnGameSettings = new JButton("Game Settings");
		
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
	}
}
