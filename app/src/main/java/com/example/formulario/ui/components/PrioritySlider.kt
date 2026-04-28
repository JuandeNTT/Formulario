package com.example.formulario.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.formulario.ui.theme.Primary
import com.example.formulario.ui.theme.PrimaryContainer

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
        // Label con valor
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            // Badge con el valor actual
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = PrimaryContainer,
                modifier = Modifier
                    .defaultMinSize(minWidth = 48.dp, minHeight = 32.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = value.toString(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = Primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Slider moderno
        Slider(
            value = value.toFloat(),
            onValueChange = onValueChange,
            valueRange = minValue.toFloat()..maxValue.toFloat(),
            steps = maxValue - minValue - 1,
            enabled = enabled,
            colors = SliderDefaults.colors(
                thumbColor = Primary,
                activeTrackColor = Primary,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledThumbColor = Primary.copy(alpha = 0.4f),
                disabledActiveTrackColor = Primary.copy(alpha = 0.4f)
            ),
            modifier = Modifier.fillMaxWidth()
        )
        
        // Etiquetas de rango
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Mín: $minValue",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Máx: $maxValue",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
