package com.example.contentprovider.compose.costume_provider

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.contentprovider.R
import com.example.contentprovider.ui.theme.lightGray800

@Composable
fun ButtonsRow(
    onClickAdd: () -> Unit,
    onClickSearch: () -> Unit,
    onClickDelete: () -> Unit,
    onClickClear: () -> Unit,
    onClickUpdate: () -> Unit
) {
    val size = 30.dp
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(25.dp, 10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_search),
            contentDescription = stringResource(R.string.search_table_row),
            modifier = Modifier
                .size(size)
                .clickable { onClickSearch() },
            colorFilter = ColorFilter.tint(lightGray800)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_add_24),
            contentDescription = stringResource(R.string.add_table_row),
            modifier = Modifier
                .size(size)
                .clickable { onClickAdd() },
            colorFilter = ColorFilter.tint(lightGray800)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_update_24),
            contentDescription = stringResource(R.string.update_table),
            modifier = Modifier
                .size(size)
                .clickable { onClickUpdate() },
            colorFilter = ColorFilter.tint(lightGray800)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_delete_24),
            contentDescription = stringResource(R.string.delete_table_row),
            modifier = Modifier
                .size(size)
                .clickable { onClickDelete() },
            colorFilter = ColorFilter.tint(lightGray800)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_clear_24),
            contentDescription = stringResource(R.string.clear_table),
            modifier = Modifier
                .size(size)
                .clickable { onClickClear() },
            colorFilter = ColorFilter.tint(lightGray800)
        )
    }
}