package com.example.mediscan.medicineplan.addmedicneplan

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediscan.BaseFragment
import com.example.mediscan.R
import com.example.mediscan.auth.PEF_USER_ID
import com.example.mediscan.auth.SHARED_PREF_NAME
import com.example.mediscan.databinding.FragmentAddMedicinePlanBinding
import com.example.mediscan.db.entity.MedicineDetails
import com.example.mediscan.db.entity.MedicinePlan
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar

class AddMedicinePlanFragment : BaseFragment() {

    private lateinit var viewModel: MedicinePlanViewModel
    private var _binding: FragmentAddMedicinePlanBinding? = null
    private val binding get() = _binding!!
    private lateinit var timesInput: EditText
    private var startDate: String? = null
    private var endDate: String? = null
    private val medicineList = mutableListOf<MedicineDetails>() // Initialize the list in the Fragment
    private lateinit var adapter: MedicineListAdapter // Declare the adapter

    override fun onNavigationIconClick(iconTapped: View?) {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMedicinePlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[MedicinePlanViewModel::class.java]

        val startDateInput = view.findViewById<EditText>(R.id.startDateInput)
        val endDateInput = view.findViewById<EditText>(R.id.endDateInput)
        val durationInput = view.findViewById<EditText>(R.id.durationInput)
        timesInput = view.findViewById(R.id.timesInput)

        binding.medicineRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Initialize the adapter with the Fragment's medicineList
        adapter = MedicineListAdapter(medicineList)
        binding.medicineRecyclerView.adapter = adapter

        startDateInput.setOnClickListener {
            showDatePicker(startDateInput, durationInput)
        }
        endDateInput.setOnClickListener {
            showDatePicker(endDateInput, durationInput)
        }

        setupDropdowns()
        val medicineNameInput = view.findViewById<EditText>(R.id.medicineNameInput)

        binding.addMedicineButton.setOnClickListener {
            val name = medicineNameInput.text.toString()
            if (name.isBlank()) {
                Toast.makeText(requireContext(), "Medicine name is required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val medicine = MedicineDetails(
                planId = 0,
                medicineName = name,
                dose = binding.doseInput.text.toString(),
                frequency = binding.frequencyInput.text.toString(),
                duration = binding.durationInput.text.toString().toIntOrNull() ?: 0,
                times = timesInput.text.toString(),
                foodInstruction = binding.foodInstructionInput.text.toString()
            )
            // Add to the Fragment's list and notify the adapter
            medicineList.add(medicine)
            adapter.notifyItemInserted(medicineList.size - 1)
            resetDropdownsAndFields()
        }

        binding.savePlanButton.setOnClickListener {
            val userId = getUserId(requireContext())
            val startDate = binding.startDateInput.text.toString()
            val endDate = binding.endDateInput.text.toString()
            val status = binding.statusInput.text.toString()
            // Get the medicine list from the Fragment
            val medicines = adapter.getMedicineList()

            if (startDate.isBlank() || endDate.isBlank() || medicines.isEmpty()) {
                Toast.makeText(requireContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val plan = MedicinePlan(userId = userId, status = status, startDate = startDate, endDate = endDate)
            viewModel.insertMedicinePlanWithDetails(plan, medicines)
            Toast.makeText(requireContext(), "Medicine Plan Saved", Toast.LENGTH_SHORT).show()
//            findNavController().navigate(R.id.action_addMedicinePlanFragment_self)
            medicineList.clear()
            adapter.notifyDataSetChanged()
            unlockAllFields()
            resetAllDropdownsAndFields()
        }
    }

    private fun setupDropdowns() {
        val doseAdapter = ArrayAdapter(requireContext(), R.layout.list_item, resources.getStringArray(R.array.dose_array))
        binding.doseInput.setAdapter(doseAdapter)

        val freqAdapter = ArrayAdapter(requireContext(), R.layout.list_item, resources.getStringArray(R.array.frequency_array))
        binding.frequencyInput.setAdapter(freqAdapter)

        binding.frequencyInput.setOnItemClickListener { parent, _, position, _ ->
            val selectedFrequency = parent.getItemAtPosition(position).toString()
            val timeSuggestions = getTimeSuggestions(selectedFrequency)
            timesInput.setText(timeSuggestions.joinToString(", "))
        }

        val foodAdapter = ArrayAdapter(requireContext(), R.layout.list_item, resources.getStringArray(R.array.food_instruction_array))
        binding.foodInstructionInput.setAdapter(foodAdapter)

        val statusAdapter = ArrayAdapter(requireContext(), R.layout.list_item, resources.getStringArray(R.array.status_array))
        binding.statusInput.setAdapter(statusAdapter)
    }

    private fun getTimeSuggestions(frequency: String): List<String> {
        return when (frequency) {
            "Once per day" -> listOf("08:00 AM")
            "2 times per day" -> listOf("08:00 AM", "08:00 PM")
            "3 times per day" -> listOf("08:00 AM", "02:00 PM", "08:00 PM")
            "4 times per day" -> listOf("08:00 AM", "12:00 PM", "04:00 PM", "08:00 PM")
            "5 times per day" -> listOf("06:00 AM", "10:00 AM", "02:00 PM", "06:00 PM", "10:00 PM")
            else -> emptyList()
        }
    }

    private fun showDatePicker(targetEditText: EditText, durationInput: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            targetEditText.setText(selectedDate)

            if (targetEditText.id == R.id.startDateInput) {
                startDate = selectedDate
            } else if (targetEditText.id == R.id.endDateInput) {
                endDate = selectedDate
            }

            calculateAndSetDuration(durationInput)
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun calculateAndSetDuration(durationInput: EditText) {
        if (!startDate.isNullOrEmpty() && !endDate.isNullOrEmpty()) {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val start = LocalDate.parse(startDate, formatter)
            val end = LocalDate.parse(endDate, formatter)
            val durationDays = ChronoUnit.DAYS.between(start, end).toInt()

            if (durationDays > 90) {
                Toast.makeText(requireContext(), "Plan can't exceed 90 days!", Toast.LENGTH_SHORT).show()
                endDate = null
            } else if (durationDays >= 0) {
                durationInput.setText(durationDays.toString())
            }
        }
    }

    private fun getUserId(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(PEF_USER_ID, -1)
    }
    private fun resetAllDropdownsAndFields() {
        unlockAllFields()
        binding.doseInput.setText("")
        binding.frequencyInput.setText("")
        binding.foodInstructionInput.setText("")
        binding.statusInput.setText("")
        binding.startDateInput.setText("")
        binding.endDateInput.setText("")
        binding.medicineNameInput.setText("")
        binding.durationInput.setText("")
        binding.timesInput.setText("")
    }

    private fun lockNonResetFields() {
        binding.statusInput.isEnabled = false
        binding.startDateInput.isEnabled = false
        binding.endDateInput.isEnabled = false
        binding.durationInput.isEnabled = false
    }

    private fun unlockAllFields() {
        binding.statusInput.isEnabled = true
        binding.startDateInput.isEnabled = true
        binding.endDateInput.isEnabled = true
        binding.durationInput.isEnabled = true
        binding.doseInput.isEnabled = true
        binding.frequencyInput.isEnabled = true
        binding.foodInstructionInput.isEnabled = true
        binding.medicineNameInput.isEnabled = true
        binding.timesInput.isEnabled = true
    }

    private fun resetDropdownsAndFields() {
        binding.doseInput.setText("")
        binding.frequencyInput.setText("")
        binding.foodInstructionInput.setText("")
        binding.medicineNameInput.setText("")
        binding.timesInput.setText("")
        lockNonResetFields()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val FRAG_NAME = "AddMedicinePlanFragment"

        @JvmStatic
        fun newInstance() = AddMedicinePlanFragment()
    }
}