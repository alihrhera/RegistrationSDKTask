package ali.hrhera.registration_sdk.presentation.features.start


sealed class StartScreenUiStat {
    data object Idel : StartScreenUiStat()
    data object AskForPermission : StartScreenUiStat()
    data object PermissionGranted : StartScreenUiStat()
    data object Error : StartScreenUiStat()
    data object None : StartScreenUiStat()
}