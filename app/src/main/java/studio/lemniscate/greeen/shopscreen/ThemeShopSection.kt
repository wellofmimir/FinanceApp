package studio.lemniscate.greeen.shopscreen

import android.app.Activity
import studio.lemniscate.greeen.ui.theme.Pistachio
import studio.lemniscate.greeen.ui.theme.Emerald
import studio.lemniscate.greeen.ui.theme.AzureBlue
import studio.lemniscate.greeen.ui.theme.CharcoalGreen
import studio.lemniscate.greeen.ui.theme.ElectricPurple
import studio.lemniscate.greeen.ui.theme.LocalAppColors
import studio.lemniscate.greeen.ui.theme.AppColors
import studio.lemniscate.greeen.ui.theme.Bordeaux
import studio.lemniscate.greeen.ui.theme.LocalAppTypography

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.material3.Text

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.Alignment

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun ThemeShopIntroSection (
    modifier: Modifier = Modifier,
    colors: AppColors = LocalAppColors.current
) {
    val typography = LocalAppTypography.current

    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(start = 12.dp, end = 12.dp)
    ) {
        Spacer (
            modifier = Modifier
                .height(12.dp)
        )

        Text (
            text = "We get it! Green isn't for everyone!",
            color = colors.secondary,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold,
            fontSize = typography.body,
            modifier = Modifier
        )

        Spacer (
            modifier = Modifier
                .height(12.dp)
        )

        Text (
            text = "Check out our purchasable content here. Purchasing themes from us also helps us make cool products for you, and keep our apps free.",
            color = colors.secondary,
            fontWeight = FontWeight.Normal,
            fontSize = typography.medium,
            modifier = Modifier
        )
    }
}
@Composable
fun ThemeShopEntry (
    modifier: Modifier = Modifier,
    alreadyBought: Boolean,
    title: String,
    price: String,
    color: Color,
    previewRequested: () -> Unit,
    applyThemeRequested: () -> Unit,
    purchaseRequested: () -> Unit
) {
    val colors = LocalAppColors.current
    val typography = LocalAppTypography.current

    Column (
        modifier = modifier
            .background (
                color = colors.secondary,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer (
            modifier = Modifier
                .weight(0.5f)
        )

        Text (
            text = if (alreadyBought) title else "$title $price",
            fontSize = typography.body,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
            modifier = Modifier
                .weight(1f)
        )

        Box (
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth(0.75f)
                .fillMaxHeight(0.8f)
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
                .weight(0.25f)
        )

        Row (
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(0.75f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (alreadyBought) {
                Box (
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(35.dp, 50.dp)
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
                        fontSize = typography.medium,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.Bold
                    )
                }

            } else {
                Box (
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(35.dp, 50.dp)
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
                        fontSize = typography.medium,
                        fontStyle = FontStyle.Italic
                    )
                }

                Spacer (
                    modifier = Modifier
                        .weight(0.1f)
                )

                Box (
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(35.dp, 50.dp)
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
                        fontSize = typography.medium,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }

        Spacer (
            modifier = Modifier
                .weight(0.25f)
        )
    }
}

@Composable
fun ThemeShopSection (
    modifier: Modifier = Modifier,
    colors: AppColors = LocalAppColors.current,
    themeShopViewModel: ThemeShopViewModel,
    context: Context = LocalContext.current,
    previewRequested: (theme: String) -> Unit,
    applyThemeRequested: (theme: String) -> Unit
) {
    val purchasedThemes by themeShopViewModel.purchasedThemes.collectAsState()
    val adremoverActive by themeShopViewModel.adRemoverPurchased.collectAsState()

    Column (
        modifier = modifier
            .safeDrawingPadding()
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

            val isCharcoalApplied = appliedTheme.value == "charcoaltheme"
            val isElectricApplied = appliedTheme.value == "electrictheme"

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
                    alreadyBought = purchasedThemes.contains("charcoaltheme"),
                    title = "Charcoal",
                    price = "$1.99",
                    color = CharcoalGreen,
                    previewRequested = {
                        previewRequested("charcoaltheme")
                    },
                    applyThemeRequested = {
                        themeShopViewModel.setAppliedTheme("charcoaltheme")
                        applyThemeRequested("charcoaltheme")
                    },
                    purchaseRequested = {
                        themeShopViewModel.purchaseTheme(context as Activity,"charcoaltheme")
                    }
                )
            }

            Spacer (
                modifier = Modifier
                    .width (4.dp)
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
                ThemeShopEntry (
                    modifier = Modifier
                        .weight(1f),
                    alreadyBought = purchasedThemes.contains("electrictheme"),
                    title = "Electric",
                    price = "$1.99",
                    color = ElectricPurple,
                    previewRequested = {
                        previewRequested("electrictheme")
                    },
                    applyThemeRequested = {
                        applyThemeRequested("electrictheme")
                        themeShopViewModel.setAppliedTheme("electrictheme")
                    },
                    purchaseRequested = {
                        themeShopViewModel.purchaseTheme(context as Activity, "electrictheme")
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

            val isAzureApplied = appliedTheme.value == "azuretheme"
            val isElegantTheme = appliedTheme.value == "eleganttheme"

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
                    alreadyBought = purchasedThemes.contains("azuretheme"),
                    title = "Azure",
                    price = "$1.99",
                    color = AzureBlue,
                    previewRequested = {
                        previewRequested("azuretheme")
                    },
                    applyThemeRequested = {
                        applyThemeRequested("azuretheme")
                        themeShopViewModel.setAppliedTheme("azuretheme")
                    },
                    purchaseRequested = {
                        themeShopViewModel.purchaseTheme(context as Activity,"azuretheme")
                    }
                )
            }

            Spacer (
                modifier = Modifier
                    .width (4.dp)
            )

            if (isElegantTheme) {
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
                    alreadyBought = purchasedThemes.contains("eleganttheme"),
                    title = "Elegant",
                    price = "$1.99",
                    color = Bordeaux,
                    previewRequested = {
                        previewRequested("eleganttheme")
                    },
                    applyThemeRequested = {
                        applyThemeRequested("eleganttheme")
                        themeShopViewModel.setAppliedTheme("eleganttheme")
                    },
                    purchaseRequested = {
                        val activity = context as Activity
                        themeShopViewModel.purchaseTheme(activity, "eleganttheme")
                    }
                )
            }
        }
    }
}