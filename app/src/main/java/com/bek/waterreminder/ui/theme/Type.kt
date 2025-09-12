package com.bek.waterreminder.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.bek.waterreminder.R

// Set of Material typography styles to start with

val Gilroy =
    FontFamily(
        Font(R.font.gilroy_bold, FontWeight.Bold),
        Font(R.font.gilroy_medium, FontWeight.Medium),
        Font(R.font.gilroy_semibold, FontWeight.SemiBold),
        Font(R.font.gilroy_regular, FontWeight.Normal),
        Font(R.font.gilroy_mediumitalic, weight = FontWeight.Medium, style = FontStyle.Italic),
    )
