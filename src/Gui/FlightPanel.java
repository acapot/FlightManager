package Gui;

import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import flightManager.BookedFlight;
import flightManager.DB;
import flightManager.EmailThread;
import flightManager.Flight;
import flightManager.FlightManager;
import flightManager.Globals;


@SuppressWarnings("serial")
public class FlightPanel extends JPanel {
	
	private BookedFlight bookedFlight;
	private Flight flight;
	private mainWindow parent;
	private boolean booked;
	
	/**
	 * @wbp.parser.constructor
	 */
	public FlightPanel(Flight f, final mainWindow parent, final boolean booked){
		this.flight = f;
		this.parent = parent;
		this.booked = booked;
		int tickets = Integer.parseInt(parent.getTicketCounter().getValue().toString());
		initialize(tickets);
	}
	public FlightPanel(BookedFlight bf, final mainWindow parent, final boolean booked){
		this.bookedFlight = bf;
		this.flight = bf.getFlight();
		this.parent = parent;
		this.booked = booked;
		initialize(bf.getTicketCount());
	}
	
	private void initialize(int tickets){
		this.setBackground(Color.GRAY);
		this.setPreferredSize(new Dimension(740, 110));
		JPanel plPrice = new JPanel();
		plPrice.setLayout(null);
		
		JButton btnReserve = new JButton(((booked) ? "Cancel" : "Book"));
		btnReserve.setBounds(40, 52, 100, 23);
		plPrice.add(btnReserve);
		
		JPanel plContainerLblinfo = new JPanel();
		plContainerLblinfo.setPreferredSize(new Dimension(100, 100));
		plContainerLblinfo.setLayout(null);

		JLabel lblTicketText = new JLabel("Tickets:");
		lblTicketText.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTicketText.setVerticalAlignment(SwingConstants.TOP);
		lblTicketText.setHorizontalAlignment(JLabel.LEFT);
		lblTicketText.setBounds(10, 6, 70, 17);
		
		JLabel lblFlightPriceText = new JLabel("Total Price");
		lblFlightPriceText.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblFlightPriceText.setVerticalAlignment(SwingConstants.TOP);
		lblFlightPriceText.setHorizontalAlignment(JLabel.RIGHT);
		lblFlightPriceText.setBounds(90, 6, 81, 17);
		
		String remaining = (!booked) ? " of " + Integer.toString(flight.getCapacity() - flight.getBookedTickets()) : "";
		
		JLabel lblTicketCount = new JLabel(Integer.toString(tickets) + remaining);
		lblTicketCount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTicketCount.setVerticalAlignment(SwingConstants.TOP);
		lblTicketCount.setHorizontalAlignment(JLabel.LEFT);
		lblTicketCount.setBounds(10, 24, 80, 17);
		
		JLabel lblFlightPrice = new JLabel(Integer.toString(flight.getPrice() * ((tickets > 1) ? tickets : 1)));
		lblFlightPrice.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFlightPrice.setVerticalAlignment(SwingConstants.TOP);
		lblFlightPrice.setHorizontalAlignment(JLabel.RIGHT);
		lblFlightPrice.setBounds(74, 24, 97, 17);
		
		plPrice.add(lblTicketText);
		plPrice.add(lblFlightPriceText);
		plPrice.add(lblTicketCount);
		plPrice.add(lblFlightPrice);
		
		String airportsInfo = flight.getRoute().getStart() + " (" + flight.getRoute().getStart().getCity().toString() + ") -> ";
		airportsInfo += flight.getRoute().getDestination() + " (" + flight.getRoute().getDestination().getCity().toString() + ")";

		JLabel lblFlightInfo = new JLabel(airportsInfo);
		lblFlightInfo.setVerticalAlignment(SwingConstants.TOP);
		lblFlightInfo.setBounds(10, 11, 513, 16);
		plContainerLblinfo.add(lblFlightInfo);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String departureInfo = "Departs at: " + df.format(flight.getDeparture());
		JLabel lblDepartureInfo = new JLabel(departureInfo);
		lblDepartureInfo.setVerticalAlignment(SwingConstants.TOP);
		lblDepartureInfo.setBounds(14, 27, 500, 16);
		plContainerLblinfo.add(lblDepartureInfo);
		
		String arrivalInfo = "Estimated time of arrival: " + df.format(flight.getArrival());
		JLabel lblArrivalInfo = new JLabel(arrivalInfo);
		lblArrivalInfo.setVerticalAlignment(SwingConstants.TOP);
		lblArrivalInfo.setBounds(14, 43, 500, 16);
		plContainerLblinfo.add(lblArrivalInfo);
		
		GroupLayout gl_plFlightInfo = new GroupLayout(this);
		gl_plFlightInfo.setHorizontalGroup(
			gl_plFlightInfo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_plFlightInfo.createSequentialGroup()
					.addContainerGap()
					.addComponent(plContainerLblinfo, GroupLayout.PREFERRED_SIZE, 533, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(plPrice, GroupLayout.PREFERRED_SIZE, 180, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_plFlightInfo.setVerticalGroup(
			gl_plFlightInfo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_plFlightInfo.createSequentialGroup()
					.addGap(14)
					.addGroup(gl_plFlightInfo.createParallelGroup(Alignment.LEADING)
						.addComponent(plPrice, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
						.addComponent(plContainerLblinfo, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		this.setLayout(gl_plFlightInfo);
		
		// LISTENER - "Book" button
		btnReserve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (booked) {
					DB.deleteBookedFlight(DB.getConnection(), bookedFlight.getId());
					JOptionPane.showMessageDialog(parent.getFrame(),
							"Booked tickets have been cancelled", "Booking Deleted",
							JOptionPane.WARNING_MESSAGE);
					EmailThread mailThread = new EmailThread(bookedFlight.getUser(), bookedFlight, EmailThread.CANCELLED_FLIGHT);
					new Thread(mailThread).start();
					parent.myFlights();
				} else {
					int tickets = (Integer.parseInt(parent.getTicketCounter().getValue().toString()) > 1) ? Integer.parseInt(parent.getTicketCounter().getValue().toString()) : 1;
					BookedFlight bf = new BookedFlight(flight, FlightManager.activeUser, new Date(), tickets, (flight.getPrice() * tickets), false, false);
					bf = DB.insertBookedFlight(DB.getConnection(Globals.SERVER_URL, Globals.SERVER_USERNAME, Globals.SERVER_PASSWORD), bf);

					JOptionPane.showMessageDialog(parent.getFrame(),
							"Tickets have been booked", "Booking Confirmed",
							JOptionPane.WARNING_MESSAGE);
					EmailThread mailThread = new EmailThread(bf.getUser(), bf, EmailThread.CREATED_FLIGHT);
					new Thread(mailThread).start();
					parent.myFlights();
				}
			}
		});
	}
}
