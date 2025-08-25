package com.bek.waterreminder.data.model.onboarding

import com.bek.waterreminder.R

val onboardingItems =
    listOf(
        OnboardingItem(
            imageRes = R.drawable.onboarding1,
            title = "Welcome to ",
            appName = "HydrateMe",
        ),
        OnboardingItem(imageRes = R.drawable.onboarding2, title = "Set your Hydration Goals"),
        OnboardingItem(
            imageRes = R.drawable.onboarding3,
            title = "Get Reminders and Tips on Hydration & Health",
        ),
    )
