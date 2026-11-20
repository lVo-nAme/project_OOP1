package booking;

import util.Format;

public class Service {
    private String serviceId;
    private String serviceName;
    private double price;
    private String unit;

    public Service(String serviceId, String serviceName, double price, String unit) {
        this.serviceId = Format.clean(serviceId);
        this.serviceName = Format.clean(serviceName);
        this.price = price;
        this.unit = Format.clean(unit);
    }

    public String getServiceId() { return serviceId; }
    public String getServiceName() { return serviceName; }
    public double getPrice() { return price; }
    public String getUnit() { return unit; }

    public void setServiceName(String serviceName) { this.serviceName = Format.clean(serviceName); }
    public void setPrice(double price) { this.price = price; }
    public void setUnit(String unit) { this.unit = Format.clean(unit); }

    public String toFileString() {
        return String.join("|", serviceId, serviceName, String.valueOf(price), unit);
    }

    public static Service fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 4) {
            return null;
        }
        try {
            return new Service(p[0], p[1], Double.parseDouble(p[2]), p[3]);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
