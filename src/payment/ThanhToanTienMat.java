package payment;

import util.Format;

public class ThanhToanTienMat implements IThanhToan {
    private double tienKhachDua;
    private double tienThua;

    public ThanhToanTienMat(double tienKhachDua) {
        this.tienKhachDua = tienKhachDua;
        this.tienThua = 0;
    }

    public double getTienKhachDua() { return tienKhachDua; }
    public double getTienThua() { return tienThua; }

    @Override
    public boolean thanhToan(double soTien) {
        if (soTien <= 0 || tienKhachDua < soTien) {
            System.out.println("Loi: Tien khach dua khong du de thanh toan!");
            return false;
        }
        this.tienThua = tienKhachDua - soTien;
        return true;
    }

    @Override
    public String layPhuongThuc() {
        return "Tien mat";
    }

    @Override
    public void hienThiThongTin() {
        System.out.println("Phuong thuc    : " + layPhuongThuc());
        System.out.println("Tien khach dua : " + Format.money(tienKhachDua));
        System.out.println("Tien thua      : " + Format.money(tienThua));
    }
}
