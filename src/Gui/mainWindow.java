package Gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXDatePicker;

import flightManager.BookedFlight;
import flightManager.DB;
import flightManager.Flight;
import flightManager.FlightManager;
import flightManager.Route;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class mainWindow {

	private JFrame frame;
	public FlightManager fm;
	private JMenu mnAdmin;
	private JComboBox<String> cbFrom;
	private JComboBox<String> cbTo;
	private JXDatePicker dpSearchStartDate;
	private JXDatePicker dpSearchEndDate;
	private DefaultComboBoxModel<String> cityListModelFrom;
	private DefaultComboBoxModel<String> cityListModelTo;
	private JButton btnSearch;
	private JPanel panelSearch;
	private List<FlightPanel> listFITR;
	private JSpinner cbTickets;
	private int maxPriceSetting = 0;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainWindow window = new mainWindow();
					login loginWindow = new login(window);
					loginWindow.setVisibility(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}

	/**
	 * Create the application.
	 */
	public mainWindow() {
		this.fm = new FlightManager();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 1006, 494);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmLogOut = new JMenuItem("Log out");
		mnNewMenu.add(mntmLogOut);
		
		JMenuItem mntmClose = new JMenuItem("Close");
		mnNewMenu.add(mntmClose);
		
		mnAdmin = new JMenu("Admin");
		menuBar.add(mnAdmin);
		
		JMenuItem mntmAirports = new JMenuItem("Airports");
		mnAdmin.add(mntmAirports);
		
		JMenuItem mntmCities = new JMenuItem("Cities");
		mnAdmin.add(mntmCities);
		
		JMenuItem mntmUsers = new JMenuItem("Users");
		mnAdmin.add(mntmUsers);

		JMenuItem mntmRoutes = new JMenuItem("Routes");
		mnAdmin.add(mntmRoutes);

		JMenuItem mntmAirplanes = new JMenuItem("Airplanes");
		mnAdmin.add(mntmAirplanes);
		
		JMenuItem mntmFlights = new JMenuItem("Flights");
		mnAdmin.add(mntmFlights);
		
		JMenuItem mntmBookedFlights = new JMenuItem("Bookings");
		mnAdmin.add(mntmBookedFlights);

		
		// LISTENER - Admin Menu - Users 
		mntmUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AdminUsers users = new AdminUsers(fm);
				users.setVisibility(true);
			}
		});
		
		// LISTENER - Admin Menu - Cities
		mntmCities.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AdminCities cities = new AdminCities(fm);
				cities.setVisibility(true);
			}
		});

		// LISTENER - Admin Menu - Airports 
		mntmAirports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AdminAirports airports = new AdminAirports(fm);
				airports.setVisibility(true);
			}
		});
		
		// LISTENER - Admin Menu - Routes 
		mntmRoutes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AdminRoutes routes = new AdminRoutes(fm);
				routes.setVisibility(true);
			}
		});
		
		// LISTENER - Admin Menu - Airplanes 
		mntmAirplanes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AdminAirplanes airplanes = new AdminAirplanes(fm);
				airplanes.setVisibility(true);
			}
		});
		
		// LISTENER - Admin Menu - Flights 
		mntmFlights.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				adminFlights flights = new adminFlights (fm);
				flights.setVisibility(true);
			}
		});

		// LISTENER - Admin Menu - BookedFlights 
		mntmBookedFlights.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AdminBookedFlights bf = new AdminBookedFlights(fm);
				bf.setVisibility(true);
			}
		});
		
		// LISTENER - Main Menu - Log Out
		mntmLogOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showLogin();
			}
		});
		
		// LISTENER - Main Menu - Close
		mntmClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		listFITR = new ArrayList<FlightPanel>();
		JPanel panel = new JPanel();
		panel.setBounds(6, 6, 230, 437);
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null,
				null, null));
		panel.setBackground(Color.DARK_GRAY);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(6, 113, 218, 111);
		panel.add(panel_2);
		panel_2.setLayout(null);

		JLabel lbDepartDate = new JLabel("Earliest Departure");
		lbDepartDate.setBounds(6, 6, 105, 16);
		panel_2.add(lbDepartDate);

		JLabel lbReturnDate = new JLabel("Latest Departure");
		lbReturnDate.setBounds(6, 61, 105, 16);
		panel_2.add(lbReturnDate);

		dpSearchStartDate = new JXDatePicker();
		dpSearchStartDate.setFormats(new String[] { "dd/MM/yyy" });
		// tfDepartDate.setText("dd/mm/yyyy");
		dpSearchStartDate.setBounds(6, 21, 105, 28);
		panel_2.add(dpSearchStartDate);
		// tfDepartDate.setColumns(10);

		dpSearchEndDate = new JXDatePicker();
		dpSearchEndDate.setFormats(new String[] { "dd/MM/yyy" });

		// dpReturnDay.setText("dd/mm/yyyy");
		// dpReturnDay.setColumns(10);
		dpSearchEndDate.setBounds(6, 78, 105, 28);
		panel_2.add(dpSearchEndDate);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(6, 6, 218, 103);
		panel.add(panel_3);
		panel_3.setLayout(null);

		cityListModelFrom = new DefaultComboBoxModel<String>();
		for (int i = 0; i < fm.cities.size(); i++) {
			cityListModelFrom.addElement(fm.cities.get(i).getCityName());
		}

		cbFrom = new JComboBox<String>(cityListModelFrom);
		cbFrom.setBounds(6, 24, 208, 28);
		panel_3.add(cbFrom);

		JLabel lbFrom = new JLabel("From");
		lbFrom.setBounds(6, 6, 32, 16);
		panel_3.add(lbFrom);

		JLabel lbTo = new JLabel("To");
		lbTo.setBounds(6, 51, 16, 16);
		panel_3.add(lbTo);

		cityListModelTo = new DefaultComboBoxModel<String>();
		for (int i = 0; i < fm.cities.size(); i++) {
			cityListModelTo.addElement(fm.cities.get(i).getCityName());
		}

		cbTo = new JComboBox<String>(cityListModelTo);
		cbTo.setBounds(6, 68, 208, 28);
		panel_3.add(cbTo);

		JPanel panel_4 = new JPanel();
		panel_4.setBounds(6, 228, 218, 127);
		panel.add(panel_4);
		panel_4.setLayout(null);
		
		SpinnerModel sm = new SpinnerNumberModel(1, 1, 5000, 1);
		
		cbTickets = new JSpinner(sm);
		cbTickets.setBounds(119, 12, 62, 27);
		panel_4.add(cbTickets);

		JLabel lbTickets = new JLabel("Tickets: ");
		lbTickets.setBounds(16, 16, 102, 16);
		panel_4.add(lbTickets);

		JLabel lbMaxPrice = new JLabel("Maximum cost (Optional)");
		lbMaxPrice.setBounds(16, 48, 200, 24);
		panel_4.add(lbMaxPrice);

		final JLabel lbPriceTotal = new JLabel("...in Total");
		lbPriceTotal.setBounds(24, 72, 60, 16);
		panel_4.add(lbPriceTotal);

		JLabel lbPriceTicket = new JLabel("...per Ticket");
		lbPriceTicket.setBounds(104, 72, 80, 16);
		panel_4.add(lbPriceTicket);
		
		final JRadioButton radPriceTotal = new JRadioButton();
		panel_4.add(radPriceTotal);
		radPriceTotal.setBounds(8, 72, 16, 16);
		radPriceTotal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				maxPriceSetting = 0;
			}
		});
		
		final JRadioButton radPriceTicket = new JRadioButton();
		panel_4.add(radPriceTicket);
		radPriceTicket.setBounds(88, 72, 16, 16);
		radPriceTicket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				maxPriceSetting = 1;
			}
		});

		lbPriceTotal.setEnabled(false);
		radPriceTotal.setEnabled(false);
		
		ButtonGroup groupPrice = new ButtonGroup();
		groupPrice.add(radPriceTicket);
		groupPrice.add(radPriceTotal);
		
		final JTextField textMaxPrice = new JTextField();
		textMaxPrice.setBounds(16, 92, 160, 24);
		panel_4.add(textMaxPrice);
		
		btnSearch = new JButton("Search");
		btnSearch.setBounds(10, 368, 80, 52);

		JButton btnMyFlights = new JButton("My Flights");
		btnMyFlights.setBounds(100, 368, 120, 52);
		
		panel.add(btnMyFlights);
		panel.add(btnSearch);
		JScrollPane scroller = new JScrollPane();
		scroller.setBounds(238, 6, 760, 437);
		frame.getContentPane().add(scroller, BorderLayout.CENTER);

		panelSearch = new JPanel();
		panelSearch.setBounds(6, 97, 393, 321);
		scroller.setViewportView(panelSearch);
		panelSearch.setLayout(new GridLayout(0,1,0,0));
		
		// LISTENER - "Search" button
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Flight> listFlights = new ArrayList<Flight>();
				List<Route> listRoutes = fm.findRoutesByCities(fm.cities.get(cbFrom.getSelectedIndex()), fm.cities.get(cbTo.getSelectedIndex()));
				for (int i = 0; i < listRoutes.size(); i++) {
					List<Flight> tempList = new ArrayList<Flight>();
					
					if (dpSearchStartDate.getDate() == null && dpSearchEndDate.getDate() == null) {
						tempList = DB.getFlightsByRoute(DB.getConnection(), listRoutes.get(i).getId(), false);
					} else {
						tempList = DB.getFlightsByRouteAndDate(DB.getConnection(), listRoutes.get(i).getId(), dpSearchStartDate.getDate(), dpSearchEndDate.getDate());
					}	
					
					for (int j = 0; j < tempList.size(); j++) {
						if ((tempList.get(j).getBookedTickets() + Integer.parseInt(cbTickets.getValue().toString())) <= tempList.get(j).getCapacity()) {
							listFlights.add(tempList.get(j));
						}
					}
				}
				if (listFlights.size() < 1) {
					JOptionPane.showMessageDialog(frame,
							"No flights were found, please try again with different search parameters.", "No flights found",
							JOptionPane.WARNING_MESSAGE);
				} else {
					try {
						int maxPrice = Integer.parseInt(textMaxPrice.getText());
						if (maxPrice > 0) {
							if (maxPriceSetting == 1) {
								listFlights = fm.filterFlightsByPrice(
										listFlights, maxPrice);
							} else if (maxPriceSetting == 0){
								listFlights = fm.filterFlightsByPrice(
										listFlights, maxPrice/Integer.parseInt(cbTickets.getValue().toString()));
							}
						}
					} catch (Exception e2) {
					}
					updateSearchList(listFlights, false);
				}
			}
		});
		
		// LISTENER - "My Flights" button
		btnMyFlights.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myFlights();
			}
		});

		// LISTENER - Tickets counter
		cbTickets.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (Integer.parseInt(cbTickets.getValue().toString()) < 1) {
					btnSearch.setEnabled(false);
				} else if (Integer.parseInt(cbTickets.getValue().toString()) == 1) {
					radPriceTotal.setEnabled(false);
					lbPriceTotal.setEnabled(false);
					btnSearch.setEnabled(true);
				} else {
					radPriceTotal.setEnabled(true);
					lbPriceTotal.setEnabled(true);
					btnSearch.setEnabled(true);
				}
			}
		});
	}
	
	public void setVisibility(boolean b){
		if(FlightManager.activeUser.getUserRank() != 1){
			this.mnAdmin.setVisible(false);
		} else {
			this.mnAdmin.setVisible(true);
		}
		this.frame.setVisible(b);
	}

	public void showLogin(){
		login loginWindow = new login(this);
		loginWindow.setVisibility(true);
		this.setVisibility(false);
	}
	

	public void myFlights(){
		if (FlightManager.activeUser == null) {
			return;
		}
		List<BookedFlight> listBookings = DB.getBookedFlightsByOwner(DB.getConnection(), FlightManager.activeUser);
		if (listBookings.size() < 1) {
			JOptionPane.showMessageDialog(frame,
					"You have no tickets booked.", "No Bookings",
					JOptionPane.WARNING_MESSAGE);
		} else {
			updateSearchList(listBookings, true);
		}
	}
	
	private void updateSearchList(List<?> searchResults, boolean booked) {
		listFITR.clear();
		if (booked) {
			for (int i = 0; i < searchResults.size(); i++) {
				FlightPanel fitr = new FlightPanel((BookedFlight)searchResults.get(i), this,
						booked);
				listFITR.add(fitr);
			}
		} else {
			for (int i = 0; i < searchResults.size(); i++) {
				FlightPanel fitr = new FlightPanel((Flight)searchResults.get(i), this,
						booked);
				listFITR.add(fitr);
			}
		}
		refreshListPanel(listFITR);
	}

	private void refreshListPanel(List<FlightPanel> searchResults){
		panelSearch.removeAll();
		for (int i = 0; i < searchResults.size(); i++) {
			panelSearch.add(searchResults.get(i));
		}
		frame.revalidate();
	}

	public void updateLists(){
		fm.updateLists();
		cityListModelFrom.removeAllElements();
		cityListModelTo.removeAllElements();
		for (int i = 0; i < fm.cities.size(); i++) {
			cityListModelFrom.addElement(fm.cities.get(i).getCityName());
			cityListModelTo.addElement(fm.cities.get(i).getCityName());
		}
	}
	public FlightManager getFM(){
		return this.fm;
	}
	public JFrame getFrame() {
		return this.frame;
	}
	public JSpinner getTicketCounter(){
		return this.cbTickets;
	}
}
