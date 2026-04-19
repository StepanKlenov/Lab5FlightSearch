package com.example.lab5flightsearch

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab5flightsearch.data.datastore.SearchPreferences
import com.example.lab5flightsearch.data.local.AppDatabase
import com.example.lab5flightsearch.data.repository.FlightRepository
import com.example.lab5flightsearch.presentation.ui.screen.flightScreen
import com.example.lab5flightsearch.presentation.viewmodel.FlightViewModel
import com.example.lab5flightsearch.presentation.viewmodel.FlightViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        try {
            Log.d("MainActivity", "Начало инициализации базы данных...")

            val db = AppDatabase.getDatabase(this)
            Log.d("MainActivity", "База данных успешно создана/открыта")

            val repository = FlightRepository(
                airportDao = db.airportDao(),
                favoriteDao = db.favoriteDao(),
                prefs = SearchPreferences(this)
            )

            val factory = FlightViewModelFactory(repository)

            setContent {
                MaterialTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        val viewModel: FlightViewModel = viewModel(factory = factory)
                        flightScreen(viewModel)
                    }
                }
            }
            Log.d("MainActivity", "UI успешно установлен")

        } catch (e: Exception) {
            Log.e("MainActivity", "КРИТИЧЕСКАЯ ОШИБКА при запуске приложения", e)
            // Показываем текст ошибки на экране, чтобы было видно без Logcat
            setContent {
                MaterialTheme {
                    Surface(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "Ошибка запуска:\n${e.message}\n\nПосмотрите Logcat для деталей",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}