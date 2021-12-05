package com.example.contentprovider.compose.add_contact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contentprovider.R
import com.example.contentprovider.compose.TitleText
import com.example.contentprovider.ui.theme.lightGray150
import com.example.contentprovider.ui.theme.lightGray600
import com.example.contentprovider.ui.theme.lightGray800

@Composable
fun AddContactScreen() {
    var inputName by rememberSaveable { mutableStateOf("") }
    var inputSurname by rememberSaveable { mutableStateOf("") }
    var inputPhone by rememberSaveable { mutableStateOf("") }
    var inputEmail by rememberSaveable { mutableStateOf("") }
    var validateInput by rememberSaveable { mutableStateOf(false) }
    validateInput = validateUserInput(
        name = inputName,
        surname = inputSurname,
        phone = inputPhone,
        email = inputEmail
    )
    LazyColumn(
        modifier = Modifier
            .background(lightGray150)
            .fillMaxSize()
            .padding(6.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        item {
            TitleText(text = stringResource(R.string.create_new_contact), textSize = 25.sp)
            Spacer(modifier = Modifier.height(50.dp))
        }
        item {
            InputField(text = inputName, onValueChange = { inputName = it }, label = "Name*")
            InputField(
                text = inputSurname,
                onValueChange = { inputSurname = it },
                label = "Surname*"
            )
            InputField(text = inputPhone, onValueChange = { inputPhone = it }, label = "Phone*")
            InputField(text = inputEmail, onValueChange = { inputEmail = it }, label = "Email")
        }
        item { Spacer(modifier = Modifier.height(150.dp)) }
        item { UserActions(validateInput) }
    }
}

@Composable
fun UserActions(validateInput: Boolean) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            ActionButton(
                text = stringResource(R.string.cancel),
                onClick = { /*TODO*/ }
            )
            ActionButton(
                text = stringResource(R.string.save),
                onClick = { /*TODO*/ },
                enabled = validateInput
            )
        }

    }
}

fun validateUserInput(name: String, surname: String, phone: String, email: String): Boolean {
    return name.length >= 3 && surname.length >= 3 && phone.length >= 3 && email.length >= 5
}


@Composable
fun InputField(text: String, onValueChange: (String) -> Unit, label: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(4.dp)
    ) {
        TextField(
            value = text,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.White),
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun ActionButton(text: String, onClick: () -> Unit, enabled: Boolean = true) {
    TextButton(
        onClick = { onClick() },
        Modifier.padding(8.dp),
        enabled = enabled
    ) { Text(text = text, fontSize = 19.sp, color = if (enabled) lightGray800 else lightGray600) }
}