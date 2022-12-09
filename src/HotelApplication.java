import ui.MainMenu;

public class HotelApplication {
    static MainMenu mainMenu = MainMenu.getInstance();
    public static void main(String[] args)
    {
        try {
            mainMenu.mainMenu();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getLocalizedMessage());
        }
    }
}
