package bill;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import payment.IThanhToan;
import room.Phong;
import util.Format;

/**
 * Hoa don khach san su dung mang tinh ChiTietHoaDon[] (khong dung ArrayList / Collection).
 */
public class HoaDon {
    public static final String CHUA_THANH_TOAN = "CHUA_THANH_TOAN";
    public static final String DA_THANH_TOAN = "DA_THANH_TOAN";
    private static final int MAX_CHI_TIET = 50;

    private static int tongSoHoaDon = 0;

    private String maHoaDon;
    private String maKhachHang;
    private String maPhong;
    private String maNguoiLap;
    private String tenNguoiLap;
    private LocalDate ngayLap;
    private LocalDate ngayNhanPhong;
    private LocalDate ngayTraPhong;
    private double donGiaPhongCoBan;
    private String trangThai;
    private String phuongThucThanhToan;
    private String maKhuyenMai;
    private double tienGiam;
    private double tongTienDaChot;

    private ChiTietHoaDon[] dsChiTiet;
    private int soChiTiet;

    public HoaDon(String maHoaDon, String maKhachHang, String maPhong, String maNguoiLap,
                  LocalDate ngayNhanPhong, double donGiaPhongCoBan) {
        this(maHoaDon, maKhachHang, maPhong, maNguoiLap, maNguoiLap, ngayNhanPhong, ngayNhanPhong, donGiaPhongCoBan);
    }

    public HoaDon(String maHoaDon, String maKhachHang, String maPhong, String maNguoiLap,
                  String tenNguoiLap, LocalDate ngayLap, LocalDate ngayNhanPhong, double donGiaPhongCoBan) {
        this.maHoaDon = maHoaDon;
        this.maKhachHang = maKhachHang;
        this.maPhong = maPhong;
        this.maNguoiLap = maNguoiLap;
        this.tenNguoiLap = (tenNguoiLap == null || tenNguoiLap.trim().isEmpty()) ? maNguoiLap : tenNguoiLap;
        this.ngayLap = (ngayLap != null) ? ngayLap : (ngayNhanPhong != null ? ngayNhanPhong : LocalDate.now());
        this.ngayNhanPhong = ngayNhanPhong;
        this.ngayTraPhong = null;
        this.donGiaPhongCoBan = donGiaPhongCoBan;
        this.trangThai = CHUA_THANH_TOAN;
        this.phuongThucThanhToan = "";
        this.maKhuyenMai = "";
        this.tienGiam = 0;
        this.tongTienDaChot = 0;
        this.dsChiTiet = new ChiTietHoaDon[MAX_CHI_TIET];
        this.soChiTiet = 0;
        tongSoHoaDon++;
    }

    public static int getTongSoHoaDon() {
        return tongSoHoaDon;
    }

    public static void datLaiTongSoHoaDon(int giaTri) {
        tongSoHoaDon = giaTri;
    }

    public boolean dangMo() {
        return CHUA_THANH_TOAN.equals(trangThai);
    }

    public boolean daThanhToan() {
        return DA_THANH_TOAN.equals(trangThai);
    }

    public String getTrangThaiChu() {
        return daThanhToan() ? "Da thanh toan" : "Chua thanh toan";
    }

    public long tinhSoDem(LocalDate ngayTraDuKien) {
        LocalDate moc = (ngayTraPhong != null) ? ngayTraPhong : ngayTraDuKien;
        if (moc == null || ngayNhanPhong == null) {
            return 1;
        }
        long dem = ChronoUnit.DAYS.between(ngayNhanPhong, moc);
        return dem <= 0 ? 1 : dem;
    }

    // Tinh tien phong dua tren da hinh cua Phong (PhongTieuChuan / PhongCaoCap / PhongSuite)
    public double tinhTienPhong(Phong phong, LocalDate ngayTraDuKien) {
        if (daThanhToan()) {
            for (int i = 0; i < soChiTiet; i++) {
                if (ChiTietHoaDon.LOAI_TIEN_PHONG.equals(dsChiTiet[i].getLoaiChiTiet())) {
                    return dsChiTiet[i].tinhTongCong();
                }
            }
        }
        int soDem = (int) tinhSoDem(ngayTraDuKien);
        if (phong != null) {
            return phong.tinhTienPhong(soDem);
        }
        return donGiaPhongCoBan * soDem;
    }

    public void themChiTiet(ChiTietHoaDon ct) {
        if (ct == null || soChiTiet >= MAX_CHI_TIET) {
            return;
        }
        if (ChiTietHoaDon.LOAI_DICH_VU.equals(ct.getLoaiChiTiet())) {
            for (int i = 0; i < soChiTiet; i++) {
                if (ChiTietHoaDon.LOAI_DICH_VU.equals(dsChiTiet[i].getLoaiChiTiet())
                        && dsChiTiet[i].getMaDichVu().equalsIgnoreCase(ct.getMaDichVu())) {
                    dsChiTiet[i].setSoLuong(dsChiTiet[i].getSoLuong() + ct.getSoLuong());
                    return;
                }
            }
        }
        dsChiTiet[soChiTiet++] = ct;
    }

    public boolean suaChiTiet(String maChiTiet, int soLuongMoi, double donGiaMoi) {
        if (maChiTiet == null || daThanhToan()) {
            return false;
        }
        for (int i = 0; i < soChiTiet; i++) {
            if (dsChiTiet[i].getMaChiTiet().equalsIgnoreCase(maChiTiet.trim())) {
                if (soLuongMoi > 0) dsChiTiet[i].setSoLuong(soLuongMoi);
                if (donGiaMoi >= 0) dsChiTiet[i].setDonGia(donGiaMoi);
                return true;
            }
        }
        return false;
    }

    public boolean xoaChiTiet(String maChiTiet) {
        if (maChiTiet == null || daThanhToan()) {
            return false;
        }
        for (int i = 0; i < soChiTiet; i++) {
            if (dsChiTiet[i].getMaChiTiet().equalsIgnoreCase(maChiTiet.trim())) {
                for (int j = i; j < soChiTiet - 1; j++) {
                    dsChiTiet[j] = dsChiTiet[j + 1];
                }
                dsChiTiet[--soChiTiet] = null;
                return true;
            }
        }
        return false;
    }

    public String taoMaChiTietTiepTheo() {
        return "CT" + String.format("%02d", soChiTiet + 1);
    }

    public double tinhTienDichVu() {
        double tong = 0;
        for (int i = 0; i < soChiTiet; i++) {
            if (ChiTietHoaDon.LOAI_DICH_VU.equals(dsChiTiet[i].getLoaiChiTiet())) {
                tong += dsChiTiet[i].tinhTongCong();
            }
        }
        return tong;
    }

    public boolean apDungKhuyenMai(KhuyenMai km, Phong phong, LocalDate ngayTraDuKien) {
        if (km == null || daThanhToan()) {
            return false;
        }
        LocalDate ngayKiemTra = (ngayTraDuKien != null) ? ngayTraDuKien : LocalDate.now();
        if (!km.kiemTraHieuLuc(ngayKiemTra)) {
            System.out.println("Ma khuyen mai da het han hoac chua den ngay ap dung!");
            return false;
        }
        double tamTinh = tinhTienPhong(phong, ngayTraDuKien) + tinhTienDichVu();
        if (tamTinh < km.getDieuKienToiThieu()) {
            System.out.println("Hoa don chua dat gia tri toi thieu " + Format.money(km.getDieuKienToiThieu()));
            return false;
        }
        this.maKhuyenMai = km.getMaKhuyenMai();
        this.tienGiam = km.tinhTienGiam(tamTinh);
        return true;
    }

    public void boKhuyenMai() {
        if (!daThanhToan()) {
            this.maKhuyenMai = "";
            this.tienGiam = 0;
        }
    }

    public double tinhTongThanhToan(Phong phong, LocalDate ngayTraDuKien) {
        if (daThanhToan() && tongTienDaChot > 0) {
            return tongTienDaChot;
        }
        double truocGiam = tinhTienPhong(phong, ngayTraDuKien) + tinhTienDichVu();
        double sauGiam = truocGiam - tienGiam;
        return sauGiam < 0 ? 0 : sauGiam;
    }

    public double getTongTien() {
        return tinhTongThanhToan(null, ngayTraPhong != null ? ngayTraPhong : LocalDate.now());
    }

    public boolean thanhToan(Phong phong, LocalDate ngayTra, IThanhToan phuongThuc) {
        if (phuongThuc == null || daThanhToan()) {
            return false;
        }
        double tongCanTra = tinhTongThanhToan(phong, ngayTra);
        if (!phuongThuc.thanhToan(tongCanTra)) {
            return false;
        }
        int soDem = (int) tinhSoDem(ngayTra);
        double tienPhong = tinhTienPhong(phong, ngayTra);
        double donGiaThucTe = tienPhong / soDem;

        this.ngayTraPhong = ngayTra;
        this.ngayLap = ngayTra;
        this.phuongThucThanhToan = phuongThuc.layPhuongThuc();
        this.tongTienDaChot = tongCanTra;
        this.trangThai = DA_THANH_TOAN;

        themChiTiet(new ChiTietHoaDon(
                taoMaChiTietTiepTheo(),
                ChiTietHoaDon.LOAI_TIEN_PHONG,
                maPhong,
                "Tien phong " + maPhong,
                ThoiDiemThanhToan.KHI_TRA_PHONG,
                soDem,
                donGiaThucTe,
                0));
        return true;
    }

    public void hienThiHoaDon(Phong phong, String tenKhachHang, LocalDate ngayTraDuKien) {
        LocalDate mocTra = (ngayTraPhong != null) ? ngayTraPhong : ngayTraDuKien;
        long soDem = tinhSoDem(mocTra);
        double tienPhong = tinhTienPhong(phong, mocTra);
        double tienDV = tinhTienDichVu();
        double tongCong = tinhTongThanhToan(phong, mocTra);

        System.out.println("\n================ HOA DON " + maHoaDon + " ================");
        System.out.println("Khach hang   : " + tenKhachHang + " (" + maKhachHang + ")");
        System.out.println("Phong        : " + maPhong + (phong != null ? " - " + phong.getLoaiPhong() : ""));
        System.out.println("Nhan vien lap: " + tenNguoiLap + " (" + maNguoiLap + ")");
        System.out.println("Ngay lap HD  : " + Format.date(ngayLap));
        System.out.println("Nhan phong   : " + Format.date(ngayNhanPhong));
        System.out.println("Tra phong    : " + Format.date(mocTra) + (daThanhToan() ? "" : " (du kien)"));
        System.out.println("Tien phong   : " + soDem + " dem = " + Format.money(tienPhong));

        System.out.println("--- Chi tiet dich vu ---");
        boolean coDV = false;
        for (int i = 0; i < soChiTiet; i++) {
            if (ChiTietHoaDon.LOAI_DICH_VU.equals(dsChiTiet[i].getLoaiChiTiet())) {
                dsChiTiet[i].hienThiChiTiet();
                coDV = true;
            }
        }
        if (!coDV) {
            System.out.println("  (Khong su dung dich vu)");
        } else {
            System.out.println("Tong dich vu : " + Format.money(tienDV));
        }

        if (tienGiam > 0) {
            System.out.println("Khuyen mai   : -" + Format.money(tienGiam) + " (" + maKhuyenMai + ")");
        }
        System.out.println("TONG CONG    : " + Format.money(tongCong));
        System.out.println("Trang thai   : " + (daThanhToan() ? "Da thanh toan (" + phuongThucThanhToan + ")" : "Dang o"));
        System.out.println("================================================");
    }

    public String getMaHoaDon() { return maHoaDon; }
    public String getMaKhachHang() { return maKhachHang; }
    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
    public String getMaNguoiLap() { return maNguoiLap; }
    public String getTenNguoiLap() { return tenNguoiLap; }
    public LocalDate getNgayLap() { return ngayLap; }
    public void setNgayLap(LocalDate ngayLap) { if (ngayLap != null) this.ngayLap = ngayLap; }
    public LocalDate getNgayNhanPhong() { return ngayNhanPhong; }
    public LocalDate getNgayTraPhong() { return ngayTraPhong; }
    public double getDonGiaPhongCoBan() { return donGiaPhongCoBan; }
    public String getTrangThai() { return trangThai; }
    public String getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public String getMaKhuyenMai() { return maKhuyenMai; }
    public double getTienGiam() { return tienGiam; }
    public ChiTietHoaDon[] getDsChiTiet() { return dsChiTiet; }
    public int getSoChiTiet() { return soChiTiet; }

    public String toFileString() {
        return String.join("|",
                Format.clean(maHoaDon),
                Format.clean(maKhachHang),
                Format.clean(maPhong),
                Format.clean(maNguoiLap),
                Format.clean(tenNguoiLap),
                Format.date(ngayLap),
                Format.date(ngayNhanPhong),
                ngayTraPhong == null ? "" : Format.date(ngayTraPhong),
                String.valueOf(donGiaPhongCoBan),
                trangThai,
                Format.clean(phuongThucThanhToan),
                Format.clean(maKhuyenMai),
                String.valueOf(tienGiam),
                String.valueOf(tongTienDaChot));
    }

    public static HoaDon fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length >= 14) {
            try {
                LocalDate lap = LocalDate.parse(p[5].trim(), Format.DATE);
                LocalDate nhan = LocalDate.parse(p[6].trim(), Format.DATE);
                LocalDate tra = p[7].trim().isEmpty() ? null : LocalDate.parse(p[7].trim(), Format.DATE);
                double gia = Double.parseDouble(p[8].trim());
                HoaDon hd = new HoaDon(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(), p[4].trim(), lap, nhan, gia);
                hd.ngayTraPhong = tra;
                hd.trangThai = p[9].trim();
                hd.phuongThucThanhToan = p[10].trim();
                hd.maKhuyenMai = p[11].trim();
                hd.tienGiam = Double.parseDouble(p[12].trim());
                hd.tongTienDaChot = Double.parseDouble(p[13].trim());
                return hd;
            } catch (Exception e) {
                return null;
            }
        }
        if (p.length >= 11) {
            try {
                LocalDate nhan = LocalDate.parse(p[4].trim(), Format.DATE);
                LocalDate tra = p[5].trim().isEmpty() ? null : LocalDate.parse(p[5].trim(), Format.DATE);
                double gia = Double.parseDouble(p[6].trim());
                HoaDon hd = new HoaDon(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(), p[3].trim(), nhan, nhan, gia);
                hd.ngayTraPhong = tra;
                hd.trangThai = p[7].trim();
                hd.phuongThucThanhToan = p[8].trim();
                hd.maKhuyenMai = p[9].trim();
                hd.tienGiam = Double.parseDouble(p[10].trim());
                return hd;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}