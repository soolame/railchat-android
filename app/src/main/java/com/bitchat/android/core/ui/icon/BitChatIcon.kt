package com.bitchat.android.core.ui.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val BitChatIcon: ImageVector
    get() {
        _BitChatIcon?.let { return it }

        return ImageVector.Builder(
            name = "BitChatIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 10f,
            viewportHeight = 8f,
        ).apply {
            // Blocky train-coach silhouette: a body block riding on two wheel tabs.
            path(fill = SolidColor(Color.Black)) {
                moveTo(1f, 0f)
                lineTo(9f, 0f)
                lineTo(9f, 6f)
                lineTo(8f, 6f)
                lineTo(8f, 8f)
                lineTo(6f, 8f)
                lineTo(6f, 6f)
                lineTo(4f, 6f)
                lineTo(4f, 8f)
                lineTo(2f, 8f)
                lineTo(2f, 6f)
                lineTo(1f, 6f)
                close()
            }
        }.build().also { _BitChatIcon = it }
    }

private var _BitChatIcon: ImageVector? = null
