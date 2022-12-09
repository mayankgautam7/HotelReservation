package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {
    private AdminResource()
    {

    }
    ReservationService reservationService = ReservationService.getInstance();
    CustomerService customerService = CustomerService.getInstance();
    private static AdminResource reference = new AdminResource();
    public static AdminResource getInstance()
    {
        return reference;
    }
    public Customer getCustomer(String email)
    {
        Customer customer = customerService.getCustomer(email);
        return customer;
    }
    public void addRoom(List<IRoom> rooms)
    {
        for(IRoom room : rooms)
            reservationService.addRoom(room);
    }
    public Collection<IRoom> getAllRooms()
    {
        Collection<IRoom> rooms = reservationService.getAllRooms();
        return rooms;
    }
    public Collection<Customer> getAllCustomers()
    {
        Collection<Customer> customers = customerService.getAllCustomers();
        return customers;
    }
    public void displayAllReservations()
    {
        reservationService.printAllReservation();
    }
}
