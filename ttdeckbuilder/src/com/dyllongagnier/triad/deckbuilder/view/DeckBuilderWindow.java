package com.dyllongagnier.triad.deckbuilder.view;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;

import com.dyllongagnier.triad.deckbuilder.controller.DeckWriter;

import java.awt.Font;
import java.io.File;

public class DeckBuilderWindow extends JFrame
{
	private static final JFileChooser explorerWindow = new JFileChooser(System.getProperty("user.dir"));
	protected final DeckPanel deckPanel;
	
	public static void main(String[] args)
	{
		new DeckBuilderWindow().setVisible(true);
	}
	
	public DeckBuilderWindow() {
		this.setSize(450, 400);
		setTitle("Deck Builder");
		
		deckPanel = new DeckPanel();
		
		JButton btnSave = new JButton("Save");
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnSave.addActionListener((act) -> this.saveDeck());
		this.getRootPane().setDefaultButton(btnSave);
		
		JLabel lblCard1 = new JLabel("Card 1:");
		lblCard1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel card2 = new JLabel("Card 2:");
		card2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel card3 = new JLabel("Card 3:");
		card3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel card4 = new JLabel("Card 4:");
		card4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel card5 = new JLabel("Card 5:");
		card5.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel card6 = new JLabel("Card 6:");
		card6.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel card7 = new JLabel("Card 7:");
		card7.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel card8 = new JLabel("Card 8:");
		card8.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel card9 = new JLabel("Card 9:");
		card9.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton btnClear = new JButton("Clear");
		btnClear.setFont(new Font("Tahoma", Font.PLAIN, 16));
		btnClear.addActionListener((act) -> deckPanel.clearNames());
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(50)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(card9)
								.addComponent(card8)
								.addComponent(card7)
								.addComponent(card6)
								.addComponent(card5)
								.addComponent(card4)
								.addComponent(card3)
								.addComponent(card2)
								.addComponent(lblCard1))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(deckPanel, GroupLayout.PREFERRED_SIZE, 291, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(37, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(35)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblCard1)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(card2, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(card3, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(card4, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(card5, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(card6, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(card7, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(card8, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(card9, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
						.addComponent(deckPanel, GroupLayout.PREFERRED_SIZE, 243, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(29, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
	}
	
	private static final long serialVersionUID = 1L;
	
	protected void saveDeck()
	{
		int opened = DeckBuilderWindow.explorerWindow.showSaveDialog(this);
		if (opened == JFileChooser.APPROVE_OPTION)
		{
			File file = explorerWindow.getSelectedFile();
			try
			{
				DeckWriter.writeDeckToDisk(file.getAbsolutePath(), this.deckPanel.getNames());
			}
			catch (Exception e)
			{
				JOptionPane.showMessageDialog(this, "Error, could save deck to: " + file.getName() + ". " + e.getMessage());
			}
		}
	}
}
