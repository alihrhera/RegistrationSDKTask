package ali.hrhera.registration_sdk.presentation.result

import ali.hrhera.registration_sdk.domain.usecase.result.ResultUseCase
import ali.hrhera.registration_sdk.util.BaseResponse
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ResultScreenViewModel @Inject constructor(
    private val resultUseCase: ResultUseCase
) : ViewModel() {

    val uiStat = mutableStateOf<ResultScreenUiState>(ResultScreenUiState.Idel)

    init {
        viewModelScope.launch {
            resultUseCase.response.collectLatest {
                Log.w("TAG", "value :$it ")
                when (it) {
                    is BaseResponse.Success -> {
                        uiStat.value = ResultScreenUiState.Success(it.data)
                    }

                    is BaseResponse.Error -> {
                        uiStat.value = ResultScreenUiState.Error(it.throwable.message)
                    }

                    else -> {}
                }
            }
        }

        resultUseCase.response.map { it is BaseResponse.Loading }.asLiveData().observeForever {
            if (it) uiStat.value = ResultScreenUiState.Loading
        }
    }

    private var getUserInfo: Job? = null
    fun getUserById(id: Int) {
        if (getUserInfo != null) return
        getUserInfo = viewModelScope.launch {
            resultUseCase(id)
        }
    }


}
