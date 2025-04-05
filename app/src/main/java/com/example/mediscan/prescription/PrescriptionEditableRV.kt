package com.example.mediscan.prescription

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.databinding.PrescriptionItemBinding


class PrescriptionEditableRV(
    private val prescriptions: MutableList<PrescriptionModelItem>,
    private val showEditDeleteOptions: (Int) -> Unit,
) : RecyclerView.Adapter<PrescriptionEditableRV.PrescriptionViewHolder>() {
    
    inner class PrescriptionViewHolder(val binding: PrescriptionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                showEditDeleteOptions(adapterPosition)
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrescriptionViewHolder {
        val binding = PrescriptionItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PrescriptionViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: PrescriptionViewHolder, position: Int) {
        val item = prescriptions[position]
        holder.binding.apply {
            tvMedicineName.text = "Medicine Name: ${item.medicineName}"
            tvDose.text = "Dose: ${item.dose}"
            tvFrequency.text = "Frequency: ${item.frequency}"
            tvDuration.text = "Duration: ${item.duration}"
            tvFoodInstruction.text = "Food Instruction: ${item.foodInstruction}"
            tvTimes.text = "Times: ${item.times.joinToString(", ")}"
        }
    }
    
    override fun getItemCount(): Int = prescriptions.size
    
    /**
     * Replace the current list entirely
     */
    fun updateList(newList: List<PrescriptionModelItem>) {
        prescriptions.clear()
        prescriptions.addAll(newList)
        notifyDataSetChanged()
    }
    
    /**
     * Update an item at a given index
     */
    fun updateItem(index: Int, updatedItem: PrescriptionModelItem) {
        if (index in prescriptions.indices) {
            prescriptions[index] = updatedItem
            notifyItemChanged(index)
        }
    }
    
    /**
     * Add a new item
     */
    fun addItem(item: PrescriptionModelItem) {
        prescriptions.add(item)
        notifyItemInserted(prescriptions.size - 1)
    }
    
    /**
     * Remove item at index
     */
    fun removeItem(index: Int) {
        if (index in prescriptions.indices) {
            prescriptions.removeAt(index)
            notifyItemRemoved(index)
        }
    }
    
    /**
     * Access item if needed outside
     */
    fun getItem(index: Int): PrescriptionModelItem? = prescriptions.getOrNull(index)
    
    fun getAllItems(): List<PrescriptionModelItem> = prescriptions.toList()
}

