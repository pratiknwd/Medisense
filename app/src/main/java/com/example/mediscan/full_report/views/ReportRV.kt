package com.example.mediscan.full_report.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.R

class ReportRV : RecyclerView.Adapter<ReportRV.ReportViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportRV.ReportViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_test_result_card,
            parent,
            false
        )
        return ReportViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ReportRV.ReportViewHolder, position: Int) {
        holder.apply {
            subTestTV.text = position.toString()
            currentValueTV.text = position.toString()
            rangeTV.text = position.toString()
            riskTV.text = position.toString()
            recommendationTV.text = position.toString()
        }
    }
    
    override fun getItemCount(): Int {
        return 20
    }
    
    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
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