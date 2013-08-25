package io.phalanx.Presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public class TerminalForm extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9196060371770762092L;
	private JPanel contentPane;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TerminalForm frame = new TerminalForm();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TerminalForm() {
		setTitle("Terminal");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 570, 370);
		contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder());
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
	    PipedInputStream inPipe = new PipedInputStream();
	    PipedInputStream outPipe = new PipedInputStream();

	    System.setIn(inPipe);
	    try {
			System.setOut(new PrintStream(new PipedOutputStream(outPipe), true));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    PrintWriter inWriter = null;
	    try {
			inWriter = new PrintWriter(new PipedOutputStream(inPipe), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		final JTextArea txtIO = console(outPipe, inWriter);
		txtIO.setFont(new Font("Andale Mono", Font.PLAIN, 12));
		txtIO.setCaretColor(new Color(50, 205, 50));
		txtIO.getCaret().setVisible(true);
		txtIO.getCaret().setSelectionVisible(true);
		txtIO.setLineWrap(true);
		txtIO.setForeground(new Color(50, 205, 50));
		txtIO.setBackground(new Color(0, 0, 0));
		txtIO.setBorder(BorderFactory.createEmptyBorder());
		contentPane.add(txtIO);
	}
	
	public static JTextArea console(final InputStream out, final PrintWriter in) {
	    final JTextArea area = new JTextArea();

	    // handle "System.out"
	    new SwingWorker<Void, String>() {
	        @Override protected Void doInBackground() throws Exception {
				Scanner s = new Scanner(out);
	            while (s.hasNextLine()) publish(s.nextLine() + "\n");
	            s.close();
	            return null;
	        }
	        @Override protected void process(List<String> chunks) {
	            for (String line : chunks) area.append(line);
	        }
	    }.execute();

	    // handle "System.in"
	    area.addKeyListener(new KeyAdapter() {
	        private StringBuffer line = new StringBuffer();
	        @Override public void keyTyped(KeyEvent e) {
	            char c = e.getKeyChar();
	            if (c == KeyEvent.VK_ENTER) {
	                in.println(line + "\n");
	                line.setLength(0); 
	            } else if (c == KeyEvent.VK_BACK_SPACE) { 
	                line.setLength(line.length() - 1); 
	            } else if (!Character.isISOControl(c)) {
	                line.append(e.getKeyChar());
	            }
	        }
	    });

	    return area;
	}

}
