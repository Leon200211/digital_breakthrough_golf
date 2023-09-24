/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.neuron.sportapp.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

//private val light = Font(R.font.raleway_light, FontWeight.W300)
//private val regular = Font(R.font.raleway_regular, FontWeight.W400)
//private val medium = Font(R.font.raleway_medium, FontWeight.W500)
//private val semibold = Font(R.font.raleway_semibold, FontWeight.W600)

//private val craneFontFamily = FontFamily(fonts = listOf(light, regular, medium, semibold))
private val sportFontFamily = FontFamily.Default

val captionTextStyle = TextStyle(
    fontFamily = sportFontFamily,
    fontWeight = FontWeight.W400,
    fontSize = 16.sp
)

val craneTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = sportFontFamily,
        fontWeight = FontWeight.W300,
        fontSize = 96.sp
    ),
    displayMedium = TextStyle(
        fontFamily = sportFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 60.sp
    ),
    displaySmall = TextStyle(
        fontFamily = sportFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 48.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = sportFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 34.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = sportFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = sportFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 20.sp
    ),
    titleMedium = TextStyle(
        fontFamily = sportFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp
    ),
    titleSmall = TextStyle(
        fontFamily = sportFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = sportFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = sportFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = sportFontFamily,
        fontWeight = FontWeight.W600,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = sportFontFamily,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp
    ),
    labelSmall = TextStyle(
        fontFamily = sportFontFamily,
        fontWeight = FontWeight.W400,
        fontSize = 12.sp
    )
)
