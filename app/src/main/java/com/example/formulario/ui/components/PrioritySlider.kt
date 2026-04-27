package com.example.formulario.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.formulario.ui.theme.Dimens

@Composable
fun PrioritySlider(
    value: Int,
    onValueChange: (Float) -> Unit,
    label: String,
    minValue: Int,
    maxValue: Int,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(Dimens.spacingSmall))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Slider(
                value = value.toFloat(),
                onValueChange = onValueChange,
                valueRange = minValue.toFloat()..maxValue.toFloat(),
                steps = maxValue - minValue - 1,
                modifier = Modifier.weight(1f),
                enabled = enabled
            )

            Spacer(modifier = Modifier.width(Dimens.paddingMedium))

            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(Dimens.buttonHeight)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = value.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
