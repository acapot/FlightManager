package Gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

import flightManager.BookedFlight;
import flightManager.DB;
import flightManager.EmailThread;
import flightManager.Flight;
import flightManager.FlightManager;
import flightManager.Route;
import flightManager.User;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;

public class AdminBookedFlights {

	private JFrame frame;
	private FlightManager fm;
	private DefaultComboBoxModel<User> userComboModel;
	private DefaultComboBoxModel<Route> routeComboModel;
	private List<AdminBookedFlightPanel> listPanels;
	private List<BookedFlight> tickets;
	private JPanel panelList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminBookedFlights window = new AdminBookedFlights(new FlightManager());
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
	public AdminBookedFlights(FlightManager fm) {
		this.fm = fm;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setPreferredSize(new Dimension(1200, 400));
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		// Elements
		JPanel panelSearch = new JPanel();
		JPanel panelActions = new JPanel();
		JPanel panelScroll = new JPanel();
		panelList = new JPanel();
		listPanels = new ArrayList<AdminBookedFlightPanel>();
		tickets = new ArrayList<BookedFlight>();
		JScrollPane scrollPane = new JScrollPane();
		userComboModel = new DefaultComboBoxModel<User>();
		routeComboModel = new DefaultComboBoxModel<Route>();
		final JComboBox<Route> comboBoxRoute = new JComboBox<Route>(routeComboModel);
		final JComboBox<User> comboBoxUser = new JComboBox<User>(userComboModel);
		JButton buttonUpdate = new JButton("Update");
		JButton buttonSearch = new JButton("Search");
		
		// Add to combo boxes
		routeComboModel.addElement(null);
		userComboModel.addElement(null);
		
		for (int i = 0; i < fm.routes.size(); i++) {
			routeComboModel.addElement(fm.routes.get(i));
		}
		for (int i = 0; i < fm.users.size(); i++) {
			userComboModel.addElement(fm.users.get(i));
		}
		
		// Add elements to panels
		panelSearch.add(buttonSearch);
		panelSearch.add(comboBoxRoute);
		panelSearch.add(comboBoxUser);
		
		panelActions.add(buttonUpdate);

		panelList.setLayout(new VerticalFlowLayout(5, VerticalFlowLayout.BOTH));
		panelList.setBackground(Color.WHITE);
		
		panelScroll.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		frame.getContentPane().add(panelSearch, BorderLayout.NORTH);
		frame.getContentPane().add(panelActions, BorderLayout.SOUTH);
		frame.getContentPane().add(panelList, BorderLayout.CENTER);
		frame.getContentPane().add(panelScroll, BorderLayout.EAST);
		frame.getContentPane().add(scrollPane);
		
		scrollPane.setViewportView(panelList);
		scrollPane.setBorder(new EmptyBorder(5,5,5,5));
		
		frame.pack();
		
		// Listeners
		// Search Button
		buttonSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tickets.clear();

				if (comboBoxRoute.getSelectedItem() == null && comboBoxUser.getSelectedItem() == null) {
					tickets = DB.getBookedFlights(DB.getConnection());
				} else if (comboBoxRoute.getSelectedItem() == null && !(comboBoxUser.getSelectedItem() == null)) {
					tickets = DB.getBookedFlightsByOwner(DB.getConnection(), (User)comboBoxUser.getSelectedItem());
				} else if (!(comboBoxRoute.getSelectedItem() == null) && comboBoxUser.getSelectedItem() == null) {
					List<Flight> flights = DB.getFlightsByRoute(DB.getConnection(), 
							((Route)comboBoxRoute.getSelectedItem()).getId(), true);
					for (int i = 0; i < flights.size(); i++) {
						tickets = DB.getBookedFlightsByFlight(DB.getConnection(), flights.get(i));
					}
				}
				updateSearchList(tickets);
			}
		});
		// Update Button
		buttonUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < listPanels.size(); i++) {
					AdminBookedFlightPanel panel = listPanels.get(i);
					if (panel.checkCancelled.isSelected()) {
						DB.deleteBookedFlight(DB.getConnection(), panel.bookedFlight.getId());
						EmailThread mailThread = new EmailThread(panel.bookedFlight.getUser(), panel.bookedFlight, EmailThread.CANCELLED_FLIGHT);
						new Thread(mailThread).start();
						continue;
					}
					if (panel.checkConfirmed.isSelected()) {
						DB.confirmBookedFlight(DB.getConnection(), panel.bookedFlight.getId());
						EmailThread mailThread = new EmailThread(panel.bookedFlight.getUser(), panel.bookedFlight, EmailThread.CONFIRMED_FLIGHT);
						new Thread(mailThread).start();
						continue;
					}
					if (panel.checkUpdate.isSelected()) {
						DB.updateBookedFlight(DB.getConnection(), panel.bookedFlight);
						EmailThread mailThread = new EmailThread(panel.bookedFlight.getUser(), panel.bookedFlight, EmailThread.UPDATED_FLIGHT);
						new Thread(mailThread).start();
						continue;
					}
				}
				if (tickets.size() > 0) {
					updateSearchList(tickets);
				}
				return;
			}
		});
	}
	
	private void updateSearchList(List<BookedFlight> searchResults) {
		listPanels.clear();
		for (int i = 0; i < searchResults.size(); i++) {
			listPanels.add(new AdminBookedFlightPanel(searchResults.get(i), fm, this));
		}
		refreshListPanel(listPanels);
	}
	
	private void refreshListPanel(List<AdminBookedFlightPanel> searchResults){
		panelList.removeAll();
		for (int i = 0; i < searchResults.size(); i++) {
			panelList.add(searchResults.get(i));
		}
		frame.revalidate();
	}
	
	public JPanel getListPanel(){
		return panelList;
	}
	
	public void setVisibility(boolean b) {
		this.frame.setVisible(b);
	}
}
