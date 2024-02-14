package fr.isen.philippe.androiderestaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import fr.isen.philippe.androiderestaurant.network.Dish

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dish = intent.getSerializableExtra(DISH_EXTRA_KEY) as? Dish
        val dishImage = intent.getStringExtra(DISH_IMAGE_EXTRA_KEY)
        val dishIngredients = intent.getStringExtra(DISH_INGREDIENTS_EXTRA_KEY)

        setContent {
            DishDetailView(dish, dishImage, dishIngredients)
        }
    }

    companion object {
        const val DISH_EXTRA_KEY = "DISH_EXTRA_KEY"
        const val DISH_IMAGE_EXTRA_KEY = "DISH_IMAGE_EXTRA_KEY"
        const val DISH_INGREDIENTS_EXTRA_KEY = "DISH_INGREDIENTS_EXTRA_KEY"
    }
}

@Composable
fun DishDetailView(dish: Dish?, dishImage: String?, dishIngredients: String?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        dish?.let {
            if (dishImage != null) {
                Image(
                    painter = rememberImagePainter(dishImage),
                    contentDescription = null,
                    modifier = Modifier.size(200.dp)
                )
            }
            Text(
                text = dish.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "Ingrédients: $dishIngredients",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}