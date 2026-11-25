package com.example.dompetku

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.UUID

class KategoriFragment : Fragment() {

    companion object {
        val listKategori = mutableListOf<Kategori>()
    }

    private lateinit var adapter: KategoriAdapter
    private var editingKategori: Kategori? = null

    private lateinit var etNamaKategori: EditText
    private lateinit var rbIncome: RadioButton
    private lateinit var rbExpense: RadioButton
    private lateinit var btnSimpan: Button
    private lateinit var btnBatal: Button
    private lateinit var rvKategori: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kategori, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etNamaKategori = view.findViewById(R.id.etNamaKategori)
        rbIncome = view.findViewById(R.id.rbIncome)
        rbExpense = view.findViewById(R.id.rbExpense)
        btnSimpan = view.findViewById(R.id.btnSimpanKategori)
        btnBatal = view.findViewById(R.id.btnBatalEdit)
        rvKategori = view.findViewById(R.id.rvKategori)

        adapter = KategoriAdapter(
            listKategori = listKategori,
            onEditClick = { kategori -> mulaiEdit(kategori) },
            onDeleteClick = { kategori -> konfirmasiHapus(kategori) }
        )

        rvKategori.layoutManager = LinearLayoutManager(requireContext())
        rvKategori.adapter = adapter

        btnSimpan.setOnClickListener { simpanKategori() }
        btnBatal.setOnClickListener { resetForm() }
    }

    private fun simpanKategori() {
        val nama = etNamaKategori.text.toString().trim()
        if (nama.isEmpty()) {
            etNamaKategori.error = "Nama kategori tidak boleh kosong"
            return
        }

        val tipe = if (rbIncome.isChecked) "Income" else "Expense"

        if (editingKategori == null) {
            val kategoriBaru = Kategori(
                id = UUID.randomUUID().toString(),
                nama = nama,
                tipe = tipe
            )
            listKategori.add(kategoriBaru)
            Toast.makeText(requireContext(), "Kategori ditambahkan", Toast.LENGTH_SHORT).show()
        } else {
            val index = listKategori.indexOfFirst { it.id == editingKategori!!.id }
            if (index != -1) {
                listKategori[index].nama = nama
                listKategori[index].tipe = tipe
                Toast.makeText(requireContext(), "Kategori diperbarui", Toast.LENGTH_SHORT).show()
            }
            resetForm()
        }

        adapter.notifyDataSetChanged()
        if (editingKategori == null) etNamaKategori.text.clear()
    }

    private fun mulaiEdit(kategori: Kategori) {
        editingKategori = kategori
        etNamaKategori.setText(kategori.nama)
        if (kategori.tipe == "Income") {
            rbIncome.isChecked = true
        } else {
            rbExpense.isChecked = true
        }
        btnSimpan.text = "Update Kategori"
        btnBatal.visibility = View.VISIBLE
    }

    private fun konfirmasiHapus(kategori: Kategori) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Kategori")
            .setMessage("Yakin ingin menghapus kategori '${kategori.nama}'?")
            .setPositiveButton("Hapus") { _, _ ->
                listKategori.remove(kategori)
                adapter.notifyDataSetChanged()
                if (editingKategori?.id == kategori.id) resetForm()
                Toast.makeText(requireContext(), "Kategori dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun resetForm() {
        editingKategori = null
        etNamaKategori.text.clear()
        rbExpense.isChecked = true
        btnSimpan.text = "Simpan Kategori"
        btnBatal.visibility = View.GONE
    }
}