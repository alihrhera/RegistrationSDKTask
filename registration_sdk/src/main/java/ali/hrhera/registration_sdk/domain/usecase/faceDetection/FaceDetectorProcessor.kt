package ali.hrhera.registration_sdk.domain.usecase.faceDetection

import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.face.Face

interface FaceDetectorProcessor {
    fun processFace(image: ImageProxy, result: (Face?,String?) -> Unit)
}