package bill;

import java.time.LocalDate;
import util.Format;

public class KhuyenMai {
    private String maKhuyenMai;
    private String tenKhuyenMai;
    private double phanTramGiam;
    private double dieuKienToiThieu;
    private double tienGiamToiDa;
    private LocalDate ngayBatDau;
    private LocalDate ngayKetThuc;

    public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, double phanTramGiam,
                     double dieuKienToiThieu, double tienGiamToiDa,
                     LocalDate ngayBatDau, LocalDate ngayKetThuc) {
        this.maKhuyenMai = maKhuyenMai;
        this.tenKhuyenMai = tenKhuyenMai;
        this.phanTramGiam = phanTramGiam;
        this.dieuKienToiThieu = dieuKienToiThieu;
        this.tienGiamToiDa = tienGiamToiDa;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
    }

    public KhuyenMai(String maKhuyenMai, String tenKhuyenMai, double phanTramGiam,
                     double dieuKienToiThieu, double tienGiamToiDa,
                     String ngayBatDauStr, String ngayKetThucStr) {
        this(maKhuyenMai, tenKhuyenMai, phanTramGiam, dieuKienToiThieu, tienGiamToiDa,
                LocalDate.parse(ngayBatDauStr.trim(), Format.DATE),
                LocalDate.parse(ngayKetThucStr.trim(), Format.DATE));
    }

    public boolean kiemTraHieuLuc(LocalDate ngay) {
        if (ngay == null || ngayBatDau == null || ngayKetThuc == null) {
            return false;
        }
        return !ngay.isBefore(ngayBatDau) && !ngay.isAfter(ngayKetThuc);
    }

    public double tinhTienGiam(double tongTien) {
        if (tongTien <= 0 || tongTien < dieuKienToiThieu) {
            return 0;
        }
        double giam = tongTien * phanTramGiam / 100.0;
        return Math.min(giam, tienGiamToiDa);
    }

    public void hienThiThongTin() {
        System.out.printf("[%s] %s - Giam %.0f%% (toi da %s, don tu %s) | %s -> %s%n",
                maKhuyenMai, tenKhuyenMai, phanTramGiam,
                Format.money(tienGiamToiDa), Format.money(dieuKienToiThieu),
                Format.date(ngayBatDau), Format.date(ngayKetThuc));
    }

    public String getMaKhuyenMai() { return maKhuyenMai; }
    public String getTenKhuyenMai() { return tenKhuyenMai; }
    public double getPhanTramGiam() { return phanTramGiam; }
    public double getDieuKienToiThieu() { return dieuKienToiThieu; }
    public double getTienGiamToiDa() { return tienGiamToiDa; }
    public LocalDate getNgayBatDau() { return ngayBatDau; }
    public LocalDate getNgayKetThuc() { return ngayKetThuc; }
    public String getNgayBatDauStr() { return Format.date(ngayBatDau); }
    public String getNgayKetThucStr() { return Format.date(ngayKetThuc); }

    public void setTenKhuyenMai(String tenKhuyenMai) {
        if (tenKhuyenMai != null && !tenKhuyenMai.trim().isEmpty()) {
            this.tenKhuyenMai = tenKhuyenMai.trim();
        }
    }

    public void setPhanTramGiam(double phanTramGiam) {
        if (phanTramGiam >= 0 && phanTramGiam <= 100) {
            this.phanTramGiam = phanTramGiam;
        }
    }

    public void setDieuKienToiThieu(double dieuKienToiThieu) {
        if (dieuKienToiThieu >= 0) {
            this.dieuKienToiThieu = dieuKienToiThieu;
        }
    }

    public void setTienGiamToiDa(double tienGiamToiDa) {
        if (tienGiamToiDa >= 0) {
            this.tienGiamToiDa = tienGiamToiDa;
        }
    }

    public void setNgayBatDau(LocalDate ngayBatDau) {
        if (ngayBatDau != null) {
            this.ngayBatDau = ngayBatDau;
        }
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        if (ngayKetThuc != null) {
            this.ngayKetThuc = ngayKetThuc;
        }
    }

    public String toFileString() {
        return String.join("|",
                Format.clean(maKhuyenMai),
                Format.clean(tenKhuyenMai),
                String.valueOf(phanTramGiam),
                String.valueOf(dieuKienToiThieu),
                String.valueOf(tienGiamToiDa),
                Format.date(ngayBatDau),
                Format.date(ngayKetThuc));
    }

    public static KhuyenMai fromFileString(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String normalized = line.contains("|") ? line : line.replace(",", "|");
        String[] p = normalized.split("\\|", -1);
        if (p.length < 7) {
            return null;
        }
        try {
            String ma = p[0].trim();
            String ten = p[1].trim();
            double pt = Double.parseDouble(p[2].trim());
            double dk = Double.parseDouble(p[3].trim());
            double td = Double.parseDouble(p[4].trim());
            LocalDate bd = LocalDate.parse(p[5].trim(), Format.DATE);
            LocalDate kt = LocalDate.parse(p[6].trim(), Format.DATE);
            return new KhuyenMai(ma, ten, pt, dk, td, bd, kt);
        } catch (Exception e) {
            return null;
        }
    }
}