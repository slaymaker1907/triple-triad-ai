package com.dyllongagnier.triad.gui.view;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import com.dyllongagnier.triad.gui.controller.GameController;

import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;

import com.dyllongagnier.triad.core.AscensionRule;

import java.awt.Dimension;

import javax.swing.SwingConstants;

public class TriadSettings extends JFrame
{
	private static final long serialVersionUID = 1L;
	private static final String infinity = "INF";
	
	private JTextField maxThreadValue;
	private JTextField maxThinkTime;
	
	public TriadSettings() 
	{
		setTitle("Settings");
		setSize(new Dimension(332, 306));
		JComboBox<AscensionRule> ascensionRuleValue = new JComboBox<>();
		ascensionRuleValue.setModel(new DefaultComboBoxModel<AscensionRule>(AscensionRule.values()));
		
		JLabel lblAscensionRule = new JLabel("Ascension Rule");
		
		JCheckBox chckbxFallenAce = new JCheckBox("Fallen Ace");
		
		JCheckBox chckbxReverse = new JCheckBox("Reverse");
		
		JCheckBox chckbxPlus = new JCheckBox("Plus");
		
		JCheckBox chckbxSame = new JCheckBox("Same");
		
		JCheckBox chckbxSuddenDeath = new JCheckBox("Sudden Death");
		
		JLabel lblMaximumThreads = new JLabel("Maximum Threads:");
		
		maxThinkTime = new JTextField();
		maxThinkTime.setText("8");
		maxThinkTime.setHorizontalAlignment(SwingConstants.RIGHT);
		maxThinkTime.setColumns(10);
		
		maxThreadValue = new JTextField();
		maxThreadValue.setHorizontalAlignment(SwingConstants.RIGHT);
		maxThreadValue.setColumns(10);
		maxThreadValue.setText(String.valueOf(Runtime.getRuntime().availableProcessors()));
		
		JCheckBox orderButton = new JCheckBox("Order");
		
		Runnable executeOptions = () ->
		{
			GameController controller = GameController.getController(false);
			controller.setAscensionRule((AscensionRule)ascensionRuleValue.getSelectedItem());
			controller.setIsFallenAce(chckbxFallenAce.isSelected());
			controller.setIsReverse(chckbxReverse.isSelected());
			controller.setIsPlus(chckbxPlus.isSelected());
			controller.setIsSame(chckbxSame.isSelected());
			controller.setIsCombo(chckbxSame.isSelected() || chckbxPlus.isSelected());
			controller.setIsSuddenDeath(chckbxSuddenDeath.isSelected());
			controller.setMaxThreads(Integer.parseInt(maxThreadValue.getText()));
			TriadSettings.parseThinkTime(maxThinkTime.getText());
			controller.setIsOrder(orderButton.isSelected());
		};
		
		Runnable reset = () ->
		{
			GameController controller = GameController.getController(false);
			controller.setDefaultOptions();
			controller.setAscensionRule(controller.getAscensionRule());
			chckbxFallenAce.setSelected(controller.getIsFallenAce());
			chckbxReverse.setSelected(controller.getIsReverse());
			chckbxPlus.setSelected(controller.getIsPlus());
			chckbxSame.setSelected(controller.getIsSame());
			chckbxSuddenDeath.setSelected(controller.getIsSuddenDeath());
			maxThreadValue.setText(String.valueOf(controller.getMaxThreads()));
			maxThinkTime.setText("8");
			orderButton.setSelected(controller.getIsOrder());
		};
		
		reset.run();
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener((act) -> this.okEvent(executeOptions, reset));
		
		JLabel maxThinkTimeLabel = new JLabel("Maximum Think Time (s):");
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnOk, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(29)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblAscensionRule)
									.addGap(18)
									.addComponent(ascensionRuleValue, 0, 167, Short.MAX_VALUE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addGroup(groupLayout.createSequentialGroup()
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(lblMaximumThreads, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
												.addComponent(maxThinkTimeLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
											.addGap(18))
										.addGroup(groupLayout.createSequentialGroup()
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addComponent(chckbxFallenAce)
												.addComponent(chckbxSame)
												.addComponent(chckbxSuddenDeath))
											.addGap(5)
											.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
												.addComponent(chckbxPlus)
												.addComponent(chckbxReverse, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(orderButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
											.addGap(16)))
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(maxThreadValue, 0, 0, Short.MAX_VALUE)
										.addComponent(maxThinkTime, GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE))))))
					.addGap(30))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(33)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAscensionRule)
						.addComponent(ascensionRuleValue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblMaximumThreads)
						.addComponent(maxThreadValue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(maxThinkTime, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(14)
							.addComponent(maxThinkTimeLabel)))
					.addGap(27)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxFallenAce)
						.addComponent(chckbxReverse))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxSame)
						.addComponent(chckbxPlus))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxSuddenDeath)
						.addComponent(orderButton))
					.addPreferredGap(ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
					.addComponent(btnOk, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
	}
	
	private void okEvent(Runnable toRun, Runnable reset)
	{
		try
		{
			toRun.run();
			GameController.getController(false).verifyOptionValidity();
			this.setVisible(false);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, "Invalid settings configuration.");
			reset.run();
		}
	}
	
	private static void parseThinkTime(String str)
	{
		GameController controller = GameController.getController(false);
		if (str.toUpperCase().equals(TriadSettings.infinity))
			controller.setTimeout(Integer.MAX_VALUE);
		else
			controller.setTimeout(Double.parseDouble(str));
	}
}
