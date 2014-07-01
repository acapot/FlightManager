package Gui;

import java.sql.Connection;

import javax.swing.JFrame;

import flightManager.DB;
import flightManager.FlightManager;
import flightManager.Globals;
import flightManager.Route;

import javax.swing.JPanel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JTextField;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class AdminRoutes {

	private JFrame frmEditRoutes;
	private FlightManager fm;
	private Connection conn;
	private JTextField txtDistance;
	private DefaultComboBoxModel<String> startListModel;
	private DefaultComboBoxModel<String> destinationListModel;
	private DefaultListModel<String> routeListModel;
	private JList<String> listRoutes;
	private JComboBox<String> startCombo;
	private JComboBox<String> destinationCombo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FlightManager fm = new FlightManager();
					AdminRoutes window = new AdminRoutes(fm);
					window.frmEditRoutes.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AdminRoutes(FlightManager fm) {
		this.fm = fm;
		
		conn = DB.getConnection(Globals.SERVER_URL, Globals.SERVER_USERNAME, Globals.SERVER_PASSWORD);
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEditRoutes = new JFrame();
		frmEditRoutes.setResizable(false);
		frmEditRoutes.setTitle("Edit Routes");
		frmEditRoutes.setBounds(100, 100, 384, 435);
		frmEditRoutes.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmEditRoutes.getContentPane().setLayout(null);

		routeListModel = new DefaultListModel<String>();
		for (int i = 0; i < fm.routes.size(); i++) {
			routeListModel.addElement(fm.routes.get(i).toString());
		}

		startListModel = new DefaultComboBoxModel<String>();
		for (int i = 0; i < fm.airports.size(); i++) {
			startListModel.addElement(fm.airports.get(i).getAirportName());
		}
		
		destinationListModel = new DefaultComboBoxModel<String>();
		for (int i = 0; i < fm.airports.size(); i++) {
			destinationListModel.addElement(fm.airports.get(i).getAirportName());
		}

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 356, 166);
		frmEditRoutes.getContentPane().add(scrollPane);
		
		listRoutes = new JList<String>(routeListModel);
		scrollPane.setViewportView(listRoutes);
		
		JPanel panelInfo = new JPanel();
		panelInfo.setBounds(10, 188, 356, 117);
		frmEditRoutes.getContentPane().add(panelInfo);
		
		JLabel lblFrom = new JLabel("From Airport:");
		lblFrom.setBounds(3, 0, 350, 12);
		
		startCombo = new JComboBox<String>();
		startCombo.setBounds(3, 14, 350, 22);
		startCombo.setModel(startListModel);
		
		JLabel lblTo = new JLabel("To Airport:");
		lblTo.setBounds(3, 40, 350, 12);
		
		destinationCombo = new JComboBox<String>();
		destinationCombo.setBounds(3, 54, 350, 22);
		destinationCombo.setModel(destinationListModel);
		
		panelInfo.setLayout(null);
		panelInfo.add(lblFrom);
		panelInfo.add(startCombo);
		panelInfo.add(lblTo);
		panelInfo.add(destinationCombo);
		
		JLabel lblDistance = new JLabel("Distance (km)");
		lblDistance.setBounds(3, 80, 350, 12);
		panelInfo.add(lblDistance);
		
		txtDistance = new JTextField();
		txtDistance.setBounds(3, 94, 350, 20);
		panelInfo.add(txtDistance);
		txtDistance.setColumns(10);
		

		JPanel panelButtons = new JPanel();
		frmEditRoutes.getContentPane().add(panelButtons);
		panelButtons.setBounds(10, 316, 356, 79);
		panelButtons.setLayout(null);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(3, 56, 350, 23);
		panelButtons.add(btnDelete);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(3, 28, 350, 23);
		panelButtons.add(btnUpdate);
		
		JButton btnSave = new JButton("Create New");
		btnSave.setBounds(3, 0, 350, 23);
		panelButtons.add(btnSave);
		
		// LISTENER - List valueChanged
		listRoutes.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				int index = listRoutes.getSelectedIndex();
				if (index >= 0) {
					startCombo.setSelectedIndex(fm.findById(fm.routes.get(index).getStart()));
					destinationCombo.setSelectedIndex(fm.findById(fm.routes.get(index).getDestination()));
					txtDistance.setText(String.valueOf(fm.routes.get(index).getDistance() / 1000));
				}
			}
		});
		
		// LISTENER - Save button
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int distance = 0;
				try {
					distance = Integer.parseInt(txtDistance.getText()) * 1000;
				} catch (Exception e) {
					// TODO: handle exception
				}
				Route route = new Route(fm.airports.get(startCombo.getSelectedIndex()), fm.airports.get(destinationCombo.getSelectedIndex()), distance);
				if(fm.findRoute(route) != -1){
					JOptionPane.showMessageDialog(frmEditRoutes,
							"Route already exists", "Error",
							JOptionPane.WARNING_MESSAGE);
				} else if(startCombo.getSelectedIndex() == destinationCombo.getSelectedIndex()) {
					JOptionPane.showMessageDialog(frmEditRoutes,
							"Start and destination cannot be identical", "Error",
							JOptionPane.WARNING_MESSAGE);
				} else {
					DB.insertRoute(conn, route);
					updateLists();
				}
			}
		});
		// LISTENER - Update button
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = listRoutes.getSelectedIndex();
				if (index >= 0) {
					int distance = 0;
					try {
						distance = Integer.parseInt(txtDistance.getText()) * 1000;
					} catch (Exception e) {
						// TODO: handle exception
					}
					Route route = new Route(fm.routes.get(index).getId(),
											fm.airports.get(startCombo.getSelectedIndex()),
											fm.airports.get(destinationCombo.getSelectedIndex()),
											distance);
					if(!(fm.findRoute(route) == -1 || fm.findRoute(route) == index)){
						JOptionPane.showMessageDialog(frmEditRoutes,
								"Route already exists", "Error",
								JOptionPane.WARNING_MESSAGE);
					} else if(startCombo.getSelectedIndex() == destinationCombo.getSelectedIndex()) {
						JOptionPane.showMessageDialog(frmEditRoutes,
								"Start and destination cannot be identical", "Error",
								JOptionPane.WARNING_MESSAGE);
					} else {
						DB.updateRoute(conn, route);
						updateLists();
					}
				}
			}
		});
		// LISTENER - Delete button
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = listRoutes.getSelectedIndex();
				if (index >= 0) {
					DB.deleteRoute(conn, fm.routes.get(index).getId());
					updateLists();
				}
			}
		});
	}

	public void updateLists(){
		fm.updateLists();
		routeListModel.clear();
		startListModel.removeAllElements();
		destinationListModel.removeAllElements();
		for (int i = 0; i < fm.routes.size(); i++) {
			routeListModel.addElement(fm.routes.get(i).toString());
		}
		for (int i = 0; i < fm.airports.size(); i++) {
			startListModel.addElement(fm.airports.get(i).getAirportName());
		}
		for (int i = 0; i < fm.airports.size(); i++) {
			destinationListModel.addElement(fm.airports.get(i).getAirportName());
		}
	}
	
	public void setVisibility(boolean b) {
		this.frmEditRoutes.setVisible(b);
	}
}
