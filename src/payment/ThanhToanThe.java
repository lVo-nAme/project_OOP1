package payment;

public class ThanhToanThe implements IThanhToan {
    private String loaiThe;
    private String soTheRutGon;
    private String maGiaoDich;

    public ThanhToanThe(String loaiThe, String soTheRutGon, String maGiaoDich) {
        this.loaiThe = loaiThe;
        this.soTheRutGon = soTheRutGon;
        this.maGiaoDich = maGiaoDich;
    }

    public String getLoaiThe() { return loaiThe; }
    public String getSoTheRutGon() { return soTheRutGon; }
    public String getMaGiaoDich() { return maGiaoDich; }

    @Override
    public boolean thanhToan(double soTien) {
        if (soTien <= 0 || loaiThe == null || loaiThe.trim().isEmpty()
                || soTheRutGon == null || soTheRutGon.trim().isEmpty()
                || maGiaoDich == null || maGiaoDich.trim().isEmpty()) {
            System.out.println("Loi: Thong tin the thanh toan khong hop le!");
            return false;
        }
        return true;
    }

    @Override
    public String layPhuongThuc() {
        return "The " + loaiThe + " (*" + soTheRutGon + ")";
    }

    @Override
    public void hienThiThongTin() {
        System.out.println("Phuong thuc  : Thanh toan bang the");
        System.out.println("Loai the     : " + loaiThe);
        System.out.println("So the       : " + soTheRutGon);
        System.out.println("Ma giao dich : " + maGiaoDich);
    }
}
