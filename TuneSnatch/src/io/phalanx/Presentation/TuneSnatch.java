package io.phalanx.Presentation;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class TuneSnatch {

	private JFrame frmTunesnatch;
	private TerminalForm frmTerminal;
	private JPanel sidebar;
	private JPanel content;
	private JLabel lblCurrent = new JLabel();
	JPanel pnlDashboard = new JPanel();
	JPanel pnlSync = new JPanel();
	JPanel pnlUpdates = new JPanel();
	JPanel pnlDownload = new JPanel();
	JPanel pnlLibrary = new JPanel();
	JPanel pnlSettings = new JPanel();
	final String DASHBOARD_PANEL = "dashboard";
	final String SYNC_PANEL = "sync";
	final String UPDATES_PANEL = "updates";
	final String DOWNLOAD_PANEL = "download";
	final String LIBRARY_PANEL = "library";
	final String SETTINGS_PANEL = "settings";
	JButton btnSync;
	JButton btnUpdates;
	JButton btnDownload;
	JButton btnLibrary;
	JButton btnTerminal;
	JButton btnSettings;
	JButton btnAbout;
	JButton btnHelp;
	CardLayout cardlayout = new CardLayout(0, 0);
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					TuneSnatch window = new TuneSnatch();
//					window.frmTunesnatch.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
		
		PreProcessor cmd = new PreProcessor();
		cmd.run();
	}

	/**
	 * Create the application.
	 */
	public TuneSnatch() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// ** BEGIN DECLARATIONS ** //
		frmTerminal = new TerminalForm();
		SpringLayout springLayout = new SpringLayout();
		// ** END DECLARATIONS ** //
		
		initializeDashboard();
		initializeSyncPanel();
		
		// Main form initialization
		frmTunesnatch = new JFrame();
		frmTunesnatch.addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {
				lblCurrent.setText("test");
				btnTerminal.setBackground(frmTerminal.isVisible() ? Color.decode("#7FB239") : Color.decode("#3E3E3E"));
			}
			public void windowLostFocus(WindowEvent e) {
			}
		});
		frmTunesnatch.setResizable(false);
		frmTunesnatch.getContentPane().setBackground(Color.decode("#34495e"));
		frmTunesnatch.getContentPane().setLayout(springLayout);
		frmTunesnatch.setTitle("TuneSnatch");
		frmTunesnatch.setBounds(100, 100, 450, 650);
		frmTunesnatch.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//** BEGIN SIDEBAR **//
		sidebar = new JPanel();
		springLayout.putConstraint(SpringLayout.EAST, sidebar, 75, SpringLayout.WEST, frmTunesnatch.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, sidebar, 628, SpringLayout.NORTH, frmTunesnatch.getContentPane());
		sidebar.setBackground(Color.decode("#3E3E3E"));
		springLayout.putConstraint(SpringLayout.NORTH, sidebar, 0, SpringLayout.NORTH, frmTunesnatch.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, sidebar, 0, SpringLayout.WEST, frmTunesnatch.getContentPane());
		sidebar.setLayout(null);
		sidebar.setBorder(BorderFactory.createEmptyBorder());
		frmTunesnatch.getContentPane().add(sidebar);
		
		lblCurrent = new JLabel("");
		lblCurrent.setForeground(Color.WHITE);
		lblCurrent.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrent.setBounds(0, 565, 75, 16);
		sidebar.add(lblCurrent);
		
		btnSync = new JButton("\uF079");
		btnSync.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				btnSync.setBackground(Color.decode("#2980b9").darker());
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				btnSync.setBackground(Color.decode("#4B4B4B"));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				btnSync.setBackground(Color.decode("#2980b9"));
				btnSync.setForeground(Color.white);
				btnUpdates.setBackground(Color.decode("#3E3E3E"));
				btnDownload.setBackground(Color.decode("#3E3E3E"));
				btnLibrary.setBackground(Color.decode("#3E3E3E"));
				btnTerminal.setBackground(frmTerminal.isVisible() ? Color.decode("#7FB239") : Color.decode("#3E3E3E"));
				btnSettings.setBackground(Color.decode("#3E3E3E"));
				cardlayout.show(content, SYNC_PANEL);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if(!pnlSync.isVisible())
					btnSync.setBackground(Color.decode("#4B4B4B"));
				lblCurrent.setText("sync");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if(!pnlSync.isVisible())
					btnSync.setBackground(Color.decode("#3E3E3E"));
				lblCurrent.setText("");
			}
		});
		btnSync.setToolTipText("sync");
		btnSync.setForeground(Color.white);
		btnSync.setBackground(Color.decode("#3E3E3E"));
		btnSync.setOpaque(true);
		btnSync.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
		btnSync.setBorderPainted(true);
		btnSync.setFont(new Font("FontAwesome", Font.PLAIN, 30));
		btnSync.setBounds(0, 60, 75, 60);
		sidebar.add(btnSync);
		
		btnUpdates = new JButton("\uF01C");
		btnUpdates.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				btnUpdates.setBackground(Color.decode("#2980b9").darker());
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				btnUpdates.setBackground(Color.decode("#4B4B4B"));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				btnSync.setBackground(Color.decode("#3E3E3E"));
				btnUpdates.setBackground(Color.decode("#2980b9"));
				btnDownload.setBackground(Color.decode("#3E3E3E"));
				btnLibrary.setBackground(Color.decode("#3E3E3E"));
				btnTerminal.setBackground(frmTerminal.isVisible() ? Color.decode("#7FB239") : Color.decode("#3E3E3E"));
				btnSettings.setBackground(Color.decode("#3E3E3E"));
				cardlayout.show(content, UPDATES_PANEL);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if(!pnlUpdates.isVisible())
					btnUpdates.setBackground(Color.decode("#4B4B4B"));
				lblCurrent.setText("updates");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if(!pnlUpdates.isVisible())
					btnUpdates.setBackground(Color.decode("#3E3E3E"));
				lblCurrent.setText("");
			}
		});
		btnUpdates.setToolTipText("updates");
		btnUpdates.setForeground(Color.WHITE);
		btnUpdates.setBackground(Color.decode("#3E3E3E"));
		btnUpdates.setOpaque(true);
		btnUpdates.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
		btnUpdates.setBorderPainted(true);
		btnUpdates.setFont(new Font("FontAwesome", Font.PLAIN, 30));
		btnUpdates.setBounds(0, 119, 75, 60);
		sidebar.add(btnUpdates);
		
		btnDownload = new JButton("\uF019");
		btnDownload.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				btnDownload.setBackground(Color.decode("#2980b9").darker());
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				btnDownload.setBackground(Color.decode("#4B4B4B"));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				btnSync.setBackground(Color.decode("#3E3E3E"));
				btnUpdates.setBackground(Color.decode("#3E3E3E"));
				btnDownload.setBackground(Color.decode("#2980b9"));
				btnLibrary.setBackground(Color.decode("#3E3E3E"));
				btnTerminal.setBackground(frmTerminal.isVisible() ? Color.decode("#7FB239") : Color.decode("#3E3E3E"));
				btnSettings.setBackground(Color.decode("#3E3E3E"));
				cardlayout.show(content, DOWNLOAD_PANEL);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if(!pnlDownload.isVisible())
					btnDownload.setBackground(Color.decode("#4B4B4B"));
				lblCurrent.setText("download");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if(!pnlDownload.isVisible())
					btnDownload.setBackground(Color.decode("#3E3E3E"));
				lblCurrent.setText("");
			}
		});
		btnDownload.setToolTipText("download");
		btnDownload.setForeground(Color.WHITE);
		btnDownload.setBackground(Color.decode("#3E3E3E"));
		btnDownload.setOpaque(true);
		btnDownload.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
		btnDownload.setBorderPainted(true);
		btnDownload.setFont(new Font("FontAwesome", Font.PLAIN, 30));
		btnDownload.setBounds(0, 178, 75, 60);
		sidebar.add(btnDownload);
		
		btnLibrary = new JButton("\uF187");
		btnLibrary.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				btnLibrary.setBackground(Color.decode("#2980b9").darker());
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				btnLibrary.setBackground(Color.decode("#4B4B4B"));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				btnSync.setBackground(Color.decode("#3E3E3E"));
				btnUpdates.setBackground(Color.decode("#3E3E3E"));
				btnDownload.setBackground(Color.decode("#3E3E3E"));
				btnLibrary.setBackground(Color.decode("#2980b9"));
				btnTerminal.setBackground(frmTerminal.isVisible() ? Color.decode("#7FB239") : Color.decode("#3E3E3E"));
				btnSettings.setBackground(Color.decode("#3E3E3E"));
				cardlayout.show(content, LIBRARY_PANEL);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if(!pnlLibrary.isVisible())
					btnLibrary.setBackground(Color.decode("#4B4B4B"));
				lblCurrent.setText("library");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if(!pnlLibrary.isVisible())
					btnLibrary.setBackground(Color.decode("#3E3E3E"));
				lblCurrent.setText("");
			}
		});
		btnLibrary.setToolTipText("library");
		btnLibrary.setForeground(Color.WHITE);
		btnLibrary.setBackground(Color.decode("#3E3E3E"));
		btnLibrary.setOpaque(true);
		btnLibrary.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
		btnLibrary.setBorderPainted(true);
		btnLibrary.setFont(new Font("FontAwesome", Font.PLAIN, 30));
		btnLibrary.setBounds(0, 237, 75, 60);
		sidebar.add(btnLibrary);
		
		btnTerminal = new JButton("\uF120");
		btnTerminal.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				btnTerminal.setBackground(Color.decode("#7FB239").darker());
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				btnTerminal.setBackground(Color.decode("#4B4B4B"));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				frmTerminal.setVisible((frmTerminal.isVisible() ? false : true));
				if(frmTerminal.isVisible())
					btnTerminal.setBackground(Color.decode("#7FB239"));
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if(!frmTerminal.isVisible())
					btnTerminal.setBackground(Color.decode("#4B4B4B"));
				lblCurrent.setText("terminal");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if(!frmTerminal.isVisible())
					btnTerminal.setBackground(Color.decode("#3E3E3E"));
				lblCurrent.setText("");
			}
		});
		btnTerminal.setToolTipText("terminal");
		btnTerminal.setForeground(Color.WHITE);
		btnTerminal.setBackground(Color.decode("#3E3E3E"));
		btnTerminal.setOpaque(true);
		btnTerminal.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
		btnTerminal.setBorderPainted(true);
		btnTerminal.setFont(new Font("FontAwesome", Font.PLAIN, 30));
		btnTerminal.setBounds(0, 296, 75, 60);
		sidebar.add(btnTerminal);
		
		btnSettings = new JButton("\uF0AD");
		btnSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				btnSettings.setBackground(Color.decode("#2980b9").darker());
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				btnSettings.setBackground(Color.decode("#4B4B4B"));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				btnSync.setBackground(Color.decode("#3E3E3E"));
				btnUpdates.setBackground(Color.decode("#3E3E3E"));
				btnDownload.setBackground(Color.decode("#3E3E3E"));
				btnLibrary.setBackground(Color.decode("#3E3E3E"));
				btnTerminal.setBackground(frmTerminal.isVisible() ? Color.decode("#7FB239") : Color.decode("#3E3E3E"));
				btnSettings.setBackground(Color.decode("#2980b9"));
				cardlayout.show(content, SETTINGS_PANEL);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				if(!pnlSettings.isVisible())
					btnSettings.setBackground(Color.decode("#4B4B4B"));
				lblCurrent.setText("settings");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if(!pnlSettings.isVisible())
					btnSettings.setBackground(Color.decode("#3E3E3E"));
				lblCurrent.setText("");
			}
		});
		btnSettings.setToolTipText("settings");
		btnSettings.setForeground(Color.WHITE);
		btnSettings.setBackground(Color.decode("#3E3E3E"));
		btnSettings.setOpaque(true);
		btnSettings.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
		btnSettings.setBorderPainted(true);
		btnSettings.setFont(new Font("FontAwesome", Font.PLAIN, 30));
		btnSettings.setBounds(0, 355, 75, 60);
		sidebar.add(btnSettings);
		
		btnAbout = new JButton("\uF129");
		btnAbout.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				btnAbout.setBackground(Color.decode("#628A2C"));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				btnAbout.setBackground(Color.decode("#7FB239"));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
//				frmAbout.setVisible(true);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lblCurrent.setText("about");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblCurrent.setText("");
			}
		});
		btnAbout.setToolTipText("about");
		btnAbout.setForeground(Color.WHITE);
		btnAbout.setBackground(Color.decode("#7FB239"));
		btnAbout.setOpaque(true);
		btnAbout.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK));
		btnAbout.setBorderPainted(true);
		btnAbout.setFont(new Font("FontAwesome", Font.PLAIN, 24));
		btnAbout.setBounds(5, 588, 30, 40);
		sidebar.add(btnAbout);
		
		btnHelp = new JButton("\uF128");
		btnHelp.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				btnHelp.setBackground(Color.decode("#007897"));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				btnHelp.setBackground(Color.decode("#009AC4"));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
//				frmHelp.setVisible(true);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lblCurrent.setText("help");
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblCurrent.setText("");
			}
		});
		btnHelp.setToolTipText("help");
		btnHelp.setForeground(Color.WHITE);
		btnHelp.setBackground(Color.decode("#009AC4"));
		btnHelp.setOpaque(true);
		btnHelp.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK));
		btnHelp.setBorderPainted(true);
		btnHelp.setFont(new Font("FontAwesome", Font.PLAIN, 24));
		btnHelp.setBounds(40, 588, 30, 40);
		sidebar.add(btnHelp);
		//** END SIDEBAR **//
		
		//** BEGIN CONTENT **//
		content = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, content, 0, SpringLayout.NORTH, frmTunesnatch.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, content, 0, SpringLayout.EAST, sidebar);
		springLayout.putConstraint(SpringLayout.SOUTH, content, 0, SpringLayout.SOUTH, frmTunesnatch.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, content, 0, SpringLayout.EAST, frmTunesnatch.getContentPane());
		frmTunesnatch.getContentPane().add(content);
		content.setBackground(Color.decode("#34495e"));
		content.setLayout(cardlayout);
		content.add(pnlDashboard, DASHBOARD_PANEL);
		content.add(pnlDownload, DOWNLOAD_PANEL);
		content.add(pnlLibrary, LIBRARY_PANEL);
		content.add(pnlSettings, SETTINGS_PANEL);
		content.add(pnlSync, SYNC_PANEL);
		content.add(pnlUpdates, UPDATES_PANEL);
		cardlayout.show(content, DASHBOARD_PANEL);
		//** END CONTENT **//
	}
	
	private void initializeSyncPanel(){
		pnlSync.setLayout(null);
		pnlSync.setBackground(new Color(248, 248, 255));
		
		JLabel lblHeader = new JLabel("Synchronize");
		lblHeader.setBounds(20, 0, 375, 50);
		lblHeader.setHorizontalAlignment(SwingConstants.LEFT);
		lblHeader.setAlignmentY(Component.TOP_ALIGNMENT);
		lblHeader.setForeground(Color.decode("#34495e"));
		lblHeader.setFont(new Font("Helvetica Neue", Font.PLAIN, 25));
		pnlSync.add(lblHeader);
	}
	
	private void initializeDashboard(){
		pnlDashboard.setBackground(Color.decode("#ecf0f1"));
	}
}
