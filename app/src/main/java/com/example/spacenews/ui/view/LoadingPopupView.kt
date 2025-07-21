package com.example.spacenews.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun LoadingPopup() {
    val loadingState by LoadingState.loadingState.collectAsState()

    if (loadingState.isVisible) {
        Dialog(
            onDismissRequest = {
                // Si es cancelable, permite ocultarlo al presionar fuera o atrás
                if (loadingState.isCancelable) {
                    LoadingState.hide()
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = loadingState.isCancelable,
                dismissOnClickOutside = loadingState.isCancelable
            )
        ) {
            // Contenido del popup de carga
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = loadingState.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingPopupPreview() {
    // Para la preview
    LoadingState.show("Cargando...", isCancelable = false)
    LoadingPopup()
}