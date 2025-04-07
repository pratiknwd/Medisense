package com.example.mediscan.medicineplan.addmedicneplan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.R
import com.example.mediscan.db.entity.MedicineDetails

class MedicineListAdapter(private val medicines: MutableList<MedicineDetails>) :
    RecyclerView.Adapter<MedicineListAdapter.MedicineViewHolder>() {

    class MedicineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val medicineName: TextView = view.findViewById(R.id.tvMedicineName)
        val dose: TextView = view.findViewById(R.id.tvDose)
        val frequency: TextView = view.findViewById(R.id.tvFrequency)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medicine, parent, false)
        return MedicineViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        val med = medicines[position]
        holder.medicineName.text = med.medicineName
        holder.dose.text = med.dose
        holder.frequency.text = med.frequency
    }

    fun addMedicine(medicine: MedicineDetails) {
        medicines.add(medicine)
        notifyItemInserted(medicines.size - 1)
    }

    fun getMedicineList(): List<MedicineDetails> {
        return medicines
    }

    override fun getItemCount(): Int = medicines.size
}