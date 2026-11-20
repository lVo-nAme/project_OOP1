package payment;

public class ThanhToanChuyenKhoan implements IThanhToan {
    private String nganHang;
    private String maGiaoDich;

    public ThanhToanChuyenKhoan(String nganHang, String maGiaoDich) {
        this.nganHang = nganHang;
        this.maGiaoDich = maGiaoDich;
    }

    public String getNganHang() { return nganHang; }
    public String getMaGiaoDich() { return maGiaoDich; }

    @Override
    public boolean thanhToan(double soTien) {
        if (soTien <= 0 || nganHang == null || nganHang.trim().isEmpty()
                || maGiaoDich == null || maGiaoDich.trim().isEmpty()) {
            System.out.println("Loi: Thong tin chuyen khoan khong hop le!");
            return false;
        }
        return true;
    }

    @Override
    public String layPhuongThuc() {
        return "Chuyen khoan (" + nganHang + ")";
    }

    @Override
    public void hienThiThongTin() {
        System.out.println("Phuong thuc  : Chuyen khoan");
        System.out.println("Ngan hang    : " + nganHang);
        System.out.println("Ma giao dich : " + maGiaoDich);
    }
}
