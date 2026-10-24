// Tyasastri Hning Kurniasih - L0325034
// Responsi 1 Pemrograman Berorientasi Objek

package Responsi1;


import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SistemLogistikBantuan {

    static ArrayList<BarangBantuan> daftarBarang =
            new ArrayList<>();

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        int pilihan = 0;

        while (pilihan != 2) {
            System.out.println("\n=== SISTEM LOGISTIK BANTUAN ===");
            System.out.println("1. Tambah Barang Bantuan");
            System.out.println("2. Selesai dan Cetak Manifes");
            System.out.print("Pilih menu: ");

            try {
                pilihan = Integer.parseInt(input.nextLine());

                if (pilihan == 1) {
                    tambahBarang();
                } else if (pilihan == 2) {
                    cetakManifes();
                } else {
                    System.out.println(
                        "Pilihan tidak valid. Silakan coba lagi."
                    );
                }

            } catch (NumberFormatException e) {
                System.out.println(
                    "Input harus berupa angka."
                );
            }
        }

        input.close();
    }

    static void tambahBarang() {
        try {
            System.out.print("Kode barang (EDX-...): ");
            String kode = input.nextLine().trim();

            if (!kode.startsWith("EDX-")) {
                throw new IllegalArgumentException(
                    "Kode barang harus diawali EDX-"
                );
            }

            if (kode.length() <= 4) {
                throw new IllegalArgumentException(
                    "Kode barang setelah EDX- tidak boleh kosong."
                );
            }

            System.out.print("Nama barang: ");
            String nama = input.nextLine().trim();

            if (nama.isEmpty()) {
                throw new IllegalArgumentException(
                    "Nama barang tidak boleh kosong."
                );
            }

            System.out.print("Kategori barang: ");
            String kategori = input.nextLine().trim();

            if (kategori.isEmpty()) {
                throw new IllegalArgumentException(
                    "Kategori barang tidak boleh kosong."
                );
            }

            System.out.print("Berat barang (kg): ");
            double berat = Double.parseDouble(input.nextLine());

            if (berat <= 0 || Double.isInfinite(berat)
                    || Double.isNaN(berat)) {
                throw new BeratInvalidException(
                    "Berat barang harus lebih dari 0 kg."
                );
            }

            // Memeriksa kode barang agar tidak duplikat
            for (BarangBantuan barang : daftarBarang) {
                if (barang.getKodeBarang().equals(kode)) {
                    throw new IllegalArgumentException(
                        "Kode barang sudah terdaftar."
                    );
                }
            }

            BarangBantuan barangBaru = new BarangBantuan(
                kode, nama, kategori, berat
            );

            daftarBarang.add(barangBaru);

            System.out.println(
                "Barang berhasil ditambahkan!"
            );

        } catch (BeratInvalidException e) {
            System.out.println("Kesalahan berat: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            System.out.println("Kesalahan input: " + e.getMessage());

        } finally {
            System.out.println("Proses input selesai.");
        }
    }

    static void cetakManifes() {
        double totalBerat = 0;
        int totalBarang = daftarBarang.size();

        for (BarangBantuan barang : daftarBarang) {
            totalBerat += barang.getBeratKg();
        }

        String armada;

        if (totalBerat <= 50) {
            armada = "Mobil Pickup";
        } else {
            armada = "Truk Ekspedisi";
        }

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("manifest_pengiriman.txt"))) {

            writer.write("========================================");
            writer.newLine();
            writer.write("       MANIFES PENGIRIMAN BANTUAN");
            writer.newLine();
            writer.write("========================================");
            writer.newLine();
            writer.newLine();

            writer.write("Daftar Barang:");
            writer.newLine();
            writer.write("----------------------------------------");
            writer.newLine();

            for (BarangBantuan barang : daftarBarang) {
                writer.write(
                    "Kode Barang : " + barang.getKodeBarang()
                );
                writer.newLine();

                writer.write(
                    "Nama Barang : " + barang.getNamaBarang()
                );
                writer.newLine();

                writer.write(
                    "Kategori    : " + barang.getKategori()
                );
                writer.newLine();

                writer.write(
                    "Berat       : " + barang.getBeratKg() + " kg"
                );
                writer.newLine();

                writer.write("----------------------------------------");
                writer.newLine();
            }

            writer.newLine();
            writer.write("Total jenis barang : " + totalBarang);
            writer.newLine();
            writer.write("Total berat muatan : " + totalBerat + " kg");
            writer.newLine();
            writer.write("Label armada       : " + armada);
            writer.newLine();

            writer.newLine();
            writer.write("Manifes dibuat oleh Sistem Logistik Bantuan.");
            writer.newLine();

            System.out.println(
                "Data manifes berhasil diekspor ke manifest_pengiriman.txt"
            );

            System.out.println("Total barang : " + totalBarang);
            System.out.println("Total berat  : " + totalBerat + " kg");
            System.out.println("Armada       : " + armada);

        } catch (IOException e) {
            System.out.println(
                "Gagal menulis manifes: " + e.getMessage()
            );
        }
    }
}