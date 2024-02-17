package fr.isen.philippe.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.ContentAlpha
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import fr.isen.philippe.androiderestaurant.network.Category
import fr.isen.philippe.androiderestaurant.network.Dish
import fr.isen.philippe.androiderestaurant.network.MenuResult
import fr.isen.philippe.androiderestaurant.network.NetworkConstants
import com.google.gson.GsonBuilder
import org.json.JSONObject

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type =
            (intent.getSerializableExtra(CATEGORY_EXTRA_KEY) as? ItemType) ?: ItemType.STARTER

        setContent {
            MenuView(type)
        }
        Log.d("lifeCycle", "Menu Activity - OnCreate")
    }

    override fun onPause() {
        Log.d("lifeCycle", "Menu Activity - OnPause")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifeCycle", "Menu Activity - OnResume")
    }

    override fun onDestroy() {
        Log.d("lifeCycle", "Menu Activity - onDestroy")
        super.onDestroy()
    }

    companion object {
        val CATEGORY_EXTRA_KEY = "CATEGORY_EXTRA_KEY"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuView(type: ItemType) {
    val category = remember {
        mutableStateOf<Category?>(null)
    }
    Column(
        Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar({
            Text(type.title())
        })
        Divider()
        LazyVerticalGrid(
            GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth()
        ) {
            category.value?.let { it ->
                items(it.items) { dish ->
                    DishCard(dish)
                }
            }
        }
    }
    PostData(type, category)
}

@Composable
fun DishCard(dish: Dish) {
    val context = LocalContext.current
    Card(modifier = Modifier
        .padding(8.dp)
        .height(225.dp)
        .clickable {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra(DetailActivity.DISH_EXTRA_KEY, dish)
            }
            context.startActivity(intent)
        }
    ) {
        Box(
            modifier = Modifier.clip(RoundedCornerShape(8.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(dish.images.first())
                        .build(),
                    null,
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    dish.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 125.dp),
                    color = Color.Black.copy(alpha = ContentAlpha.high)
                )
                Text(
                    "${dish.prices.first().price} €",
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun PostData(type: ItemType, category: MutableState<Category?>) {
    val currentCategory = type.title()
    val context = LocalContext.current
    val queue = Volley.newRequestQueue(context)

    val params = JSONObject()
    params.put(NetworkConstants.ID_SHOP, "1")

    val request = JsonObjectRequest(Request.Method.POST, NetworkConstants.URL, params, { response ->
        Log.d("request", response.toString(2))
        val result = GsonBuilder().create().fromJson(response.toString(), MenuResult::class.java)
        val filteredResult = result.data.first { category -> category.name == currentCategory }
        category.value = filteredResult
    }, {
        Log.e("request", it.toString())
    })

    queue.add(request)
}