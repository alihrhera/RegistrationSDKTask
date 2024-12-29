package ali.hrhera.registration_sdk.domain.usecase.faceDetection

import ali.hrhera.registration_sdk.domain.dto.FaceResult
import ali.hrhera.registration_sdk.util.BaseResponse
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.face.Face
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Use case for detecting faces in an image using ML Kit's face detection capabilities.
 * This class processes face detection asynchronously and emits the results through
 * a shared flow.
 *
 * @property faceDetectorProcessor The face detector processor used to detect faces.
 */
class FaceDetectionUseCase @Inject constructor(
    private val faceDetectorProcessor: FaceDetectorProcessor,
) {

    val response = MutableSharedFlow<BaseResponse<FaceResult>>()

    private val analyzer = object : ImageAnalysis.Analyzer {
        override fun analyze(imageProxy: ImageProxy) {
            analyzeImage(imageProxy)
        }
    }

    operator fun invoke(): ImageAnalysis.Analyzer {
        return analyzer
    }

    private var frameSkipCounter = 0

    /**
     * Analyzes an image for face detection every 30 frames.
     *
     * @param image The image to be analyzed for face detection.
     */
    private fun analyzeImage(image: ImageProxy) {
        if (frameSkipCounter % 30 == 0) {
            faceDetectorProcessor.processFace(image) { face, error ->
                face?.let {
                    getFaceResults(it)
                } ?: emitState(BaseResponse.Error(Throwable("No face found")))
                error?.let {
                    response.tryEmit(BaseResponse.Error(Throwable(it)))
                }

                image.close()
            }
        } else {
            image.close()
        }
        frameSkipCounter++
    }

    /**
     * Extracts face detection results and emits them through the shared flow.
     *
     * @param face The detected face object.
     */
    private fun getFaceResults(face: Face) {
        try {
            var faceResult = FaceResult(faceVisible = true)

            face.smilingProbability?.let {
                faceResult = faceResult.copy(faceIsSmiling = it >= 0.35f)
            }

            face.leftEyeOpenProbability?.let {
                faceResult = faceResult.copy(leftEyeOpen = it <= 0.6f)
            }

            face.rightEyeOpenProbability?.let {
                faceResult = faceResult.copy(rightEyeOpen = it <= 0.6f)
            }
            emitState(BaseResponse.Success(faceResult))
        } catch (ex: Exception) {
            ex.printStackTrace()
            emitState(BaseResponse.Error(ex))
        }
    }


    private fun emitState(state: BaseResponse<FaceResult>) {
        CoroutineScope(Dispatchers.IO).launch {
            response.emit(state)
        }
    }

}