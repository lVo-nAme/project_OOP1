package util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputHelper {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static String getString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String result = SCANNER.nextLine().trim();
            if (!result.isEmpty()) {
                return result;
            }
            System.out.println("Loi: Khong duoc de trong. Vui long nhap lai!");
        }
    }

    public static String getOptionalString(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    public static int getInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(SCANNER.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Loi: Vui long nhap so nguyen hop le!");
            }
        }
    }

    public static int getIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = getInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Loi: Gia tri phai tu " + min + " den " + max + "!");
        }
    }

    public static int getPositiveInt(String prompt) {
        while (true) {
            int value = getInt(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Loi: Gia tri phai lon hon 0!");
        }
    }

    public static double getDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(SCANNER.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Loi: Vui long nhap so thuc hop le!");
            }
        }
    }

    public static double getPositiveDouble(String prompt) {
        while (true) {
            double value = getDouble(prompt);
            if (value > 0) {
                return value;
            }
            System.out.println("Loi: Gia tri phai lon hon 0!");
        }
    }

    public static LocalDate getDate(String prompt, LocalDate defaultValue) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine().trim();
            if (line.isEmpty() && defaultValue != null) {
                return defaultValue;
            }
            try {
                return LocalDate.parse(line, Format.DATE);
            } catch (DateTimeParseException e) {
                System.out.println("Loi: Ngay khong hop le, vui long nhap dang dd/MM/yyyy!");
            }
        }
    }

    public static boolean getYesNo(String prompt) {
        while (true) {
            System.out.print(prompt + " (y/n): ");
            String line = SCANNER.nextLine().trim().toLowerCase();
            if (line.equals("y") || line.equals("yes") || line.equals("c") || line.equals("co")) {
                return true;
            }
            if (line.equals("n") || line.equals("no") || line.equals("k") || line.equals("khong")) {
                return false;
            }
            System.out.println("Loi: Vui long nhap y hoac n!");
        }
    }
}
