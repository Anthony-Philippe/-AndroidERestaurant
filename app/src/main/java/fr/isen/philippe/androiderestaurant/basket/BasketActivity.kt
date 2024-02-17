package fr.isen.philippe.androiderestaurant.basket

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fr.isen.philippe.androiderestaurant.R

class BasketActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasketView()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasketView() {
    val context = LocalContext.current
    val basketItems = remember {
        mutableStateListOf<BasketItem>()
    }
    val total = 0
    TopAppBar({
        Text(
            text = "Panier",
            fontWeight = FontWeight.Bold
        )
    })

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        items(basketItems) {
            BasketItemView(it, basketItems)
        }
        item {
            Text(
                text = "Total: $total €",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold
            )
            Button(
                onClick = {
                    if (basketItems.isNotEmpty())
                        Toast.makeText(context, "Commande passée!", Toast.LENGTH_SHORT).show()
                }
            ) {
                if (basketItems.isEmpty()) {
                    Text("Panier vide")
                } else
                    Text("Commander")
            }
            Button(
                onClick = {
                    basketItems.clear()
                    Basket.current(context).deleteAll(context)
                    basketItems.addAll(Basket.current(context).items)
                    Toast.makeText(context, "Panier vidé!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Vider le panier")
            }
        }
    }
    basketItems.addAll(Basket.current(context).items)
}

@Composable
fun BasketItemView(item: BasketItem, basketItems: MutableList<BasketItem>) {
    Card {
        val context = LocalContext.current
        Card(
            border = BorderStroke(1.dp, Color.Black),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Row(Modifier.padding(8.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.dish.images.first())
                        .build(),
                    null,
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(10))
                        .padding(8.dp)
                )
                Column(
                    Modifier
                        .align(alignment = Alignment.CenterVertically)
                        .padding(8.dp)
                ) {
                    Text(item.dish.name)
                    Text("${item.dish.prices.first().price} €")
                    Text("Quantité: ${item.count}")
                }
                Button(onClick = {
                    Basket.current(context).delete(item, context)
                    basketItems.clear()
                    basketItems.addAll(Basket.current(context).items)
                }) {
                    Text("X")
                }
            }
        }
    }
}