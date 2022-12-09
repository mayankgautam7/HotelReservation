package model;

public class FreeRoom extends Room{

    private String roomNumber;
    private Double price;
    private RoomType roomType;
    public FreeRoom(String roomNumber, Double price, RoomType roomType)
    {
        super(roomNumber, price, roomType);
        this.price = 0.0;
    }

    @Override
    public String toString() {
        return "RoomNumber= " + roomNumber +
                ", price= " + price +
                ", roomType= " + roomType;
    }
}
