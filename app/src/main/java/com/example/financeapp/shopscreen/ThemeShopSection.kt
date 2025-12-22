package com.example.financeapp.shopscreen
import com.example.financeapp.ui.theme.Pistachio
import com.example.financeapp.ui.theme.Emerald

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle

@Composable
fun ThemeShopIntroSection(modifier: Modifier = Modifier, context: Context = LocalContext.current) {

    Column (
        modifier = modifier
            .fillMaxWidth()
            .background (
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text (
            text = "We get it! Green isn't for everyone!",
            color = Emerald,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(top = 12.dp, start = 12.dp)
        )

        Spacer (
            modifier = Modifier
                .padding (
                    6.dp
                )
        )

        Text (
            text = "Check out our purchasable content here. Purchasing themes from us also helps us make cool products for you, and keep our apps free.",
            color = Emerald,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(start = 12.dp)
        )
    }
}
@Composable
fun ThemeShopEntry(modifier: Modifier = Modifier, title: String, price: String) {

    Column (
        modifier = modifier
            .fillMaxSize()
            .padding()
            .background (
                color = Pistachio,
                shape = RoundedCornerShape(12.dp)
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text (
            text = "$title $price",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Emerald
        )

        Spacer (
            modifier = Modifier
                .height(12.dp)
        )

        Box (
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .aspectRatio(1f)
                .background (
                    color = Emerald,
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
            Box (
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .background (
                        color = Emerald,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text (
                    text = "Preview",
                    color = Pistachio,
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
                    .background (
                        color = Emerald,
                        shape = RoundedCornerShape(12.dp)
                    ),
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

@Composable
fun ThemeShopSection(modifier: Modifier = Modifier, context: Context = LocalContext.current) {

    Column (
        modifier = modifier
            .fillMaxSize()
            .background (
                color = Emerald,
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
            ThemeShopEntry (
                modifier = Modifier
                    .weight(1f),
                title = "Charcoal",
                price = "$1.99"
            )

            Spacer (
                modifier = Modifier
                    .width (
                        4.dp
                    )
            )

            ThemeShopEntry (
                modifier = Modifier
                    .weight(1f),
                title = "Electric",
                price = "$1.99"
            )
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
            ThemeShopEntry (
                modifier = Modifier
                    .weight(1f),
                title = "Azure",
                price = "$1.99"
            )

            Spacer (
                modifier = Modifier
                    .width (4.dp)
            )
            ThemeShopEntry (
                modifier = Modifier
                    .weight(1f),
                title = "Peach",
                price = "$1.99"
            )
        }
    }
}