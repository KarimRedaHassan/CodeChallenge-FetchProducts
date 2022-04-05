package com.check24.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource

@Composable
fun AppTabBar(title_id: Int, isMainDestination: Boolean, backArrowOnClick: () -> Unit) {
    SmallTopAppBar(title = {
        Text(
            stringResource(id = title_id),
            modifier = Modifier.testTag("AppBar Title")
        )
    }, navigationIcon = {
        if (!isMainDestination) {
            IconButton(
                onClick = { backArrowOnClick() },
                modifier = Modifier.testTag("AppBar Back Button")
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack, contentDescription = ""
                )
            }
        }
    })
}
