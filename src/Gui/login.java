package Gui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JButton;
import flightManager.DB;
import flightManager.Globals;
import flightManager.User;

import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.border.EtchedBorder;
import java.awt.Panel;
import java.awt.Color;
import java.sql.Connection;

public class login {

	private JFrame frame;
	private JTextField boxUsername;
	private JTextField boxPassword;
	private mainWindow window;
	private JTextField boxCreateUsername;
	private JTextField boxCreatePassword;
	private JTextField boxCreateEmail;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					login window = new login(new mainWindow());
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public login(mainWindow window) {
		this.window = window;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 275, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel.setBounds(17, 52, 227, 102);
		frame.getContentPane().add(panel);

		JLabel lblUsername = new JLabel("Username");
		panel.add(lblUsername);

		boxUsername = new JTextField();
		boxUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					boxPassword.requestFocusInWindow();

				}
			}
		});

		panel.add(boxUsername);
		boxUsername.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		panel.add(lblPassword);

		boxPassword = new JPasswordField();
		boxPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					checkUser();
				}
			}
		});

		panel.add(boxPassword);
		boxPassword.setColumns(10);

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				checkUser();
			}
		});
		panel.add(btnOk);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();

			}
		});
		panel.add(btnCancel);

		final JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_1.setBounds(17, 243, 227, 120);
		frame.getContentPane().add(panel_1);

		JLabel lblCreateUsername = new JLabel("Username");
		panel_1.add(lblCreateUsername);

		boxCreateUsername = new JTextField();
		panel_1.add(boxCreateUsername);
		boxCreateUsername.setColumns(10);

		boxCreateUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					boxCreatePassword.requestFocusInWindow();

				}
			}
		});

		final JButton btnCreateOk = new JButton("OK");
		btnCreateOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newLogin();
			}
		});
		
		JLabel lblCreatePassword = new JLabel("Password");
		panel_1.add(lblCreatePassword);
		
		boxCreatePassword = new JTextField();
		panel_1.add(boxCreatePassword);
		boxCreatePassword.setColumns(10);
		
		boxCreatePassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					newLogin();
				}
			}
		});
		panel_1.add(boxCreatePassword);
		
		JLabel lblEmail = new JLabel("Email Add.");
		panel_1.add(lblEmail);
		
		boxCreateEmail = new JTextField();
		boxCreateEmail.setColumns(10);
		panel_1.add(boxCreateEmail);
		panel_1.add(btnCreateOk);

		final JPanel panel_3 = new JPanel();
		panel_3.setBackground(Color.LIGHT_GRAY);
		panel_3.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_3.setBounds(17, 214, 227, 30);
		frame.getContentPane().add(panel_3);
		panel_3.setVisible(false);

		JButton btnCreateCancel = new JButton("Cancel");
		btnCreateCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel_1.setVisible(false);
				panel_3.setVisible(false);
				frame.setBounds(100, 100, 276, 250);
			}
		});
		panel_1.add(btnCreateCancel);
		panel_1.setVisible(false);

		JLabel label_1 = new JLabel("");
		panel_1.add(label_1);

		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.LIGHT_GRAY);
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_2.setBounds(17, 23, 227, 30);
		frame.getContentPane().add(panel_2);

		JLabel lblExistingUser = new JLabel("Existing User");
		panel_2.add(lblExistingUser);

		JLabel label = new JLabel("Create New User");
		panel_3.add(label);

		Panel panel_4 = new Panel();
		panel_4.setBounds(17, 160, 222, 30);
		frame.getContentPane().add(panel_4);

		JButton btnNoAccountCreate = new JButton("No account? Create one.");
		btnNoAccountCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setSize(276, 430);
				panel_1.setVisible(true);
				panel_3.setVisible(true);

			}
		});
		panel_4.add(btnNoAccountCreate);

	}

	public void setVisibility(boolean b) {
		this.frame.setVisible(b);
	}

	@SuppressWarnings("static-access")
	public void checkUser() {
		String username = boxUsername.getText();
		String password = boxPassword.getText();
		Connection conn = DB.getConnection(Globals.SERVER_URL, Globals.SERVER_USERNAME, Globals.SERVER_PASSWORD);
		int userId = DB.checkUserLogin(conn, username, password);
		if ( userId != 0) {
			window.fm.activeUser = DB.getUser(conn, userId);
			window.setVisibility(true);
			frame.dispose();

		} else {
			JOptionPane.showMessageDialog(frame,
					"Username and password not matching", "Error",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	public void newLogin() {
		String username = boxCreateUsername.getText();
		String password = boxCreatePassword.getText();
		int permission = 2;
		String email = boxCreateEmail.getText();
		
		if (username.length() > 0 || password.length() > 0) {
					User user = new User(username, password, permission, email);
					Connection conn = DB.getConnection("jdbc:mysql://localhost:3306/flightmanager", "root", "");
			if (DB.insertUser(conn, user) != null) {
				JOptionPane.showMessageDialog(frame,
						"User successfuly created.", "Success",
						JOptionPane.INFORMATION_MESSAGE);

			} else {
				JOptionPane.showMessageDialog(frame,
						"User was not created, please try again", "Error",
						JOptionPane.WARNING_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(frame,
					"Username or password wrongly entered. Please try again.", "Error",
					JOptionPane.WARNING_MESSAGE);
		}

		
	}
}