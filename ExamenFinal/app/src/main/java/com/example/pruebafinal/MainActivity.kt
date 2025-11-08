package com.example.pruebafinal


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: MainViewModel = viewModel()
            Surface {

                LoginScreen(
                    onLogin = { email, pass ->
                        vm.login(email, pass) { user ->
                            vm.cargarEquipos()
                            if (user.rol == "administrador") {
                                vm.cargarPrestamosPendientes()

                            } else {
                                vm.cargarPrestamosUsuario(user.uid)

                            }
                        }
                    },
                    onGoRegister = {

                    }
                )
            }
        }
    }
}
