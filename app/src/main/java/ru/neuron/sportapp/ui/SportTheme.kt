package ru.neuron.sportapp.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val crane_caption = Color.DarkGray
val crane_divider_color = Color.LightGray
private val craneRed = Color(0xFFE30425)
private val craneWhite = Color.White
private val cranePurple700 = Color(0xFF720D5D)
private val cranePurple800 = Color(0xFF5D1049)
private val cranePurple900 = Color(0xFF4E0D3A)

val sport_caption = Color.DarkGray
val sport_divider_color = Color.LightGray
private val sportRed = Color(0xFDE30425)
private val sportWhite = Color.White
private val sportGreen600 = Color(0xF2496F4D)
private val sportGreen700 = Color(0xFF3E5C41)
private val sportGreen800 = Color(0x803E5C41)
private val sportPink = Color(0xF0AD5CC1)

val sport_learning_item = Color(0x803E5C41)

val sportColors = lightColorScheme(
    primary = sportGreen600,
    secondary = cranePurple700,
    tertiary = craneRed,
    surface = cranePurple900,
    onSurface = craneWhite,
)

val BottomSheetShape = RoundedCornerShape(
    topStart = 20.dp,
    topEnd = 20.dp,
    bottomStart = 0.dp,
    bottomEnd = 0.dp
)

@Composable
fun SportTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = sportColors,
        typography = craneTypography) {
        content()
    }
}
