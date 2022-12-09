package ui;

import api.AdminResource;
import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import static ui.MainMenu.adminMenu;
import static ui.MainMenu.scanner;

public class AdminMenu {
    HotelResource hotelResource = HotelResource.getInstance();
    AdminResource adminResource = AdminResource.getInstance();

    List<IRoom> rooms = new ArrayList<>();
    private static AdminMenu reference = new AdminMenu();
    public static AdminMenu getInstance()
    {
        return reference;
    }
    public void adminMenu()
    {
            String input = "0";
            while(!input.equals("5")) {
                System.out.println("1. See all Customers");
                System.out.println("2. See all Rooms");
                System.out.println("3. See all Reservations");
                System.out.println("4. Add a Room");
                System.out.println("5. Back to Main Menu");
                System.out.println("Please enter an input between 1 and 5");
                input = scanner.nextLine();
                switch (input) {
                    case "1":
                        seeAllCustomers();
                        break;

                    case "2":
                        seeAllRooms();
                        break;

                    case "3":
                        seeAllReservations();
                        break;

                    case "4":
                        addARoom();
                        break;

                    case "5":
                        MainMenu.mainMenu();
                        break;

                    default:
                        System.out.println("Invalid input, Please enter a valid input between 1 and 5");
                        adminMenu();
                        break;


                }
            }
    }

    private void seeAllCustomers() {
        Collection<Customer> customers =  adminResource.getAllCustomers();
        if(customers.isEmpty())
        {
            System.out.println("No customers in system.");
        }
        for(Customer customer : customers)
        {
            System.out.println(customer.toString());
        }
    }

    private void seeAllReservations() {
        adminResource.displayAllReservations();
    }

    private void seeAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();
        for(IRoom room : rooms)
        {
            System.out.println(((Room)room).toString());
        }
    }

    private void addARoom() {
        System.out.println("Enter a Room Number");
        String roomNmber = scanner.nextLine();
        System.out.println("Enter price per night");
        Double price = 0.0;
        try{
            price = validatePrice(scanner);
        }catch (Exception ex){
            System.out.println("Please enter a valid numrical price eg 156");
            price = validatePrice(scanner);
        }

        System.out.println("Enter a Room Type: 1 for Single Bed or 2 for Double Bed");
        String roomType = scanner.nextLine();
        try
        {
            validateRoom(roomNmber,price,roomType,true);
        }catch(Exception ex)
        {
            System.out.println("Please enter 1 for Single bed or 2 for Double bed as input");
            roomType = scanner.nextLine();
            validateRoom(roomNmber,price,roomType,true);
        }

        System.out.println("Would you like to enter a new room? y/n");
        String flag = scanner.nextLine();
        if(flag.equalsIgnoreCase("y"))
        {
            addARoom();
        }
        else if(flag.equalsIgnoreCase("n"))
        {
            adminResource.addRoom(rooms);
        }
    }
    private Double validatePrice(Scanner scanner)
    {
        Double price = Double.parseDouble(scanner.nextLine());
        return price;
    }

    private void validateRoom(String roomNumber, Double price, String roomType, boolean isFree)
    {
        RoomType rt = null;
        if(roomType.equalsIgnoreCase("1"))
        {
            rt = RoomType.SINGLE;
        } else if (roomType.equalsIgnoreCase("2")) {
            rt = RoomType.DOUBLE;
        }
        else{
            throw new IllegalArgumentException("Please enter 1 for Single bed or 2 for Double bed as input");
        }
        Room room = new Room(roomNumber,price, rt );
        try {
            boolean roomFlag = false;
            for(IRoom created : rooms) {
                if (!created.getRoomNumber().equalsIgnoreCase(room.getRoomNumber())) {
                    roomFlag = false;
                } else {
                    roomFlag = true;
                    break;
                }
            }
            if(roomFlag == false)
            {
                rooms.add(room);
            }
            else{
                throw new IllegalArgumentException("This room number already exists, Please add a new room number");
            }
        }catch (Exception ex)
        {
            System.out.println(ex.getLocalizedMessage());
            addARoom();
        }
    }
}
