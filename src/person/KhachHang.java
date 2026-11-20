package person;

import util.Format;
import util.InputHelper;

public class KhachHang extends Nguoi {
    private String diaChi;
    private String quocTich;
    private int diemTichLuy;

    public KhachHang() {
        super();
        this.diemTichLuy = 0;
    }

    public KhachHang(String maNguoi, String hoTen, String soDienThoai, String cccd, String email,
                     String diaChi, String quocTich, int diemTichLuy) {
        super(maNguoi, hoTen, soDienThoai, cccd, email);
        this.diaChi = diaChi;
        this.quocTich = quocTich;
        this.diemTichLuy = diemTichLuy;
    }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getQuocTich() { return quocTich; }
    public void setQuocTich(String quocTich) { this.quocTich = quocTich; }

    public int getDiemTichLuy() { return diemTichLuy; }
    public void setDiemTichLuy(int diemTichLuy) { this.diemTichLuy = diemTichLuy; }

    public void capNhatDiemTichLuy(int diem) {
        this.diemTichLuy += diem;
        if (this.diemTichLuy < 0) {
            this.diemTichLuy = 0;
        }
        System.out.println("-> Cap nhat thanh cong! Khach hang [" + getHoTen() + "] co diem tich luy moi: " + this.diemTichLuy);
    }

    @Override
    public void nhapThongTin() {
        super.nhapThongTin();
        this.diaChi = InputHelper.getString("Nhap dia chi: ");
        this.quocTich = InputHelper.getString("Nhap quoc tich: ");
        this.diemTichLuy = InputHelper.getInt("Nhap diem tich luy: ");
    }

    @Override
    public void hienThiThongTin() {
        System.out.println("----------------------------------------");
        System.out.println("           THONG TIN KHACH HANG         ");
        System.out.println("----------------------------------------");
        System.out.println("Ma khach hang   : " + getMaNguoi());
        System.out.println("Ho ten          : " + getHoTen());
        System.out.println("So dien thoai   : " + getSoDienThoai());
        System.out.println("CCCD            : " + getCccd());
        System.out.println("Email           : " + getEmail());
        System.out.println("Dia chi         : " + diaChi);
        System.out.println("Quoc tich       : " + quocTich);
        System.out.println("Diem tich luy   : " + diemTichLuy);
        System.out.println("----------------------------------------");
    }

    @Override
    public String toFileString() {
        return String.join("|",
                Format.clean(getMaNguoi()),
                Format.clean(getHoTen()),
                Format.clean(getSoDienThoai()),
                Format.clean(getCccd()),
                Format.clean(getEmail()),
                Format.clean(diaChi),
                Format.clean(quocTich),
                String.valueOf(diemTichLuy));
    }

    public static KhachHang fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 4) {
            return null;
        }
        String ma = p[0].trim();
        String ten = p[1].trim();
        // Ho tro ca dinh dang file cu 4 cot (id|name|cccd|phone) lan dinh dang 8 cot
        if (p.length < 8) {
            return new KhachHang(ma, ten, p[3].trim(), p[2].trim(), "", "Viet Nam", "Viet Nam", 0);
        }
        int diem = 0;
        try {
            diem = Integer.parseInt(p[7].trim());
        } catch (NumberFormatException e) {
            diem = 0;
        }
        return new KhachHang(ma, ten, p[2].trim(), p[3].trim(), p[4].trim(), p[5].trim(), p[6].trim(), diem);
    }
}
