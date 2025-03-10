package com.example.mediscan.report.my_reports

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.R
import com.example.mediscan.db.entity.ReportType
import com.google.android.material.button.MaterialButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class MyReportsAdapter(
    private val onSmartReportClick: (Int) -> Unit,
//    private val onViewReportClick: (Int) -> Unit
) : RecyclerView.Adapter<MyReportsAdapter.MyReportsViewHolder>() {
    private val myReportsList: MutableList<ReportType> = mutableListOf()
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReportsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_report_type, parent, false)
        return MyReportsViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: MyReportsViewHolder, position: Int) {
        holder.apply {
            testName.text = myReportsList[position].reportTypeName
            bookingId.text = "Booking ID: ${(1_000_000_000..9_999_999_999).random()}"
            receivedDate.text = "Report Received: ${generateRandomDate()}"
        }
    }
    
    override fun getItemCount(): Int {
        return myReportsList.size
    }
    
    fun updateList(newList: List<ReportType>) {
        myReportsList.clear()
        myReportsList.addAll(newList)
        notifyDataSetChanged()
    }
    
    private fun generateRandomDate(): String {
        val startDate = LocalDate.of(2000, 1, 1)  // Start date (adjust if needed)
        val endDate = LocalDate.of(2030, 12, 31)  // End date (adjust if needed)
        
        val randomDays = Random.nextLong(startDate.toEpochDay(), endDate.toEpochDay()) // Random epoch day
        val randomDate = LocalDate.ofEpochDay(randomDays) // Convert epoch day to LocalDate
        
        val formatter = DateTimeFormatter.ofPattern("dd MMM yy") // Format: "DD MMM YY"
        return randomDate.format(formatter)
    }
    
    
    inner class MyReportsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val testName: TextView = itemView.findViewById(R.id.testName)
        val bookingId: TextView = itemView.findViewById(R.id.bookingId)
        val receivedDate: TextView = itemView.findViewById(R.id.reportReceivedDate)
        
        val btnSmartReport: MaterialButton = itemView.findViewById(R.id.btnSmartReport)
        val btnViewReport: MaterialButton = itemView.findViewById(R.id.btnViewReport)
        
        init {
            btnSmartReport.setOnClickListener {
                val position = adapterPosition
                if (position == RecyclerView.NO_POSITION) return@setOnClickListener
                onSmartReportClick.invoke(myReportsList[position].reportTypeId)
            }
            
//            btnViewReport.setOnClickListener {
//                val position = adapterPosition
//                if (position == RecyclerView.NO_POSITION) return@setOnClickListener
//                onViewReportClick.invoke(myReportsList[position].reportTypeId)
//            }
        }
        
    }
}