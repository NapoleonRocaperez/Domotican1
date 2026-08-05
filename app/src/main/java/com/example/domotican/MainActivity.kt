package com.example.domotican

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.domotican.ui.theme.DomoticanTheme
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.ArrowBack



//https://domotican-b7177-default-rtdb.firebaseio.com/
//Po9f5qW7FJIVL322x4za3W2X4y09bL0dK6Yo3m2U

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DomoticanTheme {
                    Surface(
                        modifier= Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ){
                        DomoticanApp()
                    }
            }
        }
    }
}

@Composable
fun DomoticanApp(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home"){
        composable(route = "home"){ PantallaPrincipal(navController) }
        composable(route = "aire"){ PantallaAire(navController) }
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PantallaPrincipal(navController: NavController) {
    val database= Firebase.database
    val refPuerta= database.getReference("reles/puerta")
    val refLuces= database.getReference("reles/luces")

    var lucesEncendidas by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Domótica Roca", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )

                )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Control General",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 32.dp)
                )

            //Boton luces
            BotonModerno(
               texto = if (lucesEncendidas) "Apagar Luces" else "Encender Luces",
               ColorFondo = if (lucesEncendidas) MaterialTheme.colorScheme.secondary else Color.Gray,
            ){
              lucesEncendidas=!lucesEncendidas
              refLuces.setValue(lucesEncendidas)
            }
            Spacer(modifier = Modifier.height(20.dp))

            //Boton puerta
            BotonModerno(
                texto = "Abrir Puerta (5 Seg)",
                ColorFondo = Color(0xFF43A047)
            ){
                refPuerta.setValue(true)
            }
            Spacer(modifier = Modifier.height(40.dp))

            //boton aire acondicionado
            BotonModerno(
                texto = "Aire Acondicionado",
                ColorFondo = Color(0xFFF39C00)
            ){
                navController.navigate("aire")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAire(navController: NavController) {
    val refAire = Firebase.database.getReference("aire/comando")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Domótica Roca -Aire", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                       Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    )  { padding ->
         Column(
             modifier = Modifier
                 .fillMaxSize()
                 .padding(padding)
                 .padding(24.dp),
             horizontalAlignment = Alignment.CenterHorizontally,
             verticalArrangement = Arrangement.Center
         ) {
             BotonModerno("Encender", MaterialTheme.colorScheme.error){ refAire.setValue("encender") }
             Spacer(modifier = Modifier.height(16.dp))
             BotonModerno("Subida", MaterialTheme.colorScheme.primary){ refAire.setValue("subida") }
             Spacer(modifier = Modifier.height(16.dp))
             BotonModerno("Bajada", MaterialTheme.colorScheme.secondary){ refAire.setValue("bajada") }
             Spacer(modifier = Modifier.height(16.dp))
             BotonModerno("Más (+)", Color(0xFF43A047)){ refAire.setValue("mas") }
             Spacer(modifier = Modifier.height(16.dp))
             BotonModerno("Menos (-)", Color(0xFFFFB300)){ refAire.setValue("menos") }
             Spacer(modifier = Modifier.height(16.dp))
         }
    }
}


@Composable
fun BotonModerno(texto: String, ColorFondo: Color, onClick: () -> Unit) {
    Button(
          onClick=onClick,
          modifier = Modifier
              .fillMaxWidth()
              .height(60.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = ColorFondo),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp, pressedElevation = 2.dp)

    ) {
        Text(
            text = texto,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}




