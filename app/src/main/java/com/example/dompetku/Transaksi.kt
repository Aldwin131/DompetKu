package com.example.dompetku

data class Transaksi(
    val id: String,
    var nominal: Long,
    var kategori: String,
    var tanggal: String,
    var tipe: String
)