package Gui;

import java.sql.Connection;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import flightManager.Airplane;
import flightManager.DB;
import flightManager.FlightManager;
import flightManager.Globals;

import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;

public class AdminAirplanes {

	private JFrame frmEditAirplanes;
	private FlightManager fm;
	private Connection conn;
	private JTextField txtFirstClass;
	private JTextField txtSecondClass;
	private JTextField txtThirdClass;
	private JTextField txtManufacturer;
	private JTextField txtModel;
	private JTextField txtVelocity;
	private JList<String> listAirplanes;
	private DefaultListModel<String> planeListModel;
	private JTextField txtKilometerCost;
	private JTextField txtId;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FlightManager fm = new FlightManager();
					AdminAirplanes window = new AdminAirplanes(fm);
					window.frmEditAirplanes.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AdminAirplanes(FlightManager fm) {
		this.fm = fm;
		conn = DB.getConnection(Globals.SERVER_URL, Globals.SERVER_USERNAME, Globals.SERVER_PASSWORD);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEditAirplanes = new JFrame();
		frmEditAirplanes.setResizable(false);
		frmEditAirplanes.setTitle("Edit Airplanes");
		frmEditAirplanes.setBounds(100, 100, 474, 439);
		frmEditAirplanes.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmEditAirplanes.getContentPane().setLayout(null);
		
		JScrollPane panelList = new JScrollPane();
		panelList.setBounds(10, 11, 170, 253);
		frmEditAirplanes.getContentPane().add(panelList);
		
		planeListModel = new DefaultListModel<String>();
		for (int i = 0; i < fm.airplanes.size(); i++) {
			planeListModel.addElement(fm.airplanes.get(i).toString());
		}
		
		listAirplanes = new JList<String>(planeListModel);
		listAirplanes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panelList.setViewportView(listAirplanes);
		
		JPanel panelButtons = new JPanel();
		panelButtons.setBounds(247, 302, 156, 82);
		frmEditAirplanes.getContentPane().add(panelButtons);
		panelButtons.setLayout(null);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(3, 56, 150, 23);
		panelButtons.add(btnDelete);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(3, 28, 150, 23);
		panelButtons.add(btnUpdate);
		
		JButton btnSave = new JButton("Create New");
		btnSave.setBounds(3, 0, 150, 23);
		panelButtons.add(btnSave);
		
		JPanel panelInfo = new JPanel();
		panelInfo.setBounds(190, 11, 270, 211);
		frmEditAirplanes.getContentPane().add(panelInfo);
		panelInfo.setLayout(null);
		
		JPanel panelCapacity = new JPanel();
		panelCapacity.setBounds(140, 5, 120, 187);
		panelInfo.add(panelCapacity);
		panelCapacity.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblFirstClass = new JLabel("First Class");
		panelCapacity.add(lblFirstClass);
		
		txtFirstClass = new JTextField();
		panelCapacity.add(txtFirstClass);
		txtFirstClass.setColumns(10);
		
		JLabel lblSecondClass = new JLabel("Second Class");
		panelCapacity.add(lblSecondClass);
		
		txtSecondClass = new JTextField();
		txtSecondClass.setColumns(10);
		panelCapacity.add(txtSecondClass);
		
		JLabel lblThirdClass = new JLabel("Third Class");
		panelCapacity.add(lblThirdClass);
		
		txtThirdClass = new JTextField();
		txtThirdClass.setColumns(10);
		panelCapacity.add(txtThirdClass);
		
		JLabel lblKilometerCostPassenger = new JLabel("Km Cost/Passenger");
		panelCapacity.add(lblKilometerCostPassenger);
		
		txtKilometerCost = new JTextField();
		panelCapacity.add(txtKilometerCost);
		txtKilometerCost.setColumns(10);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 5, 120, 187);
		panelInfo.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblId = new JLabel("ID: ");
		panel.add(lblId);
		
		txtId = new JTextField();
		txtId.setEditable(false);
		panel.add(txtId);
		txtId.setColumns(10);
		
		JLabel lblManufacturer = new JLabel("Manufacturer");
		panel.add(lblManufacturer);
		
		txtManufacturer = new JTextField();
		panel.add(txtManufacturer);
		txtManufacturer.setColumns(10);
		
		JLabel lblModel = new JLabel("Model");
		panel.add(lblModel);
		
		txtModel = new JTextField();
		panel.add(txtModel);
		txtModel.setColumns(10);
		
		JLabel lblVelocity = new JLabel("Velocity");
		panel.add(lblVelocity);
		
		txtVelocity = new JTextField();
		panel.add(txtVelocity);
		txtVelocity.setColumns(10);
		
		// LISTENER - listAirplanes - valueChanged
		listAirplanes.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				int index = listAirplanes.getSelectedIndex();
				if (index >= 0) {
					txtManufacturer.setText(fm.airplanes.get(index).getBrand());
					txtModel.setText(fm.airplanes.get(index).getModel());
					txtVelocity.setText(Integer.toString(fm.airplanes.get(index).getVelocity()));
					txtFirstClass.setText(Integer.toString(fm.airplanes.get(index).getfCapacity()));
					txtSecondClass.setText(Integer.toString(fm.airplanes.get(index).getsCapacity()));
					txtThirdClass.setText(Integer.toString(fm.airplanes.get(index).gettCapacity()));
					txtId.setText(Integer.toString(fm.airplanes.get(index).getId()));
					txtKilometerCost.setText(Integer.toString(fm.airplanes.get(index).getKilometerCost()));
					
				}
			}
		});
		
		// LISTENER - btnSave pressed
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Airplane airplane = new Airplane(txtManufacturer.getText(),
													txtModel.getText(),
													Integer.parseInt(txtFirstClass.getText()),
													Integer.parseInt(txtSecondClass.getText()),
													Integer.parseInt(txtThirdClass.getText()),
													Integer.parseInt(txtVelocity.getText()),
													Integer.parseInt(txtKilometerCost.getText()));
													
				if (fm.findAirplane(airplane) != -1) {
					JOptionPane.showMessageDialog(frmEditAirplanes,
							"Airplane already exists", "Error",
							JOptionPane.WARNING_MESSAGE);
				} else {
					DB.insertAirplane(conn, airplane);
					updateLists();
				}
			}
		});
		// LISTENER - btnUpdate pressed
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Airplane airplane = new Airplane(Integer.parseInt(txtId.getText()),
													txtManufacturer.getText(),
													txtModel.getText(),
													Integer.parseInt(txtFirstClass.getText()),
													Integer.parseInt(txtSecondClass.getText()),
													Integer.parseInt(txtThirdClass.getText()),
													Integer.parseInt(txtVelocity.getText()),
													Integer.parseInt(txtKilometerCost.getText()));
													
				if (fm.findAirplane(airplane) != -1) {
					JOptionPane.showMessageDialog(frmEditAirplanes,
							"Airplane already exists", "Error",
							JOptionPane.WARNING_MESSAGE);
				} else {
					DB.updateAirplane(conn, airplane);
					updateLists();
				}
			}
		});
		// LISTENER - btnDelete pressed
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = listAirplanes.getSelectedIndex();
				if (index >= 0) {
					DB.deleteAirplane(conn, fm.airplanes.get(index).getId());
					updateLists();
				}
			}
		});

	}
	
	public void updateLists(){
		fm.updateLists();
		planeListModel.clear();
		for (int i = 0; i < fm.airplanes.size(); i++) {
			planeListModel.addElement(fm.airplanes.get(i).toString());
		}
	}
	
	public void setVisibility(boolean b) {
		this.frmEditAirplanes.setVisible(b);
	}
	public JList<String> getListAirplanes() {
		return listAirplanes;
	}
}
