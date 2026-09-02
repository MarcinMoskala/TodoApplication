package com.marcinmoskala.todoapplication.ui

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "1. Light theme")
@Preview(name = "2. Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "3. Scaled", fontScale = 2f)
@Preview(name = "4. Tablet", device = Devices.AUTOMOTIVE_1024p)
annotation class TodoAppPreview