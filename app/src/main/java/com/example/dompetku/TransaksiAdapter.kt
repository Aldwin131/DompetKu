package com.example.dompetku

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class TransaksiAdapter(
    private val listTransaksi: List<Transaksi>,
    private val onEditClick: (Transaksi) -> Unit,
    private val onDeleteClick: (Transaksi) -> Unit
) : RecyclerView.Adapter<TransaksiAdapter.TransaksiViewHolder>() {

    class TransaksiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNominal: TextView = view.findViewById(R.id.tvNominalTransaksi)
        val tvKategoriTanggal: TextView = view.findViewById(R.id.tvKategoriTanggal)
        val btnEdit: Button = view.findViewById(R.id.btnEditTransaksi)
        val btnHapus: Button = view.findViewById(R.id.btnHapusTransaksi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransaksiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaksi, parent, false)
        return TransaksiViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransaksiViewHolder, position: Int) {
        val transaksi = listTransaksi[position]
        val formattedNominal = NumberFormat.getNumberInstance(Locale("in", "ID")).format(transaksi.nominal)

        holder.tvNominal.text = "Rp $formattedNominal"
        holder.tvNominal.setTextColor(
            if (transaksi.tipe == "Income") Color.parseColor("#2E7D32")
            else Color.parseColor("#C62828")
        )
        holder.tvKategoriTanggal.text = "${transaksi.kategori} • ${transaksi.tanggal}"

        holder.btnEdit.setOnClickListener { onEditClick(transaksi) }
        holder.btnHapus.setOnClickListener { onDeleteClick(transaksi) }
    }

    override fun getItemCount(): Int = listTransaksi.size
}