package ali.hrhera.registration_sdk.presentation.start

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor() : ViewModel() {

    private val _startScreenUiStat = MutableStateFlow<StartScreenUiStat>(StartScreenUiStat.Idel)

    val startScreenUiStat = mutableStateOf<StartScreenUiStat>(StartScreenUiStat.Idel)

    fun updateEvent(newState: StartScreenUiStat) {
        viewModelScope.launch {
            _startScreenUiStat.emit(newState)
        }
    }

    init {
        viewModelScope.launch {
            _startScreenUiStat.collectLatest {
                startScreenUiStat.value = it
            }
        }
    }

}