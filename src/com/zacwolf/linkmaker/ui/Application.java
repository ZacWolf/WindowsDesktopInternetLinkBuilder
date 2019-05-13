/**
 *
 */
package com.zacwolf.linkmaker.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import com.zacwolf.linkmaker.LinkMaker;

/**
 * @author Zac Morris <zac@zacwolf.com>
 *
 */
public final class Application extends JFrame {
final	static	private	long			serialVersionUID	=	-2604465444949636705L;
final			private	JTextField		textField			=	new JTextField();
final			private	JButton			btnCreate			=	new JButton("Create Link");
final 			private	JLabel 			errorMsg 			=	new JLabel("");
final			private Clipboard		cb					=	Toolkit.getDefaultToolkit().getSystemClipboard();
final			private	Image			icon				=	new ImageIcon(this.getClass().getResource("icon.png")).getImage();

		static	private	LinkMaker		maker;
		static {
			try {
										maker				=	new LinkMaker();
			} catch (final Exception e) {
				e.printStackTrace();
			}
		}

	/**
	 * @throws HeadlessException
	 */
	public Application() throws HeadlessException{
		setIconImage(icon);
		setTitle("Desktop Link Maker");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


							errorMsg.setFont(new Font("Tahoma", Font.BOLD, 11));
							errorMsg.setForeground(Color.RED);
							errorMsg.setText("");

							textField.getDocument().addDocumentListener(new DocumentListener() {

								@Override
								public void insertUpdate(final DocumentEvent e){
									errorMsg.setText("");
final	Document			document		=	e.getDocument();
        							try {
final	String				fieldData		=	document.getText(0, document.getLength());
final	String				clipboardData	=	cb.getData(DataFlavor.stringFlavor)+"";
        								if (clipboardData.equals(fieldData)) {
        									try {
        										validateLink(fieldData);
        									} catch (final Exception e1) {
												errorMsg.setText("Not a valid link address. Must start with http:// or https://");
											}
        								}
        							} catch (final Exception e2) {
        								errorMsg.setText(e2.getMessage());
        							}
								 }

								@Override
								public void removeUpdate(final DocumentEvent e){
									errorMsg.setText("");
								}

								@Override
								public void changedUpdate(final DocumentEvent e){
									errorMsg.setText("");
								}

							});

							btnCreate.setEnabled(true);
							btnCreate.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(final ActionEvent e){
									try {
										validateLink(textField.getText());
										maker.writeDotURL(textField.getText());
										textField.setText("");
									} catch (final Exception ie) {
										errorMsg.setText(ie.getMessage());
									}
								}

							});

final	JLabel				lblNewLabel		=	new JLabel("Paste Link Text Here:");
//							textField.setColumns(10);
final	GroupLayout			groupLayout		=	new GroupLayout(getContentPane());
							groupLayout.setHorizontalGroup(
								groupLayout.createParallelGroup(Alignment.LEADING)
									.addGroup(groupLayout.createSequentialGroup()
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
											.addGroup(groupLayout.createSequentialGroup()
												.addContainerGap()
												.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
													.addComponent(textField, GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE)
													.addGroup(groupLayout.createSequentialGroup()
														.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(errorMsg, GroupLayout.PREFERRED_SIZE, 426, GroupLayout.PREFERRED_SIZE))))
											.addGroup(groupLayout.createSequentialGroup()
												.addGap(222)
												.addComponent(btnCreate, GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
												.addGap(244)))
										.addContainerGap())
							);
							groupLayout.setVerticalGroup(
								groupLayout.createParallelGroup(Alignment.LEADING)
									.addGroup(groupLayout.createSequentialGroup()
										.addContainerGap()
										.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
											.addComponent(lblNewLabel)
											.addComponent(errorMsg))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(btnCreate)
										.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							);
		getContentPane().setLayout(groupLayout);
		setSize(800,130);
		setVisible(true);
	}

	/**
	 * @param gc
	 */
	public Application(final GraphicsConfiguration gc){
		super(gc);
	}

	/**
	 * @param title
	 * @throws HeadlessException
	 */
	public Application(final String title) throws HeadlessException{
		super(title);
	}

	/**
	 * @param title
	 * @param gc
	 */
	public Application(final String title, final GraphicsConfiguration gc){
		super(title, gc);
	}

	private void validateLink(final String u) throws Exception {
		new URL(u).toURI();
	}

	public static void main(final String[] args) {
		try {
			new Application();
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
