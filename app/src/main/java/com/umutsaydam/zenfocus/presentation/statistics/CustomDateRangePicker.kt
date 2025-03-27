package com.umutsaydam.zenfocus.presentation.statistics

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDateRangePicker(
    dateRangePickerState: DateRangePickerState,
    onDateRangeStateChange: (Boolean) -> Unit,
    onSelectedDates: (String, String) -> Unit
) {
    DatePickerDialog(
        onDismissRequest = { onDateRangeStateChange(false) },
        confirmButton = {
            TextButton(
                onClick = {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val startDate = dateRangePickerState.selectedStartDateMillis?.let {
                        dateFormat.format(Date(it))
                    } ?: ""
                    val endDate = dateRangePickerState.selectedEndDateMillis?.let {
                        dateFormat.format(Date(it))
                    } ?: ""
                    onSelectedDates(startDate, endDate)
                    onDateRangeStateChange(false)
                }
            ) { Text("Confirm") }
        },
        dismissButton = {
            TextButton(
                onClick = { onDateRangeStateChange(false) }
            ) { Text("Cancel") }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = { Text("Select date range") },
            showModeToggle = false,
            modifier = Modifier
                .height(500.dp)
                .padding(16.dp)
        )
    }
}