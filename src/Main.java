import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        menu();
    }

    public static void menu() {
        System.out.println("Добро пожаловать в автомат розыгрыша игрушек");
        while (true) {
            System.out.println("Чтобы просмотреть меню нажмите 1");
            System.out.print("Ваш выбор: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    View.showMenu();
                    break;
                case "2":
                    View.showToys(ToysManager.getAllToys());
                    break;
                case "3":
                    ToysManager.addToy();
                    break;
                case "4":
                    ToysManager.chooseRandomToy();
                    break;
                case "5":
                    ToysManager.moveLastWinningToyToFile();
                    break;
                case "6":
                    View.showWiningToys();
                    break;
                case "7":
                    ToysManager.addNewChance();
                    break;
                case "0":
                    return;
                default:
            }
        }
    }
}