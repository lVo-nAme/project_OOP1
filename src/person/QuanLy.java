package person;

import util.Format;
import util.InputHelper;

public class QuanLy extends NhanVien {
    private double phuCap;
    private int soNamKinhNghiem;

    public QuanLy() {
        super();
        setChucVu("Quan ly");
    }

    public QuanLy(String maNguoi, String hoTen, String soDienThoai, String cccd, String email,
                  String chucVu, double luong, String ngayVaoLam,
                  double phuCap, int soNamKinhNghiem) {
        super(maNguoi, hoTen, soDienThoai, cccd, email, chucVu, luong, ngayVaoLam);
        this.phuCap = phuCap;
        this.soNamKinhNghiem = soNamKinhNghiem;
    }

    public double getPhuCap() { return phuCap; }
    public void setPhuCap(double phuCap) { this.phuCap = phuCap; }

    public int getSoNamKinhNghiem() { return soNamKinhNghiem; }
    public void setSoNamKinhNghiem(int soNamKinhNghiem) { this.soNamKinhNghiem = soNamKinhNghiem; }

    @Override
    public double tinhLuong() {
        return getLuong() + this.phuCap;
    }

    public boolean duyetKhuyenMai() {
        System.out.println("\n--- [XET DUYET KHUYEN MAI - QUAN LY: " + getHoTen() + "] ---");
        String tenKM = InputHelper.getString("Nhap ten chuong trinh khuyen mai: ");
        double phanTram = InputHelper.getPositiveDouble("Nhap muc giam gia de xuat (%): ");
        boolean duyet = InputHelper.getYesNo("Ban co chap thuan phe duyet khuyen mai nay khong?");

        if (duyet) {
            System.out.println(">> KET QUA: Quan ly " + getHoTen() + " DA DUYET [" + tenKM + "] (Giam " + phanTram + "%).");
        } else {
            System.out.println(">> KET QUA: Quan ly " + getHoTen() + " TU CHOI [" + tenKM + "].");
        }
        return duyet;
    }

    public void xemBaoCaoDoanhThu(double doanhThuThucTe, int soHoaDonDaThanhToan, int tongSoPhong) {
        System.out.println("\n========================================================");
        System.out.println("        BAO CAO DOANH THU & HIEU SUAT HOAT DONG         ");
        System.out.println("========================================================");
        System.out.println("Nguoi phu trach duyet    : " + getHoTen() + " (" + getChucVu() + ")");
        System.out.println("Tham nien kinh nghiem    : " + this.soNamKinhNghiem + " nam");
        System.out.println("Tong so phong hien co    : " + tongSoPhong + " phong");
        System.out.println("So hoa don da thanh toan : " + soHoaDonDaThanhToan + " hoa don");
        System.out.printf("Tong doanh thu thuc te   : %,.0f VND%n", doanhThuThucTe);
        System.out.println("========================================================");
    }

    @Override
    public void nhapThongTin() {
        super.nhapThongTin();
        this.phuCap = InputHelper.getPositiveDouble("Nhap muc phu cap quan ly (VND): ");
        this.soNamKinhNghiem = InputHelper.getInt("Nhap so nam kinh nghiem: ");
    }

    @Override
    public void hienThiThongTin() {
        System.out.println("----------------------------------------");
        System.out.println("           THONG TIN QUAN LY            ");
        System.out.println("----------------------------------------");
        System.out.println("Ma quan ly      : " + getMaNguoi());
        System.out.println("Ho ten          : " + getHoTen());
        System.out.println("So dien thoai   : " + getSoDienThoai());
        System.out.println("CCCD            : " + getCccd());
        System.out.println("Email           : " + getEmail());
        System.out.println("Chuc vu         : " + getChucVu());
        System.out.printf("Muc luong co ban: %,.0f VND%n", getLuong());
        System.out.printf("Phu cap quan ly : %,.0f VND%n", phuCap);
        System.out.printf("Tong thuc nhan  : %,.0f VND%n", tinhLuong());
        System.out.println("Kinh nghiem     : " + soNamKinhNghiem + " nam");
        System.out.println("Ngay vao lam    : " + getNgayVaoLam());
        System.out.println("----------------------------------------");
    }

    @Override
    public String toFileString() {
        return String.join("|",
                "QuanLy",
                Format.clean(getMaNguoi()),
                Format.clean(getHoTen()),
                Format.clean(getSoDienThoai()),
                Format.clean(getCccd()),
                Format.clean(getEmail()),
                Format.clean(getChucVu()),
                String.valueOf(getLuong()),
                Format.clean(getNgayVaoLam()),
                String.valueOf(phuCap),
                String.valueOf(soNamKinhNghiem));
    }
}
