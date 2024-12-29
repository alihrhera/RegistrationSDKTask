package ali.hrhera.registration_sdk.presentation.features.smileDetection

import ali.hrhera.registration_sdk.domain.usecase.faceDetection.FaceDetectionUseCase
import ali.hrhera.registration_sdk.domain.usecase.saveUserPhoto.SaveUserPhoto
import ali.hrhera.registration_sdk.util.BaseResponse
import android.graphics.Bitmap
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A [ViewModel] that is used to handle the business logic of the register camera step
 * screen. It uses the [FaceDetectionUseCase] to detect faces in the camera preview and
 * the [SaveUserPhoto] use case to save the user's photo.
 *
 * @property faceDetectionUseCase The use case that is used to detect faces in the
 * camera preview.
 * @property saveUserPhoto The use case that is used to save the user's photo.
 */
@HiltViewModel
class RegisterCameraStepScreenViewModel @Inject constructor(
    val faceDetectionUseCase: FaceDetectionUseCase,
    val saveUserPhoto: SaveUserPhoto
) : ViewModel() {
    /**
     * This value is used to track if the camera is ready to take a photo. It is set to true
     * after the face detector detects a face and the user has not yet taken a photo. It is
     * set to false after the user takes a photo or if the face detector no longer detects a
     * face.
     */
    val isReadyForCapture = mutableStateOf(false)

    init {
        // Observe the response of the face detection use case. If the response is a success,
        // set isReadyForCapture to true and start the timer. If the response is a failure,
        // set isReadyForCapture to false and cancel the timer.
        faceDetectionUseCase.response.asLiveData().observeForever {
            if (it is BaseResponse.Success) {
                isReadyForCapture.value = it.data.faceIsSmiling
                if (!isRunning && it.data.faceIsSmiling) {
                    time.intValue = 3
                    isRunning = true
                    timer()
                }
                if (!isReadyForCapture.value) {
                    timer?.cancel()
                    time.intValue = 3
                    isRunning = false
                }

            } else {
                isReadyForCapture.value = false
                time.intValue = 3
                isRunning = false
                timer?.cancel()
            }
        }
    }

    // This value is used to track if the timer is currently running.
    private var isRunning = false

    // This value is used to track the amount of time remaining before the photo is taken.
    var time = mutableIntStateOf(3)

    // This function is called when the user takes a photo.
    var onTakePhoto: () -> Unit = {}
    private fun takePhoto() {
        onTakePhoto()
        viewModelScope.launch {
            delay(1000)
            isRunning = false
        }
    }

    // This is the timer that is used to count down from 3 to 0 before taking a photo.
    private var timer: Job? = null
    private fun timer() {
        timer?.cancel()
        timer = viewModelScope.launch {
            if (isRunning) {
                while (time.intValue > 0) {
                    delay(1000) // Wait 1 second
                    time.intValue -= 1
                }
                if (time.intValue == 0) {
                    takePhoto()
                }
            }
        }
    }

    init {
        // Observe the response of the save user photo use case. If the response is a success,
        // set the ui state to success and update the ui state.
        viewModelScope.launch {
            saveUserPhoto.response.collectLatest {
                if (it is BaseResponse.Success) {
                    isReadyForCapture.value = false
                    time.intValue = 3
                    isRunning = false
                    timer?.cancel()
                    updateEvent(RegisterCameraUiState.Success)
                }

            }

        }
    }

    /**
     * Saves the user's image by invoking the saveUserPhoto use case.
     *
     * @param bitmap The bitmap image to be saved.
     * @param userId The ID of the user to associate with the image.
     */
    fun saveImage(bitmap: Bitmap?, userId: Int) {
        bitmap?.let {
            viewModelScope.launch {
                saveUserPhoto(it, userId)
            }
        }
    }

    // Holds the current UI state of the Register Camera Screen
    val uiStat = mutableStateOf<RegisterCameraUiState>(RegisterCameraUiState.Idel)

    /**
     * Updates the current UI state with a new state.
     *
     * @param newState The new UI state to be set.
     */
    fun updateEvent(newState: RegisterCameraUiState) {
        uiStat.value = newState
    }
}