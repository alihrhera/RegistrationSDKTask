package ali.hrhera.registration_sdk.domain.dto

/**
 * Result of face detection
 * @property faceVisible is the face visible?
 * @property faceIsSmiling is the face smiling?
 * @property leftEyeOpen is the left eye open?
 * @property rightEyeOpen is the right eye open?
 */
data class FaceResult(
    val faceVisible: Boolean = false,
    val faceIsSmiling: Boolean = false,
    val leftEyeOpen: Boolean = false,
    val rightEyeOpen: Boolean = false,
)