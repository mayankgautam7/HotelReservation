package ui;

import api.AdminResource;
import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class MainMenu {
    static HotelResource hotelResource = HotelResource.getInstance();
    static AdminResource adminResource = AdminResource.getInstance();
    static  AdminMenu adminMenu = AdminMenu.getInstance();
    public static final Pattern pattern = Pattern.compile("^(.+)@(.+).(.+)$");
    static Scanner scanner = new Scanner(System.in);
    private MainMenu(){

    }
    private static MainMenu reference = new MainMenu();
    public static MainMenu getInstance()
    {
        return reference;
    }
    public static void mainMenu()
    {
        String input = "0";
        while(!input.equals("5")) {
            System.out.println("1. Find and reserve a room");
            System.out.println("2. See my reservations");
            System.out.println("3. Create an account");
            System.out.println("4. Admin");
            System.out.println("5. Exit");
            System.out.println("Please enter an input between 1 and 5");
            input = scanner.nextLine();
            switch (input) {
                case "1":
                    findAndReserveRoom();
                    break;

                case "2":
                    System.out.println("Please enter your email address");
                    seeMyReservations();
                    break;

                case "3":
                    createAnAccount();
                    break;

                case "4":
                    adminMenu.adminMenu();
                    break;

                case "5":
                    return;

                default:
                    System.out.println("Invalid input, Please enter a valid input between 1 and 5");
                    mainMenu();
                    break;


            }
        }
    }

    private static void createAnAccount() {
        System.out.println("Enter your email");
        String email = "";
        while(email == "") {
            try {
                email = emailValidator(scanner);
            } catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
        System.out.println("Enter your firstName");
        String firstName = scanner.nextLine();
        System.out.println("Enter your lastName");
        String lastName = scanner.nextLine();
        hotelResource.createACustomer(firstName, lastName, email);
    }

    private static void seeMyReservations() {
        String email = "";
        while(email == "") {
            try {
                email = emailValidator(scanner);
            } catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }

        try {
            Collection<Reservation> reservations = hotelResource.getCustomersReservations(email);
            for(Reservation reservation : reservations)
            {
                System.out.println(reservation.toString());
            }
        }catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            mainMenu();
        }

    }

    public static String emailValidator(Scanner scanner)
    {
        String email = scanner.nextLine();
        if(pattern.matcher(email).matches()) {
            return email;
        }else
            throw new IllegalArgumentException("Please enter a valid email address");
    }
    public static void findAndReserveRoom()
    {
        System.out.println("Enter CheckIn Date in dd/MM/yyyy format");
        Date date1 = null;
        while(date1 == null){
            try {
                date1 = dateValidator(scanner);
            }catch (Exception ex){
                System.out.println(ex.getLocalizedMessage());
            }
        }

        System.out.println("Enter CheckOut Date in dd/MM/yyyy format");
        Date date2 = null;
        while (date2 == null) {
            try {
                date2 = dateValidator(scanner);
            } catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }

        if(date2.compareTo(date1) < 0)
        {
            System.out.println("CheckOut Date can not be Greater than CheckIn Date, Enter dates in valid range.");
            findAndReserveRoom();
        }

        Collection<IRoom> rooms = hotelResource.findARoom(date1, date2);
        if(rooms.isEmpty())
        {
            System.out.println("Sorry! No rooms are available from " + date1 + " to " + date2);
            System.out.println("We recommend these rooms for alternate dates: ");
            Collection<Collection<Reservation>> reservationsList = hotelResource.recommendRooms(date1,date2);
            if(reservationsList.isEmpty())
            {
                System.out.println("No rooms are available!");
                return;
            }
            for(Collection<Reservation> reservations : reservationsList)
            {
                for(Reservation reservation : reservations)
                {
                    //Date roomCheckIn = reservation.getCheckInDate();
                    Date roomCheckOut = reservation.getCheckOutDate();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(roomCheckOut);
                    cal.add(Calendar.DATE, 1);

                    IRoom room = reservation.getRoom();
                    System.out.println("Room number: " + room.getRoomNumber() + " is available from " + cal.getTime());
                }
            }
            return;
        }
        else{
            Iterator iterator = rooms.iterator();
            System.out.println("Available Rooms: ");
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        }
        System.out.println("Would you like to book a room? y/n");
        String bookingFlag = "";
        while (bookingFlag == "") {
            try {
                bookingFlag = YnNFlag(scanner);
            } catch (Exception ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
        try {
            if (bookingFlag.equalsIgnoreCase("y")) {
                System.out.println("Do you have an account with us? y/n");
                String response = "";
                while (response == "") {
                    try {
                        response = YnNFlag(scanner);
                    } catch (Exception ex) {
                        System.out.println(ex.getLocalizedMessage());
                    }
                }

                if (response.equalsIgnoreCase("y")) {
                    System.out.println("Please enter your email");
                    String email = "";
                    while(email == "") {
                        try {
                            email = emailValidator(scanner);
                        } catch (Exception ex) {
                            System.out.println(ex.getLocalizedMessage());
                        }
                    }
                    Customer customer = hotelResource.getCustomer(email);
                    if (customer == null) {
                        System.out.println("Please select option 3 to create a new account before booking a room");
                        return;
                    } else {
                        System.out.println("Which room would you like to reserve?");
                        String roomNumber = "";
                        int rNum = -1;
                        while (rNum == -1) {
                            try {
                                roomNumber = scanner.nextLine();
                                rNum = Integer.parseInt(roomNumber);
                            } catch (Exception ex) {
                                System.out.println("Please enter a valid room number, it should be a numerical value eg 101");
                            }
                        }
                        boolean flag = false;
                        for (IRoom room : rooms) {
                            if (room.getRoomNumber().equalsIgnoreCase(roomNumber)) {
                                IRoom bookedRoom = hotelResource.getRoom(roomNumber);
                                Reservation reservation = hotelResource.bookARoom(email, room, date1, date2);
                                System.out.println("Congratulations!! You have successfully booked a room.");
                                System.out.println("Reservation Details: ");
                                System.out.println(reservation);
                                flag = true;
                                break;
                            }
                        }
                        if (flag == false) {
                            System.out.println("Sorry! The room you are looking for is not available!\nPlease Try Again for a new room!");
                        }
                    }

                } else if (response.equalsIgnoreCase("n")) {
                    System.out.println("Please select option 3 to create a new account before booking a room");
                    return;
                }
            }
            else if (bookingFlag.equalsIgnoreCase("n")) {
                return;
            }
        }catch (Exception ex)
        {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public static String YnNFlag(Scanner scanner)
    {
        String flag = scanner.nextLine();
        try{
            if(flag.equalsIgnoreCase("y") || flag.equalsIgnoreCase("n"))
                return flag;
            else
                throw new IllegalArgumentException("Invalid input, Please enter y for Yes or n for No");
        }catch (Exception ex){
            throw ex;
        }
    }

    public static Date dateValidator(Scanner scanner)
    {
        String dateStr = scanner.nextLine();
        try
        {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
            return date;
        }catch (Exception ex){
            throw new IllegalArgumentException("Enter a valid Date in dd/MM/yyyy format");
        }

    }
}
