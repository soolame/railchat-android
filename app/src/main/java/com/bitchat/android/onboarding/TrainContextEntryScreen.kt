package com.bitchat.android.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bitchat.android.R
import com.bitchat.android.ui.theme.BitchatFontFamily

/**
 * Final onboarding step: optional train number + coach entry, used only to derive
 * local room IDs (see com.hop.rail.train.RoomResolver). Both fields are skippable.
 */
@Composable
fun TrainContextEntryScreen(
    modifier: Modifier,
    onSubmit: (trainNumber: String?, coachId: String?) -> Unit,
    onSkip: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    var trainNumber by remember { mutableStateOf("") }
    var coachId by remember { mutableStateOf("") }

    Box(
        modifier = modifier.padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.train_context_title),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = BitchatFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    ),
                    color = colorScheme.onBackground
                )
                Text(
                    text = stringResource(R.string.train_context_subtitle),
                    fontSize = 12.sp,
                    fontFamily = BitchatFontFamily,
                    color = colorScheme.onBackground.copy(alpha = 0.7f)
                )

                OutlinedTextField(
                    value = trainNumber,
                    onValueChange = { trainNumber = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(R.string.train_context_train_number_label),
                            fontFamily = BitchatFontFamily
                        )
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    value = coachId,
                    onValueChange = { coachId = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(R.string.train_context_coach_id_label),
                            fontFamily = BitchatFontFamily
                        )
                    },
                    singleLine = true
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        onSubmit(
                            trainNumber.trim().takeIf { it.isNotEmpty() },
                            coachId.trim().takeIf { it.isNotEmpty() }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
                ) {
                    Text(
                        text = stringResource(R.string.train_context_continue),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = BitchatFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                TextButton(
                    onClick = onSkip,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.train_context_skip),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = BitchatFontFamily
                        )
                    )
                }
            }
        }
    }
}
