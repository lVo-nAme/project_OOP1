package room;

public class PhongTieuChuan extends Phong {

    public PhongTieuChuan(String maPhong, String soPhong, int tang,
                          double giaCoBan, TrangThaiPhong trangThai,
                          int soNguoiToiDa, int soGiuong, double dienTich) {
        super(maPhong, soPhong, tang, giaCoBan, trangThai, soNguoiToiDa, soGiuong, dienTich);
    }

    @Override
    public double tinhTienPhong(int soDem) {
        return getGiaCoBan() * soDem;
    }

    @Override
    public String getLoaiPhong() {
        return "Tieu chuan";
    }

    @Override
    public void hienThiThongTin() {
        System.out.println("===== PHONG TIEU CHUAN =====");
        super.hienThiThongTin();
    }

    @Override
    public String toFileString() {
        return String.join("|",
                "TieuChuan",
                getMaPhong(),
                getSoPhong(),
                String.valueOf(getTang()),
                String.valueOf(getGiaCoBan()),
                getTrangThai().name(),
                String.valueOf(getSoNguoiToiDa()),
                String.valueOf(getSoGiuong()),
                String.valueOf(getDienTich()));
    }
}
