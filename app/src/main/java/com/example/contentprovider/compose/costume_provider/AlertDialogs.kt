package com.example.contentprovider.compose.costume_provider

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun SearchAndDeleteDialog(
    fieldCount: Int = 1,
    callBack: (String) -> Unit,
    cancelCallBack: () -> Unit,
    title: String,
    confirmTextButton: String,
    secondField: String = "",
    thirdField: String = ""
) {
    val openDialog = remember { mutableStateOf(true) }
    var id by remember { mutableStateOf("") }
    var nameOrTitle by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        if (openDialog.value) {
            AlertDialog(
                //Клик за границей диалога
                onDismissRequest = {
                    openDialog.value = false
                    cancelCallBack()
                },
                text = {
                    Column {
                        Text(text = title, style = MaterialTheme.typography.h6)
                        Spacer(modifier = Modifier.height(24.dp))
                        TextField(
                            value = id,
                            onValueChange = { id = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            label = {
                                Text(text = "Enter table id")
                            }
                        )
                        if (fieldCount >= 2) {
                            Spacer(modifier = Modifier.height(24.dp))
                            TextField(
                                value = nameOrTitle,
                                onValueChange = { nameOrTitle = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                label = {
                                    Text(text = secondField)
                                }
                            )
                        }
                        if (fieldCount == 3) {
                            Spacer(modifier = Modifier.height(24.dp))
                            TextField(
                                value = age,
                                onValueChange = { age = it },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                label = {
                                    Text(text = thirdField)
                                }
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            openDialog.value = false
                            callBack("$id $nameOrTitle $age")
                        },
                        enabled = id.isNotEmpty()
                    ) { Text(confirmTextButton) }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            openDialog.value = false
                            cancelCallBack()
                        }) { Text("Cancel") }
                }
            )
        }
    }
}


@Composable
fun AreYouSureDialog(callBack: () -> Unit) {
    val openDialog = remember { mutableStateOf(true) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        if (openDialog.value) {
            AlertDialog(
                //Клик за границей диалога
                onDismissRequest = {
                    openDialog.value = false
                },
                text = {
                    Column {
                        Text(text = "Delete all users?", style = MaterialTheme.typography.h6)
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            openDialog.value = false
                            callBack()
                        }
                    ) { Text("Delete") }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            openDialog.value = false
                        }) { Text("Cancel") }
                }
            )
        }
    }
}
