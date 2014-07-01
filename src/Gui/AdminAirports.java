package Gui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;

import java.sql.Connection;

import flightManager.Airport;
import flightManager.DB;
import flightManager.FlightManager;
import flightManager.Globals;

import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class AdminAirports {

	private JFrame frame;
	private JTextField textAirportName;
	private FlightManager fm;
	private DefaultListModel<String> airportListModel;
	private DefaultComboBoxModel<String> cityListModel;
	private JList<String> airportList;
	private JComboBox<String> comboCities;
	private Connection conn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FlightManager fm = new FlightManager();
					AdminAirports window = new AdminAirports(fm);
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
	public AdminAirports(FlightManager fm) {
		this.fm = fm;		
		conn = DB.getConnection(Globals.SERVER_URL, Globals.SERVER_USERNAME, Globals.SERVER_PASSWORD);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Edit Airports");
		frame.setBounds(100, 100, 510, 315);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 482, 269);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		airportListModel = new DefaultListModel<String>();
		for (int i = 0; i < fm.airports.size(); i++) {
			airportListModel.addElement(fm.airports.get(i).getAirportName());
		}
		airportList = new JList<String>(airportListModel);
		airportList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		airportList.setBounds(0, 0, 192, 269);
		panel.add(airportList);

		JPanel panelName = new JPanel();
		panelName.setBounds(202, 11, 270, 59);
		panel.add(panelName);
		panelName.setLayout(null);

		JLabel lblAirportName = new JLabel("Airport Name");
		lblAirportName.setBounds(10, 11, 106, 14);
		panelName.add(lblAirportName);

		textAirportName = new JTextField();
		textAirportName.setBounds(10, 26, 250, 20);
		panelName.add(textAirportName);
		textAirportName.setColumns(10);

		JPanel panelCity = new JPanel();
		panelCity.setLayout(null);
		panelCity.setBounds(202, 81, 270, 59);
		panel.add(panelCity);

		JLabel lblCity = new JLabel("City");
		lblCity.setBounds(10, 11, 106, 14);
		panelCity.add(lblCity);

		cityListModel = new DefaultComboBoxModel<String>();
		for (int i = 0; i < fm.cities.size(); i++) {
			cityListModel.addElement(fm.cities.get(i).getCityName());
		}
		comboCities = new JComboBox<String>();
		comboCities.setBounds(10, 26, 250, 22);
		comboCities.setModel(cityListModel);
		panelCity.add(comboCities);

		JPanel panelButtons = new JPanel();
		panelButtons.setBounds(194, 151, 288, 118);
		panel.add(panelButtons);
		panelButtons.setLayout(null);

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(4, 11, 88, 23);
		panelButtons.add(btnSave);

		JButton buttonNew = new JButton("Create");
		buttonNew.setBounds(102, 11, 88, 23);
		panelButtons.add(buttonNew);

		JButton buttonDelete = new JButton("Delete");
		buttonDelete.setBounds(200, 11, 88, 23);
		panelButtons.add(buttonDelete);

		JButton btnClose = new JButton("Close");
		btnClose.setBounds(200, 84, 88, 23);
		panelButtons.add(btnClose);

		// LISTENER - value changed for airportList
		airportList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				int index = airportList.getSelectedIndex();
				if (index >= 0) {
					textAirportName.setText(fm.airports.get(index).getAirportName());
					comboCities.setSelectedIndex(fm.findById(fm.airports.get(index).getCity()));
				}
			}
		});
			
		// LISTENER - btnSave pressed
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = airportList.getSelectedIndex();
				Airport airport = new Airport(fm.airports.get(index).getId(),
						textAirportName.getText(), fm.cities.get(comboCities.getSelectedIndex()));
				DB.updateAirport(conn, airport);
				updateLists();
				
			}
		});

		// LISTENER - btnNew pressed
		buttonNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Airport airport = new Airport(textAirportName.getText(), fm.cities.get(comboCities.getSelectedIndex()));
				DB.insertAirport(conn, airport);
				updateLists();
			}
		});

		// LISTENER - btnDelete pressed
		buttonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = airportList.getSelectedIndex();
				DB.deleteAirport(conn, fm.airports.get(index).getId());
				updateLists();
			}
		});

		// LISTENER - btnClose pressed
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
	}

	public void updateLists(){
		fm.updateLists();
		cityListModel.removeAllElements();
		for (int i = 0; i < fm.cities.size(); i++) {
			cityListModel.addElement(fm.cities.get(i).getCityName());
		}
		airportListModel.clear();
		for (int i = 0; i < fm.airports.size(); i++) {
			airportListModel.addElement(fm.airports.get(i).getAirportName());
		}
	}
	
	public void setVisibility(boolean b) {
		this.frame.setVisible(b);
	}
	
	public JList<String> getAirportList() {
		return airportList;
	}

	public JComboBox<String> getComboCities() {
		return comboCities;
	}
}
