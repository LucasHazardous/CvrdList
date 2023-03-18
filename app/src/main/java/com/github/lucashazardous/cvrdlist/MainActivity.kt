package com.github.lucashazardous.cvrdlist

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.lucashazardous.cvrdlist.ui.theme.Beige
import com.github.lucashazardous.cvrdlist.ui.theme.Black
import com.github.lucashazardous.cvrdlist.ui.theme.CvrdListTheme
import com.github.lucashazardous.cvrdlist.ui.theme.Red

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CvrdListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CvrdListView()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CvrdListView() {
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            SmallTopAppBar(
                title = {
                    Row {
                        Button(
                            content = { Text(text = "Clear acquired") },
                            onClick = {
                            var removed = 0
                                for (i in 0 until cards.size) {
                                    if (cards[i - removed].acquired) {
                                        cards.removeAt(i - removed)
                                        removed++
                                    }
                                }
                            }, colors = ButtonDefaults.buttonColors(containerColor = Beige)
                        )
                    }
                }, colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Black,
                    titleContentColor = Black
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { addCardOpened.value = !addCardOpened.value },
                containerColor = Beige, contentColor = Red
            ) {
                Icon(
                    Icons.Filled.Add, "Add card",
                    modifier = Modifier.size(20.dp)
                )
            }
        }) { _ ->
        CardAdder()
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp, 75.dp, 10.dp, 0.dp),
        ) {
            items(cards.size) { item ->
                CardItem(cards[item])
            }
        }
    }
}