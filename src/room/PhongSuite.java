package room;

public class PhongSuite extends Phong {
    private boolean coPhongKhach;
    private boolean coBonTam;

    public PhongSuite(String maPhong, String soPhong, int tang,
                      double giaCoBan, TrangThaiPhong trangThai,
                      int soNguoiToiDa, int soGiuong, double dienTich,
                      boolean coPhongKhach, boolean coBonTam) {
        super(maPhong, soPhong, tang, giaCoBan, trangThai, soNguoiToiDa, soGiuong, dienTich);
        this.coPhongKhach = coPhongKhach;
        this.coBonTam = coBonTam;
    }

    public boolean isCoPhongKhach() { return coPhongKhach; }
    public void setCoPhongKhach(boolean coPhongKhach) { this.coPhongKhach = coPhongKhach; }

    public boolean isCoBonTam() { return coBonTam; }
    public void setCoBonTam(boolean coBonTam) { this.coBonTam = coBonTam; }

    @Override
    public double tinhTienPhong(int soDem) {
        // Phong Suite phu thu 25%
        return (getGiaCoBan() * soDem) * 1.25;
    }

    @Override
    public String getLoaiPhong() {
        return "Suite";
    }

    @Override
    public void hienThiThongTin() {
        System.out.println("===== PHONG SUITE =====");
        super.hienThiThongTin();
        System.out.println("Phong khach  : " + (coPhongKhach ? "Co" : "Khong"));
        System.out.println("Bon tam      : " + (coBonTam ? "Co" : "Khong"));
    }

    @Override
    public String toFileString() {
        return String.join("|",
                "Suite",
                getMaPhong(),
                getSoPhong(),
                String.valueOf(getTang()),
                String.valueOf(getGiaCoBan()),
                getTrangThai().name(),
                String.valueOf(getSoNguoiToiDa()),
                String.valueOf(getSoGiuong()),
                String.valueOf(getDienTich()),
                String.valueOf(coPhongKhach),
                String.valueOf(coBonTam));
    }
}
