package room;

public abstract class Phong {
    private String maPhong;
    private String soPhong;
    private int tang;
    private double giaCoBan;
    private TrangThaiPhong trangThai;
    private int soNguoiToiDa;
    private int soGiuong;
    private double dienTich;

    private static int tongSoPhong = 0;

    public Phong(String maPhong, String soPhong, int tang,
                 double giaCoBan, TrangThaiPhong trangThai,
                 int soNguoiToiDa, int soGiuong, double dienTich) {
        this.maPhong = maPhong;
        this.soPhong = soPhong;
        this.tang = tang;
        this.giaCoBan = giaCoBan;
        this.trangThai = trangThai;
        this.soNguoiToiDa = soNguoiToiDa;
        this.soGiuong = soGiuong;
        this.dienTich = dienTich;
        tongSoPhong++;
    }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }

    public String getSoPhong() { return soPhong; }
    public void setSoPhong(String soPhong) { this.soPhong = soPhong; }

    public int getTang() { return tang; }
    public void setTang(int tang) { this.tang = tang; }

    public double getGiaCoBan() { return giaCoBan; }
    public void setGiaCoBan(double giaCoBan) { this.giaCoBan = giaCoBan; }

    public TrangThaiPhong getTrangThai() { return trangThai; }
    public void setTrangThai(TrangThaiPhong trangThai) { this.trangThai = trangThai; }

    public int getSoNguoiToiDa() { return soNguoiToiDa; }
    public void setSoNguoiToiDa(int soNguoiToiDa) { this.soNguoiToiDa = soNguoiToiDa; }

    public int getSoGiuong() { return soGiuong; }
    public void setSoGiuong(int soGiuong) { this.soGiuong = soGiuong; }

    public double getDienTich() { return dienTich; }
    public void setDienTich(double dienTich) { this.dienTich = dienTich; }

    public static int getTongSoPhong() {
        return tongSoPhong;
    }

    public static void datLaiTongSoPhong(int giaTri) {
        tongSoPhong = giaTri;
    }

    // Phuong thuc truu tuong (abstract)
    public abstract double tinhTienPhong(int soDem);

    public abstract String getLoaiPhong();

    public abstract String toFileString();

    public boolean kiemTraTrong() {
        return trangThai == TrangThaiPhong.TRONG;
    }

    public void hienThiThongTin() {
        System.out.println("Ma phong     : " + maPhong);
        System.out.println("So phong     : " + soPhong);
        System.out.println("Tang         : " + tang);
        System.out.println("Loai phong   : " + getLoaiPhong());
        System.out.printf("Gia co ban   : %,.0f VND/dem%n", giaCoBan);
        System.out.println("Trang thai   : " + trangThai.getMoTa());
        System.out.println("Suc chua     : " + soNguoiToiDa + " nguoi");
        System.out.println("So giuong    : " + soGiuong);
        System.out.println("Dien tich    : " + dienTich + " m2");
    }

    // Doc 1 dong tu file room.txt tao doi tuong Phong tuong ung (da hinh)
    public static Phong fromFileString(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 9) {
            return null;
        }
        try {
            String loai = p[0].trim();
            String maPhong = p[1].trim();
            String soPhong = p[2].trim();
            int tang = Integer.parseInt(p[3].trim());
            double giaCoBan = Double.parseDouble(p[4].trim());
            TrangThaiPhong trangThai = TrangThaiPhong.valueOf(p[5].trim());
            int soNguoi = Integer.parseInt(p[6].trim());
            int soGiuong = Integer.parseInt(p[7].trim());
            double dienTich = Double.parseDouble(p[8].trim());

            if (loai.equalsIgnoreCase("CaoCap") && p.length >= 10) {
                boolean coBanCong = Boolean.parseBoolean(p[9].trim());
                return new PhongCaoCap(maPhong, soPhong, tang, giaCoBan, trangThai, soNguoi, soGiuong, dienTich, coBanCong);
            } else if (loai.equalsIgnoreCase("Suite") && p.length >= 11) {
                boolean coPhongKhach = Boolean.parseBoolean(p[9].trim());
                boolean coBonTam = Boolean.parseBoolean(p[10].trim());
                return new PhongSuite(maPhong, soPhong, tang, giaCoBan, trangThai, soNguoi, soGiuong, dienTich, coPhongKhach, coBonTam);
            } else {
                return new PhongTieuChuan(maPhong, soPhong, tang, giaCoBan, trangThai, soNguoi, soGiuong, dienTich);
            }
        } catch (Exception e) {
            return null;
        }
    }
}
