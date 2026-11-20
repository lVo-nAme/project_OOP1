package main;

import bill.ChiTietHoaDon;
import bill.HoaDon;
import bill.KhuyenMai;
import bill.ThoiDiemThanhToan;
import booking.Service;
import java.time.LocalDate;
import management.CustomerList;
import management.QuanLyHoaDon;
import management.RoomList;
import management.ServiceList;
import payment.IThanhToan;
import payment.ThanhToanChuyenKhoan;
import payment.ThanhToanThe;
import payment.ThanhToanTienMat;
import person.KhachHang;
import person.NhanVien;
import person.QuanLyNguoi;
import room.Phong;
import room.PhongCaoCap;
import room.PhongSuite;
import room.PhongTieuChuan;
import room.TrangThaiPhong;
import util.Format;
import util.InputHelper;

/**
 * Lop dieu khien chinh cua he thong Quan Ly Khach San.
 * Su dung mang tinh (array) va vong lap for co ban (khong dung ArrayList / Collection).
 */
public class HotelManager {
    private final RoomList rooms = new RoomList();
    private final CustomerList customers = new CustomerList();
    private final ServiceList services = new ServiceList();
    private final QuanLyHoaDon bills = new QuanLyHoaDon();
    private QuanLyNguoi quanLyNguoi;

    public void run() {
        rooms.load();
        customers.load();
        services.load();
        bills.load();
        quanLyNguoi = new QuanLyNguoi(customers);

        System.out.println("==========================================");
        System.out.println("      HE THONG QUAN LY KHACH SAN          ");
        System.out.println("==========================================");

        boolean running = true;
        while (running) {
            System.out.println("\n========== MENU CHINH ==========");
            System.out.println("1. Quan ly phong");
            System.out.println("2. Quan ly khach hang");
            System.out.println("3. Quan ly nhan su & vai tro (QuanLyNguoi)");
            System.out.println("4. Quan ly dich vu");
            System.out.println("5. Nhan phong (Check-in)");
            System.out.println("6. Goi dich vu cho phong dang o");
            System.out.println("7. Tra phong & thanh toan (Check-out)");
            System.out.println("8. Hoa don, khuyen mai & thong ke");
            System.out.println("0. Thoat");

            int choice = InputHelper.getIntInRange("Chon chuc nang: ", 0, 8);
            switch (choice) {
                case 1: roomMenu(); break;
                case 2: customerMenu(); break;
                case 3:
                    quanLyNguoi.menuQuanLyNguoi(
                            bills.totalRevenue(rooms),
                            bills.countPaid(),
                            rooms.getCount());
                    break;
                case 4: serviceMenu(); break;
                case 5: checkIn(); break;
                case 6: orderService(); break;
                case 7: checkOut(); break;
                case 8: billMenu(); break;
                default: running = false;
            }
        }
        System.out.println("Tam biet! Du lieu da duoc luu.");
    }

    // ===================== 1. QUAN LY PHONG =====================

    private void roomMenu() {
        while (true) {
            System.out.println("\n--- QUAN LY PHONG ---");
            System.out.println("1. Danh sach tat ca phong");
            System.out.println("2. Danh sach phong trong");
            System.out.println("3. Them phong moi (Tieu chuan / Cao cap / Suite)");
            System.out.println("4. Sua thong tin / trang thai phong");
            System.out.println("5. Xoa phong");
            System.out.println("6. Tim kiem phong (theo ma / so phong / loai)");
            System.out.println("7. Thong ke phong & doanh thu du kien");
            System.out.println("0. Quay lai");

            int c = InputHelper.getIntInRange("Chon: ", 0, 7);
            switch (c) {
                case 1: printRoomTable(rooms.getAll()); break;
                case 2: printRoomTable(rooms.getByStatus(TrangThaiPhong.TRONG)); break;
                case 3: addRoom(); break;
                case 4: rooms.suaPhong(InputHelper.getString("Ma phong can sua: ")); break;
                case 5: deleteRoom(); break;
                case 6: printRoomTable(rooms.timKiem(InputHelper.getString("Nhap tu khoa (ma/so/loai phong): "))); break;
                case 7:
                    rooms.thongKe();
                    rooms.tinhTongTienDuKien();
                    break;
                default: return;
            }
        }
    }

    private void printRoomTable(Phong[] list) {
        if (list.length == 0) {
            System.out.println("(Khong co phong nao)");
            return;
        }
        System.out.printf("%-8s %-8s %-12s %16s  %-15s%n",
                "Ma phong", "So phong", "Loai phong", "Gia co ban", "Trang thai");
        for (int i = 0; i < list.length; i++) {
            Phong r = list[i];
            System.out.printf("%-8s %-8s %-12s %16s  %-15s%n",
                    r.getMaPhong(), r.getSoPhong(), r.getLoaiPhong(),
                    Format.money(r.getGiaCoBan()), r.getTrangThai().getMoTa());
        }
    }

    private void addRoom() {
        String id = InputHelper.getString("Ma phong (vd: P102): ");
        if (rooms.findById(id) != null) {
            System.out.println("Loi: Ma phong da ton tai!");
            return;
        }
        String soPhong = InputHelper.getString("So phong (vd: 102): ");
        int tang = InputHelper.getPositiveInt("Tang: ");
        double giaCoBan = InputHelper.getPositiveDouble("Gia co ban moi dem (VND): ");
        int soNguoi = InputHelper.getPositiveInt("So nguoi toi da: ");
        int soGiuong = InputHelper.getPositiveInt("So giuong: ");
        double dienTich = InputHelper.getPositiveDouble("Dien tich (m2): ");

        System.out.println("Chon loai phong:");
        System.out.println("1. Phong Tieu Chuan");
        System.out.println("2. Phong Cao Cap (phu thu 10%)");
        System.out.println("3. Phong Suite (phu thu 25%)");
        int loai = InputHelper.getIntInRange("Loai phong: ", 1, 3);

        Phong phongMoi;
        if (loai == 1) {
            phongMoi = new PhongTieuChuan(id, soPhong, tang, giaCoBan, TrangThaiPhong.TRONG, soNguoi, soGiuong, dienTich);
        } else if (loai == 2) {
            boolean coBanCong = InputHelper.getYesNo("Phong co ban cong khong?");
            phongMoi = new PhongCaoCap(id, soPhong, tang, giaCoBan, TrangThaiPhong.TRONG, soNguoi, soGiuong, dienTich, coBanCong);
        } else {
            boolean coPhongKhach = InputHelper.getYesNo("Co phong khach rieng khong?");
            boolean coBonTam = InputHelper.getYesNo("Co bon tam khong?");
            phongMoi = new PhongSuite(id, soPhong, tang, giaCoBan, TrangThaiPhong.TRONG, soNguoi, soGiuong, dienTich, coPhongKhach, coBonTam);
        }

        if (rooms.themPhong(phongMoi)) {
            rooms.save();
            System.out.println("Da them phong " + id + " (" + phongMoi.getLoaiPhong() + ") thanh cong!");
        }
    }

    private void deleteRoom() {
        String ma = InputHelper.getString("Ma phong can xoa: ");
        Phong r = rooms.findById(ma);
        if (r == null) {
            System.out.println("Khong tim thay phong!");
            return;
        }
        if (r.getTrangThai() == TrangThaiPhong.DANG_SU_DUNG) {
            System.out.println("Phong dang co khach, khong the xoa!");
            return;
        }
        if (InputHelper.getYesNo("Xac nhan xoa phong " + r.getMaPhong() + "?")) {
            rooms.xoaPhong(r.getMaPhong());
        }
    }

    // ===================== 2. QUAN LY KHACH HANG =====================

    private void customerMenu() {
        while (true) {
            System.out.println("\n--- QUAN LY KHACH HANG ---");
            System.out.println("1. Danh sach khach hang");
            System.out.println("2. Tim kiem khach hang (theo ma / ten / CCCD / SDT)");
            System.out.println("3. Them khach hang moi");
            System.out.println("4. Sua thong tin khach hang");
            System.out.println("5. Xoa khach hang");
            System.out.println("0. Quay lai");

            int c = InputHelper.getIntInRange("Chon: ", 0, 5);
            switch (c) {
                case 1: customers.xuatDanhSach(); break;
                case 2: printCustomerTable(customers.search(InputHelper.getString("Tu khoa (ma/ten/CCCD/SDT): "))); break;
                case 3: customers.themMoiKhachHang(); break;
                case 4: customers.suaCustomer(InputHelper.getString("Ma khach hang hoac CCCD can sua: ")); break;
                case 5: deleteCustomer(); break;
                default: return;
            }
        }
    }

    private void printCustomerTable(KhachHang[] list) {
        if (list.length == 0) {
            System.out.println("(Khong co khach hang nao)");
            return;
        }
        System.out.printf("%-8s %-22s %-14s %-12s %-14s %-8s%n",
                "Ma KH", "Ho ten", "CCCD", "SDT", "Quoc tich", "Diem");
        for (int i = 0; i < list.length; i++) {
            KhachHang kh = list[i];
            System.out.printf("%-8s %-22s %-14s %-12s %-14s %-8d%n",
                    kh.getMaNguoi(), kh.getHoTen(), kh.getCccd(), kh.getSoDienThoai(),
                    kh.getQuocTich(), kh.getDiemTichLuy());
        }
    }

    private void deleteCustomer() {
        String key = InputHelper.getString("Ma khach hang hoac CCCD can xoa: ");
        KhachHang kh = customers.timCustomer(key);
        if (kh == null) {
            System.out.println("Khong tim thay khach hang!");
            return;
        }
        if (bills.hasOpenBillOfCustomer(kh.getMaNguoi())) {
            System.out.println("Khach dang o khach san, khong the xoa!");
            return;
        }
        if (InputHelper.getYesNo("Xac nhan xoa khach " + kh.getHoTen() + "?")) {
            customers.xoaCustomer(kh.getMaNguoi());
        }
    }

    // ===================== 4. QUAN LY DICH VU =====================

    private void serviceMenu() {
        while (true) {
            System.out.println("\n--- QUAN LY DICH VU ---");
            System.out.println("1. Danh sach dich vu");
            System.out.println("2. Them dich vu");
            System.out.println("3. Sua dich vu");
            System.out.println("4. Xoa dich vu");
            System.out.println("0. Quay lai");

            int c = InputHelper.getIntInRange("Chon: ", 0, 4);
            switch (c) {
                case 1: printServiceTable(); break;
                case 2: addService(); break;
                case 3: editService(); break;
                case 4: deleteService(); break;
                default: return;
            }
        }
    }

    private void printServiceTable() {
        Service[] list = services.getAll();
        if (list.length == 0) {
            System.out.println("(Chua co dich vu nao)");
            return;
        }
        System.out.printf("%-8s %-22s %16s  %-10s%n", "Ma DV", "Ten dich vu", "Don gia", "Don vi");
        for (int i = 0; i < list.length; i++) {
            Service s = list[i];
            System.out.printf("%-8s %-22s %16s  %-10s%n",
                    s.getServiceId(), s.getServiceName(), Format.money(s.getPrice()), s.getUnit());
        }
    }

    private void addService() {
        String name = InputHelper.getString("Ten dich vu: ");
        double price = InputHelper.getPositiveDouble("Don gia (VND): ");
        String unit = InputHelper.getString("Don vi tinh (chai, kg, suat...): ");
        Service s = new Service(services.nextId(), name, price, unit);
        services.add(s);
        services.save();
        System.out.println("Da them dich vu " + s.getServiceId());
    }

    private void editService() {
        Service s = services.findById(InputHelper.getString("Ma dich vu can sua: "));
        if (s == null) {
            System.out.println("Khong tim thay dich vu!");
            return;
        }
        String name = InputHelper.getOptionalString("Ten moi [" + s.getServiceName() + "] (Enter = giu nguyen): ");
        if (!name.isEmpty()) s.setServiceName(name);

        String priceText = InputHelper.getOptionalString("Don gia moi [" + Format.money(s.getPrice()) + "] (Enter = giu nguyen): ");
        if (!priceText.isEmpty()) {
            try {
                double price = Double.parseDouble(priceText);
                if (price > 0) s.setPrice(price);
            } catch (NumberFormatException e) {
                System.out.println("Gia khong hop le, giu nguyen.");
            }
        }
        services.save();
        System.out.println("Da cap nhat dich vu " + s.getServiceId());
    }

    private void deleteService() {
        Service s = services.findById(InputHelper.getString("Ma dich vu can xoa: "));
        if (s == null) {
            System.out.println("Khong tim thay dich vu!");
            return;
        }
        if (InputHelper.getYesNo("Xac nhan xoa dich vu " + s.getServiceName() + "?")) {
            services.remove(s.getServiceId());
            services.save();
            System.out.println("Da xoa dich vu.");
        }
    }

    // ===================== 5. NHAN PHONG (CHECK-IN) =====================

    private void checkIn() {
        Phong[] free = rooms.getByStatus(TrangThaiPhong.TRONG);
        if (free.length == 0) {
            System.out.println("Hien khong con phong trong!");
            return;
        }
        System.out.println("\n--- NHAN PHONG ---");
        printRoomTable(free);

        Phong phong = null;
        while (phong == null) {
            String id = InputHelper.getString("Nhap ma phong muon nhan (0 = huy): ");
            if (id.equals("0")) return;
            Phong p = rooms.findById(id);
            if (p == null || !p.kiemTraTrong()) {
                System.out.println("Phong khong ton tai hoac khong trong!");
            } else {
                phong = p;
            }
        }

        KhachHang kh = chooseCustomer();
        if (kh == null) {
            System.out.println("Da huy nhan phong.");
            return;
        }

        NhanVien nv = quanLyNguoi.layNhanVienMacDinh();
        LocalDate ngayNhan = InputHelper.getDate("Ngay nhan phong (dd/MM/yyyy, Enter = hom nay): ", LocalDate.now());

        HoaDon hd = new HoaDon(
                bills.nextId(), kh.getMaNguoi(), phong.getMaPhong(),
                nv.getMaNguoi(), nv.getHoTen(), ngayNhan, ngayNhan, phong.getGiaCoBan());
        bills.themHoaDon(hd);
        phong.setTrangThai(TrangThaiPhong.DANG_SU_DUNG);
        bills.save();
        rooms.save();

        System.out.println("Nhan phong thanh cong! Hoa don " + hd.getMaHoaDon()
                + " - Phong " + phong.getMaPhong() + " (" + phong.getLoaiPhong() + ")"
                + " - Khach " + kh.getHoTen());
    }

    private KhachHang chooseCustomer() {
        while (true) {
            System.out.println("1. Khach da co trong he thong");
            System.out.println("2. Khach moi (tu dong luu vao CustomerList)");
            System.out.println("0. Huy");
            int c = InputHelper.getIntInRange("Chon: ", 0, 2);
            if (c == 0) return null;
            if (c == 2) {
                KhachHang kh = customers.themMoiKhachHang();
                if (kh != null) return kh;
                continue;
            }
            String id = InputHelper.getString("Ma khach hang hoac CCCD (vd: KH001, 0 = quay lai): ");
            if (id.equals("0")) continue;
            KhachHang kh = customers.timCustomer(id);
            if (kh == null) {
                System.out.println("Khong tim thay khach hang!");
            } else if (bills.hasOpenBillOfCustomer(kh.getMaNguoi())) {
                System.out.println("Khach nay dang o mot phong khac!");
            } else {
                return kh;
            }
        }
    }

    // ===================== 6. GOI DICH VU =====================

    private HoaDon chooseOpenBill() {
        HoaDon[] open = bills.getOpenBills();
        if (open.length == 0) {
            System.out.println("Hien khong co phong nao dang co khach!");
            return null;
        }
        System.out.printf("%-8s %-8s %-22s %-12s%n", "Phong", "Hoa don", "Khach hang", "Nhan phong");
        for (int i = 0; i < open.length; i++) {
            HoaDon hd = open[i];
            System.out.printf("%-8s %-8s %-22s %-12s%n",
                    hd.getMaPhong(), hd.getMaHoaDon(), customerName(hd.getMaKhachHang()), Format.date(hd.getNgayNhanPhong()));
        }
        while (true) {
            String maPhong = InputHelper.getString("Nhap ma phong (0 = huy): ");
            if (maPhong.equals("0")) return null;
            HoaDon hd = bills.findOpenByRoom(maPhong);
            if (hd != null) return hd;
            System.out.println("Phong nay khong co khach dang o!");
        }
    }

    private void orderService() {
        System.out.println("\n--- GOI DICH VU ---");
        if (services.getCount() == 0) {
            System.out.println("Chua co dich vu nao!");
            return;
        }
        HoaDon hd = chooseOpenBill();
        if (hd == null) return;

        printServiceTable();
        Service service = null;
        while (service == null) {
            String id = InputHelper.getString("Nhap ma dich vu (0 = huy): ");
            if (id.equals("0")) return;
            service = services.findById(id);
            if (service == null) {
                System.out.println("Khong tim thay dich vu!");
            }
        }
        int qty = InputHelper.getPositiveInt("So luong (" + service.getUnit() + "): ");
        ChiTietHoaDon ct = new ChiTietHoaDon(
                hd.taoMaChiTietTiepTheo(),
                ChiTietHoaDon.LOAI_DICH_VU,
                service.getServiceId(),
                service.getServiceName(),
                ThoiDiemThanhToan.KHI_TRA_PHONG,
                qty,
                service.getPrice(),
                8); // VAT 8%
        hd.themChiTiet(ct);
        bills.save();
        System.out.println("Da them dich vu " + service.getServiceName() + " vao phong " + hd.getMaPhong());
    }

    // ===================== 7. TRA PHONG & THANH TOAN =====================

    private void checkOut() {
        System.out.println("\n--- TRA PHONG & THANH TOAN ---");
        HoaDon hd = chooseOpenBill();
        if (hd == null) return;

        Phong phong = rooms.findById(hd.getMaPhong());
        LocalDate ngayTra = InputHelper.getDate("Ngay tra phong (dd/MM/yyyy, Enter = hom nay): ", LocalDate.now());
        if (ngayTra.isBefore(hd.getNgayNhanPhong())) {
            System.out.println("Loi: Ngay tra phong khong duoc truoc ngay nhan phong!");
            return;
        }

        // Hoi ap dung ma khuyen mai
        if (bills.getSoLuongKhuyenMai() > 0 && InputHelper.getYesNo("Ban co muon ap dung ma khuyen mai khong?")) {
            System.out.println("1. Chon ma khuyen mai cu the");
            System.out.println("2. Tu dong chon khuyen mai tot nhat");
            int kmChoice = InputHelper.getIntInRange("Chon: ", 1, 2);
            if (kmChoice == 2) {
                bills.apDungKhuyenMaiTotNhat(hd.getMaHoaDon(), phong, ngayTra);
            } else {
                bills.hienThiDanhSachKhuyenMai();
                String maKM = InputHelper.getString("Nhap ma khuyen mai: ");
                KhuyenMai km = bills.timKhuyenMaiTheoMa(maKM);
                if (km == null) {
                    System.out.println("Khong tim thay ma khuyen mai!");
                } else if (hd.apDungKhuyenMai(km, phong, ngayTra)) {
                    System.out.println("Da ap dung khuyen mai " + km.getMaKhuyenMai() + "!");
                }
            }
        }

        hd.hienThiHoaDon(phong, customerName(hd.getMaKhachHang()), ngayTra);

        if (!InputHelper.getYesNo("Tien hanh thanh toan?")) {
            System.out.println("Chua thanh toan, phong van dang o.");
            return;
        }

        double tongCanTra = hd.tinhTongThanhToan(phong, ngayTra);
        System.out.println("Chon phuong thuc thanh toan:");
        System.out.println("1. Tien mat");
        System.out.println("2. Chuyen khoan");
        System.out.println("3. The ngan hang");
        System.out.println("0. Huy");
        int m = InputHelper.getIntInRange("Chon: ", 0, 3);
        if (m == 0) return;

        IThanhToan phuongThuc;
        if (m == 1) {
            double tienDua = InputHelper.getPositiveDouble("Nhap so tien khach dua (VND): ");
            phuongThuc = new ThanhToanTienMat(tienDua);
        } else if (m == 2) {
            String nganHang = InputHelper.getString("Nhap ten ngan hang: ");
            String maGD = InputHelper.getString("Nhap ma giao dich: ");
            phuongThuc = new ThanhToanChuyenKhoan(nganHang, maGD);
        } else {
            String loaiThe = InputHelper.getString("Nhap loai the (Visa/Master/ATM): ");
            String soThe = InputHelper.getString("Nhap 4 so cuoi the: ");
            String maGD = InputHelper.getString("Nhap ma giao dich: ");
            phuongThuc = new ThanhToanThe(loaiThe, soThe, maGD);
        }

        if (!hd.thanhToan(phong, ngayTra, phuongThuc)) {
            System.out.println("Thanh toan that bai!");
            return;
        }

        phuongThuc.hienThiThongTin();

        if (phong != null) {
            phong.setTrangThai(TrangThaiPhong.TRONG);
        }

        // Cong diem tich luy cho khach hang (100,000 VND = 1 diem)
        KhachHang kh = customers.findById(hd.getMaKhachHang());
        if (kh != null) {
            int diemCong = (int) (tongCanTra / 100000);
            if (diemCong > 0) {
                kh.capNhatDiemTichLuy(diemCong);
            }
        }

        bills.save();
        rooms.save();
        customers.save();
        System.out.println("Thanh toan thanh cong! Phong " + hd.getMaPhong() + " da duoc tra.");
    }

    private String customerName(String maKH) {
        KhachHang kh = customers.findById(maKH);
        return kh == null ? "(" + maKH + ")" : kh.getHoTen();
    }

    // ===================== 8. HOA DON, KHUYEN MAI & THONG KE =====================

    private void billMenu() {
        while (true) {
            System.out.println("\n--- HOA DON, KHUYEN MAI & THONG KE ---");
            System.out.println("1. Danh sach tat ca hoa don");
            System.out.println("2. Xem chi tiet mot hoa don");
            System.out.println("3. Tim kiem hoa don");
            System.out.println("4. Sua / xoa hoa don chua thanh toan");
            System.out.println("5. Sap xep hoa don (theo tong tien / ngay lap)");
            System.out.println("6. Quan ly khuyen mai");
            System.out.println("7. Thong ke doanh thu chi tiet");
            System.out.println("0. Quay lai");

            int c = InputHelper.getIntInRange("Chon: ", 0, 7);
            switch (c) {
                case 1: printBillTable(bills.getAll()); break;
                case 2: showBillDetail(); break;
                case 3: printBillTable(bills.timKiemHoaDon(InputHelper.getString("Nhap tu khoa hoa don (ma HD/KH/phong/trang thai): "))); break;
                case 4: editOrDeleteBill(); break;
                case 5: sortBills(); break;
                case 6: promotionMenu(); break;
                case 7: printStatistics(); break;
                default: return;
            }
        }
    }

    private void printBillTable(HoaDon[] list) {
        if (list.length == 0) {
            System.out.println("(Chua co hoa don nao)");
            return;
        }
        System.out.printf("%-8s %-8s %-20s %-12s %-12s %-15s %16s%n",
                "Ma HD", "Phong", "Khach hang", "Nhan", "Tra", "Trang thai", "Tong tien");
        for (int i = 0; i < list.length; i++) {
            HoaDon hd = list[i];
            Phong p = rooms.findById(hd.getMaPhong());
            String status = hd.daThanhToan() ? "Da thanh toan" : "Dang o";
            LocalDate mocTra = hd.daThanhToan() ? hd.getNgayTraPhong() : LocalDate.now();
            String total = Format.money(hd.tinhTongThanhToan(p, mocTra)) + (hd.daThanhToan() ? "" : " (tam tinh)");
            System.out.printf("%-8s %-8s %-20.20s %-12s %-12s %-15s %16s%n",
                    hd.getMaHoaDon(), hd.getMaPhong(), customerName(hd.getMaKhachHang()),
                    Format.date(hd.getNgayNhanPhong()), Format.date(hd.getNgayTraPhong()),
                    status, total);
        }
    }

    private void showBillDetail() {
        HoaDon hd = bills.findById(InputHelper.getString("Ma hoa don (vd: HD001): "));
        if (hd == null) {
            System.out.println("Khong tim thay hoa don!");
            return;
        }
        Phong p = rooms.findById(hd.getMaPhong());
        LocalDate out = hd.daThanhToan() ? hd.getNgayTraPhong() : LocalDate.now();
        hd.hienThiHoaDon(p, customerName(hd.getMaKhachHang()), out);
    }

    private void editOrDeleteBill() {
        System.out.println("1. Sua hoa don chua thanh toan");
        System.out.println("2. Xoa hoa don");
        int chon = InputHelper.getIntInRange("Chon: ", 1, 2);
        String ma = InputHelper.getString("Nhap ma hoa don: ");
        if (chon == 1) {
            bills.suaHoaDon(ma, rooms);
        } else {
            if (bills.xoaHoaDon(ma)) {
                System.out.println("=> Da xoa hoa don " + ma);
            }
        }
    }

    private void sortBills() {
        System.out.println("1. Sap xep theo tong tien tang dan");
        System.out.println("2. Sap xep theo tong tien giam dan");
        System.out.println("3. Sap xep theo ngay lap hoa don");
        int chon = InputHelper.getIntInRange("Chon: ", 1, 3);
        if (chon == 1) {
            bills.sapXepHoaDonTheoTongTien(true, rooms);
        } else if (chon == 2) {
            bills.sapXepHoaDonTheoTongTien(false, rooms);
        } else {
            bills.sapXepHoaDonTheoNgayLap();
        }
        printBillTable(bills.getAll());
    }

    private void promotionMenu() {
        while (true) {
            System.out.println("\n--- QUAN LY KHUYEN MAI ---");
            System.out.println("1. Danh sach khuyen mai");
            System.out.println("2. Them khuyen mai moi");
            System.out.println("3. Xoa khuyen mai");
            System.out.println("0. Quay lai");
            int c = InputHelper.getIntInRange("Chon: ", 0, 3);
            switch (c) {
                case 1: bills.hienThiDanhSachKhuyenMai(); break;
                case 2: bills.themKhuyenMaiTuBanPhim(); break;
                case 3: bills.xoaKhuyenMai(InputHelper.getString("Nhap ma khuyen mai can xoa: ")); break;
                default: return;
            }
        }
    }

    private void printStatistics() {
        System.out.println("\n--- THONG KE KHACH SAN ---");
        System.out.println("Tong so phong             : " + rooms.getCount());
        System.out.println("  Phong trong             : " + rooms.getByStatus(TrangThaiPhong.TRONG).length);
        System.out.println("  Phong dang su dung      : " + rooms.getByStatus(TrangThaiPhong.DANG_SU_DUNG).length);
        System.out.println("  Phong bao tri           : " + rooms.getByStatus(TrangThaiPhong.BAO_TRI).length);
        System.out.println("Tong so khach hang        : " + customers.getCount());
        System.out.println("Hoa don da thanh toan     : " + bills.countPaid());
        System.out.println("Hoa don dang mo           : " + bills.getOpenBills().length);
        System.out.println("TONG DOANH THU THUC TE    : " + Format.money(bills.totalRevenue(rooms)));

        bills.thongKeTienPhongVaDichVu(rooms);
        bills.thongKeTheoPhuongThucThanhToan(rooms);
        bills.thongKeDoanhThuTheoThang(LocalDate.now().getYear(), rooms);
    }
}