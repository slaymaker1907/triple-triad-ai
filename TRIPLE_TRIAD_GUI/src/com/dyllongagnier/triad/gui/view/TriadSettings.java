package com.dyllongagnier.triad.gui.view;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import com.dyllongagnier.triad.gui.controller.Players;

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
	
	private JTextField maxThreadValue;
	
	public TriadSettings() 
	{
		setTitle("Settings");
		setSize(new Dimension(299, 291));
		JComboBox<AscensionRule> ascensionRuleValue = new JComboBox<>();
		ascensionRuleValue.setModel(new DefaultComboBoxModel<AscensionRule>(AscensionRule.values()));
		
		JLabel lblAscensionRule = new JLabel("Ascension Rule");
		
		JCheckBox chckbxFallenAce = new JCheckBox("Fallen Ace");
		
		JCheckBox chckbxReverse = new JCheckBox("Reverse");
		
		JCheckBox chckbxPlus = new JCheckBox("Plus");
		
		JCheckBox chckbxSame = new JCheckBox("Same");
		
		JCheckBox chckbxCombo = new JCheckBox("Combo");
		
		JCheckBox chckbxSuddenDeath = new JCheckBox("Sudden Death");
		
		JLabel lblMaximumThreads = new JLabel("Maximum Threads:");
		
		maxThreadValue = new JTextField();
		maxThreadValue.setHorizontalAlignment(SwingConstants.RIGHT);
		maxThreadValue.setColumns(10);
		maxThreadValue.setText(String.valueOf(Runtime.getRuntime().availableProcessors()));
		
		Runnable executeOptions = () ->
		{
			Players.setAscensionRule((AscensionRule)ascensionRuleValue.getSelectedItem());
			Players.setIsFallenAce(chckbxFallenAce.isSelected());
			Players.setIsReverse(chckbxReverse.isSelected());
			Players.setIsPlus(chckbxPlus.isSelected());
			Players.setIsSame(chckbxSame.isSelected());
			Players.setIsCombo(chckbxCombo.isSelected());
			Players.setIsSuddenDeath(chckbxSuddenDeath.isSelected());
			Players.setMaxThreads(Integer.parseInt(maxThreadValue.getText()));
		};
		
		Runnable reset = () ->
		{
			Players.setDefaultOptions();
			Players.setAscensionRule(AscensionRule.NONE);
			chckbxFallenAce.setSelected(false);
			chckbxReverse.setSelected(false);
			chckbxPlus.setSelected(false);
			chckbxSame.setSelected(false);
			chckbxCombo.setSelected(false);
			chckbxSuddenDeath.setSelected(false);
			maxThreadValue.setText(String.valueOf(Players.getMaxThreads()));
		};
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener((act) -> this.okEvent(executeOptions, reset));
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(29)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(chckbxSuddenDeath)
								.addComponent(chckbxCombo)
								.addComponent(chckbxPlus)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(lblAscensionRule)
										.addGap(18)
										.addComponent(ascensionRuleValue, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE))
									.addGroup(groupLayout.createSequentialGroup()
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
											.addComponent(lblMaximumThreads)
											.addComponent(chckbxFallenAce))
										.addPreferredGap(ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
											.addComponent(chckbxReverse)
											.addComponent(chckbxSame)
											.addComponent(maxThreadValue, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(117)
							.addComponent(btnOk)))
					.addContainerGap(22, Short.MAX_VALUE))
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
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxFallenAce)
						.addComponent(chckbxReverse))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(chckbxPlus)
						.addComponent(chckbxSame))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chckbxCombo)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(chckbxSuddenDeath)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnOk)
					.addContainerGap(30, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
	}
	
	private void okEvent(Runnable toRun, Runnable reset)
	{
		try
		{
			toRun.run();
			Players.verifyOptionValidity();
			this.setVisible(false);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, "Invalid settings configuration.");
			reset.run();
		}
	}
}
