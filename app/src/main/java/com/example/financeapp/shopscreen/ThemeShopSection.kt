package com.example.financeapp.shopscreen
import android.app.Activity
import com.example.financeapp.ui.theme.Pistachio
import com.example.financeapp.ui.theme.Emerald
import com.example.financeapp.ui.theme.AzureBlue
import com.example.financeapp.ui.theme.CharcoalGreen
import com.example.financeapp.ui.theme.ElectricPurple
import com.example.financeapp.ui.theme.LocalAppColors
import com.example.financeapp.ui.theme.Peach
import com.example.financeapp.ui.theme.AppColors
import com.example.financeapp.advertisement.AdvertisementViewModel
import com.example.financeapp.advertisement.AdSectionSmallBanner

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.runtime.remember


@Composable
fun ThemeShopIntroSection(modifier: Modifier = Modifier, colors: AppColors = LocalAppColors.current, context: Context = LocalContext.current) {

    Column (
        modifier = modifier
            .fillMaxWidth()
            .background (
                color = colors.primary,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text (
            text = "We get it! Green isn't for everyone!",
            color = colors.secondary,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(top = 12.dp, start = 12.dp)
        )

        Spacer (
            modifier = Modifier
                .padding (6.dp)
        )

        Text (
            text = "Check out our purchasable content here. Purchasing themes from us also helps us make cool products for you, and keep our apps free.",
            color = colors.secondary,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(start = 12.dp, bottom = 4.dp)
        )
    }
}
@Composable
fun ThemeShopEntry(modifier: Modifier = Modifier, colors: AppColors = LocalAppColors.current, alreadyBought: Boolean, title: String, price: String, color: Color, previewRequested: () -> Unit, applyThemeRequested: () -> Unit, purchaseRequested: () -> Unit) {

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding()
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text (
            text = if (alreadyBought) title else "$title $price",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary
        )

        Spacer (
            modifier = Modifier
                .height(12.dp)
        )

        Box (
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .aspectRatio(1f)
                .border (
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .background (
                    color = color,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
        }

        Spacer (
            modifier = Modifier
                .height(12.dp)
        )

        Row (
            modifier = Modifier
                .fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (alreadyBought) {
                Box (
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .border (
                            width = 1.dp,
                            color = Pistachio,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background (
                            color = Emerald,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            applyThemeRequested()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text (
                        text = "Apply",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold
                    )
                }

            } else {
                Box (
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .border (
                            width = 1.dp,
                            color = Pistachio,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background (
                            color = Emerald,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            previewRequested()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text (
                        text = "Preview",
                        color = Color.Gray,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic
                    )
                }

                Spacer (
                    modifier = Modifier
                        .width (
                            12.dp
                        )
                )

                Box (
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .border (
                            width = 1.dp,
                            color = Pistachio,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background (
                            color = Emerald, //Die Farbe soll immmer Emerald sein, um mehr zum Kauf anzuregen ;)
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable (
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            purchaseRequested()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text (
                        text = "Buy",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}

@Composable
fun ThemeShopSection(modifier: Modifier = Modifier, colors: AppColors = LocalAppColors.current, themeShopViewModel: ThemeShopViewModel, advertisementViewModel: AdvertisementViewModel, context: Context = LocalContext.current, previewRequested: (theme: String) -> Unit, applyThemeRequested: (theme: String) -> Unit) {

    Column (
        modifier = modifier
            .fillMaxSize()
            .background (
                color = colors.primary,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val appliedTheme = themeShopViewModel.appliedTheme.collectAsState()
            val isCharcoalApplied = appliedTheme.value == "Charcoal"
            val isElectricApplied = appliedTheme.value == "Electric"

            if (isCharcoalApplied) {
                ThemeShopEntry (
                    modifier = Modifier
                        .weight(1f),
                    alreadyBought = true,
                    title = "Greeen",
                    price = "",
                    color = Emerald,
                    previewRequested = {
                    },
                    applyThemeRequested = {
                        applyThemeRequested("Greeen")
                        themeShopViewModel.setAppliedTheme("Greeen")
                    },
                    purchaseRequested = {
                    }
                )
            } else {
                ThemeShopEntry (
                    modifier = Modifier
                        .weight(1f),
                    alreadyBought = themeShopViewModel.getThemePurchased("Charcoal"),
                    title = "Charcoal",
                    price = "$1.99",
                    color = CharcoalGreen,
                    previewRequested = {
                        previewRequested("Charcoal")
                    },
                    applyThemeRequested = {
                        applyThemeRequested("Charcoal")
                        themeShopViewModel.setAppliedTheme("Charcoal")
                    },
                    purchaseRequested = {
                        themeShopViewModel.purchaseTheme(context as Activity,"Charcoal")
                    }
                )
            }

            Spacer (
                modifier = Modifier
                    .width (
                        4.dp
                    )
            )

            if (isElectricApplied) {
                ThemeShopEntry (
                    modifier = Modifier
                        .weight(1f),
                    alreadyBought = true,
                    title = "Greeen",
                    price = "",
                    color = Emerald,
                    previewRequested = {
                    },
                    applyThemeRequested = {
                        applyThemeRequested("Greeen")
                        themeShopViewModel.setAppliedTheme("Greeen")
                    },
                    purchaseRequested = {
                    }
                )
            } else {
                ThemeShopEntry(
                    modifier = Modifier
                        .weight(1f),
                    alreadyBought = themeShopViewModel.getThemePurchased("Electric"),
                    title = "Electric",
                    price = "$1.99",
                    color = ElectricPurple,
                    previewRequested = {
                        previewRequested("Electric")
                    },
                    applyThemeRequested = {
                        applyThemeRequested("Electric")
                        themeShopViewModel.setAppliedTheme("Electric")
                    },
                    purchaseRequested = {
                        themeShopViewModel.purchaseTheme(context as Activity, "Electric")
                    }
                )
            }
        }

        Spacer (
            modifier = Modifier
                .height(4.dp)
        )

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val appliedTheme = themeShopViewModel.appliedTheme.collectAsState()
            val isAzureApplied = appliedTheme.value == "Azure"
            val isPeachApplied = appliedTheme.value == "Peach"

            if (isAzureApplied) {
                ThemeShopEntry (
                    modifier = Modifier
                        .weight(1f),
                    alreadyBought = true,
                    title = "Greeen",
                    price = "",
                    color = Emerald,
                    previewRequested = {
                    },
                    applyThemeRequested = {
                        applyThemeRequested("Greeen")
                        themeShopViewModel.setAppliedTheme("Greeen")
                    },
                    purchaseRequested = {
                    }
                )
            } else {
                ThemeShopEntry (
                    modifier = Modifier
                        .weight(1f),
                    alreadyBought = themeShopViewModel.getThemePurchased("Azure"),
                    title = "Azure",
                    price = "$1.99",
                    color = AzureBlue,
                    previewRequested = {
                        previewRequested("Azure")
                    },
                    applyThemeRequested = {
                        applyThemeRequested("Azure")
                        themeShopViewModel.setAppliedTheme("Azure")
                    },
                    purchaseRequested = {
                        themeShopViewModel.purchaseTheme(context as Activity,"Azure")
                    }
                )
            }

            Spacer (
                modifier = Modifier
                    .width (4.dp)
            )

            if (isPeachApplied) {
                ThemeShopEntry (
                    modifier = Modifier
                        .weight(1f),
                    alreadyBought = true,
                    title = "Greeen",
                    price = "",
                    color = Emerald,
                    previewRequested = {
                    },
                    applyThemeRequested = {
                        applyThemeRequested("Greeen")
                        themeShopViewModel.setAppliedTheme("Greeen")
                    },
                    purchaseRequested = {
                    }
                )
            } else {
                ThemeShopEntry (
                    modifier = Modifier
                        .weight(1f),
                    alreadyBought = themeShopViewModel.getThemePurchased("Peach"),
                    title = "Peach",
                    price = "$1.99",
                    color = Peach,
                    previewRequested = {
                        previewRequested("Peach")
                    },
                    applyThemeRequested = {
                        applyThemeRequested("Peach")
                        themeShopViewModel.setAppliedTheme("Peach")
                    },
                    purchaseRequested = {
                        val activity = context as Activity
                        themeShopViewModel.purchaseTheme(activity, "Peach")
                    }
                )
            }
        }

        if (!advertisementViewModel.getRemoveAllAds()) {

            Spacer (
                modifier = Modifier
                    .height(4.dp)
            )

            RemoveAdsSection(
                modifier = Modifier
                    .weight(0.35f),
                purchaseRequested = {
                    val activity = context as Activity
                    themeShopViewModel.purchaseRemoveAllAds(activity = activity)
                }
            )

            AdSectionSmallBanner (
                modifier = Modifier
                    .weight(0.2f)
            )
        }
    }
}