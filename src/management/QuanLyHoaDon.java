package management;

import bill.ChiTietHoaDon;
import bill.HoaDon;
import bill.KhuyenMai;
import fileio.FileHandler;
import java.text.Normalizer;
import java.time.LocalDate;
import payment.IThanhToan;
import room.Phong;
import util.Format;
import util.InputHelper;

/**
 * Lop quan ly Hoa Don va Khuyen Mai su dung mang tinh (array) - khong dung ArrayList / Collection.
 * Tich hop day du chuc nang tu QuanLyHoaDon_2.java va dong bo voi HotelManager.
 */
public class QuanLyHoaDon {
    private static final int DUNG_LUONG_MAC_DINH = 300;
    private static final String TEN_FILE_HOA_DON = "bill.txt";
    private static final String TEN_FILE_CHI_TIET = "billdetail.txt";
    private static final String TEN_FILE_KHUYEN_MAI = "khuyenmai.txt";

    private HoaDon[] danhSachHoaDon;
    private KhuyenMai[] danhSachKhuyenMai;
    private int soLuongHoaDon;
    private int soLuongKhuyenMai;

    public QuanLyHoaDon() {
        this(DUNG_LUONG_MAC_DINH);
    }

    public QuanLyHoaDon(int sucChua) {
        int kichThuoc = sucChua > 0 ? sucChua : DUNG_LUONG_MAC_DINH;
        this.danhSachHoaDon = new HoaDon[kichThuoc];
        this.danhSachKhuyenMai = new KhuyenMai[100];
        this.soLuongHoaDon = 0;
        this.soLuongKhuyenMai = 0;
    }

    // ==================== DOC / GHI DU LIEU ====================

    public void load() {
        docDuLieu();
        if (soLuongKhuyenMai == 0) {
            khoiTaoKhuyenMaiMau();
            luuDanhSachKhuyenMai(TEN_FILE_KHUYEN_MAI);
        }
    }

    public void save() {
        luuDuLieu();
    }

    public void luuDuLieu() {
        luuDanhSachHoaDon(TEN_FILE_HOA_DON);
        luuDanhSachKhuyenMai(TEN_FILE_KHUYEN_MAI);
    }

    public void docDuLieu() {
        docDanhSachKhuyenMai(TEN_FILE_KHUYEN_MAI);
        docDanhSachHoaDon(TEN_FILE_HOA_DON);
    }

    private void khoiTaoKhuyenMaiMau() {
        themKhuyenMai(new KhuyenMai(
                "KM10", "Giam 10% don tu 1 trieu", 10, 1000000, 300000,
                LocalDate.of(2025, 1, 1), LocalDate.of(2027, 12, 31)));
        themKhuyenMai(new KhuyenMai(
                "VIP20", "Giam 20% don tu 3 trieu", 20, 3000000, 1000000,
                LocalDate.of(2025, 1, 1), LocalDate.of(2027, 12, 31)));
    }

    public void luuDanhSachKhuyenMai(String tenFile) {
        String[] duLieu = new String[soLuongKhuyenMai];
        for (int i = 0; i < soLuongKhuyenMai; i++) {
            duLieu[i] = danhSachKhuyenMai[i].toFileString();
        }
        FileHandler.writeLinesToFile(tenFile, duLieu, false);
    }

    public void docDanhSachKhuyenMai(String tenFile) {
        String[] cacDong = FileHandler.readLinesFromFile(tenFile);
        soLuongKhuyenMai = 0;
        for (int i = 0; i < cacDong.length; i++) {
            String line = cacDong[i];
            if (line == null || line.trim().isEmpty() || line.trim().startsWith("#")) {
                continue;
            }
            KhuyenMai km = KhuyenMai.fromFileString(line);
            if (km != null) {
                themKhuyenMai(km);
            }
        }
    }

    public void luuDanhSachHoaDon(String tenFile) {
        String[] billLines = new String[soLuongHoaDon];
        int tongDongChiTiet = 0;
        for (int i = 0; i < soLuongHoaDon; i++) {
            billLines[i] = danhSachHoaDon[i].toFileString();
            tongDongChiTiet += danhSachHoaDon[i].getSoChiTiet();
        }

        String[] detailLines = new String[tongDongChiTiet];
        int idx = 0;
        for (int i = 0; i < soLuongHoaDon; i++) {
            HoaDon hd = danhSachHoaDon[i];
            ChiTietHoaDon[] dsCT = hd.getDsChiTiet();
            for (int j = 0; j < hd.getSoChiTiet(); j++) {
                detailLines[idx++] = dsCT[j].toFileString(hd.getMaHoaDon());
            }
        }

        FileHandler.writeLinesToFile(tenFile, billLines, false);
        FileHandler.writeLinesToFile(TEN_FILE_CHI_TIET, detailLines, false);
    }

    public void docDanhSachHoaDon(String tenFile) {
        soLuongHoaDon = 0;
        HoaDon.datLaiTongSoHoaDon(0);
        String[] billLines = FileHandler.readLinesFromFile(tenFile);
        for (int i = 0; i < billLines.length; i++) {
            String line = billLines[i];
            if (line == null || line.trim().isEmpty() || line.trim().startsWith("#")) {
                continue;
            }
            HoaDon hd = HoaDon.fromFileString(line);
            if (hd != null) {
                themHoaDon(hd);
            }
        }

        String[] detailLines = FileHandler.readLinesFromFile(TEN_FILE_CHI_TIET);
        for (int i = 0; i < detailLines.length; i++) {
            String line = detailLines[i];
            if (line == null || line.trim().isEmpty() || line.trim().startsWith("#")) {
                continue;
            }
            String[] p = line.split("\\|", -1);
            HoaDon hd = timHoaDonTheoMa(p[0]);
            ChiTietHoaDon ct = ChiTietHoaDon.fromParts(p, 1);
            if (hd != null && ct != null) {
                hd.themChiTiet(ct);
            }
        }
    }

    // ==================== GETTERS & CRUD HOA DON ====================

    public int getSoLuongHoaDon() { return soLuongHoaDon; }
    public int getCount() { return soLuongHoaDon; }

    public HoaDon getHoaDon(int viTri) {
        if (viTri < 0 || viTri >= soLuongHoaDon) return null;
        return danhSachHoaDon[viTri];
    }

    public HoaDon[] getAll() {
        HoaDon[] result = new HoaDon[soLuongHoaDon];
        for (int i = 0; i < soLuongHoaDon; i++) {
            result[i] = danhSachHoaDon[i];
        }
        return result;
    }

    public String nextId() {
        String[] ids = new String[soLuongHoaDon];
        for (int i = 0; i < soLuongHoaDon; i++) {
            ids[i] = danhSachHoaDon[i].getMaHoaDon();
        }
        return Format.nextId("HD", ids, soLuongHoaDon);
    }

    public boolean themHoaDon(HoaDon hoaDon) {
        if (hoaDon == null || timViTriHoaDon(hoaDon.getMaHoaDon()) >= 0) {
            return false;
        }
        if (soLuongHoaDon >= danhSachHoaDon.length) {
            HoaDon[] mangMoi = new HoaDon[danhSachHoaDon.length * 2];
            for (int i = 0; i < soLuongHoaDon; i++) {
                mangMoi[i] = danhSachHoaDon[i];
            }
            danhSachHoaDon = mangMoi;
        }
        danhSachHoaDon[soLuongHoaDon++] = hoaDon;
        return true;
    }

    public boolean add(HoaDon hd) {
        return themHoaDon(hd);
    }

    public HoaDon timHoaDonTheoMa(String maHoaDon) {
        int viTri = timViTriHoaDon(maHoaDon);
        return viTri < 0 ? null : danhSachHoaDon[viTri];
    }

    public HoaDon findById(String id) {
        return timHoaDonTheoMa(id);
    }

    public HoaDon findOpenByRoom(String maPhong) {
        if (maPhong == null) return null;
        for (int i = 0; i < soLuongHoaDon; i++) {
            if (danhSachHoaDon[i].dangMo() && danhSachHoaDon[i].getMaPhong().equalsIgnoreCase(maPhong.trim())) {
                return danhSachHoaDon[i];
            }
        }
        return null;
    }

    public boolean hasOpenBillOfCustomer(String maKhachHang) {
        if (maKhachHang == null) return false;
        for (int i = 0; i < soLuongHoaDon; i++) {
            if (danhSachHoaDon[i].dangMo() && danhSachHoaDon[i].getMaKhachHang().equalsIgnoreCase(maKhachHang.trim())) {
                return true;
            }
        }
        return false;
    }

    public HoaDon[] getOpenBills() {
        int matched = 0;
        for (int i = 0; i < soLuongHoaDon; i++) {
            if (danhSachHoaDon[i].dangMo()) matched++;
        }
        HoaDon[] result = new HoaDon[matched];
        int idx = 0;
        for (int i = 0; i < soLuongHoaDon; i++) {
            if (danhSachHoaDon[i].dangMo()) {
                result[idx++] = danhSachHoaDon[i];
            }
        }
        return result;
    }

    public boolean xoaHoaDon(String maHoaDon) {
        int viTri = timViTriHoaDon(maHoaDon);
        if (viTri < 0) {
            System.out.println("Khong tim thay hoa don [" + maHoaDon + "].");
            return false;
        }
        if (danhSachHoaDon[viTri].daThanhToan()) {
            System.out.println("Hoa don da thanh toan nen khong the xoa.");
            return false;
        }
        if (danhSachHoaDon[viTri].dangMo()) {
            System.out.println("Hoa don dang mo (khach dang o phong " + danhSachHoaDon[viTri].getMaPhong() + "), vui long tra phong thay vi xoa!");
            return false;
        }
        for (int i = viTri; i < soLuongHoaDon - 1; i++) {
            danhSachHoaDon[i] = danhSachHoaDon[i + 1];
        }
        danhSachHoaDon[--soLuongHoaDon] = null;
        save();
        return true;
    }

    public boolean suaHoaDon(String maHoaDon, RoomList rooms) {
        HoaDon hd = timHoaDonTheoMa(maHoaDon);
        if (hd == null) {
            System.out.println("Khong tim thay hoa don [" + maHoaDon + "].");
            return false;
        }
        if (hd.daThanhToan()) {
            System.out.println("Hoa don da thanh toan nen khong the chinh sua.");
            return false;
        }

        int chon;
        do {
            System.out.println("\n---------- SUA HOA DON " + hd.getMaHoaDon() + " ----------");
            System.out.println("1. Sua so luong / don gia chi tiet dich vu");
            System.out.println("2. Xoa chi tiet dich vu");
            System.out.println("3. Ap dung khuyen mai tot nhat");
            System.out.println("4. Bo khuyen mai");
            System.out.println("0. Quay lai");
            chon = InputHelper.getIntInRange("Chon: ", 0, 4);

            switch (chon) {
                case 1: {
                    String maCT = InputHelper.getString("Nhap ma chi tiet (vd: CT01): ");
                    int sl = InputHelper.getPositiveInt("Nhap so luong moi: ");
                    double gia = InputHelper.getPositiveDouble("Nhap don gia moi (VND): ");
                    if (hd.suaChiTiet(maCT, sl, gia)) {
                        save();
                        System.out.println("=> Da cap nhat chi tiet " + maCT);
                    } else {
                        System.out.println("=> Khong tim thay chi tiet " + maCT);
                    }
                    break;
                }
                case 2: {
                    String maCT = InputHelper.getString("Nhap ma chi tiet can xoa: ");
                    if (hd.xoaChiTiet(maCT)) {
                        save();
                        System.out.println("=> Da xoa chi tiet " + maCT);
                    } else {
                        System.out.println("=> Khong tim thay chi tiet " + maCT);
                    }
                    break;
                }
                case 3: {
                    Phong p = (rooms != null) ? rooms.findById(hd.getMaPhong()) : null;
                    if (apDungKhuyenMaiTotNhat(hd.getMaHoaDon(), p, LocalDate.now())) {
                        save();
                    }
                    break;
                }
                case 4:
                    hd.boKhuyenMai();
                    save();
                    System.out.println("=> Da bo khuyen mai khoi hoa don.");
                    break;
                default:
                    break;
            }
        } while (chon != 0);
        return true;
    }

    // ==================== TIM KIEM & SAP XEP ====================

    public HoaDon[] timKiemHoaDon(String tuKhoa) {
        String key = chuanHoa(tuKhoa);
        int matched = 0;
        for (int i = 0; i < soLuongHoaDon; i++) {
            if (hoaDonPhuHopTuKhoa(danhSachHoaDon[i], key)) {
                matched++;
            }
        }
        HoaDon[] ketQua = new HoaDon[matched];
        int idx = 0;
        for (int i = 0; i < soLuongHoaDon; i++) {
            if (hoaDonPhuHopTuKhoa(danhSachHoaDon[i], key)) {
                ketQua[idx++] = danhSachHoaDon[i];
            }
        }
        return ketQua;
    }

    public void sapXepHoaDonTheoTongTien(boolean tangDan, RoomList rooms) {
        for (int i = 0; i < soLuongHoaDon - 1; i++) {
            int best = i;
            for (int j = i + 1; j < soLuongHoaDon; j++) {
                double tienJ = layGiaTriHoaDon(danhSachHoaDon[j], rooms);
                double tienBest = layGiaTriHoaDon(danhSachHoaDon[best], rooms);
                boolean totHon = tangDan ? (tienJ < tienBest) : (tienJ > tienBest);
                if (totHon) {
                    best = j;
                }
            }
            HoaDon tam = danhSachHoaDon[i];
            danhSachHoaDon[i] = danhSachHoaDon[best];
            danhSachHoaDon[best] = tam;
        }
    }

    public void sapXepHoaDonTheoNgayLap() {
        for (int i = 0; i < soLuongHoaDon - 1; i++) {
            int best = i;
            for (int j = i + 1; j < soLuongHoaDon; j++) {
                if (danhSachHoaDon[j].getNgayLap().isBefore(danhSachHoaDon[best].getNgayLap())) {
                    best = j;
                }
            }
            HoaDon tam = danhSachHoaDon[i];
            danhSachHoaDon[i] = danhSachHoaDon[best];
            danhSachHoaDon[best] = tam;
        }
    }

    private double layGiaTriHoaDon(HoaDon hd, RoomList rooms) {
        Phong p = (rooms != null) ? rooms.findById(hd.getMaPhong()) : null;
        LocalDate mocTra = hd.daThanhToan() ? hd.getNgayTraPhong() : LocalDate.now();
        return hd.tinhTongThanhToan(p, mocTra);
    }

    // ==================== KHUYEN MAI ====================

    public int getSoLuongKhuyenMai() { return soLuongKhuyenMai; }

    public KhuyenMai[] getAllKhuyenMai() {
        KhuyenMai[] res = new KhuyenMai[soLuongKhuyenMai];
        for (int i = 0; i < soLuongKhuyenMai; i++) {
            res[i] = danhSachKhuyenMai[i];
        }
        return res;
    }

    public boolean themKhuyenMai(KhuyenMai km) {
        if (km == null || timViTriKhuyenMai(km.getMaKhuyenMai()) >= 0) {
            return false;
        }
        if (soLuongKhuyenMai >= danhSachKhuyenMai.length) {
            KhuyenMai[] mangMoi = new KhuyenMai[danhSachKhuyenMai.length * 2];
            for (int i = 0; i < soLuongKhuyenMai; i++) {
                mangMoi[i] = danhSachKhuyenMai[i];
            }
            danhSachKhuyenMai = mangMoi;
        }
        danhSachKhuyenMai[soLuongKhuyenMai++] = km;
        return true;
    }

    public KhuyenMai timKhuyenMaiTheoMa(String maKhuyenMai) {
        int viTri = timViTriKhuyenMai(maKhuyenMai);
        return viTri < 0 ? null : danhSachKhuyenMai[viTri];
    }

    public void themKhuyenMaiTuBanPhim() {
        System.out.println("\n--- THEM KHUYEN MAI MOI ---");
        String ma = InputHelper.getString("Ma khuyen mai (vd: SUMMER15): ");
        if (timKhuyenMaiTheoMa(ma) != null) {
            System.out.println("Loi: Ma khuyen mai da ton tai!");
            return;
        }
        String ten = InputHelper.getString("Ten khuyen mai: ");
        double phanTram = InputHelper.getPositiveDouble("Phan tram giam (1 - 100): ");
        if (phanTram > 100) phanTram = 100;
        double dieuKien = InputHelper.getDouble("Gia tri hoa don toi thieu (VND): ");
        if (dieuKien < 0) dieuKien = 0;
        double toiDa = InputHelper.getPositiveDouble("So tien giam toi da (VND): ");
        LocalDate batDau = InputHelper.getDate("Ngay bat dau (dd/MM/yyyy): ", LocalDate.now());
        LocalDate ketThuc = InputHelper.getDate("Ngay ket thuc (dd/MM/yyyy): ", batDau.plusMonths(1));
        if (ketThuc.isBefore(batDau)) {
            System.out.println("Loi: Ngay ket thuc khong duoc truoc ngay bat dau!");
            return;
        }

        KhuyenMai km = new KhuyenMai(ma, ten, phanTram, dieuKien, toiDa, batDau, ketThuc);
        if (themKhuyenMai(km)) {
            luuDanhSachKhuyenMai(TEN_FILE_KHUYEN_MAI);
            System.out.println("=> Da them khuyen mai [" + ma + "] thanh cong!");
        }
    }

    public boolean xoaKhuyenMai(String maKhuyenMai) {
        int viTri = timViTriKhuyenMai(maKhuyenMai);
        if (viTri < 0) {
            System.out.println("Khong tim thay khuyen mai [" + maKhuyenMai + "].");
            return false;
        }
        for (int i = 0; i < soLuongHoaDon; i++) {
            if (maKhuyenMai.equalsIgnoreCase(danhSachHoaDon[i].getMaKhuyenMai())) {
                System.out.println("Khuyen mai dang duoc su dung trong hoa don "
                        + danhSachHoaDon[i].getMaHoaDon() + ", khong the xoa.");
                return false;
            }
        }
        for (int i = viTri; i < soLuongKhuyenMai - 1; i++) {
            danhSachKhuyenMai[i] = danhSachKhuyenMai[i + 1];
        }
        danhSachKhuyenMai[--soLuongKhuyenMai] = null;
        luuDanhSachKhuyenMai(TEN_FILE_KHUYEN_MAI);
        System.out.println("=> Da xoa khuyen mai [" + maKhuyenMai + "] thanh cong!");
        return true;
    }

    public boolean apDungKhuyenMaiTotNhat(String maHoaDon, Phong phong, LocalDate ngayTraDuKien) {
        HoaDon hd = timHoaDonTheoMa(maHoaDon);
        if (hd == null) {
            System.out.println("Khong tim thay hoa don [" + maHoaDon + "].");
            return false;
        }
        LocalDate ngayKiemTra = (ngayTraDuKien != null) ? ngayTraDuKien : LocalDate.now();
        double tamTinh = hd.tinhTienPhong(phong, ngayKiemTra) + hd.tinhTienDichVu();

        KhuyenMai totNhat = null;
        double giamCaoNhat = 0;
        for (int i = 0; i < soLuongKhuyenMai; i++) {
            KhuyenMai km = danhSachKhuyenMai[i];
            if (km.kiemTraHieuLuc(ngayKiemTra) && tamTinh >= km.getDieuKienToiThieu()) {
                double giam = km.tinhTienGiam(tamTinh);
                if (giam > giamCaoNhat) {
                    giamCaoNhat = giam;
                    totNhat = km;
                }
            }
        }
        if (totNhat == null) {
            System.out.println("Khong co khuyen mai phu hop cho hoa don " + maHoaDon + ".");
            return false;
        }
        boolean ok = hd.apDungKhuyenMai(totNhat, phong, ngayKiemTra);
        if (ok) {
            System.out.println("=> Da tu dong ap dung khuyen mai tot nhat [" + totNhat.getMaKhuyenMai()
                    + "] giam " + Format.money(giamCaoNhat));
        }
        return ok;
    }

    public void hienThiDanhSachKhuyenMai() {
        System.out.println("\n================ DANH SACH KHUYEN MAI (" + soLuongKhuyenMai + ") ================");
        if (soLuongKhuyenMai == 0) {
            System.out.println("(Chua co khuyen mai nao)");
            return;
        }
        for (int i = 0; i < soLuongKhuyenMai; i++) {
            danhSachKhuyenMai[i].hienThiThongTin();
        }
        System.out.println("=================================================================");
    }

    // ==================== THONG KE DOANH THU ====================

    public double totalRevenue(RoomList roomList) {
        double sum = 0;
        for (int i = 0; i < soLuongHoaDon; i++) {
            if (danhSachHoaDon[i].daThanhToan()) {
                Phong p = (roomList != null) ? roomList.findById(danhSachHoaDon[i].getMaPhong()) : null;
                sum += danhSachHoaDon[i].tinhTongThanhToan(p, danhSachHoaDon[i].getNgayTraPhong());
            }
        }
        return sum;
    }

    public int countPaid() {
        int n = 0;
        for (int i = 0; i < soLuongHoaDon; i++) {
            if (danhSachHoaDon[i].daThanhToan()) n++;
        }
        return n;
    }

    public void thongKeDoanhThuTheoThang(int nam, RoomList roomList) {
        double[] doanhThu = new double[12];
        int[] soHD = new int[12];
        for (int i = 0; i < soLuongHoaDon; i++) {
            HoaDon hd = danhSachHoaDon[i];
            if (hd.daThanhToan() && hd.getNgayLap().getYear() == nam) {
                int thang = hd.getNgayLap().getMonthValue() - 1;
                Phong p = (roomList != null) ? roomList.findById(hd.getMaPhong()) : null;
                doanhThu[thang] += hd.tinhTongThanhToan(p, hd.getNgayTraPhong());
                soHD[thang]++;
            }
        }
        System.out.println("\n========== DOANH THU THEO THANG NAM " + nam + " ==========");
        double tongNam = 0;
        for (int i = 0; i < 12; i++) {
            System.out.printf("Thang %02d: %3d hoa don | %18s%n", i + 1, soHD[i], Format.money(doanhThu[i]));
            tongNam += doanhThu[i];
        }
        System.out.printf("TONG DOANH THU NAM %d: %18s%n", nam, Format.money(tongNam));
    }

    public void thongKeTheoPhuongThucThanhToan(RoomList roomList) {
        String[] tenPT = new String[soLuongHoaDon];
        int[] soHD = new int[soLuongHoaDon];
        double[] doanhThu = new double[soLuongHoaDon];
        int soNhom = 0;

        for (int i = 0; i < soLuongHoaDon; i++) {
            HoaDon hd = danhSachHoaDon[i];
            if (!hd.daThanhToan()) continue;
            String ten = hd.getPhuongThucThanhToan();
            if (ten == null || ten.trim().isEmpty()) ten = "Chua xac dinh";

            int viTri = -1;
            for (int j = 0; j < soNhom; j++) {
                if (tenPT[j].equalsIgnoreCase(ten)) {
                    viTri = j;
                    break;
                }
            }
            if (viTri < 0) {
                viTri = soNhom++;
                tenPT[viTri] = ten;
            }
            Phong p = (roomList != null) ? roomList.findById(hd.getMaPhong()) : null;
            soHD[viTri]++;
            doanhThu[viTri] += hd.tinhTongThanhToan(p, hd.getNgayTraPhong());
        }

        System.out.println("\n========== DOANH THU THEO PHUONG THUC THANH TOAN ==========");
        if (soNhom == 0) {
            System.out.println("Chua co du lieu thanh toan.");
            return;
        }
        System.out.printf("%-28s %10s %18s%n", "Phuong thuc", "So HD", "Doanh thu");
        for (int i = 0; i < soNhom; i++) {
            System.out.printf("%-28.28s %10d %18s%n", tenPT[i], soHD[i], Format.money(doanhThu[i]));
        }
    }

    public void thongKeTienPhongVaDichVu(RoomList roomList) {
        double tongPhong = 0;
        double tongDV = 0;
        for (int i = 0; i < soLuongHoaDon; i++) {
            HoaDon hd = danhSachHoaDon[i];
            if (hd.daThanhToan()) {
                Phong p = (roomList != null) ? roomList.findById(hd.getMaPhong()) : null;
                tongPhong += hd.tinhTienPhong(p, hd.getNgayTraPhong());
                tongDV += hd.tinhTienDichVu();
            }
        }
        System.out.println("\n========== DOANH THU PHONG VA DICH VU ==========");
        System.out.println("Tien phong   : " + Format.money(tongPhong));
        System.out.println("Tien dich vu : " + Format.money(tongDV));
        System.out.println("Tong truoc KM: " + Format.money(tongPhong + tongDV));
    }

    // ==================== HELPERS NOI BO ====================

    private int timViTriHoaDon(String maHoaDon) {
        if (maHoaDon == null) return -1;
        for (int i = 0; i < soLuongHoaDon; i++) {
            if (danhSachHoaDon[i].getMaHoaDon().equalsIgnoreCase(maHoaDon.trim())) {
                return i;
            }
        }
        return -1;
    }

    private int timViTriKhuyenMai(String maKhuyenMai) {
        if (maKhuyenMai == null) return -1;
        for (int i = 0; i < soLuongKhuyenMai; i++) {
            if (danhSachKhuyenMai[i].getMaKhuyenMai().equalsIgnoreCase(maKhuyenMai.trim())) {
                return i;
            }
        }
        return -1;
    }

    private boolean hoaDonPhuHopTuKhoa(HoaDon hd, String key) {
        if (key.isEmpty()) return true;
        return chuanHoa(hd.getMaHoaDon()).contains(key)
                || chuanHoa(hd.getMaKhachHang()).contains(key)
                || chuanHoa(hd.getMaPhong()).contains(key)
                || chuanHoa(hd.getMaNguoiLap()).contains(key)
                || chuanHoa(hd.getTenNguoiLap()).contains(key)
                || chuanHoa(hd.getTrangThaiChu()).contains(key);
    }

    private static String chuanHoa(String chuoi) {
        if (chuoi == null) return "";
        String khongDau = Normalizer.normalize(chuoi, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        return khongDau.toLowerCase().trim();
    }
}