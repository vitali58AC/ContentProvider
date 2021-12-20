package com.example.contentprovider.compose.costume_provider

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contentprovider.R
import com.example.contentprovider.compose.RunLaunchedEffect
import com.example.contentprovider.compose.TitleText
import com.example.contentprovider.costume_content_provider.CustomContentViewModel
import com.example.contentprovider.ui.theme.lightGray150

@Composable
fun CustomContentProviderScreen(
    onUserClick: (Long) -> Unit,
    onCourseClick: (Long) -> Unit,
    viewModel: CustomContentViewModel,
    onClickAdd: () -> Unit,
    onClickSearch: (String) -> Unit,
    onClickDelete: () -> Unit,
    onClickClear: () -> Unit,
    onClickUpdate: () -> Unit
) {
    RunLaunchedEffect(viewModel::getAllUserAndCourses)
    val users = viewModel.users
    val course = viewModel.courses
    val dialogSearchState = rememberSaveable { mutableStateOf(false) }
    val dialogAddState = rememberSaveable { mutableStateOf(false) }
    val dialogDeleteState = rememberSaveable { mutableStateOf(false) }
    val dialogDeleteAllUsersState = rememberSaveable { mutableStateOf(false) }
    val dialogUpdateState = rememberSaveable { mutableStateOf(false) }
    if (dialogSearchState.value) {
        SearchAndDeleteDialog(
            callBack = { id ->
                onClickSearch(id.trim())
                dialogSearchState.value = false
            },
            cancelCallBack = { dialogSearchState.value = false },
            title = stringResource(R.string.search_table_row_by_id),
            confirmTextButton = stringResource(R.string.search)
        )
    }
    if (dialogAddState.value) {
        SearchAndDeleteDialog(
            fieldCount = 3,
            callBack = { userString ->
                viewModel.saveNewUser(userString) { onClickAdd() }
                dialogAddState.value = false
            },
            cancelCallBack = { dialogAddState.value = false },
            title = stringResource(R.string.add_new_user),
            confirmTextButton = stringResource(R.string.save),
            secondField = stringResource(R.string.enter_user_name),
            thirdField = stringResource(R.string.enter_user_age)
        )
    }
    if (dialogDeleteState.value) {
        SearchAndDeleteDialog(
            callBack = { id ->
                viewModel.deleteUserFromId(id) { onClickDelete() }
                dialogDeleteState.value = false
            },
            cancelCallBack = { dialogDeleteState.value = false },
            title = stringResource(R.string.delete_with_id),
            confirmTextButton = stringResource(R.string.delete),
        )
    }
    if (dialogDeleteAllUsersState.value) {
        AreYouSureDialog {
            viewModel.deleteAllUsers { onClickClear() }
            dialogDeleteAllUsersState.value = false
        }
    }
    if (dialogUpdateState.value) {
        SearchAndDeleteDialog(
            fieldCount = 3,
            callBack = { userString ->
                viewModel.updateUserById(userString) { onClickUpdate() }
                dialogUpdateState.value = false
            },
            cancelCallBack = { dialogUpdateState.value = false },
            title = stringResource(R.string.update_user),
            confirmTextButton = stringResource(R.string.update),
            secondField = stringResource(R.string.enter_user_name),
            thirdField = stringResource(R.string.enter_user_age)
        )
    }
    LazyColumn(Modifier.padding(16.dp)) {
        item {
            TitleText(text = stringResource(R.string.custom_provider_example), textSize = 20.sp)
            TitleText(text = stringResource(R.string.users_list), textSize = 15.sp)
        }
        item {
            UserHeaderRow()
        }
        items(users) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { onUserClick(it.id) }) {
                TableCell(text = it.id.toString(), weight = .2f, fontWeight = FontWeight.Bold)
                TableCell(text = it.name, weight = 1f, fontWeight = FontWeight.Bold)
                TableCell(text = it.age.toString(), weight = .3f, fontWeight = FontWeight.Bold)
            }
        }
        item {
            ButtonsRow(
                onClickAdd = { dialogAddState.value = true },
                onClickSearch = { dialogSearchState.value = true },
                onClickDelete = { dialogDeleteState.value = true },
                onClickClear = { dialogDeleteAllUsersState.value = true },
                onClickUpdate = { dialogUpdateState.value = true }
            )
        }

        item { TitleText(text = stringResource(R.string.courses_list), textSize = 15.sp) }
        item {
            CourseHeaderRow()
        }
        items(course) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable { onCourseClick(it.id) }) {
                TableCell(text = it.id.toString(), weight = .2f, fontWeight = FontWeight.Bold)
                TableCell(text = it.title, weight = 1f, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    fontWeight: FontWeight = FontWeight.Light,
    align: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(8.dp),
        fontWeight = fontWeight,
        textAlign = align
    )
}

@Composable
fun CourseHeaderRow() {
    Row(Modifier.background(lightGray150)) {
        TableCell(
            text = "id",
            weight = .2f,
            fontWeight = FontWeight.Bold,
            align = TextAlign.Center
        )
        TableCell(
            text = "title",
            weight = 1f,
            fontWeight = FontWeight.Bold,
            align = TextAlign.Center
        )
    }
}

@Composable
fun UserHeaderRow() {
    Row(Modifier.background(lightGray150)) {
        TableCell(
            text = "id",
            weight = .2f,
            fontWeight = FontWeight.Bold,
            align = TextAlign.Center
        )
        TableCell(
            text = "name",
            weight = 1f,
            fontWeight = FontWeight.Bold,
            align = TextAlign.Center
        )
        TableCell(
            text = "age",
            weight = .3f,
            fontWeight = FontWeight.Bold,
            align = TextAlign.Center
        )
    }
}


