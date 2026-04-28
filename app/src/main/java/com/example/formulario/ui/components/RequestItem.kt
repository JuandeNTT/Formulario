package com.example.formulario.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.formulario.R
import com.example.formulario.data.model.FormEntity
import com.example.formulario.ui.theme.Dimens
import com.example.formulario.utils.convertirAHoraLocal

@Composable
fun RequestItem(
    request: FormEntity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.cardElevation),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.paddingMedium)
        ) {
            // Título y prioridad
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = request.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                
                Spacer(modifier = Modifier.width(Dimens.paddingSmall))
                
                // Badge de prioridad
                Text(
                    text = "${stringResource(R.string.priority_label)}: ${request.priority}",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .clip(RoundedCornerShape(Dimens.cornerRadiusSmall))
                        .background(getPriorityColor(request.priority))
                        .padding(
                            horizontal = Dimens.paddingSmall,
                            vertical = Dimens.paddingExtraSmall
                        )
                )
            }
            
            Spacer(modifier = Modifier.height(Dimens.paddingSmall))
            
            // Categoría
            Text(
                text = "${stringResource(R.string.category_label)}: ${request.category}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(Dimens.paddingSmall))
            
            // Descripción
            Text(
                text = request.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3
            )
            
            Spacer(modifier = Modifier.height(Dimens.paddingSmall))
            
            // Email
            Text(
                text = "${stringResource(R.string.email_label)}: ${request.email}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
            
            // Fecha de creación
            request.createdAt?.let { timestamp ->
                Spacer(modifier = Modifier.height(Dimens.paddingExtraSmall))
                Text(
                    text = convertirAHoraLocal(timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun getPriorityColor(priority: Int) = when (priority) {
    1 -> MaterialTheme.colorScheme.tertiary
    2 -> MaterialTheme.colorScheme.secondary
    3, 4 -> MaterialTheme.colorScheme.primary
    5 -> MaterialTheme.colorScheme.error
    else -> MaterialTheme.colorScheme.primary
}
