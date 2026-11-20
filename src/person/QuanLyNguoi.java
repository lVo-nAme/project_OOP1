package person;

import fileio.FileHandler;
import management.CustomerList;
import util.InputHelper;

/**
 * Lop quan ly con nguoi (Quan ly, Nhan vien, Khach hang) dung mang tinh (array).
 * Khong dung ArrayList, Vector, LinkedList hay Iterator.
 */
public class QuanLyNguoi {
    private static final int MAX = 100;
    private static final String FILE_NHAN_SU = "staff.txt";

    private NhanVien[] dsNhanVien;
    private int soLuongNhanVien;
    private CustomerList customerList;

    public QuanLyNguoi(CustomerList customerList) {
        this.dsNhanVien = new NhanVien[MAX];
        this.soLuongNhanVien = 0;
        this.customerList = customerList;
        load();
    }

    public void load() {
        soLuongNhanVien = 0;
        String[] lines = FileHandler.readLinesFromFile(FILE_NHAN_SU);
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line == null || line.trim().isEmpty() || line.startsWith("#")) {
                continue;
            }
            NhanVien nv = NhanVien.fromFileString(line);
            if (nv != null && soLuongNhanVien < MAX) {
                dsNhanVien[soLuongNhanVien++] = nv;
            }
        }
        // Neu chua co du lieu trong file thi khoi tao du lieu mau va luu xuong file
        if (soLuongNhanVien == 0) {
            khoiTaoDuLieuMau();
            save();
        }
    }

    public void save() {
        String[] lines = new String[soLuongNhanVien];
        for (int i = 0; i < soLuongNhanVien; i++) {
            lines[i] = dsNhanVien[i].toFileString();
        }
        FileHandler.writeLinesToFile(FILE_NHAN_SU, lines, false);
    }

    private void khoiTaoDuLieuMau() {
        dsNhanVien[soLuongNhanVien++] = new QuanLy(
                "QL01", "Tran Anh Quan", "0901234567", "079099001234",
                "anhquan@hotel.com", "Quan ly dieu hanh", 20000000, "01/01/2020", 5000000, 5);
        dsNhanVien[soLuongNhanVien++] = new NhanVien(
                "NV01", "Nguyen Van Hung", "0987654321", "079098001111",
                "hungnv@hotel.com", "Le tan", 8000000, "10/05/2022");
        dsNhanVien[soLuongNhanVien++] = new NhanVien(
                "NV02", "Pham Thi Huong", "0978111222", "079098002222",
                "huongpt@hotel.com", "Ke toan", 9000000, "01/08/2022");
    }

    public NhanVien timNhanVienTheoMa(String ma) {
        if (ma == null) return null;
        for (int i = 0; i < soLuongNhanVien; i++) {
            if (dsNhanVien[i].getMaNguoi().equalsIgnoreCase(ma.trim())) {
                return dsNhanVien[i];
            }
        }
        return null;
    }

    public NhanVien layNhanVienMacDinh() {
        for (int i = 0; i < soLuongNhanVien; i++) {
            if (!(dsNhanVien[i] instanceof QuanLy)) {
                return dsNhanVien[i];
            }
        }
        if (soLuongNhanVien > 0) {
            return dsNhanVien[0];
        }
        return new NhanVien("NV01", "Le Tan Mac Dinh", "0900000000", "000000000000", "letan@hotel.com", "Le tan", 8000000, "01/01/2025");
    }

    public QuanLy layQuanLyMacDinh() {
        for (int i = 0; i < soLuongNhanVien; i++) {
            if (dsNhanVien[i] instanceof QuanLy) {
                return (QuanLy) dsNhanVien[i];
            }
        }
        return new QuanLy("QL01", "Tran Anh Quan", "0901234567", "079099001234", "anhquan@hotel.com", "Quan ly", 20000000, "01/01/2020", 5000000, 5);
    }

    // Menu quan ly nhan su & vai tro (tich hop vao HotelManager)
    public void menuQuanLyNguoi(double doanhThuThucTe, int soHoaDonDaThanhToan, int tongSoPhong) {
        int chon;
        do {
            System.out.println("\n========================================");
            System.out.println("     QUAN LY NHAN SU & PHAN QUYEN       ");
            System.out.println("========================================");
            System.out.println("1. Xem danh sach nhan vien & bang luong");
            System.out.println("2. Them Quan ly / Nhan vien moi");
            System.out.println("3. Sua thong tin nhan su");
            System.out.println("4. Xoa nhan su");
            System.out.println("5. Tim kiem nhan su / khach hang");
            System.out.println("6. Quan ly duyet khuyen mai (QuanLy)");
            System.out.println("7. Quan ly xem bao cao doanh thu (QuanLy)");
            System.out.println("8. Cap nhat diem tich luy khach hang");
            System.out.println("0. Quay lai menu chinh");
            chon = InputHelper.getIntInRange("Chon chuc nang: ", 0, 8);

            switch (chon) {
                case 1:
                    xemBangLuong();
                    break;
                case 2:
                    themNhanSu();
                    break;
                case 3:
                    suaNhanSu();
                    break;
                case 4:
                    xoaNhanSu();
                    break;
                case 5:
                    timKiemTongHop();
                    break;
                case 6:
                    layQuanLyMacDinh().duyetKhuyenMai();
                    break;
                case 7:
                    layQuanLyMacDinh().xemBaoCaoDoanhThu(doanhThuThucTe, soHoaDonDaThanhToan, tongSoPhong);
                    break;
                case 8:
                    capNhatDiemKhachHang();
                    break;
                default:
                    break;
            }
        } while (chon != 0);
    }

    public void xemBangLuong() {
        System.out.println("\n====================== BANG LUONG & THONG TIN NHAN SU ======================");
        if (soLuongNhanVien == 0) {
            System.out.println("Danh sach nhan su trong!");
            return;
        }
        System.out.printf("%-8s | %-20s | %-18s | %-14s | %-14s%n",
                "Ma NV", "Ho Ten", "Chuc Vu", "Luong Co Ban", "Thuc Nhan");
        System.out.println("--------------------------------------------------------------------------------");
        for (int i = 0; i < soLuongNhanVien; i++) {
            NhanVien nv = dsNhanVien[i];
            // Tinh da hinh: nv.tinhLuong() tu dong goi dung ban cua NhanVien hoac QuanLy
            System.out.printf("%-8s | %-20s | %-18s | %,14.0f | %,14.0f VND%n",
                    nv.getMaNguoi(), nv.getHoTen(), nv.getChucVu(), nv.getLuong(), nv.tinhLuong());
        }
        System.out.println("================================================================================");
    }

    public void themNhanSu() {
        if (soLuongNhanVien >= MAX) {
            System.out.println("Danh sach nhan su da day!");
            return;
        }
        System.out.println("\n--- THEM NHAN SU MOI ---");
        System.out.println("1. Them Quan ly");
        System.out.println("2. Them Nhan vien");
        int loai = InputHelper.getIntInRange("Chon loai: ", 1, 2);

        NhanVien moi = (loai == 1) ? new QuanLy() : new NhanVien();
        moi.nhapThongTin();
        if (timNhanVienTheoMa(moi.getMaNguoi()) != null) {
            System.out.println("Loi: Ma nhan su da ton tai!");
            return;
        }
        dsNhanVien[soLuongNhanVien++] = moi;
        save();
        System.out.println("=> Da them thanh cong: " + moi.getHoTen());
    }

    public void suaNhanSu() {
        String ma = InputHelper.getString("Nhap ma nhan su can sua (vd: NV01): ");
        NhanVien nv = timNhanVienTheoMa(ma);
        if (nv == null) {
            System.out.println("Khong tim thay nhan su co ma " + ma);
            return;
        }
        nv.hienThiThongTin();
        String tenMoi = InputHelper.getOptionalString("Ho ten moi (Enter = giu nguyen): ");
        if (!tenMoi.isEmpty()) nv.setHoTen(tenMoi);

        String sdtMoi = InputHelper.getOptionalString("So dien thoai moi (Enter = giu nguyen): ");
        if (!sdtMoi.isEmpty()) nv.setSoDienThoai(sdtMoi);

        String chucVuMoi = InputHelper.getOptionalString("Chuc vu moi (Enter = giu nguyen): ");
        if (!chucVuMoi.isEmpty()) nv.setChucVu(chucVuMoi);

        String luongStr = InputHelper.getOptionalString("Luong co ban moi (Enter = giu nguyen): ");
        if (!luongStr.isEmpty()) {
            try {
                double luongMoi = Double.parseDouble(luongStr);
                if (luongMoi > 0) nv.setLuong(luongMoi);
            } catch (NumberFormatException e) {
                System.out.println("Luong khong hop le, giu nguyen luong cu.");
            }
        }

        if (nv instanceof QuanLy) {
            QuanLy ql = (QuanLy) nv;
            String phuCapStr = InputHelper.getOptionalString("Phu cap moi (Enter = giu nguyen): ");
            if (!phuCapStr.isEmpty()) {
                try {
                    double pc = Double.parseDouble(phuCapStr);
                    if (pc >= 0) ql.setPhuCap(pc);
                } catch (NumberFormatException e) {
                    System.out.println("Phu cap khong hop le, giu nguyen.");
                }
            }
        }
        save();
        System.out.println("=> Da cap nhat thong tin nhan su!");
    }

    public void xoaNhanSu() {
        String ma = InputHelper.getString("Nhap ma nhan su can xoa: ");
        for (int i = 0; i < soLuongNhanVien; i++) {
            if (dsNhanVien[i].getMaNguoi().equalsIgnoreCase(ma.trim())) {
                if (InputHelper.getYesNo("Xac nhan xoa " + dsNhanVien[i].getHoTen() + "?")) {
                    for (int j = i; j < soLuongNhanVien - 1; j++) {
                        dsNhanVien[j] = dsNhanVien[j + 1];
                    }
                    dsNhanVien[--soLuongNhanVien] = null;
                    save();
                    System.out.println("=> Da xoa nhan su thanh cong!");
                }
                return;
            }
        }
        System.out.println("Khong tim thay nhan su co ma " + ma);
    }

    public void timKiemTongHop() {
        String tuKhoa = InputHelper.getString("Nhap ma hoac ho ten can tim: ").toLowerCase();
        System.out.println("\n--- KET QUA NHAN SU ---");
        boolean coNV = false;
        for (int i = 0; i < soLuongNhanVien; i++) {
            NhanVien nv = dsNhanVien[i];
            if (nv.getMaNguoi().toLowerCase().contains(tuKhoa) || nv.getHoTen().toLowerCase().contains(tuKhoa)) {
                nv.hienThiThongTin();
                coNV = true;
            }
        }
        if (!coNV) {
            System.out.println("(Khong tim thay nhan su nao)");
        }

        System.out.println("--- KET QUA KHACH HANG ---");
        KhachHang[] dsKH = customerList.search(tuKhoa);
        if (dsKH.length == 0) {
            System.out.println("(Khong tim thay khach hang nao)");
        } else {
            for (int i = 0; i < dsKH.length; i++) {
                dsKH[i].hienThiThongTin();
            }
        }
    }

    private void capNhatDiemKhachHang() {
        String maKH = InputHelper.getString("Nhap ma khach hang (vd: KH001): ");
        KhachHang kh = customerList.findById(maKH);
        if (kh == null) {
            System.out.println("Khong tim thay khach hang!");
            return;
        }
        int diem = InputHelper.getInt("Nhap so diem cong them (hoac so am de tru): ");
        kh.capNhatDiemTichLuy(diem);
        customerList.save();
    }
}
