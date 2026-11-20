package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Format {
    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String money(double value) {
        return String.format(Locale.US, "%,.0f VND", value);
    }

    public static String date(LocalDate date) {
        return date == null ? "-" : date.format(DATE);
    }

    public static String clean(String s) {
        return s == null ? "" : s.replace("|", "/").trim();
    }

    // Sinh ma tu dong tu mang String[] (khong dung Iterable / Iterator)
    public static String nextId(String prefix, String[] existingIds, int count) {
        int max = 0;
        for (int i = 0; i < count; i++) {
            String id = existingIds[i];
            if (id != null && id.startsWith(prefix)) {
                try {
                    int num = Integer.parseInt(id.substring(prefix.length()));
                    if (num > max) {
                        max = num;
                    }
                } catch (NumberFormatException e) {
                    // Bo qua ma khong dung dinh dang so
                }
            }
        }
        return prefix + String.format("%03d", max + 1);
    }
}
