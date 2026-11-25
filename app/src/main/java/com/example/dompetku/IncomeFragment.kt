package com.example.dompetku

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class IncomeFragment : Fragment() {

    companion object {
        val listTransaksi = mutableListOf<Transaksi>()
    }

    private lateinit var adapter: TransaksiAdapter
    private val listIncomeTampil = mutableListOf<Transaksi>()
    private var listNamaKategoriIncome = listOf<String>()
    private var editingTransaksi: Transaksi? = null

    private lateinit var etNominal: EditText
    private lateinit var spKategori: Spinner
    private lateinit var etTanggal: EditText
    private lateinit var btnSimpan: Button
    private lateinit var btnBatal: Button
    private lateinit var rvIncome: RecyclerView

    private val calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_income, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etNominal = view.findViewById(R.id.etNominalIncome)
        spKategori = view.findViewById(R.id.spKategoriIncome)
        etTanggal = view.findViewById(R.id.etTanggalIncome)
        btnSimpan = view.findViewById(R.id.btnSimpanIncome)
        btnBatal = view.findViewById(R.id.btnBatalEditIncome)
        rvIncome = view.findViewById(R.id.rvIncome)

        setupSpinnerKategori()

        etTanggal.setText(dateFormat.format(calendar.time))
        etTanggal.setOnClickListener { tampilkanDatePicker() }

        adapter = TransaksiAdapter(
            listTransaksi = listIncomeTampil,
            onEditClick = { transaksi -> mulaiEdit(transaksi) },
            onDeleteClick = { transaksi -> konfirmasiHapus(transaksi) }
        )

        rvIncome.layoutManager = LinearLayoutManager(requireContext())
        rvIncome.adapter = adapter

        refreshListIncome()

        btnSimpan.setOnClickListener { simpanIncome() }
        btnBatal.setOnClickListener { resetForm() }
    }

    private fun setupSpinnerKategori() {
        val kategoriDariMaster = KategoriFragment.listKategori
            .filter { it.tipe == "Income" }
            .map { it.nama }

        listNamaKategoriIncome = kategoriDariMaster.ifEmpty { listOf("Umum") }

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listNamaKategoriIncome
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spKategori.adapter = spinnerAdapter
    }

    private fun tampilkanDatePicker() {
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                etTanggal.setText(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun simpanIncome() {
        val nominalStr = etNominal.text.toString().trim()
        val nominal = nominalStr.toLongOrNull()

        if (nominal == null || nominal <= 0) {
            etNominal.error = "Masukkan nominal pemasukan yang valid"
            return
        }

        val kategori = spKategori.selectedItem.toString()
        val tanggal = etTanggal.text.toString().trim()

        if (editingTransaksi == null) {
            val transaksiBaru = Transaksi(
                id = UUID.randomUUID().toString(),
                nominal = nominal,
                kategori = kategori,
                tanggal = tanggal,
                tipe = "Income"
            )
            listTransaksi.add(transaksiBaru)
            Toast.makeText(requireContext(), "Pemasukan dicatat", Toast.LENGTH_SHORT).show()
        } else {
            val index = listTransaksi.indexOfFirst { it.id == editingTransaksi!!.id }
            if (index != -1) {
                listTransaksi[index].nominal = nominal
                listTransaksi[index].kategori = kategori
                listTransaksi[index].tanggal = tanggal
                Toast.makeText(requireContext(), "Pemasukan diperbarui", Toast.LENGTH_SHORT).show()
            }
        }

        resetForm()
        refreshListIncome()
    }

    private fun mulaiEdit(transaksi: Transaksi) {
        editingTransaksi = transaksi
        etNominal.setText(transaksi.nominal.toString())
        etTanggal.setText(transaksi.tanggal)

        val posisiKategori = listNamaKategoriIncome.indexOf(transaksi.kategori)
        if (posisiKategori != -1) {
            spKategori.setSelection(posisiKategori)
        }

        btnSimpan.text = "Update Pemasukan"
        btnBatal.visibility = View.VISIBLE
    }

    private fun konfirmasiHapus(transaksi: Transaksi) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Pemasukan")
            .setMessage("Yakin ingin menghapus pemasukan kategori '${transaksi.kategori}'?")
            .setPositiveButton("Hapus") { _, _ ->
                listTransaksi.remove(transaksi)
                if (editingTransaksi?.id == transaksi.id) resetForm()
                refreshListIncome()
                Toast.makeText(requireContext(), "Pemasukan dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun refreshListIncome() {
        listIncomeTampil.clear()
        listIncomeTampil.addAll(listTransaksi.filter { it.tipe == "Income" })
        adapter.notifyDataSetChanged()
    }

    private fun resetForm() {
        editingTransaksi = null
        etNominal.text.clear()
        if (listNamaKategoriIncome.isNotEmpty()) spKategori.setSelection(0)
        btnSimpan.text = "Simpan Pemasukan"
        btnBatal.visibility = View.GONE
    }
}