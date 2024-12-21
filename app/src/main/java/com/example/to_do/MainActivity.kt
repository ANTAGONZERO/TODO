package com.example.to_do


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.to_do.ui.theme.TODOTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TODOTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(id = R.color.background)
                ) {
                    MainPage()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage() {


    val todoName = remember {
        mutableStateOf("")
    }

    val focusManager = LocalFocusManager.current
//    val topBarBehaviour = TopAppBarDefaults.pinnedScrollBehavior()

    val myContext = LocalContext.current
    val taskList = readData(myContext)

    val deleteDialogStatus = remember {
        mutableStateOf(false)
    }
    val clickedItemIndex = remember {
        mutableIntStateOf(0)
    }

    val updateDialogStatus = remember {
        mutableStateOf(false)
    }

    val clickedItem = remember {
        mutableStateOf("")
    }

    val taskDialogStatus = remember {
        mutableStateOf(false)
    }

    Scaffold(
//        modifier = Modifier.nestedScroll(topBarBehaviour.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.shadow(
                    15.dp,
                    RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp),
                    true,
                    colorResource(id = R.color.shadow)
                ),
                title = { Text(text = "Todo") },
//                scrollBehavior = topBarBehaviour,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.primary),
                    titleContentColor = colorResource(id = R.color.on_primary),
                    scrolledContainerColor = colorResource(id = R.color.secondary)
                ),
            )
        }, content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {

                Spacer(modifier = Modifier.size(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextField(
                        value = todoName.value,
                        onValueChange = {
                            todoName.value = it
                            if (it.isEmpty()) {
                                focusManager.clearFocus()
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(7f),
                        label = {
                            Text(
                                "Enter task",
                                color = colorResource(id = R.color.on_surface)
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = colorResource(id = R.color.on_surface),
                            unfocusedTextColor = colorResource(id = R.color.on_surface),
                            cursorColor = colorResource(id = R.color.primary),
                            focusedIndicatorColor = colorResource(id = R.color.secondary), // Border color when focused
                            unfocusedIndicatorColor = colorResource(id = R.color.border_accent),
                            focusedLabelColor = colorResource(id = R.color.secondary),
                            unfocusedLabelColor = colorResource(id = R.color.on_surface)
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp)
                    )
                    Button(
                        onClick = {
                            if (todoName.value.isNotEmpty()) {
                                taskList.add(todoName.value)
                                writeData(taskList, myContext)
                                todoName.value = ""
                                focusManager.clearFocus()
                                Toast.makeText(myContext, "Task Added", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(myContext, "Please enter a task", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                        modifier = Modifier
                            .height(65.dp)
                            .weight(3f),
                        shape = RoundedCornerShape(5.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.primary),
                            contentColor = colorResource(id = R.color.on_primary)
                        )
                    ) {
                        Text(
                            text = "Add",
                            color = colorResource(id = R.color.on_button),
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.size(2.dp))
                        Icon(Icons.Rounded.Add, contentDescription = "Add Task")
                    }
                }
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "Tasks",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(
                        id = R.color.on_surface
                    ),
                    modifier = Modifier.offset(15.dp)
                )
                Spacer(modifier = Modifier.size(5.dp))
                HorizontalDivider(
                    thickness = 2.dp,
                    color = colorResource(id = R.color.shadow),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(7.dp, 0.dp, 7.dp, 0.dp)
                        .shadow(10.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))
                LazyColumn {
                    items(
                        count = taskList.size,
                        itemContent = { index ->
                            val item = taskList[index]
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 5.dp, start = 7.dp, end = 7.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = colorResource(id = R.color.surface),
                                    contentColor = colorResource(id = R.color.item_text)
                                ),
                                shape = RoundedCornerShape(10)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                        .clickable {
                                            clickedItem.value = item
                                            taskDialogStatus.value = true
                                        },
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                                ) {
                                    Text(
                                        text = item,
                                        fontSize = 13.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Start,
                                        fontWeight = FontWeight.Bold,
                                        color = colorResource(id = R.color.item_text)
                                    )

                                    Row {
                                        IconButton(
                                            onClick = { /*TODO*/ }, modifier = Modifier
                                                .background(
                                                    color = colorResource(
                                                        id = R.color.done_button_bg
                                                    ), shape = RoundedCornerShape(5.dp)
                                                )
                                                .size(33.dp)
                                        ) {
                                            Icon(Icons.Filled.Done, contentDescription = "Done")
                                        }
                                        Spacer(modifier = Modifier.size(7.dp))
                                        IconButton(
                                            onClick = {
                                                updateDialogStatus.value = true
                                                clickedItemIndex.intValue = index
                                                clickedItem.value = item
                                            }, modifier = Modifier
                                                .background(
                                                    color = colorResource(
                                                        id = R.color.edit_button_bg
                                                    ), shape = RoundedCornerShape(5.dp)
                                                )
                                                .size(33.dp)
                                        ) {
                                            Icon(Icons.Filled.Edit, contentDescription = "Done")
                                        }
                                        Spacer(modifier = Modifier.size(7.dp))
                                        IconButton(
                                            onClick = {
                                                deleteDialogStatus.value = true
                                                clickedItemIndex.intValue = index
                                            }, modifier = Modifier
                                                .background(
                                                    color = colorResource(
                                                        id = R.color.delete_button_bg
                                                    ), shape = RoundedCornerShape(5.dp)
                                                )
                                                .size(33.dp)
                                        ) {
                                            Icon(Icons.Filled.Delete, contentDescription = "Done")
                                        }
                                    }

                                }
                            }
                        }

                    )
                }

                if (taskDialogStatus.value) {
                    AlertDialog(
                        onDismissRequest = { taskDialogStatus.value = false },
                        text = { Text(text = clickedItem.value) },
                        shape = RoundedCornerShape(10),
                        confirmButton = {
                            TextButton(onClick = {
                                taskDialogStatus.value = false
                            }) {
                                Text(text = "OK")
                            }
                        })
                }

                if (deleteDialogStatus.value) {

                    AlertDialog(
                        onDismissRequest = { deleteDialogStatus.value = false },
                        confirmButton = {
                            TextButton(onClick = {
                                taskList.removeAt(clickedItemIndex.intValue)
                                writeData(taskList, myContext)
                                deleteDialogStatus.value = false
                                Toast.makeText(myContext, "Task Deleted", Toast.LENGTH_SHORT).show()
                            }) {
                                Text(text = "YES")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { deleteDialogStatus.value = false }) {
                                Text(text = "NO")
                            }
                        },
                        shape = RoundedCornerShape(10),
                        title = { Text(text = "Delete Task") },
                        text = { Text(text = "Are you sure you want to delete this task?") }
                    )

                }

                if (updateDialogStatus.value) {

                    AlertDialog(
                        onDismissRequest = { deleteDialogStatus.value = false },
                        confirmButton = {
                            TextButton(onClick = {
                                taskList[clickedItemIndex.intValue] = clickedItem.value
                                writeData(taskList, myContext)
                                updateDialogStatus.value = false
                                Toast.makeText(myContext, "Task Updated", Toast.LENGTH_SHORT).show()
                            }) {
                                Text(text = "YES")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { updateDialogStatus.value = false }) {
                                Text(text = "NO")
                            }
                        },
                        shape = RoundedCornerShape(10),
                        title = { Text(text = "Update Task") },
                        text = {
                            TextField(
                                value = clickedItem.value,
                                onValueChange = { clickedItem.value = it })
                        }
                    )

                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TODOTheme {
        MainPage()
    }
}