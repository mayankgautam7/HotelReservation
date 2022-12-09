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
        try{
            email = emailValidator(scanner);
        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
            email = emailValidator(scanner);
        }
        System.out.println("Enter your firstName");
        String firstName = scanner.nextLine();
        System.out.println("Enter your lastName");
        String lastName = scanner.nextLine();
        hotelResource.createACustomer(firstName, lastName, email);
    }

    private static void seeMyReservations() {
        String email = "";
        try{
            email = emailValidator(scanner);
        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
            email = emailValidator(scanner);
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
        try {
            date1 = dateValidator(scanner);
        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
            date1 = dateValidator(scanner);
        }

        System.out.println("Enter CheckOut Date in dd/MM/yyyy format");
        Date date2 = null;
        try {
            date2 = dateValidator(scanner);
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
            date2 = dateValidator(scanner);
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
        try {
            bookingFlag = YnNFlag(scanner);
        }catch (Exception ex){
            System.out.println(ex.getLocalizedMessage());
            bookingFlag = YnNFlag(scanner);
        }
        try {
            if (bookingFlag.equalsIgnoreCase("y")) {
                System.out.println("Do you have an account with us? y/n");
                String response = "";
                try{
                    response = YnNFlag(scanner);
                }catch (Exception ex) {
                    System.out.println(ex.getLocalizedMessage());
                    response = YnNFlag(scanner);
                }

                if (response.equalsIgnoreCase("y")) {
                    System.out.println("Please enter your email");
                    String email = scanner.nextLine();
                    Customer customer = hotelResource.getCustomer(email);
                    if (customer == null) {
                        System.out.println("Please select option 3 to create a new account before booking a room");
                        return;
                    } else {
                        System.out.println("Which room would you like to reserve?");
                        String roomNumber = scanner.nextLine();
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
                            System.out.println("Sorry! The room you are looking for is not available!,\n Please Try Again for a new room!");
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
