package com.example.mediscan.full_report.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.R
import com.example.mediscan.db.entity.Report

class ReportRV : RecyclerView.Adapter<ReportRV.ReportViewHolder>() {
    private val reports: MutableList<Report> = mutableListOf()
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportRV.ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_test_result_card,
            parent,
            false
        )
        return ReportViewHolder(view)
    }
    
    
    /*
    data class Report(
    @PrimaryKey(autoGenerate = true)
    val reportId: Int,
    val userId: Int,  // Foreign Key
    val reportTypeId: Int,  // Foreign Key
    val testName: String?,
    val testValue: Float?,
    val unit: String?,
    val upperLimit: Float?,
    val lowerLimit: Float?,
    val explanation: String?,
    val bioReferenceInterval: String?,
    * */
    
    override fun onBindViewHolder(holder: ReportRV.ReportViewHolder, position: Int) {
        holder.apply {
            subTestTV.text = reports[position].testName
            currentValueTV.text = reports[position].testValue.toString()
            rangeTV.text = "${reports[position].lowerLimit} - ${reports[position].upperLimit}"
            riskTV.text = "TODO: get indication from LLM"
            recommendationTV.text = reports[position].explanation
        }
    }
    
    override fun getItemCount(): Int {
        return reports.size
    }
    
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    
    fun addReport(report: List<Report>) {
        reports.clear()
        reports.addAll(report)
        notifyDataSetChanged()
    }
    
    
    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val indicator: View = itemView.findViewById(R.id.indicator)
        val subTestTV: TextView = itemView.findViewById(R.id.subTestTV)
        val currentValueTV: TextView = itemView.findViewById(R.id.currentValueTV)
        val rangeTV: TextView = itemView.findViewById(R.id.rangeTV)
        val riskTV: TextView = itemView.findViewById(R.id.riskTV)
        val recommendationTV: TextView = itemView.findViewById(R.id.recommendationTV)
        
        /*init {
            riskTV.visibility = View.GONE
            recommendationTV.visibility = View.GONE
            
            itemView.setOnClickListener {
                if (riskTV.visibility == View.GONE) {
                    riskTV.visibility = View.VISIBLE
                    recommendationTV.visibility = View.VISIBLE
                } else {
                    riskTV.visibility = View.GONE
                    recommendationTV.visibility = View.GONE
                }
            }
        }*/
        
    }
}