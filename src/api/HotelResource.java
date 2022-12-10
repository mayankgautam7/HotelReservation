package api;

import service.CustomerService;
import service.ReservationService;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.Collection;
import java.util.Date;


public class HotelResource {
    private HotelResource()
    {

    }
    ReservationService reservationService = ReservationService.getInstance();
    CustomerService customerService = CustomerService.getInstance();
    private static HotelResource reference = new HotelResource();
    public static HotelResource getInstance()
    {
        return reference;
    }
    public Customer getCustomer(String email)
    {
        Customer customer = customerService.getCustomer(email);
        return customer;
    }
    public void createACustomer(String firstName, String lastName, String email)
    {
        try {
            customerService.addCustomer(firstName, lastName, email);
        }catch (Exception ex)
        {
            throw ex;
        }
    }
    public IRoom getRoom(String roomNumber)
    {
        IRoom room = reservationService.getARoom(roomNumber);
        return room;
    }
    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate)
    {
        Reservation reservation = reservationService.reserveARoom(getCustomer(customerEmail), room, checkInDate, checkOutDate);
        return reservation;
    }
    public Collection<Reservation> getCustomersReservations(String customerEmail)
    {
        Customer customer = getCustomer(customerEmail);

        try {
            Collection<Reservation> reservations = reservationService.getCustomerReservation(customer);
            if(reservations == null)
                throw new IllegalArgumentException("There are no reservations for this customer!");
            return reservations;
        }catch (Exception ex) {
            throw ex;
        }

    }

    public Collection<IRoom> findARoom(Date checkInDate, Date checkOutDate)
    {
        Collection<IRoom> rooms = reservationService.findRooms(checkInDate, checkOutDate);
        return rooms;
    }

    public Collection<Collection<Reservation>> recommendRooms(Date checkIn, Date checkOut) {
        Collection<Collection<Reservation>> reservationsList = reservationService.recommendRooms(checkIn, checkOut);
        return reservationsList;
    }
}
