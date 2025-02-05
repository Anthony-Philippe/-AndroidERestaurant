package fr.isen.philippe.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fr.isen.philippe.androiderestaurant.basket.Basket
import fr.isen.philippe.androiderestaurant.basket.BasketActivity
import fr.isen.philippe.androiderestaurant.network.Dish
import fr.isen.philippe.androiderestaurant.ui.theme.AndroidERestaurantTheme
import kotlin.math.max

@Suppress("DEPRECATION")
class DetailActivity : ComponentActivity() {
    @OptIn(
        ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
        ExperimentalMaterial3Api::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dish = intent.getSerializableExtra(DISH_EXTRA_KEY) as? Dish
        setContent {
            val context = LocalContext.current
            val count = remember {
                mutableIntStateOf(1)
            }
            val ingredient = dish?.ingredients?.map { it.name }?.joinToString(", ") ?: ""
            val price = dish?.prices?.get(0)?.price
            val pagerState = rememberPagerState(pageCount = {
                dish?.images?.count() ?: 0
            })
            AndroidERestaurantTheme {
                TopAppBar({
                    Text(dish?.name ?: "")
                })
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalPager(state = pagerState) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(dish?.images?.get(it))
                                .build(),
                            null,
                            placeholder = painterResource(R.drawable.ic_launcher_foreground),
                            error = painterResource(R.drawable.ic_launcher_foreground),
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .padding(15.dp)
                        )
                    }
                    Text(
                        text = "Ingrédients: \n $ingredient",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(price.toString() + "€")
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.weight(1f))
                        OutlinedButton(onClick = {
                            count.value = max(1, count.value - 1)
                        }) {
                            Text("-")
                        }
                        Text(count.value.toString())
                        OutlinedButton(onClick = {
                            count.value = count.value + 1
                        }) {
                            Text("+")
                        }
                        Spacer(Modifier.weight(1f))
                    }
                    Button(onClick = {
                        if (dish != null) {
                            Basket.current(context).add(dish, count.value, context)
                        }
                        Toast.makeText(context, "Ajouté au panier", Toast.LENGTH_SHORT).show()
                    }) {
                        Text("Ajouter au panier",)
                    }
                    Button(onClick = {
                        val intent = Intent(context, BasketActivity::class.java)
                        context.startActivity(intent)
                    }) {
                        Text("Voir mon panier")
                    }
                }
            }
        }
    }

    companion object {
        val DISH_EXTRA_KEY = "DISH_EXTRA_KEY"
    }
}