package person;

import util.InputHelper;

public abstract class Nguoi {
    private String maNguoi;
    private String hoTen;
    private String soDienThoai;
    private String cccd;
    private String email;

    public Nguoi() {
    }

    public Nguoi(String maNguoi, String hoTen, String soDienThoai, String cccd, String email) {
        this.maNguoi = maNguoi;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
        this.cccd = cccd;
        this.email = email;
    }

    public String getMaNguoi() { return maNguoi; }
    public void setMaNguoi(String maNguoi) { this.maNguoi = maNguoi; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public void nhapThongTin() {
        this.maNguoi = InputHelper.getString("Nhap ma dinh danh: ");
        this.hoTen = InputHelper.getString("Nhap ho ten: ");
        this.soDienThoai = InputHelper.getString("Nhap so dien thoai: ");
        this.cccd = InputHelper.getString("Nhap CCCD: ");
        this.email = InputHelper.getString("Nhap email: ");
    }

    // Phuong thuc truu tuong (abstract)
    public abstract void hienThiThongTin();

    public abstract String toFileString();
}
