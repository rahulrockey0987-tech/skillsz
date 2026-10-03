package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.navigation.SkillszMainScaffold
import com.example.ui.theme.SkillszTheme
import com.example.viewmodel.SkillszViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SkillszTheme(darkTheme = false) {
                val viewModel: SkillszViewModel = viewModel()
                SkillszMainScaffold(viewModel = viewModel)
            }
        }
    }
}
