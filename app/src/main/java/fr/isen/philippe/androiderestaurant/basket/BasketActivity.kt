package fr.isen.philippe.androiderestaurant.basket

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fr.isen.philippe.androiderestaurant.R
import fr.isen.philippe.androiderestaurant.ui.theme.AndroidERestaurantTheme
import java.math.BigDecimal

class BasketActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                BasketView()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasketView() {
    val context = LocalContext.current
    val basketItems = remember { mutableStateListOf<BasketItem>() }

    TopAppBar(title = { Text(text = stringResource(id = R.string.panier_title), fontWeight = FontWeight.Bold) })
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        items(basketItems) { BasketItemView(item = it, basketItems = basketItems) }
        item {
            val totalPrice = calculateTotalPrice(basketItems)
            Text(
                text = "Total: $totalPrice €",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold
            )
            Button(onClick = {
                if (basketItems.isNotEmpty())
                    Toast.makeText(context, "Commande passée", Toast.LENGTH_SHORT).show()
            }) {
                Text(if (basketItems.isEmpty()) stringResource(id = R.string.empty_basket) else stringResource(id = R.string.order_button))
            }
            if (basketItems.isNotEmpty()) {
                Button(onClick = {
                    basketItems.clear()
                    Basket.current(context).deleteAll(context)
                    basketItems.addAll(Basket.current(context).items)
                    Toast.makeText(context, "Panier vidé!", Toast.LENGTH_SHORT).show()
                }, modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(id = R.string.clear_basket))
                }
            }
        }
    }
    basketItems.addAll(Basket.current(context).items)
}

@Composable
fun BasketItemView(item: BasketItem, basketItems: MutableList<BasketItem>) {
    val context = LocalContext.current
    Card(modifier = Modifier.padding(8.dp)) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(Modifier.padding(10.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.dish.images.first())
                        .build(),
                    null,
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(
                    Modifier
                        .align(alignment = Alignment.CenterVertically)
                        .weight(1f)) {
                    Text(item.dish.name, fontWeight = FontWeight.Bold)
                    Text("${item.dish.prices.first().price} €")
                    Text(stringResource(id = R.string.quantity_basket, item.count))
                }
                Button(onClick = {
                    Basket.current(context).delete(item, context)
                    basketItems.clear()
                    basketItems.addAll(Basket.current(context).items)
                }, modifier = Modifier.align(Alignment.Bottom)) {
                    Text("X")
                }
            }
        }
    }
}

fun calculateTotalPrice(basketItems: List<BasketItem>): BigDecimal {
    var totalPrice = BigDecimal.ZERO
    for (item in basketItems) {
        val itemPrice = BigDecimal(item.dish.prices.first().price)
        totalPrice += itemPrice * BigDecimal(item.count)
    }
    return totalPrice
}