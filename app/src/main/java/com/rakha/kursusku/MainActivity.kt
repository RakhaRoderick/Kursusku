package com.rakha.kursusku

import android.content.res.Configuration
import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rakha.kursusku.ui.theme.DarkSuccessContainer
import com.rakha.kursusku.ui.theme.DarkSuccessIcon
import com.rakha.kursusku.ui.theme.DarkSuccessText
import com.rakha.kursusku.ui.theme.KursuskuTheme
import com.rakha.kursusku.ui.theme.LightSuccessContainer
import com.rakha.kursusku.ui.theme.LightSuccessIcon
import com.rakha.kursusku.ui.theme.LightSuccessText

/**
 * Activity utama aplikasi yang menjadi titik masuk (entry point).
 * Mengatur tema dasar [KursuskuTheme] dan menampilkan layar pendaftaran [RegistrationScreen].
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KursuskuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    RegistrationScreen()
                }
            }
        }
    }
}

/**
 * Screen-level state owner untuk pendaftaran akun [RegistrationScreen].
 * Memperagakan penerapan State, Event, Anatomi Field (OutlinedTextField), dan Form Validation di Jetpack Compose
 * dengan dukungan penyesuaian Mode Terang (Light Mode) dan Mode Gelap (Dark Mode) secara otomatis.
 */
@Composable
fun RegistrationScreen() {
    // =========================================================================
    // 1. STATE MANAGEMENT
    // State adalah data yang menentukan apa yang ditampilkan oleh UI.
    // Ketika state berubah, Jetpack Compose akan melakukan Rekomposisi (Recomposition).
    // =========================================================================

    // State input formulir
    // - rememberSaveable: Menjaga state tetap bertahan saat terjadi perubahan konfigurasi (misal: rotasi layar / lanskap).
    var nama by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var wa by rememberSaveable { mutableStateOf("") } // State input Nomor WhatsApp

    var password by rememberSaveable { mutableStateOf("") }
    var konfirmasiPassword by rememberSaveable { mutableStateOf("") }
    var setuju by rememberSaveable { mutableStateOf(value = false) }
    var passwordTerlihat by rememberSaveable { mutableStateOf(value = false) }

    // State status pengiriman dan hasil validasi
    // - rememberSaveable: Menyimpan status submit & berhasil agar tetap bertahan saat rotasi layar.
    var sudahSubmit by rememberSaveable { mutableStateOf(value = false) }
    var berhasil by rememberSaveable { mutableStateOf(value = false) }

    // Evaluasi mode tema untuk warna dinamis khusus
    val isDark = isSystemInDarkTheme()
    val successContainer = if (isDark) DarkSuccessContainer else LightSuccessContainer
    val successText = if (isDark) DarkSuccessText else LightSuccessText
    val successIcon = if (isDark) DarkSuccessIcon else LightSuccessIcon

    // =========================================================================
    // 2. FORM VALIDATION (VALIDASI FORMULIR)
    // Evaluasi validasi dihitung secara reaktif/deklaratif setiap kali terjadi perubahan state input.
    // =========================================================================
    // Evaluasi pesan error untuk setiap field (hanya ditampilkan setelah tombol submit ditekan)
    val errorNama = sudahSubmit && nama.trim().isEmpty()
    val errorEmail = sudahSubmit && !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    val errorWa = sudahSubmit && (wa.trim().filter { it.isDigit() }.length < 10) // Validasi minimal 10 angka untuk WA
    val errorPassword = sudahSubmit && (password.length < 8)
    val errorKonfirmasi = sudahSubmit && (konfirmasiPassword == "" || konfirmasiPassword != password)
    val errorPersetujuan = sudahSubmit && !setuju

    // Seluruh formulir dinyatakan valid jika semua kriteria terpenuhi:
    // 1. Nama tidak kosong.
    // 2. Email sesuai format alamat email yang valid.
    // 3. Nomor WA diisi minimal 10 digit angka.
    // 4. Password minimal 8 karakter.
    // 5. Konfirmasi password sama dengan password.
    // 6. Checkbox persetujuan telah dicentang.
    val formValid = nama.trim().isNotEmpty() &&
            Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() &&
            wa.trim().filter { it.isDigit() }.length >= 10 &&
            password.length >= 8 && konfirmasiPassword == password && setuju

    // Layout utama layar dengan dukungan scroll vertikal dan spacing yang leluasa
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Header Aplikasi (Copywriting Baru & Branding Adaptif Terang/Gelap)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "KursusKu",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.secondary, // Menggunakan warna secondary yang kontras tinggi di mode gelap
                fontWeight = FontWeight.ExtraBold,
            )
            Text(
                text = "Buat Akun Baru",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "Daftarkan dirimu untuk memulai perjalanan belajar",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 14.sp,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Kartu kontainer formulir dengan sudut organik melengkung dan elevasi lembut
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // =========================================================================
                // 3. ANATOMI FIELD (OutlinedTextField) & EVENT HANDLING
                // =========================================================================

                // Anatomi Field 1: Input Nama Lengkap
                OutlinedTextField(
                    value = nama, // [Anatomi Field: Value] State penampung teks nama
                    onValueChange = { // [EVENT] Dipanggil setiap ada perubahan teks dari input pengguna
                        nama = it
                        berhasil = false // Reset status berhasil saat input berubah
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nama Lengkap") }, // [Anatomi Field: Label]
                    placeholder = { Text("Nama sesuai identitas") },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = null,
                            tint = if (isDark) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                        )
                    }, // [Anatomi Field: Leading Icon]
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    isError = errorNama, // [Anatomi Field & Validasi: Status Error]
                    supportingText = { if (errorNama) Text("Nama wajib diisi") }, // [Anatomi Field & Validasi: Supporting Text]
                )

                // Anatomi Field 2: Input Email
                OutlinedTextField(
                    value = email, // [Anatomi Field: Value]
                    onValueChange = { // [EVENT]
                        email = it
                        berhasil = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Email") }, // [Anatomi Field: Label]
                    placeholder = { Text("contoh@email.com") },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Email,
                            contentDescription = null,
                            tint = if (isDark) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                        )
                    }, // [Anatomi Field: Leading Icon]
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), // [Anatomi Field: Keyboard Options]
                    isError = errorEmail, // [Anatomi Field & Validasi: Status Error]
                    supportingText = { if (errorEmail) Text("Masukkan alamat email yang valid") }, // [Anatomi Field & Validasi: Supporting Text]
                )

                // Anatomi Field 3: Input Nomor WhatsApp (Field Baru)
                OutlinedTextField(
                    value = wa, // [Anatomi Field: Value] State penampung teks Nomor WA
                    onValueChange = { // [EVENT]
                        wa = it
                        berhasil = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nomor WhatsApp") }, // [Anatomi Field: Label]
                    placeholder = { Text("08xxxxxxxxxx") },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Phone,
                            contentDescription = null,
                            tint = if (isDark) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                        )
                    }, // [Anatomi Field: Leading Icon]
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), // [Anatomi Field: Keyboard Options]
                    isError = errorWa, // [Anatomi Field & Validasi: Status Error]
                    supportingText = { if (errorWa) Text("Nomor WA minimal 10 angka") }, // [Anatomi Field & Validasi: Supporting Text]
                )

                // Anatomi Field 4: Input Kata Sandi
                OutlinedTextField(
                    value = password, // [Anatomi Field: Value]
                    onValueChange = { // [EVENT]
                        password = it
                        berhasil = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Kata Sandi") }, // [Anatomi Field: Label]
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Lock,
                            contentDescription = null,
                            tint = if (isDark) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                        )
                    }, // [Anatomi Field: Leading Icon]
                    trailingIcon = { // [Anatomi Field: Trailing Icon] Tombol toggle visibilitas kata sandi
                        IconButton(onClick = { passwordTerlihat = !passwordTerlihat }) { // [EVENT] Toggle nilai boolean passwordTerlihat
                            Icon(
                                imageVector = if (passwordTerlihat) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = if (passwordTerlihat) "Sembunyikan kata sandi" else "Tampilkan kata sandi",
                            )
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    visualTransformation = if (passwordTerlihat) VisualTransformation.None else PasswordVisualTransformation(), // [Anatomi Field: Visual Transformation]
                    singleLine = true,
                    isError = errorPassword, // [Anatomi Field & Validasi: Status Error]
                    supportingText = { if (errorPassword) Text("Kata sandi minimal 8 karakter") }, // [Anatomi Field & Validasi: Supporting Text]
                )

                // Anatomi Field 5: Input Konfirmasi Kata Sandi
                OutlinedTextField(
                    value = konfirmasiPassword, // [Anatomi Field: Value]
                    onValueChange = { // [EVENT]
                        konfirmasiPassword = it
                        berhasil = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Konfirmasi Kata Sandi") }, // [Anatomi Field: Label]
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Lock,
                            contentDescription = null,
                            tint = if (isDark) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                        )
                    }, // [Anatomi Field: Leading Icon]
                    shape = RoundedCornerShape(14.dp),
                    visualTransformation = PasswordVisualTransformation(), // [Anatomi Field: Visual Transformation]
                    singleLine = true,
                    isError = errorKonfirmasi, // [Anatomi Field & Validasi: Status Error]
                    supportingText = { if (errorKonfirmasi) Text("Konfirmasi harus sama dengan kata sandi") }, // [Anatomi Field & Validasi: Supporting Text]
                )

                // Checkbox Persetujuan Ketentuan (Event & Validasi)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Checkbox(
                        checked = setuju, // [State] Status centang
                        onCheckedChange = { // [EVENT] Dipanggil saat Checkbox diklik
                            setuju = it
                            berhasil = false
                        },
                        colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.secondary),
                    )
                    Text(
                        text = "Saya menyetujui ketentuan penggunaan",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                if (errorPersetujuan) { // [Form Validation: Pesan error persetujuan]
                    Text(
                        text = "Persetujuan wajib dicentang",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // =========================================================================
                // 4. EVENT SUBMIT FORMULIR
                // Mengatur alur saat pengguna menekan tombol pendaftaran.
                // =========================================================================
                Button(
                    onClick = { // [EVENT] Dipanggil saat tombol "Daftar Sekarang" diklik oleh pengguna
                        sudahSubmit = true // Memicu pembaruan state bahwa formulir telah dikirim
                        berhasil = formValid // Menyimpan hasil validasi formulir
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp, pressedElevation = 6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    Text(
                        text = "Daftar Sekarang",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 6.dp),
                    )
                }

                // Tampilan Pesan Sukses jika validasi berhasil (Adaptif Terang & Gelap)
                if (berhasil) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 4.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = successContainer),
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = null,
                                tint = successIcon,
                            )
                            Column {
                                Text(
                                    text = "Pendaftaran Berhasil!",
                                    color = successText,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                )
                                Text(
                                    text = "Selamat bergabung, ${nama.trim()}! Formulir siap diproses.",
                                    color = successText.copy(alpha = 0.9f),
                                    fontSize = 13.sp,
                                )
                            }
                        }
                    }
                }
            }
        }

        Text(
            text = "Petunjuk: Isi semua kolom (Nama, Email, WA minimal 10 angka, Kata Sandi minimal 8 karakter) dan centang persetujuan.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
        )
    }
}

/**
 * Fungsi Preview Mode Terang (Light Mode) untuk Interactive Preview.
 */
@Preview(showBackground = true, name = "Light Mode")
@Composable
fun RegistrationScreenLightPreview() {
    KursuskuTheme(darkTheme = false) {
        RegistrationScreen()
    }
}

/**
 * Fungsi Preview Mode Gelap (Dark Mode) untuk Interactive Preview.
 */
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun RegistrationScreenDarkPreview() {
    KursuskuTheme(darkTheme = true) {
        RegistrationScreen()
    }
}
