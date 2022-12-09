package model;

import model.IRoom;
import model.RoomType;

public class Room implements IRoom {
    private String roomNumber;
    private Double price;
    private RoomType roomType;
    private boolean  isFree;

    public Room(String roomNumber, Double price, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.price = price;
        this.roomType = roomType;
    }

    /**
     * @return
     */
    @Override
    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * @return
     */
    @Override
    public Double getRoomPrice() {
        return price;
    }

    /**
     * @return
     */
    @Override
    public RoomType getRoomType() {
        return roomType;
    }

    /**
     * @return
     */
    @Override
    public boolean isFree() {
        return isFree;
    }

    @Override
    public String toString() {
        return "RoomNumber = " + roomNumber + ", Price = " + price + ", RoomType = " + roomType;
    }
}
