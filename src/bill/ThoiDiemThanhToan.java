package bill;

public enum ThoiDiemThanhToan {
    NGAY_SU_DUNG("Thanh toan ngay khi su dung"),
    KHI_TRA_PHONG("Thanh toan khi tra phong");

    private final String moTa;

    ThoiDiemThanhToan(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}
