package person;

import util.Format;
import util.InputHelper;

public class NhanVien extends Nguoi {
    private String chucVu;
    private double luong;
    private String ngayVaoLam;

    public NhanVien() {
        super();
    }

    public NhanVien(String maNguoi, String hoTen, String soDienThoai, String cccd, String email,
                    String chucVu, double luong, String ngayVaoLam) {
        super(maNguoi, hoTen, soDienThoai, cccd, email);
        this.chucVu = chucVu;
        this.luong = luong;
        this.ngayVaoLam = ngayVaoLam;
    }

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }

    public double getLuong() { return luong; }
    public void setLuong(double luong) { this.luong = luong; }

    public String getNgayVaoLam() { return ngayVaoLam; }
    public void setNgayVaoLam(String ngayVaoLam) { this.ngayVaoLam = ngayVaoLam; }

    public double tinhLuong() {
        return this.luong;
    }

    @Override
    public void nhapThongTin() {
        super.nhapThongTin();
        this.chucVu = InputHelper.getString("Nhap chuc vu: ");
        this.luong = InputHelper.getPositiveDouble("Nhap muc luong co ban (VND): ");
        this.ngayVaoLam = InputHelper.getString("Nhap ngay vao lam (dd/MM/yyyy): ");
    }

    @Override
    public void hienThiThongTin() {
        System.out.println("----------------------------------------");
        System.out.println("           THONG TIN NHAN VIEN          ");
        System.out.println("----------------------------------------");
        System.out.println("Ma nhan vien    : " + getMaNguoi());
        System.out.println("Ho ten          : " + getHoTen());
        System.out.println("So dien thoai   : " + getSoDienThoai());
        System.out.println("CCCD            : " + getCccd());
        System.out.println("Email           : " + getEmail());
        System.out.println("Chuc vu         : " + chucVu);
        System.out.printf("Muc luong co ban: %,.0f VND%n", luong);
        System.out.println("Ngay vao lam    : " + ngayVaoLam);
        System.out.printf("Tong thuc nhan  : %,.0f VND%n", tinhLuong());
        System.out.println("----------------------------------------");
    }

    @Override
    public String toFileString() {
        return String.join("|",
                "NhanVien",
                Format.clean(getMaNguoi()),
                Format.clean(getHoTen()),
                Format.clean(getSoDienThoai()),
                Format.clean(getCccd()),
                Format.clean(getEmail()),
                Format.clean(chucVu),
                String.valueOf(luong),
                Format.clean(ngayVaoLam));
    }

    public static NhanVien fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 9) {
            return null;
        }
        try {
            String loai = p[0].trim();
            String ma = p[1].trim();
            String ten = p[2].trim();
            String sdt = p[3].trim();
            String cccd = p[4].trim();
            String email = p[5].trim();
            String chucVu = p[6].trim();
            double luong = Double.parseDouble(p[7].trim());
            String ngayVaoLam = p[8].trim();

            if (loai.equalsIgnoreCase("QuanLy") && p.length >= 11) {
                double phuCap = Double.parseDouble(p[9].trim());
                int kinhNghiem = Integer.parseInt(p[10].trim());
                return new QuanLy(ma, ten, sdt, cccd, email, chucVu, luong, ngayVaoLam, phuCap, kinhNghiem);
            }
            return new NhanVien(ma, ten, sdt, cccd, email, chucVu, luong, ngayVaoLam);
        } catch (Exception e) {
            return null;
        }
    }
}
