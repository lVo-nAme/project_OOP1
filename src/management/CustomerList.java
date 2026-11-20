package management;

import fileio.FileHandler;
import java.util.Scanner;
import person.KhachHang;
import util.Format;
import util.InputHelper;

/**
 * Lop quan ly danh sach khach hang su dung mang tinh KhachHang[] (khong dung ArrayList / Collection).
 * Tich hop cac ham nghiep vu tu CustomerList_3.java va dong bo voi he thong HotelManager & QuanLyNguoi.
 */
public class CustomerList {
    private static final int DUNG_LUONG_MAC_DINH = 200;
    private static final String FILE_NAME = "customers.txt";

    private KhachHang[] danhSach;
    private int soLuong;

    public CustomerList() {
        this(DUNG_LUONG_MAC_DINH);
    }

    public CustomerList(int dungLuong) {
        this.danhSach = new KhachHang[dungLuong > 0 ? dungLuong : DUNG_LUONG_MAC_DINH];
        this.soLuong = 0;
    }

    private void khoiTaoDuLieuMau() {
        luuCustomerKhiDatPhong(new KhachHang("KH001", "Nguyen Van An", "0901112222", "079200001111", "an@gmail.com", "TP HCM", "Viet Nam", 0));
        luuCustomerKhiDatPhong(new KhachHang("KH002", "Tran Thi Binh", "0903334444", "079200002222", "binh@gmail.com", "Ha Noi", "Viet Nam", 10));
    }

    // ==================== DOC / GHI FILE ====================

    public void load() {
        docFile();
        if (soLuong == 0) {
            khoiTaoDuLieuMau();
            ghiFile();
        }
    }

    public void save() {
        ghiFile();
    }

    public void ghiFile() {
        String[] lines = new String[soLuong];
        for (int i = 0; i < soLuong; i++) {
            lines[i] = danhSach[i].toFileString();
        }
        FileHandler.writeLinesToFile(FILE_NAME, lines, false);
    }

    public void docFile() {
        String[] lines = FileHandler.readLinesFromFile(FILE_NAME);
        soLuong = 0;
        if (lines == null || lines.length == 0) {
            return;
        }
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line == null || line.trim().isEmpty() || line.trim().startsWith("#")) {
                continue;
            }
            // Ho tro ca dinh dang chuan dau '|' va dinh dang cu dau ';'
            String normalizedLine = line.contains("|") ? line : line.replace(";", "|");
            KhachHang kh = KhachHang.fromFileString(normalizedLine);
            if (kh != null && soLuong < danhSach.length) {
                if (timCustomer(kh.getMaNguoi()) == null) {
                    danhSach[soLuong++] = kh;
                }
            }
        }
    }

    // ==================== NGHIEP VU QUAN LY KHACH HANG ====================

    /**
     * Luu khach hang vao CustomerList khi dat phong / nhan phong.
     * - Kiem tra trung Ma KH hoac CCCD thi khong luu trung.
     * - Neu la khach moi thi them vao danh sach va luu file.
     */
    public boolean luuCustomerKhiDatPhong(KhachHang customer) {
        if (customer == null) {
            System.out.println("Loi: Thong tin khach hang khong hop le!");
            return false;
        }

        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i] != null) {
                boolean trungMa = danhSach[i].getMaNguoi() != null
                        && danhSach[i].getMaNguoi().equalsIgnoreCase(customer.getMaNguoi());
                boolean trungCccd = danhSach[i].getCccd() != null
                        && !customer.getCccd().trim().isEmpty()
                        && danhSach[i].getCccd().equalsIgnoreCase(customer.getCccd());

                if (trungMa || trungCccd) {
                    System.out.println("=> Khach hang [" + danhSach[i].getHoTen() + "] (Ma: "
                            + danhSach[i].getMaNguoi() + ") da ton tai trong he thong.");
                    return false;
                }
            }
        }

        if (soLuong >= danhSach.length) {
            KhachHang[] mangMoi = new KhachHang[danhSach.length * 2];
            for (int i = 0; i < soLuong; i++) {
                mangMoi[i] = danhSach[i];
            }
            danhSach = mangMoi;
        }

        danhSach[soLuong++] = customer;
        return true;
    }

    public boolean add(KhachHang kh) {
        return luuCustomerKhiDatPhong(kh);
    }

    public boolean xoaCustomer(String maKhachHang) {
        if (maKhachHang == null || maKhachHang.trim().isEmpty()) {
            System.out.println("Loi: Ma khach hang can xoa khong duoc de trong!");
            return false;
        }

        int viTri = -1;
        String key = maKhachHang.trim();
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i] != null) {
                boolean trungMa = danhSach[i].getMaNguoi() != null
                        && danhSach[i].getMaNguoi().equalsIgnoreCase(key);
                boolean trungCccd = danhSach[i].getCccd() != null
                        && danhSach[i].getCccd().equalsIgnoreCase(key);
                if (trungMa || trungCccd) {
                    viTri = i;
                    break;
                }
            }
        }

        if (viTri == -1) {
            System.out.println("=> Khong tim thay khach hang co ma/CCCD [" + maKhachHang + "] de xoa!");
            return false;
        }

        String tenXoa = danhSach[viTri].getHoTen();
        String maXoa = danhSach[viTri].getMaNguoi();
        for (int i = viTri; i < soLuong - 1; i++) {
            danhSach[i] = danhSach[i + 1];
        }
        danhSach[--soLuong] = null;
        save();
        System.out.println("=> Da xoa khach hang [" + tenXoa + "] (Ma: " + maXoa + ") thanh cong!");
        return true;
    }

    public boolean remove(String id) {
        return xoaCustomer(id);
    }

    public KhachHang timCustomer(String maHoacCccd) {
        if (maHoacCccd == null || maHoacCccd.trim().isEmpty()) {
            return null;
        }
        String key = maHoacCccd.trim();
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i] != null) {
                if (danhSach[i].getMaNguoi().equalsIgnoreCase(key)
                        || danhSach[i].getCccd().equalsIgnoreCase(key)) {
                    return danhSach[i];
                }
            }
        }
        return null;
    }

    public KhachHang findById(String id) {
        if (id == null) return null;
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i].getMaNguoi().equalsIgnoreCase(id.trim())) {
                return danhSach[i];
            }
        }
        return null;
    }

    public KhachHang findByCccd(String cccd) {
        if (cccd == null) return null;
        for (int i = 0; i < soLuong; i++) {
            if (danhSach[i].getCccd().equalsIgnoreCase(cccd.trim())) {
                return danhSach[i];
            }
        }
        return null;
    }

    public KhachHang[] search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new KhachHang[0];
        }
        String k = keyword.trim().toLowerCase();
        int matched = 0;
        for (int i = 0; i < soLuong; i++) {
            KhachHang kh = danhSach[i];
            if (kh.getMaNguoi().toLowerCase().contains(k)
                    || kh.getHoTen().toLowerCase().contains(k)
                    || kh.getCccd().toLowerCase().contains(k)
                    || kh.getSoDienThoai().toLowerCase().contains(k)) {
                matched++;
            }
        }
        KhachHang[] result = new KhachHang[matched];
        int index = 0;
        for (int i = 0; i < soLuong; i++) {
            KhachHang kh = danhSach[i];
            if (kh.getMaNguoi().toLowerCase().contains(k)
                    || kh.getHoTen().toLowerCase().contains(k)
                    || kh.getCccd().toLowerCase().contains(k)
                    || kh.getSoDienThoai().toLowerCase().contains(k)) {
                result[index++] = kh;
            }
        }
        return result;
    }

    public void timKiem(String tuKhoa) {
        KhachHang[] ketQua = search(tuKhoa);
        System.out.println("\n--- KET QUA TIM KIEM KHACH HANG ---");
        if (ketQua.length == 0) {
            System.out.println("=> Khong tim thay khach hang nao phu hop voi tu khoa: " + tuKhoa);
            return;
        }
        for (int i = 0; i < ketQua.length; i++) {
            ketQua[i].hienThiThongTin();
        }
    }

    public void suaCustomer(String maKhachHang) {
        KhachHang kh = timCustomer(maKhachHang);
        if (kh == null) {
            System.out.println("=> Khong tim thay khach hang co ma [" + maKhachHang + "]!");
            return;
        }
        kh.hienThiThongTin();
        String ten = InputHelper.getOptionalString("Ho ten moi [" + kh.getHoTen() + "] (Enter = giu nguyen): ");
        if (!ten.isEmpty()) kh.setHoTen(ten);

        String sdt = InputHelper.getOptionalString("So dien thoai moi [" + kh.getSoDienThoai() + "] (Enter = giu nguyen): ");
        if (!sdt.isEmpty()) kh.setSoDienThoai(sdt);

        String cccd = InputHelper.getOptionalString("CCCD moi [" + kh.getCccd() + "] (Enter = giu nguyen): ");
        if (!cccd.isEmpty()) {
            KhachHang trung = findByCccd(cccd);
            if (trung != null && !trung.getMaNguoi().equalsIgnoreCase(kh.getMaNguoi())) {
                System.out.println("Loi: CCCD moi bi trung voi khach hang khac, giu nguyen CCCD cu.");
            } else {
                kh.setCccd(cccd);
            }
        }

        String email = InputHelper.getOptionalString("Email moi [" + kh.getEmail() + "] (Enter = giu nguyen): ");
        if (!email.isEmpty()) kh.setEmail(email);

        String diaChi = InputHelper.getOptionalString("Dia chi moi [" + kh.getDiaChi() + "] (Enter = giu nguyen): ");
        if (!diaChi.isEmpty()) kh.setDiaChi(diaChi);

        String quocTich = InputHelper.getOptionalString("Quoc tich moi [" + kh.getQuocTich() + "] (Enter = giu nguyen): ");
        if (!quocTich.isEmpty()) kh.setQuocTich(quocTich);

        save();
        System.out.println("=> Cap nhat thong tin khach hang [" + kh.getMaNguoi() + "] thanh cong!");
    }

    public KhachHang themMoiKhachHang() {
        String name = InputHelper.getString("Ho ten: ");
        String cccd = InputHelper.getString("CCCD/CMND: ");
        if (findByCccd(cccd) != null) {
            System.out.println("Loi: CCCD nay da ton tai trong he thong!");
            return null;
        }
        String phone = InputHelper.getString("So dien thoai: ");
        String email = InputHelper.getOptionalString("Email (Enter = bo qua): ");
        String diaChi = InputHelper.getOptionalString("Dia chi (Enter = TP HCM): ");
        if (diaChi.isEmpty()) diaChi = "TP HCM";
        String quocTich = InputHelper.getOptionalString("Quoc tich (Enter = Viet Nam): ");
        if (quocTich.isEmpty()) quocTich = "Viet Nam";

        KhachHang kh = new KhachHang(nextId(), name, phone, cccd, email, diaChi, quocTich, 0);
        if (luuCustomerKhiDatPhong(kh)) {
            save();
            System.out.println("=> Da them khach hang " + kh.getMaNguoi() + " - " + kh.getHoTen() + " thanh cong!");
            return kh;
        }
        return null;
    }

    public void xuatDanhSach() {
        System.out.println("\n================================ DANH SACH KHACH HANG (" + soLuong + ") ================================");
        if (soLuong == 0) {
            System.out.println("Danh sach khach hang dang trong!");
            return;
        }
        System.out.printf("%-4s | %-8s | %-20s | %-12s | %-14s | %-16s | %-10s | %-6s%n",
                "STT", "Ma KH", "Ho Ten", "SDT", "CCCD", "Dia Chi", "Quoc Tich", "Diem");
        System.out.println("-------------------------------------------------------------------------------------------------------");
        for (int i = 0; i < soLuong; i++) {
            KhachHang kh = danhSach[i];
            System.out.printf("%-4d | %-8s | %-20.20s | %-12s | %-14s | %-16.16s | %-10.10s | %-6d%n",
                    i + 1, kh.getMaNguoi(), kh.getHoTen(), kh.getSoDienThoai(),
                    kh.getCccd(), kh.getDiaChi(), kh.getQuocTich(), kh.getDiemTichLuy());
        }
        System.out.println("=======================================================================================================");
    }

    public void menuCustomerList() {
        int chon;
        do {
            System.out.println("\n======== PHAN HE QUAN LY KHACH HANG ========");
            System.out.println("1. Xuat danh sach khach hang");
            System.out.println("2. Them moi khach hang");
            System.out.println("3. Sua thong tin khach hang theo ma");
            System.out.println("4. Xoa khach hang theo ma");
            System.out.println("5. Tim kiem khach hang");
            System.out.println("6. Ghi file khach hang");
            System.out.println("7. Doc file khach hang");
            System.out.println("0. Quay lai");
            chon = InputHelper.getIntInRange("Chon chuc nang khach hang: ", 0, 7);

            switch (chon) {
                case 1:
                    xuatDanhSach();
                    break;
                case 2:
                    themMoiKhachHang();
                    break;
                case 3:
                    suaCustomer(InputHelper.getString("Nhap ma khach hang can sua: "));
                    break;
                case 4:
                    xoaCustomer(InputHelper.getString("Nhap ma khach hang hoac CCCD can xoa: "));
                    break;
                case 5:
                    timKiem(InputHelper.getString("Nhap ma, ho ten, CCCD hoac SDT can tim: "));
                    break;
                case 6:
                    ghiFile();
                    System.out.println("=> Da luu file data/" + FILE_NAME);
                    break;
                case 7:
                    docFile();
                    System.out.println("=> Da doc " + soLuong + " khach hang tu data/" + FILE_NAME);
                    break;
                default:
                    break;
            }
        } while (chon != 0);
    }

    // ==================== GETTERS & HELPERS ====================

    public String nextId() {
        String[] ids = new String[soLuong];
        for (int i = 0; i < soLuong; i++) {
            ids[i] = danhSach[i].getMaNguoi();
        }
        return Format.nextId("KH", ids, soLuong);
    }

    public KhachHang[] getAll() {
        KhachHang[] result = new KhachHang[soLuong];
        for (int i = 0; i < soLuong; i++) {
            result[i] = danhSach[i];
        }
        return result;
    }

    public KhachHang[] getDanhSach() {
        return danhSach;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public int getCount() {
        return soLuong;
    }
}