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
    viewModel: CustomContentViewModel
) {
    RunLaunchedEffect(viewModel::getAllUserAndCourses)
    val users = viewModel.users
    val course = viewModel.courses
    LazyColumn(Modifier.padding(16.dp)) {
        item {
            TitleText(text = stringResource(R.string.custom_provider_example), textSize = 20.sp)
            TitleText(text = stringResource(R.string.users_list), textSize = 15.sp)
        }
        item {
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
        item { TitleText(text = stringResource(R.string.courses_list), textSize = 15.sp) }
        item {
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


