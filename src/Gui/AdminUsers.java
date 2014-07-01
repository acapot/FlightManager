package Gui;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import flightManager.DB;
import flightManager.EmailConfirmation;
import flightManager.FlightManager;
import flightManager.Globals;
import flightManager.User;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;

public class AdminUsers {

	private JFrame frmEditUsers;
	private JTextField txtUsername;
	private JTextField txtPassword;
	private DefaultListModel<String> userListModel;
	private JList<String> listUsers;
	private FlightManager fm;
	private Connection conn;

	EmailConfirmation EC = new EmailConfirmation();
	private JTextField txtEmail;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FlightManager fm = new FlightManager();
					AdminUsers window = new AdminUsers(fm);
					window.frmEditUsers.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AdminUsers(FlightManager fm) {
		this.fm = fm;
		conn = DB.getConnection(Globals.SERVER_URL, Globals.SERVER_USERNAME,
				Globals.SERVER_PASSWORD);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEditUsers = new JFrame();
		frmEditUsers.setTitle("Edit Users");
		frmEditUsers.setResizable(false);
		frmEditUsers.setBounds(100, 100, 185, 461);
		frmEditUsers.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmEditUsers.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 180, 421);
		frmEditUsers.getContentPane().add(panel);
		panel.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 297, 160, 113);
		panel.add(panel_1);
		panel_1.setLayout(null);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(10, 79, 140, 23);
		panel_1.add(btnDelete);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(10, 45, 140, 23);
		panel_1.add(btnUpdate);

		JButton btnSave = new JButton("Create New");
		btnSave.setBounds(10, 11, 140, 23);
		panel_1.add(btnSave);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 176, 160, 110);
		panel.add(panel_2);
		panel_2.setLayout(null);

		txtUsername = new JTextField();
		txtUsername.setBounds(10, 14, 140, 20);
		panel_2.add(txtUsername);
		txtUsername.setText("Username");
		txtUsername.setColumns(10);

		txtPassword = new JTextField();
		txtPassword.setBounds(10, 45, 104, 20);
		panel_2.add(txtPassword);
		txtPassword.setText("Password");
		txtPassword.setColumns(10);

		final JSpinner spinnerRank = new JSpinner();
		spinnerRank.setBounds(124, 46, 26, 18);
		panel_2.add(spinnerRank);

		txtEmail = new JTextField();
		txtEmail.setText("Email");
		txtEmail.setBounds(10, 76, 140, 20);
		panel_2.add(txtEmail);
		txtEmail.setColumns(10);

		JScrollPane panel_3 = new JScrollPane();
		panel_3.setBounds(10, 11, 160, 154);
		panel.add(panel_3);

		userListModel = new DefaultListModel<String>();
		for (int i = 0; i < fm.users.size(); i++) {
			userListModel.addElement(fm.users.get(i).getUserName());
		}
		listUsers = new JList<String>(userListModel);
		listUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listUsers.setBounds(0, 0, 160, 154);
		panel_3.setViewportView(listUsers);

		// List Selection
		listUsers.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				int index = listUsers.getSelectedIndex();
				if (index >= 0) {
					txtUsername.setText(fm.users.get(index).getUserName());
					txtPassword.setText(fm.users.get(index).getPassword());
					spinnerRank.setValue(fm.users.get(index).getUserRank());
					txtEmail.setText(fm.users.get(index).getEmail());
				}
			}
		});

		// Create New Button
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				User user = new User(txtUsername.getText(), txtPassword
						.getText(), Integer.parseInt(spinnerRank.getValue().toString()), txtEmail
						.getText());
				if (!(txtUsername.getText() != ""
						&& txtPassword.getText() != ""
						&& fm.findUsername(txtUsername.getText()) != -1 && txtEmail
						.getText() != "")) {
					if (DB.insertUser(conn, user) != null) {
						JOptionPane.showMessageDialog(frmEditUsers,
								"User successfuly created.", "Success",
								JOptionPane.INFORMATION_MESSAGE);

					} else {
						JOptionPane.showMessageDialog(frmEditUsers,
								"User not created! Please try again.", "Error",
								JOptionPane.WARNING_MESSAGE);

					}
					updateLists();
				}
			}
		});

		// Update Button
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = listUsers.getSelectedIndex();
				Object oSpinner =  spinnerRank.getValue();
				User user = new User(fm.users.get(index).getId(), txtUsername.getText(), txtPassword.getText(), Integer.parseInt(oSpinner.toString()), txtEmail.getText());
				DB.updateUser(conn, user);
				updateLists();
				if (DB.updateUser(conn, user) != null) {
					JOptionPane.showMessageDialog(frmEditUsers,
							"User successfuly updated.", "Success",
							JOptionPane.INFORMATION_MESSAGE);

				} else {
					JOptionPane.showMessageDialog(frmEditUsers,
							"User not updated. Please try again.", "Error",
							JOptionPane.WARNING_MESSAGE);

				}
			}
		});

		// Delete Button
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = listUsers.getSelectedIndex();
				if (index >= 0) {
					DB.deleteUser(conn, fm.users.get(index).getId());
					updateLists();
				}
			}
		});
	}

	public void updateLists() {
		fm.updateLists();
		userListModel.clear();
		for (int i = 0; i < fm.users.size(); i++) {
			userListModel.addElement(fm.users.get(i).getUserName());
		}
	}

	public void setVisibility(boolean b) {
		this.frmEditUsers.setVisible(b);
	}
}
