package management;

import fileio.FileHandler;
import java.util.Scanner;
import room.Phong;
import room.PhongCaoCap;
import room.PhongSuite;
import room.PhongTieuChuan;
import room.TrangThaiPhong;
import util.InputHelper;

/**
 * Lop quan ly danh sach phong su dung mang tinh Phong[] (khong dung ArrayList / Collection).
 * Tich hop day du chuc nang tu roomlist.java va tuong thich he thong HotelManager.
 */
public class RoomList {
    private static final String FILE = "room.txt";
    private static final int MAX = 100;

    private Phong[] danhSach;
    private int soLuong;

    public RoomList() {
        this.danhSach = new Phong[MAX];
        this.soLuong = 0;
    }

    // ==================== DOC / GHI FILE ====================

    public void load() {
        docFile(FILE);
    }

    public void save() {
        ghiFile(FILE);
    }

    public void ghiFile(String tenFile) {
        String[] lines = new String[soLuong];
        for (int i = 0; i < soLuong; i++) {
            lines[i] = danhSach[i].toFileString();
        }
        FileHandler.writeLinesToFile(tenFile, lines, false);
        System.out.println("=> Da ghi " + soLuong + " phong vao file data/" + tenFile);
    }

    public void docFile(String tenFile) {
        soLuong = 0;
        Phong.datLaiTongSoPhong(0);
        String[] lines = FileHandler.readLinesFromFile(tenFile);
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line == null || line.trim().isEmpty() || line.startsWith("#")) {
                continue;
            }
            String normalizedLine = line.contains("|") ? line : line.replace(",", "|");
            Phong p = Phong.fromFileString(normalizedLine);
            if (p != null && soLuong < MAX) {
                if (findById(p.getMaPhong()) == null) {
                    danhSach[soLuong++] = p;
                }
            }
        }
    }

    // ==================== THEM / SUA / XOA / TIM KIEM ====================

    public boolean themPhong(Phong p) {
        if (p == null) {
            System.out.println("=> Loi: Thong tin phong khong hop le!");
            return false;
        }
        if (soLuong >= MAX) {
            System.out.println("=> Danh sach phong da day!");
            return false;
        }
        if (findById(p.getMaPhong()) != null) {
            System.out.println("=> Loi: Ma phong [" + p.getMaPhong() + "] da ton tai!");
            return false;
        }
        danhSach[soLuong++] = p;
        return true;
    }

    public boolean add(Phong room) {
        return themPhong(room);
    }

    public void themPhongTuBanPhim() {
        System.out.println("--- THEM MOI 1 PHONG ---");
        String ma = InputHelper.getString("Ma phong (vd: P102): ");
        if (findById(ma) != null) {
            System.out.println("=> Loi: Ma phong [" + ma + "] da ton tai!");
            return;
        }
        String so = InputHelper.getString("So phong (vd: 102): ");
        int tang = InputHelper.getPositiveInt("Tang: ");
        double gia = InputHelper.getPositiveDouble("Gia co ban (VND/dem): ");
        int nguoi = InputHelper.getPositiveInt("So nguoi toi da: ");
        int giuong = InputHelper.getPositiveInt("So giuong: ");
        double dt = InputHelper.getPositiveDouble("Dien tich (m2): ");

        System.out.println("Chon loai phong (1-Tieu chuan, 2-Cao cap, 3-Suite): ");
        int loai = InputHelper.getIntInRange("Loai phong: ", 1, 3);

        Phong p;
        if (loai == 1) {
            p = new PhongTieuChuan(ma, so, tang, gia, TrangThaiPhong.TRONG, nguoi, giuong, dt);
        } else if (loai == 2) {
            boolean bc = InputHelper.getYesNo("Co ban cong khong?");
            p = new PhongCaoCap(ma, so, tang, gia, TrangThaiPhong.TRONG, nguoi, giuong, dt, bc);
        } else {
            boolean pk = InputHelper.getYesNo("Co phong khach rieng khong?");
            boolean bt = InputHelper.getYesNo("Co bon tam khong?");
            p = new PhongSuite(ma, so, tang, gia, TrangThaiPhong.TRONG, nguoi, giuong, dt, pk, bt);
        }

        if (themPhong(p)) {
            save();
            System.out.println("=> Da them phong [" + ma + "] thanh cong!");
        }
    }

    public void suaPhong(String maPhong) {
        Phong p = findById(maPhong);
        if (p == null) {
            System.out.println("=> Khong tim thay phong co ma [" + maPhong + "]!");
            return;
        }
        p.hienThiThongTin();
        String giaStr = InputHelper.getOptionalString("Nhap gia co ban moi (Enter = giu nguyen): ");
        if (!giaStr.isEmpty()) {
            try {
                double giaMoi = Double.parseDouble(giaStr);
                if (giaMoi > 0) {
                    p.setGiaCoBan(giaMoi);
                } else {
                    System.out.println("Gia phai > 0, giu nguyen gia cu.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Gia khong hop le, giu nguyen gia cu.");
            }
        }

        if (p.getTrangThai() == TrangThaiPhong.DANG_SU_DUNG) {
            System.out.println("Phong dang co khach o (DANG_SU_DUNG), khong the doi trang thai thu cong!");
        } else {
            System.out.println("Chon trang thai moi (0-Giu nguyen, 1-TRONG, 2-DA_DAT, 3-BAO_TRI): ");
            int tt = InputHelper.getIntInRange("Trang thai: ", 0, 3);
            if (tt == 1) p.setTrangThai(TrangThaiPhong.TRONG);
            else if (tt == 2) p.setTrangThai(TrangThaiPhong.DA_DAT);
            else if (tt == 3) p.setTrangThai(TrangThaiPhong.BAO_TRI);
        }
        save();
        System.out.println("=> Da cap nhat phong [" + p.getMaPhong() + "] thanh cong!");
    }

    public boolean xoaPhong(String maPhong) {
        if (maPhong == null) return false;
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i].getMaPhong().equalsIgnoreCase(maPhong.trim())) {
                if (danhSach[i].getTrangThai() == TrangThaiPhong.DANG_SU_DUNG) {
                    System.out.println("=> Phong dang co khach su dung, khong the xoa!");
                    return false;
                }
                for (int j = i; j < soLuong - 1; j++) {
                    danhSach[j] = danhSach[j + 1];
                }
                danhSach[--soLuong] = null;
                Phong.datLaiTongSoPhong(soLuong);
                save();
                System.out.println("=> Da xoa phong [" + maPhong + "] thanh cong!");
                return true;
            }
        }
        System.out.println("=> Khong tim thay phong co ma [" + maPhong + "]!");
        return false;
    }

    public boolean remove(String id) {
        return xoaPhong(id);
    }

    public Phong findById(String id) {
        if (id == null) return null;
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i].getMaPhong().equalsIgnoreCase(id.trim())) {
                return danhSach[i];
            }
        }
        return null;
    }

    public Phong[] timKiem(String khoa) {
        if (khoa == null || khoa.trim().isEmpty()) {
            return new Phong[0];
        }
        String k = khoa.trim().toLowerCase();
        int matched = 0;
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i].getMaPhong().toLowerCase().contains(k)
                    || danhSach[i].getSoPhong().toLowerCase().contains(k)
                    || danhSach[i].getLoaiPhong().toLowerCase().contains(k)) {
                matched++;
            }
        }
        Phong[] result = new Phong[matched];
        int idx = 0;
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i].getMaPhong().toLowerCase().contains(k)
                    || danhSach[i].getSoPhong().toLowerCase().contains(k)
                    || danhSach[i].getLoaiPhong().toLowerCase().contains(k)) {
                result[idx++] = danhSach[i];
            }
        }
        return result;
    }

    // ==================== XUAT / THONG KE / TINH TIEN ====================

    public void xuatDanhSach() {
        if (soLuong == 0) {
            System.out.println("=> Danh sach phong rong!");
            return;
        }
        for (int i = 0; i < soLuong; i++) {
            danhSach[i].hienThiThongTin();
        }
    }

    public void thongKe() {
        int trong = 0, daDat = 0, dangDung = 0, baoTri = 0;
        int tieuChuan = 0, caoCap = 0, suite = 0;

        for (int i = 0; i < soLuong; i++) {
            TrangThaiPhong tt = danhSach[i].getTrangThai();
            if (tt == TrangThaiPhong.TRONG) trong++;
            else if (tt == TrangThaiPhong.DA_DAT) daDat++;
            else if (tt == TrangThaiPhong.DANG_SU_DUNG) dangDung++;
            else baoTri++;

            if (danhSach[i] instanceof PhongTieuChuan) tieuChuan++;
            else if (danhSach[i] instanceof PhongCaoCap) caoCap++;
            else if (danhSach[i] instanceof PhongSuite) suite++;
        }
        System.out.println("\n--- THONG KE PHONG ---");
        System.out.println("Tong so phong    : " + soLuong);
        System.out.println("Theo trang thai  : Trong(" + trong + ") | Da dat(" + daDat + ") | Dang dung(" + dangDung + ") | Bao tri(" + baoTri + ")");
        System.out.println("Theo loai phong  : Tieu chuan(" + tieuChuan + ") | Cao cap(" + caoCap + ") | Suite(" + suite + ")");
    }

    public double tinhTongTienDuKien() {
        double tong = 0;
        for (int i = 0; i < soLuong; i++) {
            tong += danhSach[i].tinhTienPhong(1);
        }
        System.out.printf("=> Tong doanh thu du kien toi da (1 dem tat ca phong): %,.0f VND%n", tong);
        return tong;
    }

    public Phong[] getAll() {
        Phong[] result = new Phong[soLuong];
        for (int i = 0; i < soLuong; i++) {
            result[i] = danhSach[i];
        }
        return result;
    }

    public int getCount() {
        return soLuong;
    }

    public Phong[] getByStatus(TrangThaiPhong status) {
        int matched = 0;
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i].getTrangThai() == status) {
                matched++;
            }
        }
        Phong[] result = new Phong[matched];
        int index = 0;
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i].getTrangThai() == status) {
                result[index++] = danhSach[i];
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        RoomList ql = new RoomList();
        ql.load();
        System.out.println("Da tai " + ql.getCount() + " phong tu data/room.txt");
        ql.xuatDanhSach();
        ql.thongKe();
        ql.tinhTongTienDuKien();
        sc.close();
    }
}