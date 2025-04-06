package com.example.mediscan.medicineplan

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.R
import com.google.android.material.button.MaterialButton


class MedicinePlanRecyclerAdapter(
    private var medicinePlans: MutableList<MedicinePlanWithDetails>,
    private val onInactiveClick: (Int) -> Unit,
    private val onEditClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit,
) : RecyclerView.Adapter<MedicinePlanRecyclerAdapter.MedicinePlanViewHolder>() {
    
    inner class MedicinePlanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val planId: TextView = itemView.findViewById(R.id.planId)
        val medicineName: TextView = itemView.findViewById(R.id.medicineName)
        val times: TextView = itemView.findViewById(R.id.times)
        val startEnd: TextView = itemView.findViewById(R.id.startEnd)
        val status: TextView = itemView.findViewById(R.id.status)
        val inactiveBtn: MaterialButton = itemView.findViewById(R.id.inactiveBtn)
        val editBtn: MaterialButton = itemView.findViewById(R.id.editBtn)
        val deleteBtn: MaterialButton = itemView.findViewById(R.id.deleteBtn)
        
        init {
            inactiveBtn.setOnClickListener { onInactiveClick(medicinePlans[adapterPosition].plan.planId) }
            editBtn.setOnClickListener { onEditClick(medicinePlans[adapterPosition].plan.planId) }
            deleteBtn.setOnClickListener { onDeleteClick(medicinePlans[adapterPosition].plan.planId) }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicinePlanViewHolder {
        Log.d("9155881234", "onCreateViewHolder")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_medicine_plan, parent, false)
        return MedicinePlanViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: MedicinePlanViewHolder, position: Int) {
        val item = medicinePlans[position]
        holder.planId.text = "Plan Id: ${item.plan.planId}"
        holder.status.text = "Status: ${item.plan.status}"
        holder.startEnd.text = "Start - End: ${item.plan.startDate} - ${item.plan.endDate}"
        
        // Showing only first medicine details or combined name + time (customize as needed)
        holder.medicineName.text = "Medicine Name: ${item.medicineDetails.joinToString { it.medicineName }}"
        holder.times.text = "Times: ${item.medicineDetails.joinToString { it.times }}"
        
    }
    
    override fun getItemCount() = medicinePlans.size
    
    fun notifyChanges() {
        notifyDataSetChanged()
    }
}

