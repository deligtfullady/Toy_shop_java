import java.io.*;
import java.util.*;

public class ToysManager {
    static Scanner scanner = new Scanner(System.in);
    private static final String FILE_PATH = "toys.txt";
    private static final String WINNING_TOY_FILE_PATH = "wining_toys.txt";

    static Queue<String> winningToysQueue = new LinkedList<>();


    public static void createToy(String name, int balance, float chance) {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
                writeLineToFile("100001," + name + "," + balance + "," + chance);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<String> lines = readLinesFromFile();

        int nextId = getNextId(lines);

        Toys toy = new Toys(String.valueOf(nextId), name, balance, chance);

        String newToyInfo = toy.getId() + "," + toy.getName() + "," + toy.getBalance() + "," + toy.getChance();
        lines.add(newToyInfo);
        writeLinesToFile(lines);
    }

    public static void addToy() {
        int balance;
        float chance;
        System.out.print("Введите имя игрушки: ");
        String name = scanner.nextLine();
        while (true) {
            System.out.print("Введите количество игрушек, шт.: ");
            try {
                balance = Integer.parseInt(scanner.nextLine());
                if (balance > 0) break;
                else {
                    System.out.println("Количество игрушек не может быть меньше 1");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат ввода данных");
            }
        }
        while (true) {
            System.out.print("Введите шанс выпадения игрушки, %: ");
            try {
                chance = Float.parseFloat(scanner.nextLine());
                if (chance >= 0.1 && chance <= 99.9) break;
                else {
                    System.out.println("Шанс выпадения не может быть меньше 0,1 % или больше 99,9%");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат ввода данных");
            }
        }
        ToysManager.createToy(name, balance, chance);
    }

    public static List<Toys> getAllToys() {
        List<Toys> toysList = new ArrayList<>();
        List<String> lines = readLinesFromFile();

        for (String line : lines) {
            String[] parts = line.split(",");
            Toys toy = new Toys(parts[0], parts[1], Integer.parseInt(parts[2]), Float.parseFloat(parts[3]));
            toysList.add(toy);
        }

        return toysList;
    }

    public static void chooseRandomToy() {
        List<Toys> toysList = getAllToys();
        if (toysList.isEmpty()) {
            System.out.println("Список игрушек пуст.");
            return;
        }

        float totalChance = 0;
        for (Toys toy : toysList) {
            totalChance += toy.getChance();
        }

        Random random = new Random();
        float randomValue = random.nextFloat() * totalChance;

        float cumulativeProbability = 0;
        for (Toys toy : toysList) {
            cumulativeProbability += toy.getChance();
            if (randomValue <= cumulativeProbability) {
                toy.setBalance(toy.getBalance() - 1);
                if (toy.getBalance() <= 0) {
                    toysList.remove(toy);
                }
                updateToFile(toysList);
                String winningToyName = toy.getName();
                winningToysQueue.offer(winningToyName);
                System.out.printf("Поздравляем! Вы выиграли %s \n", winningToyName);
                return;
            }
        }
    }

    public static void moveLastWinningToyToFile() {
        String lastWinningToy = winningToysQueue.poll();
        System.out.printf("Вы получили %s  \n", lastWinningToy);
        if (lastWinningToy != null) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(WINNING_TOY_FILE_PATH, true))) {
                bw.write(lastWinningToy);
                bw.write(",");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<String> readLinesFromFile() {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("База игрушек пуста. Наполните базу, чтобы перейти к розыгрышу");
            ;
        }
        return lines;
    }

    private static int getNextId(List<String> lines) {
        if (lines.isEmpty()) {
            return 100001;
        } else {
            String lastLine = lines.get(lines.size() - 1);
            String[] parts = lastLine.split(",");
            return Integer.parseInt(parts[0]) + 1;
        }
    }

    private static void writeLineToFile(String line) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ToysManager.FILE_PATH))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeLinesToFile(List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateToFile(List<Toys> toysList) {
        List<String> lines = new ArrayList<>();
        for (Toys toy : toysList) {
            String toyInfo = toy.getId() + "," + toy.getName() + "," + toy.getBalance() + "," + toy.getChance();
            lines.add(toyInfo);
        }
        writeLinesToFile(lines);
    }

    public static void updateToyChance(String id, float newChance) {
        List<String> lines = readLinesFromFile();
        boolean found = false;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");
            if (parts.length >= 4 && parts[0].equals(id)) {
                parts[3] = String.valueOf(newChance);
                lines.set(i, String.join(",", parts));
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Игрушка с указанным ID не найдена.");
            return;
        }

        writeLinesToFile(lines);
        System.out.println("Шанс для игрушки с ID " + id + " успешно обновлен.");
    }

    public static void addNewChance() {
        System.out.print("Введите ID игрушки, шанс выпадения которой Вы хотите изменить: ");
        String id = scanner.nextLine();
        float chance;
        while (true) {
            System.out.print("Введите шанс выпадения игрушки, %: ");
            try {
                chance = Float.parseFloat(scanner.nextLine());
                if (chance >= 0.1 && chance <= 99.9) break;
                else {
                    System.out.println("Шанс выпадения не может быть меньше 0,1 % или больше 99,9%");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат ввода данных");
            }
        }
        ToysManager.updateToyChance(id, chance);
    }
}