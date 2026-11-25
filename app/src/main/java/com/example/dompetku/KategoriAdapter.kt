package com.example.dompetku

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class KategoriAdapter(
    private val listKategori: List<Kategori>,
    private val onEditClick: (Kategori) -> Unit,
    private val onDeleteClick: (Kategori) -> Unit
) : RecyclerView.Adapter<KategoriAdapter.KategoriViewHolder>() {

    class KategoriViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNamaKategori)
        val tvTipe: TextView = view.findViewById(R.id.tvTipeKategori)
        val btnEdit: Button = view.findViewById(R.id.btnEditKategori)
        val btnHapus: Button = view.findViewById(R.id.btnHapusKategori)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kategori, parent, false)
        return KategoriViewHolder(view)
    }

    override fun onBindViewHolder(holder: KategoriViewHolder, position: Int) {
        val kategori = listKategori[position]
        holder.tvNama.text = kategori.nama
        holder.tvTipe.text = kategori.tipe

        if (kategori.tipe == "Income") {
            holder.tvTipe.setTextColor(Color.parseColor("#2E7D32"))
        } else {
            holder.tvTipe.setTextColor(Color.parseColor("#C62828"))
        }

        holder.btnEdit.setOnClickListener { onEditClick(kategori) }
        holder.btnHapus.setOnClickListener { onDeleteClick(kategori) }
    }

    override fun getItemCount(): Int = listKategori.size
}