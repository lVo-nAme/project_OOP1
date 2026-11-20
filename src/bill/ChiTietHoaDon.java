package bill;

import util.Format;

/**
 * Chi tiet tung khoan trong hoa don (tien phong hoac dich vu).
 * Viet don gian, khong dung Exception phuc tap.
 */
public class ChiTietHoaDon {
    public static final String LOAI_TIEN_PHONG = "TIEN_PHONG";
    public static final String LOAI_DICH_VU = "DICH_VU";

    private String maChiTiet;
    private String loaiChiTiet;
    private String maDichVu;
    private String tenDichVu;
    private ThoiDiemThanhToan thoiDiemThanhToan;
    private int soLuong;
    private double donGia;
    private double thueSuatVAT;

    public ChiTietHoaDon(String maChiTiet, String loaiChiTiet, String maDichVu, String tenDichVu,
                         ThoiDiemThanhToan thoiDiemThanhToan, int soLuong, double donGia, double thueSuatVAT) {
        this.maChiTiet = maChiTiet;
        this.loaiChiTiet = loaiChiTiet;
        this.maDichVu = maDichVu;
        this.tenDichVu = tenDichVu;
        this.thoiDiemThanhToan = (thoiDiemThanhToan == null) ? ThoiDiemThanhToan.KHI_TRA_PHONG : thoiDiemThanhToan;
        this.soLuong = soLuong > 0 ? soLuong : 1;
        this.donGia = donGia >= 0 ? donGia : 0;
        this.thueSuatVAT = thueSuatVAT >= 0 ? thueSuatVAT : 0;
    }

    public double tinhThanhTien() {
        return soLuong * donGia;
    }

    public double tinhThue() {
        return tinhThanhTien() * thueSuatVAT / 100.0;
    }

    public double tinhTongCong() {
        return tinhThanhTien() + tinhThue();
    }

    public void hienThiChiTiet() {
        System.out.printf("  %-6s | %-22s | SL: %3d | Don gia: %14s | VAT: %4.0f%% | Cong: %14s (%s)%n",
                maChiTiet, tenDichVu, soLuong, Format.money(donGia), thueSuatVAT,
                Format.money(tinhTongCong()), thoiDiemThanhToan.getMoTa());
    }

    public String getMaChiTiet() { return maChiTiet; }
    public String getLoaiChiTiet() { return loaiChiTiet; }
    public String getMaDichVu() { return maDichVu; }
    public String getTenDichVu() { return tenDichVu; }
    public ThoiDiemThanhToan getThoiDiemThanhToan() { return thoiDiemThanhToan; }
    public int getSoLuong() { return soLuong; }
    public double getDonGia() { return donGia; }
    public double getThueSuatVAT() { return thueSuatVAT; }

    public void setSoLuong(int soLuong) { if (soLuong > 0) this.soLuong = soLuong; }
    public void setDonGia(double donGia) { if (donGia >= 0) this.donGia = donGia; }

    public String toFileString(String maHoaDon) {
        return String.join("|",
                Format.clean(maHoaDon),
                Format.clean(maChiTiet),
                Format.clean(loaiChiTiet),
                Format.clean(maDichVu),
                Format.clean(tenDichVu),
                thoiDiemThanhToan.name(),
                String.valueOf(soLuong),
                String.valueOf(donGia),
                String.valueOf(thueSuatVAT));
    }

    public static ChiTietHoaDon fromParts(String[] p, int startIndex) {
        if (p.length < startIndex + 8) {
            return null;
        }
        try {
            String maCT = p[startIndex].trim();
            String loai = p[startIndex + 1].trim();
            String maDV = p[startIndex + 2].trim();
            String tenDV = p[startIndex + 3].trim();
            ThoiDiemThanhToan td = ThoiDiemThanhToan.valueOf(p[startIndex + 4].trim());
            int sl = Integer.parseInt(p[startIndex + 5].trim());
            double gia = Double.parseDouble(p[startIndex + 6].trim());
            double vat = Double.parseDouble(p[startIndex + 7].trim());
            return new ChiTietHoaDon(maCT, loai, maDV, tenDV, td, sl, gia, vat);
        } catch (Exception e) {
            return null;
        }
    }
}
