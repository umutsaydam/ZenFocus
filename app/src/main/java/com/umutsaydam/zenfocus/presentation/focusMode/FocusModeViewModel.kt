package com.umutsaydam.zenfocus.presentation.focusMode

import androidx.lifecycle.ViewModel
import com.umutsaydam.zenfocus.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class FocusModeViewModel @Inject constructor() : ViewModel() {

    private val _remainTime = MutableStateFlow<String>("25:00")
    val remainTime: StateFlow<String> = _remainTime

    fun getDefaultTheme(): Int {
        //TODO get real theme process
        return R.drawable.lofi1
    }

    fun playTimer(){
        //TODO play timer
    }

    fun stopTimer(){
        //TODO stop timer
    }
}