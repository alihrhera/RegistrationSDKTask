package ali.hrhera.registration_sdk.presentation.features.registration_form

import ali.hrhera.registration_sdk.presentation.ui.component.ButtonState
import ali.hrhera.registration_sdk.presentation.ui.component.ButtonWithAnimation
import ali.hrhera.registration_sdk.presentation.ui.component.StyledTextField
import ali.hrhera.registration_sdk.presentation.ui.component.VerticalSpace
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun RegisterScreen(navController: NavController) {
    val viewModel: RegisterFormViewModel = hiltViewModel()

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val name = rememberSaveable { mutableStateOf<String?>(null) }
        val password = rememberSaveable { mutableStateOf<String?>(null) }
        val phone = rememberSaveable { mutableStateOf<String?>(null) }
        val email = rememberSaveable { mutableStateOf<String?>(null) }

        if (viewModel.registerScreenState.value.generalError.isNullOrBlank().not()) {
            Text(text = viewModel.registerScreenState.value.generalError ?: "", color = Color.Red)
        }

        StyledTextField(modifier = Modifier.fillMaxWidth(),
            label = "Name",
            errorMessage = viewModel.registerScreenState.value.nameError,
            keyboardType = KeyboardType.Text,
            onValueChange = { name.value = it })

        VerticalSpace(8)

        StyledTextField(modifier = Modifier.fillMaxWidth(),
            label = "phone",
            keyboardType = KeyboardType.Phone,
            errorMessage = viewModel.registerScreenState.value.phoneError,
            onValueChange = { phone.value = it })

        VerticalSpace(8)

        StyledTextField(modifier = Modifier.fillMaxWidth(),
            label = "email",
            keyboardType = KeyboardType.Email,
            errorMessage = viewModel.registerScreenState.value.emailError,
            onValueChange = { email.value = it })

        VerticalSpace(8)

        StyledTextField(modifier = Modifier.fillMaxWidth(),
            label = "password",
            keyboardType = KeyboardType.Password,
            errorMessage = viewModel.registerScreenState.value.passwordError,
            onValueChange = { password.value = it })

        VerticalSpace(16)

        val buttonState = when {
            viewModel.registerScreenState.value.done -> ButtonState.Success("Success")
            viewModel.registerScreenState.value.isLoading -> ButtonState.Loading("Loading")
            else -> ButtonState.Idle("Register")
        }
        ButtonWithAnimation(buttonState = buttonState,
            onClick = {
                viewModel.registerUser(name.value, phone.value, email.value, password.value)
            }, onSuccess = {
                Log.w("TAG", "RegisterScreen: ${viewModel.registerScreenState.value.insertedUserId}", )
                navController.navigate("cameraStep/${viewModel.registerScreenState.value.insertedUserId}")
            })


    }


}




