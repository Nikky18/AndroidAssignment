package com.nikky.clientassignmnet.base.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.nikky.clientassignmnet.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarView(
    title: String = "",
    showLeftIcon: Boolean = true,
    leftIcon: ImageVector = Icons.AutoMirrored.Outlined.ArrowBack,
    leftIconClickAction: () -> Unit,
) {
    Column {
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(title, textAlign = TextAlign.Center)
                }
            },
            navigationIcon = {
                if (showLeftIcon) {
                    Icon(
                        imageVector = leftIcon,
                        contentDescription = stringResource(R.string.back_button),
                        modifier = Modifier.clickable {
                            leftIconClickAction()
                        },
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )
    }
}

@Composable
fun LoadingView(
    isLoading: Boolean? = false,
    content: @Composable () -> Unit,
) {
    Box {
        content()
        if (isLoading == true) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }
}