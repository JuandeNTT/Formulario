package com.example.formulario

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.formulario.navigation.NavigationGraph
import com.example.formulario.ui.theme.FormularioTheme

//Segundo ejercicio con IA Juan de Dios Panches
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FormularioTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
                    NavigationGraph(
                        modifier = Modifier
                    )
                }
            }
        }
    }
}