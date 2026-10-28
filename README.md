# KursusKu - Jetpack Compose UI Project

Aplikasi KursusKu adalah proyek antarmuka (UI) pendaftaran akun berbasis Android yang dikembangkan menggunakan **Jetpack Compose**. Proyek ini merupakan tugas praktik untuk mendemonstrasikan pemahaman mengenai *State Management*, *Event Handling*, dan *Form Validation* secara deklaratif.

## ✨ Fitur Utama
*   **Form Validasi Dinamis:** Mengevaluasi input pengguna (Nama, Email, Nomor WhatsApp minimal 10 digit, dan Kata Sandi minimal 8 karakter).
*   **State Retention (Tahan Rotasi):** Menggunakan `rememberSaveable` sehingga data yang sedang diketik dan hasil validasi tidak hilang saat layar perangkat dirotasi (Configuration Change).
*   **Adaptive Theme (Terang & Gelap):** Tampilan UI yang secara otomatis beradaptasi dengan mode sistem (Light/Dark mode) menggunakan palet warna kustom yang modern.
*   **Smart UX:** 
    * Keyboard virtual otomatis menyesuaikan tipe field (contoh: Numpad untuk WhatsApp).
    * Fitur *toggle* (ikon mata) untuk melihat dan menyembunyikan kata sandi.

## 🧠 Konsep State & Validasi
*   **State:** Aplikasi ini menerapkan `rememberSaveable` untuk menyimpan data input (*text field*) dan status pengiriman formulir. Hal ini memperbaiki kelemahan *state loss* pada `remember` biasa saat terjadi rotasi layar.
*   **Validasi:** Dilakukan secara reaktif. Variabel error (seperti `errorEmail`) terus mengevaluasi validitas *state*, namun peringatan visual (warna merah) baru diaktifkan (di-*trigger*) setelah *event onClick* pada tombol "Daftar Sekarang" mengubah nilai status `sudahSubmit` menjadi `true`.

## 🚀 Cara Menjalankan Project

**Prasyarat:**
*   Android Studio (disarankan versi terbaru: Koala / Ladybug).
*   Emulator Android (AVD) atau perangkat fisik Android.

**Langkah-langkah:**
1. Buka terminal atau *command prompt*.
2. Clone repositori ini ke komputer lokal Anda:
   ```bash
   git clone [https://github.com/RakhaRoderick/Kursusku]

1.Buka Android Studio.

2.Pilih File > Open, lalu cari folder proyek yang baru saja di-clone.

3.Tunggu hingga proses sinkronisasi Gradle selesai (indikator loading di pojok kanan bawah hilang).

4.Pilih emulator atau device fisik pada panel perangkat di atas.

5.Klik tombol Run (ikon segitiga hijau) atau tekan Shift + F10 untuk menjalankan aplikasi.
