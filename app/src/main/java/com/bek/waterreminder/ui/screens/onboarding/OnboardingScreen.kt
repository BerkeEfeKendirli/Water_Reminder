package com.bek.waterreminder.ui.screens.onboarding

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bek.waterreminder.R
import com.bek.waterreminder.data.model.onboarding.onboardingItems
import com.bek.waterreminder.ui.components.CustomButton
import com.bek.waterreminder.ui.theme.Gilroy

@Composable
fun OnboardingScreen() {
  var page by remember { mutableIntStateOf(0) }
  val item = onboardingItems[page]
  val buttonText = if (page == onboardingItems.size - 1) "Let's Go" else "Next"
  val context = LocalContext.current

  Column(modifier = Modifier.fillMaxSize()) {
    Image(
        painter = painterResource(item.imageRes),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.6f),
        contentScale = ContentScale.Crop,
    )
    Spacer(modifier = Modifier.height(60.dp))
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
          buildAnnotatedString {
            withStyle(style = SpanStyle(color = colorResource(R.color.primary_black))) {
              append(item.title)
            }
            withStyle(style = SpanStyle(color = colorResource(R.color.primary_blue))) {
              append(item.appName)
            }
          },
          fontFamily = Gilroy,
          fontWeight = FontWeight.Bold,
          fontSize = 24.sp,
          textAlign = TextAlign.Center,
      )
      CustomButton(
          onClick = {
            if (page < onboardingItems.size - 1) {
              page++
            } else {
              Toast.makeText(context, "End", Toast.LENGTH_LONG).show()
            }
          },
          text = buttonText,
          modifier = Modifier.width(150.dp),
      )
    }
  }
}
