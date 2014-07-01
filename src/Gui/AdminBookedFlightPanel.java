package Gui;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.JXDatePicker;

import flightManager.BookedFlight;
import flightManager.Flight;
import flightManager.FlightManager;
import flightManager.User;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class AdminBookedFlightPanel extends JPanel {

	public BookedFlight bookedFlight;
	private AdminBookedFlights parent;
	private FlightManager fm;
	public JCheckBox checkUpdate;
	public JCheckBox checkConfirmed;
	public JCheckBox checkCancelled;
	public boolean dirtyFlag = false;
	
	public AdminBookedFlightPanel(BookedFlight bf, FlightManager fm, AdminBookedFlights parent) {
		this.bookedFlight = bf;
		this.parent = parent;
		this.fm = fm;
		initialize();
	}
	
	private void initialize() {
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 5, 5);
		flowLayout.setAlignOnBaseline(true);
		this.setLayout(flowLayout);
		this.setPreferredSize(new Dimension(parent.getListPanel().getWidth(), 35));

		SpinnerModel sm = new SpinnerNumberModel(bookedFlight.getTicketCount(), 1, 5000, 1);
		DefaultComboBoxModel<Flight> cbmFlight= new DefaultComboBoxModel<Flight>();
		DefaultComboBoxModel<User> cbmUser= new DefaultComboBoxModel<User>();
		
		final JTextField txtId = new JTextField(Integer.toString(bookedFlight.getId()));
		final JTextField txtPrice = new JTextField(Integer.toString(bookedFlight.getTotalPrice()));
		final JComboBox<Flight> cbFlight = new JComboBox<Flight>(cbmFlight);
		final JComboBox<User> cbUser = new JComboBox<User>(cbmUser);
		final JXDatePicker dpDateBooked = new JXDatePicker(bookedFlight.getDateOfBooking());
		final JSpinner spinTicketCount = new JSpinner(sm);
		checkUpdate = new JCheckBox("Update", dirtyFlag);
		checkCancelled = new JCheckBox("Cancelled", bookedFlight.getCancelled());
		checkConfirmed = new JCheckBox("Confirmed", bookedFlight.getConfirmed());
		
		for (int i = 0; i < fm.flights.size(); i++) {
			cbmFlight.addElement(fm.flights.get(i));
		}
		cbmFlight.setSelectedItem(fm.flights.get(fm.findById(bookedFlight.getFlight())));
		
		for (int i = 0; i < fm.users.size(); i++) {
			cbmUser.addElement(fm.users.get(i));
		}
		cbmUser.setSelectedItem(fm.users.get(fm.findById(bookedFlight.getUser())));
		
		Dimension textField = new Dimension(40, 20);
		
		txtId.setPreferredSize(textField);
		txtPrice.setPreferredSize(textField);
		
		
		checkUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!checkUpdate.isSelected()) {
					dirtyFlag = false;
				} else {
					dirtyFlag = true;
				}
			}
		});
		txtId.setEnabled(false);
		txtPrice.setEnabled(false);
		cbFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bookedFlight.setFlight((Flight)cbFlight.getSelectedItem());
				setDirtyFlag();
			}
		});
		cbUser.addActionListener(new ActionListener()  {
			public void actionPerformed(ActionEvent arg0) {
				bookedFlight.setUser((User)cbUser.getSelectedItem());
				setDirtyFlag();
			}
		});
		dpDateBooked.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bookedFlight.setDateOfBooking(dpDateBooked.getDate());
				setDirtyFlag();
			}
		});
		checkCancelled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				bookedFlight.setCancelled(false);
				setDirtyFlag();
			}
		});
		checkConfirmed.addActionListener(new ActionListener()  {
			public void actionPerformed(ActionEvent arg0) {
				bookedFlight.setConfirmed(true);
				setDirtyFlag();
			}
		});
		spinTicketCount.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Object os=spinTicketCount.getValue();
				bookedFlight.setTicketCount(Integer.parseInt(os.toString()));
				bookedFlight.setTotalPrice(bookedFlight.getFlight().getPrice() * Integer.parseInt(os.toString()));
				setDirtyFlag();
			}
		});

		this.add(checkUpdate);
		this.add(txtId);
		this.add(txtPrice);
		this.add(cbFlight);
		this.add(cbUser);
		this.add(dpDateBooked);
		this.add(spinTicketCount);
		this.add(checkCancelled);
		this.add(checkConfirmed);
	}
	private void setDirtyFlag(){
		dirtyFlag = true;
		checkUpdate.setSelected(true);
		//System.out.println("added to update");		
	}
}
