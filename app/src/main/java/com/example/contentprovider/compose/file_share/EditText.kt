package com.example.contentprovider.compose.file_share

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.contentprovider.ui.theme.lightGray150
import com.example.contentprovider.ui.theme.lightGray200

@Composable
fun EditText(
    active: Boolean,
    label: String,
    currentText: String,
    onTextChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = currentText,
        onValueChange = onTextChange,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        maxLines = 2,
        modifier = Modifier
            .fillMaxWidth()
            .padding(9.dp),
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = lightGray200,
            focusedLabelColor = lightGray150
        ),
        enabled = active
    )
}