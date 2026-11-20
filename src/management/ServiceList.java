package management;

import booking.Service;
import fileio.FileHandler;
import util.Format;

/**
 * Lop mang doi tuong Service[] (khong dung ArrayList, Vector, LinkedList).
 */
public class ServiceList {
    private static final String FILE = "services.txt";
    private static final int MAX = 100;

    private Service[] services = new Service[MAX];
    private int count = 0;

    public void load() {
        count = 0;
        String[] lines = FileHandler.readLinesFromFile(FILE);
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line == null || line.trim().isEmpty() || line.startsWith("#")) {
                continue;
            }
            Service s = Service.fromFileString(line);
            if (s != null && count < MAX) {
                services[count++] = s;
            }
        }
    }

    public void save() {
        String[] lines = new String[count];
        for (int i = 0; i < count; i++) {
            lines[i] = services[i].toFileString();
        }
        FileHandler.writeLinesToFile(FILE, lines, false);
    }

    public Service[] getAll() {
        Service[] result = new Service[count];
        for (int i = 0; i < count; i++) {
            result[i] = services[i];
        }
        return result;
    }

    public int getCount() {
        return count;
    }

    public String nextId() {
        String[] ids = new String[count];
        for (int i = 0; i < count; i++) {
            ids[i] = services[i].getServiceId();
        }
        return Format.nextId("DV", ids, count);
    }

    public Service findById(String id) {
        if (id == null) return null;
        for (int i = 0; i < count; i++) {
            if (services[i].getServiceId().equalsIgnoreCase(id.trim())) {
                return services[i];
            }
        }
        return null;
    }

    public boolean add(Service s) {
        if (s == null || count >= MAX) {
            return false;
        }
        services[count++] = s;
        return true;
    }

    public boolean remove(String id) {
        if (id == null) return false;
        for (int i = 0; i < count; i++) {
            if (services[i].getServiceId().equalsIgnoreCase(id.trim())) {
                for (int j = i; j < count - 1; j++) {
                    services[j] = services[j + 1];
                }
                services[--count] = null;
                return true;
            }
        }
        return false;
    }
}
