package com.example.mediscan.report

import ReportWithRecommendation
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.R

class RecommendationReportAdapter : RecyclerView.Adapter<RecommendationReportAdapter.RecommendationReportViewHolder>() {
    private val reports: MutableList<ReportWithRecommendation> = mutableListOf()
    
    inner class RecommendationReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_test_recommendation_card,
            parent,
            false
        )
        return RecommendationReportViewHolder(view)
    }
    
    override fun getItemCount(): Int = reports.size
    
    override fun onBindViewHolder(holder: RecommendationReportViewHolder, position: Int) {
        holder.apply {
            subTestTV.text = reports[position].testName
            currentValueTV.text = reports[position].testValue.toString()
            valueUnit.text = reports[position].unit ?: ""
            rangeTV.text = "${reports[position].lowerLimit} - ${reports[position].upperLimit}"
            riskTV.text = "Recomendation For This"
            recommendationTV.text = reports[position].recommendation
        }
    }
    
    fun updateData(report: List<ReportWithRecommendation>) {
        reports.clear()
        reports.addAll(report)
        notifyDataSetChanged()
    }
}