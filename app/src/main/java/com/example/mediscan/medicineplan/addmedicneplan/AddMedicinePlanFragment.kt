package com.example.mediscan.medicineplan.addmedicneplan

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.BaseFragment
import com.example.mediscan.R
import com.example.mediscan.auth.PEF_USER_ID
import com.example.mediscan.auth.SHARED_PREF_NAME
import com.example.mediscan.db.entity.MedicineDetails
import com.example.mediscan.db.entity.MedicinePlan
import java.util.Calendar

class AddMedicinePlanFragment : BaseFragment() {

    private lateinit var viewModel: MedicinePlanViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MedicineListAdapter
    private val medicineList = mutableListOf<MedicineDetails>()
    override fun onNavigationIconClick(iconTapped: View?) {

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_medicine_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[MedicinePlanViewModel::class.java]
        recyclerView = view.findViewById(R.id.medicineRecyclerView)
        adapter = MedicineListAdapter(medicineList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val startDateInput = view.findViewById<EditText>(R.id.startDateInput)
        val endDateInput = view.findViewById<EditText>(R.id.endDateInput)

        val calendar = Calendar.getInstance()

        val dateSetListener = { editText: EditText ->
            DatePickerDialog(requireContext(),
                { _, year, month, day ->
                    val dateStr = String.format("%04d-%02d-%02d", year, month + 1, day)
                    editText.setText(dateStr)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        startDateInput.setOnClickListener {
            dateSetListener(startDateInput)
        }
        endDateInput.setOnClickListener {
            dateSetListener(endDateInput)
        }


        val statusOptions = listOf("Active", "Blocked")
        val statusSpinner = view.findViewById<Spinner>(R.id.statusSpinner)

        val spinnerAdapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item_status,
            R.id.statusText,
            statusOptions
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val bgColor = if (statusOptions[position] == "Active") Color.parseColor("#4CAF50")
                else Color.parseColor("#F44336")
                view.setBackgroundColor(bgColor)
                return view
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val dropdownView = super.getDropDownView(position, convertView, parent)
                val bgColor = if (statusOptions[position] == "Active") Color.parseColor("#4CAF50")
                else Color.parseColor("#F44336")
                dropdownView.setBackgroundColor(bgColor)
                return dropdownView
            }
        }

        statusSpinner.adapter = spinnerAdapter
        statusSpinner.setSelection(0)



        val medicineNameInput = view.findViewById<EditText>(R.id.medicineNameInput)
        val doseInput = view.findViewById<EditText>(R.id.doseInput)
        val frequencyInput = view.findViewById<EditText>(R.id.frequencyInput)
        val durationInput = view.findViewById<EditText>(R.id.durationInput)
        val timesInput = view.findViewById<EditText>(R.id.timesInput)
        val foodInstructionInput = view.findViewById<EditText>(R.id.foodInstructionInput)

        val addMedicineButton = view.findViewById<Button>(R.id.addMedicineButton)
        val savePlanButton = view.findViewById<Button>(R.id.savePlanButton)

        addMedicineButton.setOnClickListener {
            val medicine = MedicineDetails(
                planId = 0, // temp
                medicineName = medicineNameInput.text.toString(),
                dose = doseInput.text.toString(),
                frequency = frequencyInput.text.toString(),
                duration = durationInput.text.toString().toIntOrNull() ?: 0,
                times = timesInput.text.toString(),
                foodInstruction = foodInstructionInput.text.toString()
            )
            medicineList.add(medicine)
            adapter.notifyItemInserted(medicineList.size - 1)

            // Clear inputs
            medicineNameInput.text.clear()
            doseInput.text.clear()
            frequencyInput.text.clear()
            durationInput.text.clear()
            timesInput.text.clear()
            foodInstructionInput.text.clear()
        }

        savePlanButton.setOnClickListener {
            val userId = getUserId(requireContext())
            val startDate = startDateInput.text.toString()
            val endDate = endDateInput.text.toString()
            val status = statusSpinner.selectedItem.toString()

            val plan = MedicinePlan(
                userId = userId,
                status = status,
                startDate = startDate,
                endDate = endDate
            )

            viewModel.insertMedicinePlanWithDetails(plan, medicineList)

            Toast.makeText(requireContext(), "Medicine Plan Saved", Toast.LENGTH_SHORT).show()
            // Optional: navigate back or clear UI
        }
    }

    private fun getUserId(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(PEF_USER_ID, -1)
    }

    companion object {
        const val FRAG_NAME = "AddMedicinePlanFragment"

        @JvmStatic
        fun newInstance() = AddMedicinePlanFragment()
    }
}
