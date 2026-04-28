package com.example.formulario.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.formulario.R
import com.example.formulario.ui.components.*
import com.example.formulario.ui.theme.BackgroundGray
import com.example.formulario.ui.theme.Dimens
import com.example.formulario.ui.viewmodel.FormViewModel

@Composable
fun FormScreen(
    modifier: Modifier = Modifier,
    onNavigateToRequests: () -> Unit = {},
    viewModel: FormViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    
    val categories = listOf(
        stringResource(R.string.category_general),
        stringResource(R.string.category_technical),
        stringResource(R.string.category_support),
        stringResource(R.string.category_billing),
        stringResource(R.string.category_feedback)
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.paddingMedium)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FormHeader()
            
            // Botón para ver todas las solicitudes
            OutlinedButton(
                onClick = onNavigateToRequests,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimens.paddingMedium)
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null,
                    modifier = Modifier.padding(end = Dimens.paddingSmall)
                )
                Text(text = stringResource(R.string.button_view_requests))
            }
            
            FormCard(
                uiState = uiState,
                viewModel = viewModel,
                categories = categories
            )
        }
        
        // Error Dialog
        uiState.errorMessage?.let { errorMessage ->
            ErrorDialog(
                errorMessage = errorMessage,
                onDismiss = { viewModel.dismissErrorMessage() }
            )
        }
    }
}

@Composable
private fun FormHeader() {
    Text(
        text = stringResource(R.string.form_title),
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(
            top = Dimens.paddingMedium,
            bottom = Dimens.paddingLarge
        )
    )
}

@Composable
private fun FormCard(
    uiState: com.example.formulario.ui.viewmodel.FormUiState,
    viewModel: FormViewModel,
    categories: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.cardElevation),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            TitleField(
                value = uiState.formData.title,
                onValueChange = { viewModel.onTitleChange(it) },
                errorMessage = uiState.titleError,
                charCount = uiState.titleCharCount,
                enabled = !uiState.isSubmitting
            )
            
            Spacer(modifier = Modifier.height(Dimens.spacingMedium))
            
            DescriptionField(
                value = uiState.formData.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                errorMessage = uiState.descriptionError,
                charCount = uiState.descriptionCharCount,
                enabled = !uiState.isSubmitting
            )
            
            Spacer(modifier = Modifier.height(Dimens.spacingMedium))
            
            CategoryDropdown(
                selectedCategory = uiState.formData.category,
                onCategorySelected = { viewModel.onCategoryChange(it) },
                categories = categories,
                label = stringResource(R.string.label_category),
                placeholder = stringResource(R.string.hint_category),
                errorMessage = uiState.categoryError,
                enabled = !uiState.isSubmitting
            )
            
            Spacer(modifier = Modifier.height(Dimens.spacingLarge))
            
            PrioritySlider(
                value = uiState.formData.priority,
                onValueChange = { viewModel.onPriorityChange(it) },
                label = stringResource(R.string.label_priority),
                minValue = FormViewModel.PRIORITY_MIN,
                maxValue = FormViewModel.PRIORITY_MAX,
                enabled = !uiState.isSubmitting
            )
            
            Spacer(modifier = Modifier.height(Dimens.spacingMedium))
            
            EmailTextField(
                value = uiState.formData.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = stringResource(R.string.label_email),
                placeholder = stringResource(R.string.hint_email),
                errorMessage = uiState.emailError,
                charCount = uiState.emailCharCount,
                maxLength = FormViewModel.EMAIL_MAX_LENGTH,
                enabled = !uiState.isSubmitting
            )
            
            Spacer(modifier = Modifier.height(Dimens.spacingLarge))
            
            SubmitButton(
                onClick = { viewModel.submitForm() },
                enabled = viewModel.isFormValid(),
                isSubmitting = uiState.isSubmitting,
                buttonText = stringResource(R.string.button_submit),
                sendingText = stringResource(R.string.button_sending)
            )
            
            if (uiState.submitSuccess) {
                Spacer(modifier = Modifier.height(Dimens.spacingMedium))
                SuccessMessage(
                    message = stringResource(R.string.success_message)
                )
            }
        }
    }
}

@Composable
private fun TitleField(
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String?,
    charCount: Int,
    enabled: Boolean
) {
    FormTextField(
        value = value,
        onValueChange = onValueChange,
        label = stringResource(R.string.label_title),
        placeholder = stringResource(R.string.hint_title),
        errorMessage = errorMessage,
        charCount = charCount,
        maxLength = FormViewModel.TITLE_MAX_LENGTH,
        singleLine = true,
        enabled = enabled
    )
}

@Composable
private fun DescriptionField(
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String?,
    charCount: Int,
    enabled: Boolean
) {
    FormTextField(
        value = value,
        onValueChange = onValueChange,
        label = stringResource(R.string.label_description),
        placeholder = stringResource(R.string.hint_description),
        errorMessage = errorMessage,
        charCount = charCount,
        maxLength = FormViewModel.DESCRIPTION_MAX_LENGTH,
        singleLine = false,
        minLines = 4,
        maxLines = 6,
        enabled = enabled
    )
}
