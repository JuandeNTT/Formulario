package com.example.formulario.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.formulario.ui.theme.Dimens

@Composable
fun SubmitButton(
    onClick: () -> Unit,
    enabled: Boolean,
    isSubmitting: Boolean,
    buttonText: String,
    sendingText: String,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.buttonHeight)
    ) {
        if (isSubmitting) {
            CircularProgressIndicator(
                modifier = Modifier.size(Dimens.paddingLarge),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = Dimens.paddingExtraSmall
            )
            Spacer(modifier = Modifier.width(Dimens.paddingSmall))
            Text(sendingText)
        } else {
            Text(
                text = buttonText,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
