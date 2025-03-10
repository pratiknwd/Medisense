package com.example.mediscan.report

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.R
import com.example.mediscan.db.entity.Report

class CriticalReportsAdapter : RecyclerView.Adapter<CriticalReportsAdapter.CriticalReportViewHolder>() {
    private val reports: MutableList<Report> = mutableListOf()
    
    inner class CriticalReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val indicator: View = itemView.findViewById(R.id.indicator)
        val subTestTV: TextView = itemView.findViewById(R.id.subTestTV)
        val currentValueTV: TextView = itemView.findViewById(R.id.currentValueTV)
        val rangeTV: TextView = itemView.findViewById(R.id.rangeTV)
        val riskTV: TextView = itemView.findViewById(R.id.riskTV)
        val recommendationTV: TextView = itemView.findViewById(R.id.recommendationTV)
        val valueUnit: TextView = itemView.findViewById(R.id.valueUnit)
        
        init {
            indicator.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.critical))
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CriticalReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_test_result_card,
            parent,
            false
        )
        return CriticalReportViewHolder(view)
    }
    
    
    override fun getItemCount(): Int = reports.size
    
    override fun onBindViewHolder(holder: CriticalReportViewHolder, position: Int) {
        holder.apply {
            subTestTV.text = reports[position].testName
            currentValueTV.text = reports[position].testValue.toString()
            currentValueTV.setTextColor(ContextCompat.getColor(itemView.context, R.color.critical))
            valueUnit.text = reports[position].unit ?: ""
            rangeTV.text = "${reports[position].lowerLimit} - ${reports[position].upperLimit}"
            riskTV.text = "Explain the risk"
            recommendationTV.text = reports[position].explanation
        }
    }
    
    fun addReport(report: List<Report>) {
        reports.clear()
        reports.addAll(report)
        notifyDataSetChanged()
    }
}