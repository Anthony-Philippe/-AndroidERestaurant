package fr.isen.philippe.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import fr.isen.philippe.androiderestaurant.ui.theme.AndroidERestaurantTheme

enum class ItemType {
    STARTER, MAIN, DESSERT;

    @Composable
    fun title(): String {
        return when (this) {
            STARTER -> stringResource(id = R.string.entrées_btn)
            MAIN -> stringResource(id = R.string.plats_btn)
            DESSERT -> stringResource(id = R.string.desserts_btn)
        }
    }
}

interface MenuInterface {
    fun itemPressed(itemType: ItemType)
}

class HomeActivity : ComponentActivity(), MenuInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetView(this)
                }
            }
        }
        Log.d("lifeCycle", "Home Activity - OnCreate")
    }

    override fun itemPressed(itemType: ItemType) {
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra(MenuActivity.CATEGORY_EXTRA_KEY, itemType)
        startActivity(intent)
    }

    override fun onPause() {
        Log.d("lifeCycle", "Home Activity - OnPause")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifeCycle", "Home Activity - OnResume")
    }

    override fun onDestroy() {
        Log.d("lifeCycle", "Home Activity - onDestroy")
        super.onDestroy()
    }
}

@Composable
fun SetView(menu: MenuInterface) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painterResource(R.drawable.ic_launcher_foreground), null)
            Text("Restaurant", style = MaterialTheme.typography.titleLarge)
        }
        CustomButton(type = ItemType.STARTER, menu)
        CustomButton(type = ItemType.MAIN, menu)
        CustomButton(type = ItemType.DESSERT, menu)
    }
}

@Composable
fun CustomButton(type: ItemType, menu: MenuInterface) {
    ElevatedButton(onClick = { menu.itemPressed(type) }) {
        Text(type.title())
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidERestaurantTheme {
        SetView(HomeActivity())
    }
}