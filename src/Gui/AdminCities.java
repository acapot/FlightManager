package Gui;

import java.sql.Connection;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import flightManager.City;
import flightManager.DB;
import flightManager.FlightManager;
import flightManager.Globals;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.Dialog.ModalExclusionType;
import java.awt.EventQueue;

public class AdminCities {

	private JFrame frame;
	private JTextField textCityName;
	private FlightManager fm;
	private DefaultListModel<String> cityListModel;
	private JList<String> listCities;
	private Connection conn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FlightManager fm = new FlightManager();
					AdminCities window = new AdminCities(fm);
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
	public AdminCities(FlightManager fm) {
		this.fm = fm;
		conn = DB.getConnection(Globals.SERVER_URL, Globals.SERVER_USERNAME, Globals.SERVER_PASSWORD);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Edit Cities");
		frame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		frame.setBounds(100, 100, 510, 314);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 482, 269);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		cityListModel = new DefaultListModel<String>();
		for (int i = 0; i < fm.cities.size(); i++) {
			cityListModel.addElement(fm.cities.get(i).getCityName());
		}
		listCities = new JList<String>(cityListModel);
		listCities.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listCities.setBounds(0, 0, 192, 269);
		panel.add(listCities);

		JPanel panelName = new JPanel();
		panelName.setBounds(202, 11, 270, 59);
		panel.add(panelName);
		panelName.setLayout(null);

		JLabel lblCityName = new JLabel("City Name");
		lblCityName.setBounds(10, 11, 106, 14);
		panelName.add(lblCityName);

		textCityName = new JTextField();
		textCityName.setBounds(10, 26, 250, 20);
		panelName.add(textCityName);
		textCityName.setColumns(10);

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
		listCities.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				int index = listCities.getSelectedIndex();
				if (index >= 0) {
					textCityName.setText(fm.cities.get(index).getCityName());
				}
			}
		});
			
		// LISTENER - btnSave pressed
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = listCities.getSelectedIndex();
				if (index >= 0) {
					City city = new City(fm.cities.get(index).getId(), textCityName.getText());
					DB.updateCity(conn, city);
					updateLists();
				}
			}
		});

		// LISTENER - btnNew pressed
		buttonNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				City city = new City(textCityName.getText());
				DB.insertCity(conn, city);
				updateLists();
			}
		});

		// LISTENER - btnDelete pressed
		buttonDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = listCities.getSelectedIndex();
				if (index >= 0) {
					DB.deleteCity(conn, fm.cities.get(index).getId());
					updateLists();
				}
			}
		});
		
		// LISTENER - btnClose pressed
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
	}
	
	public void updateLists(){
		fm.updateLists();
		cityListModel.clear();
		for (int i = 0; i < fm.cities.size(); i++) {
			cityListModel.addElement(fm.cities.get(i).getCityName());
		}
	}
	
	public void setVisibility(boolean b) {
		this.frame.setVisible(b);
	}
}
