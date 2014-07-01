package Gui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

import org.freixas.jcalendar.JCalendar;

import flightManager.DB;
import flightManager.Flight;
import flightManager.FlightManager;
import flightManager.Globals;


public class adminFlights extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JCalendar calendar_1;
	private JList<String> list_1;
	private JTextField textField_1;
	private FlightManager fm;
	private Connection conn;
	private DefaultComboBoxModel<String> airportListModel;
	private DefaultComboBoxModel<String> airportListModel2;
	private DefaultComboBoxModel<String> routeListModel;
	private DefaultComboBoxModel<String> airplaneListModel;
	private DefaultListModel<String> listModel;
	private JComboBox<String> comboBox;
	private JComboBox<String> comboBox_1;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FlightManager fm = new FlightManager();
					adminFlights frame = new adminFlights(fm);
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
	public adminFlights(FlightManager fm) {
		this.fm = fm;
		conn = DB.getConnection(Globals.SERVER_URL, Globals.SERVER_USERNAME, Globals.SERVER_PASSWORD);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setTitle("Add new flight:");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 706, 622);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Route: ");
		lblNewLabel.setBounds(20, 221, 95, 21);
		contentPane.add(lblNewLabel);
		
		airportListModel = new DefaultComboBoxModel<String>();
		for (int i = 0; i < fm.airports.size(); i++) {
			airportListModel.addElement(fm.airports.get(i).getAirportName());
		}
		
		airportListModel2 = new DefaultComboBoxModel<String>();
		for (int i = 0; i < fm.airports.size(); i++) {
			airportListModel2.addElement(fm.airports.get(i).getAirportName());
		}
		
		routeListModel = new DefaultComboBoxModel<String>();
		for (int i = 0; i < fm.routes.size(); i++) {
			routeListModel.addElement(fm.routes.get(i).toString());
		}
		
		airplaneListModel = new DefaultComboBoxModel<String>();
		for (int i = 0; i < fm.airplanes.size(); i++) {
			airplaneListModel.addElement(fm.airplanes.get(i).getModel() + " " + fm.airplanes.get(i).getBrand());
		}
		
		final JComboBox<String> routes = new JComboBox<String>(routeListModel);
		routes.setBounds(20, 241, 270, 20);
		contentPane.add(routes);
		
		final JComboBox<String> airplanes = new JComboBox<String>(airplaneListModel);
		airplanes.setBounds(310, 241, 160, 20);
		contentPane.add(airplanes);
		
		final JTextField priceField = new JTextField();
		priceField.setBounds(492, 241, 89, 20);
		priceField.setEditable(false);
		contentPane.add(priceField);
		priceField.setColumns(10);
		
		
		/*
		textCityName = new JTextField();
		textCityName.setBounds(10, 26, 250, 20);
		panelName.add(textCityName);
		textCityName.setColumns(10);
		*/
		
		JLabel lblPris = new JLabel("Price:");
		lblPris.setBounds(492, 224, 46, 14);
		contentPane.add(lblPris);
		
		
		JLabel lblNewLabel_1 = new JLabel("Time of departure:");
		lblNewLabel_1.setBounds(20, 311, 106, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblAnkomst = new JLabel("Travel time:");
		lblAnkomst.setBounds(310, 272, 114, 20);
		contentPane.add(lblAnkomst);
		
		calendar_1 = new JCalendar(3, true);
		calendar_1.setBounds(20, 336, 337, 224);
		contentPane.add(calendar_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setLocation(20, 33);
		scrollPane.setSize(462, 161);
		scrollPane.setPreferredSize(new Dimension(200, 200));
		contentPane.add(scrollPane);

		
		listModel = new DefaultListModel<String>();
		list_1 = new JList<String>(listModel);
		scrollPane.setViewportView(list_1);
	
		for (int i = 0; i < fm.flights.size(); i++) {
			listModel.addElement(fm.flights.get(i).toString());
		}
		
		list_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
		
		/*
		airportListModel = new DefaultComboBoxModel<String>();
		for (int i = 0; i < fm.airports.size(); i++) {
			airportListModel.addElement(fm.airports.get(i).getAirportName());
		}*/
		
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setBounds(311, 293, 89, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		
		JLabel lblListOfFlights = new JLabel("List of flights:");
		lblListOfFlights.setBounds(20, 11, 89, 14);
		contentPane.add(lblListOfFlights);
		
		JButton btnCreate = new JButton("Create new");
		btnCreate.setBounds(393, 492, 111, 23);
		contentPane.add(btnCreate);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(514, 492, 89, 23);
		contentPane.add(btnSave);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setBounds(492, 171, 89, 23);
		contentPane.add(btnDelete);
		
		JLabel lblAirplane = new JLabel("Airplane:");
		lblAirplane.setBounds(310, 224, 79, 14);
		contentPane.add(lblAirplane);		
		
		// LISTENER - value changed for flightList
		list_1.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				int index = list_1.getSelectedIndex();
				if (index >= 0) {
					priceField.setText(Integer.toString(fm.flights.get(index).getPrice()));
					routes.setSelectedIndex(fm.findById(fm.flights.get(index).getRoute()));
					airplanes.setSelectedIndex(fm.findById(fm.flights.get(index).getAirplane()));
					//fromAirport.setSelectedIndex(fm.find(fm.airports.get(fm.find(fm.flights.get(index).getRoute().getStart())).getCity()));
					//toAirport.setSelectedIndex(fm.find(fm.airports.get(fm.find(fm.flights.get(index).getRoute().getDestination())).getCity()));
					textField_1.setText(Integer.toString(fm.flights.get(index).getTravelTime()));
					calendar_1.setDate(fm.flights.get(index).getDeparture());
				}
			}
		});
		
		// LISTENER - btnSave pressed
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int index = list_1.getSelectedIndex();
				Flight flight = new Flight(fm.flights.get(index).getId(),
											fm.routes.get(routes.getSelectedIndex()),
											fm.airplanes.get(airplanes.getSelectedIndex()), 
											calendar_1.getDate());
				fm.flights.set(index, DB.updateFlight(conn, flight));
				listModel.set(index, flight.toString());			
			}
		});
		
		// LISTENER - btnCreate pressed
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Flight flight = new Flight(fm.routes.get(routes.getSelectedIndex()),
											fm.airplanes.get(airplanes.getSelectedIndex()), 
											calendar_1.getDate());
				fm.flights.add(DB.insertFlight(conn, flight));
				listModel.addElement(flight.toString());
			}
		});
		
		// LISTENER - btnDelete pressed
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = list_1.getSelectedIndex();
				DB.deleteFlight(conn, fm.flights.get(index).getId());
				fm.flights.remove(index);
				listModel.remove(index);
			}
		});
	}
	public void updateLists(){
		fm.updateLists();
		airportListModel.removeAllElements();
		airplaneListModel.removeAllElements();
		routeListModel.removeAllElements();
		airportListModel2.removeAllElements();
		for (int i = 0; i < fm.airports.size(); i++) {
			airportListModel.addElement(fm.airports.get(i).getAirportName());
		}
		for (int i = 0; i < fm.airports.size(); i++) {
			airportListModel2.addElement(fm.airports.get(i).getAirportName());
		}
		for (int i = 0; i < fm.routes.size(); i++) {
			routeListModel.addElement(fm.routes.get(i).toString());
		}
		for (int i = 0; i < fm.airplanes.size(); i++) {
			airplaneListModel.addElement(fm.airplanes.get(i).getModel() + " " + fm.airplanes.get(i).getBrand());
		}

		listModel.clear();
		for (int i = 0; i < fm.flights.size(); i++) {
			listModel.addElement(fm.flights.get(i).toString());
		}
	}
	public JCalendar getCalendar_1() {
		return calendar_1;
	}
	public JList<String> getList_1() {
		return list_1;
	}
	public JComboBox<String> getComboBoxRoute() {
		return comboBox;
	}
	public JComboBox<String> getComboBoxAirplane() {
		return comboBox_1;
	}
	public void setVisibility(boolean b) {
		this.setVisible(b);
	}
}
