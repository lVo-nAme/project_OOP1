package fileio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Doc/ghi file van ban su dung mang String[] co ban (khong dung ArrayList / Iterator).
 */
public class FileHandler {
    private static final String DATA_DIR = "data";
    private static final int MAX_LINES = 500;

    private static void ensureDirectoryExists() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void writeLinesToFile(String fileName, String[] lines, boolean append) {
        ensureDirectoryExists();
        File file = new File(DATA_DIR, fileName);

        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(file, append), StandardCharsets.UTF_8))) {
            for (int i = 0; i < lines.length; i++) {
                if (lines[i] != null) {
                    writer.println(lines[i]);
                }
            }
        } catch (IOException e) {
            System.out.println("Loi khi ghi file " + fileName + ": " + e.getMessage());
        }
    }

    public static String[] readLinesFromFile(String fileName) {
        File file = new File(DATA_DIR, fileName);
        if (!file.exists()) {
            return new String[0];
        }

        String[] temp = new String[MAX_LINES];
        int count = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null && count < MAX_LINES) {
                temp[count++] = currentLine;
            }
        } catch (IOException e) {
            System.out.println("Loi khi doc file " + fileName + ": " + e.getMessage());
        }

        String[] result = new String[count];
        for (int i = 0; i < count; i++) {
            result[i] = temp[i];
        }
        return result;
    }
}
