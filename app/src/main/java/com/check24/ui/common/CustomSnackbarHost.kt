package com.check24.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.check24.ui.productsListScreen.DataLoadingStates
import com.check24.ui.productsListScreen.ProductsListScreenConstants.TestTag.Snackbar_Host_Card
import com.check24.ui.productsListScreen.ProductsListScreenConstants.TestTag.Snackbar_Retry_Button

@Composable
fun CustomSnackbarHost(
    snackbarHostState: SnackbarHostState, loading: DataLoadingStates, retryButtonOnClick: () -> Unit
) {
    SnackbarHost(hostState = snackbarHostState, snackbar = {
        SnacbarContent(loading = loading, retryButtonOnClick = retryButtonOnClick)
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnacbarContent(loading: DataLoadingStates, retryButtonOnClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(5.dp)
            .wrapContentSize()
            .testTag(Snackbar_Host_Card)
    ) {
        Row(
            modifier = Modifier
                .padding(0.dp)
                .background(color = MaterialTheme.colorScheme.primary),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            when (loading) {
                DataLoadingStates.Loading_In_Progress -> {}
                DataLoadingStates.Failed -> {
                    Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(25.dp)
                    )
                    Text(
                        text = loading.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedButton(modifier = Modifier
                        .padding(7.dp)
                        .testTag(Snackbar_Retry_Button),
                        onClick = { retryButtonOnClick() }) {
                        Text(
                            modifier = Modifier.padding(3.dp),
                            text = loading.message,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                DataLoadingStates.Loaded -> {}
            }
        }
    }

}