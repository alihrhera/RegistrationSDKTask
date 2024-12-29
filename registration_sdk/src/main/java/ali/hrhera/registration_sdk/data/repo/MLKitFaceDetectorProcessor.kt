package ali.hrhera.registration_sdk.data.repo

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import ali.hrhera.registration_sdk.domain.usecase.faceDetection.FaceDetectorProcessor
import javax.inject.Inject
/**
 * An implementation of [FaceDetectorProcessor] that uses MLKit's face
 * detector. It is an advanced face detector that can detect faces in a
 * variety of orientations and at different distances from the camera. It
 * can also detect facial features such as eyes, nose, and mouth. It is more
 * accurate than the [CameraXFaceDetectorProcessor] but also more expensive
 * in terms of resources. It is a good choice when the user is far from the
 * camera or when the face is not directly facing the camera.
 *
 * @author Ali
 */
class MLKitFaceDetectorProcessor @Inject constructor() : FaceDetectorProcessor {

    /**
     * A face detector that detects faces in an image. It is an advanced
     * face detector that can detect faces in a variety of orientations and
     * at different distances from the camera. It can also detect facial
     * features such as eyes, nose, and mouth.
     */
    private val detector by lazy {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(MIN_FACE_SIZE)
            .enableTracking()
            .build()
        FaceDetection.getClient(options)
    }

    /**
     * Detects faces in an image.
     * @param image The image to detect faces in.
     * @param result A callback that is called with the detected faces. The
     * first parameter is a list of [Face] objects detected in the image.
     * The second parameter is an error message if the face detection failed.
     */
    @OptIn(ExperimentalGetImage::class)
    override fun processFace(image: ImageProxy, result: (Face?, String?) -> Unit) {
        detectInImage(InputImage.fromMediaImage(image.image!!, image.imageInfo.rotationDegrees))
            .addOnSuccessListener { faces ->
                result(faces.firstOrNull(), null)
            }
            .addOnFailureListener { ex: Exception ->
                result(null, "Face detection failed ${ex.message}")
            }
            .addOnCompleteListener {
            }
    }
    /**
     * Detects faces in an image.
     * @param image The image to detect faces in.
     * @return A [Task] that contains a list of [Face] objects detected in the image.
     */
    private fun detectInImage(image: InputImage): Task<List<Face>> {
        return detector.process(image)
    }


    companion object {
        /**
         * The minimum size of the face that can be detected. The size is
         * relative to the size of the image.
         */
        private const val MIN_FACE_SIZE = 0.15f
    }

}