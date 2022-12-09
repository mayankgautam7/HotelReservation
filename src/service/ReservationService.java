package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

import java.util.*;

public class ReservationService {
    private static  ReservationService reference = new ReservationService();
    private static HashMap<String, Collection<Reservation>> reservationMap = new HashMap<>();
    private HashMap<String, IRoom> rooms = new HashMap<>();
    public static ReservationService getInstance()
    {
        return reference;
    }
    List<Reservation>reservationList = new ArrayList<Reservation>();
    public void addRoom(IRoom room)
    {
        rooms.put(room.getRoomNumber(),room);
    }

    public IRoom getARoom(String roomId)
    {
        IRoom room = rooms.get(roomId);
        return room;
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate)
    {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);

        //Collection<Reservation> reservations = getCustomerReservation(customer);
        Collection<Reservation> reservations = reservationMap.get(customer.getEmail());
        if(reservations == null)
        {
            Collection<Reservation> reservationList = new ArrayList<>();
            reservationList.add(reservation);
            reservationMap.put(customer.getEmail(),reservationList);
        }
        else
        {
            reservations.add(reservation);
            reservationMap.put(customer.getEmail(),reservations);
        }


        return reservation;
    }

    // TODO - JUST A PLACEHOLDER
    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate)
    {
        Collection<IRoom> roomsList = new ArrayList<>();
        Collection<IRoom> reservedRoomsList = new ArrayList<>();
        Collection<IRoom> openRooms = new ArrayList<>();
        for(String customerId : reservationMap.keySet()) {
            Collection<Reservation> reservations = reservationMap.get(customerId);
            for(Reservation reservation : reservations)
            {
                Date roomCheckIn = reservation.getCheckInDate();
                Date roomCheckOut = reservation.getCheckOutDate();
                if(checkInDate.compareTo(roomCheckOut) < 0  || checkOutDate.compareTo(roomCheckIn) < 0)
                {
                    reservedRoomsList.add(reservation.getRoom());
                }
            }
        }
        for (String roomNumber : rooms.keySet()) {
            IRoom room = rooms.get(roomNumber);
            if(!reservedRoomsList.contains(room))
            {
                openRooms.add(room);
            }
        }
        return openRooms;
    }
    public Collection<Reservation> getCustomerReservation(Customer customer)
    {
        Collection<Reservation> reservations = reservationMap.get(customer.getEmail());

        return reservations;
    }
    public void printAllReservation()
    {
        if(reservationMap.isEmpty())
        {
            System.out.println("No reservations in system");
        }
        for(Collection<Reservation> reservations : reservationMap.values())
        {
            for (Reservation reservation : reservations)
                System.out.println(reservation.toString());
        }
    }
    public Collection<IRoom> getAllRooms()
    {
        Collection<IRoom> roomList = rooms.values();
        if(roomList.isEmpty())
        {
            System.out.println("No rooms in system.");
        }
        return roomList;
    }
}
