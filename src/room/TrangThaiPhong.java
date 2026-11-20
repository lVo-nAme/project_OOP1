package room;

public enum TrangThaiPhong {
    TRONG("Trong"),
    DA_DAT("Da dat"),
    DANG_SU_DUNG("Dang su dung"),
    BAO_TRI("Bao tri");

    private final String moTa;

    TrangThaiPhong(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}
