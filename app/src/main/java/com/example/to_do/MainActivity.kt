package com.example.to_do


import android.content.res.Configuration
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
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
            val isDarkTheme = remember { mutableStateOf(false) }
            TODOTheme(darkTheme = isDarkTheme.value, dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ) {
                    MainPage(toggleTheme = isDarkTheme)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(toggleTheme: MutableState<Boolean>) {

    val todoName = remember {
        mutableStateOf("")
    }

    val focusManager = LocalFocusManager.current
//    val topBarBehaviour = TopAppBarDefaults.pinnedScrollBehavior()

    val myContext = LocalContext.current
    val taskList = remember { mutableStateListOf<String>().apply { addAll(taskReadData(myContext)) } }
    val doneList = remember { mutableStateListOf<String>().apply { addAll(doneReadData(myContext)) } }

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
    val doneCheckStatus = remember {
        mutableStateMapOf<String, Boolean>().apply {
            taskList.forEach { put(it, false) }
        }
    }
    val doneClickedItemIndex = remember {
        mutableIntStateOf(0)
    }

    val doneClickedItem = remember {
        mutableStateOf("")
    }

    val deleteDoneDialogStatus = remember {
        mutableStateOf(false)
    }
    val taskDoneDialogStatus = remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.shadow(
                    15.dp,
                    RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp),
                    true,
                    MaterialTheme.colorScheme.primary
                ),
                title = { Text(text = "Todo") },
//                scrollBehavior = topBarBehaviour,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {

                        // SWITCH

                        Switch(
                            checked = toggleTheme.value,
                            onCheckedChange = { toggleTheme.value = !toggleTheme.value },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                                uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
                                uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer
                            ),
                            modifier = Modifier.offset((-5).dp),
                            thumbContent = {
                                if (toggleTheme.value) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_dark_mode_24),
                                        contentDescription = "Dark Mode",
                                        modifier = Modifier.padding(3.dp)
                                    )
                                } else {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_light_mode_24),
                                        contentDescription = "Dark Mode",
                                        modifier = Modifier.padding(3.dp)
                                    )
                                }
                            }
                        )
                    }
                }
            )
        }, content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {

                Spacer(modifier = Modifier.size(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
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
                            .padding(end = 8.dp)
                            .weight(1f),
                        label = {
                            Text(
                                "Enter task",
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            focusedIndicatorColor = MaterialTheme.colorScheme.secondary, // Border color when focused
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface, // Border color when not focused,
                            focusedLabelColor = MaterialTheme.colorScheme.secondary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        maxLines = 2,
                        shape = RoundedCornerShape(8.dp, 8.dp, 0.dp, 0.dp)
                    )
                    ElevatedButton(
                        onClick = {
                            if (todoName.value.isNotEmpty()) {
                                taskList.add(todoName.value)
                                doneCheckStatus[todoName.value] = false
                                taskWriteData(taskList, myContext)
                                todoName.value = ""
                                focusManager.clearFocus()
                                Toast.makeText(myContext, "Task Added", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(myContext, "Please enter a task", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                        elevation = ButtonDefaults.buttonElevation(5.dp),
                        modifier = Modifier
                            .height(60.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = "Add",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.size(2.dp))
                        Icon(Icons.Rounded.Add, contentDescription = "Add Task")
                    }
                }
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "Tasks",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.offset(15.dp)
                )
                Spacer(modifier = Modifier.size(5.dp))
                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.outline,
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
                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 5.dp, start = 7.dp, end = 7.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 6.dp
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
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Checkbox(
                                        checked = doneCheckStatus[item] ?: false,
                                        onCheckedChange = { isChecked ->
                                            doneCheckStatus[item] = isChecked

                                            if (isChecked) {
                                                Toast.makeText(myContext, "Task Completed", Toast.LENGTH_SHORT).show()
                                                doneList.add(item)
                                                doneWriteData(doneList, myContext)
                                                taskList.remove(item)
                                                taskWriteData(taskList, myContext)
                                                doneCheckStatus.remove(item) // Remove from the map
                                            }
                                        }
                                    )
                                    Spacer(modifier = Modifier.size(10.dp))
                                    Text(
                                        text = item,
                                        fontSize = 13.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Start,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(8F)
                                    )
                                    Spacer(modifier = Modifier.size(7.dp))
                                    Row(modifier = Modifier.weight(3f)) {
                                        IconButton(
                                            onClick = {
                                                updateDialogStatus.value = true
                                                clickedItemIndex.intValue = index
                                                clickedItem.value = item
                                            }, modifier = Modifier
                                                .background(
                                                    color = MaterialTheme.colorScheme.tertiaryContainer,
                                                    shape = RoundedCornerShape(5.dp)
                                                )
                                                .size(33.dp)
                                        ) {
                                            Icon(
                                                Icons.Filled.Edit,
                                                contentDescription = "Edit",
                                                tint = MaterialTheme.colorScheme.onTertiaryContainer
                                            )
                                        }
                                        Spacer(modifier = Modifier.size(7.dp))
                                        IconButton(
                                            onClick = {
                                                deleteDialogStatus.value = true
                                                clickedItemIndex.intValue = index
                                            }, modifier = Modifier
                                                .background(
                                                    color = MaterialTheme.colorScheme.errorContainer,
                                                    shape = RoundedCornerShape(5.dp)
                                                )
                                                .size(33.dp)
                                        ) {
                                            Icon(
                                                Icons.Filled.Delete,
                                                contentDescription = "Delete",
                                                tint = MaterialTheme.colorScheme.onErrorContainer
                                            )
                                        }
                                    }

                                }
                            }
                        }

                    )
                }
                Spacer(modifier = Modifier.size(10.dp))
                Text(
                    text = "Completed",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.offset(15.dp)
                )
                Spacer(modifier = Modifier.size(5.dp))
                HorizontalDivider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(7.dp, 0.dp, 7.dp, 0.dp)
                        .shadow(10.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))
                LazyColumn {
                    items(
                        count = doneList.size,
                        itemContent = { index ->
                            val item = doneList[index]
                            ElevatedCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 5.dp, start = 7.dp, end = 7.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 6.dp
                                ),
                                shape = RoundedCornerShape(10)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                        .clickable {
                                            doneClickedItem.value = item
                                            taskDoneDialogStatus.value = true
                                        },
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                                ) {
                                    Checkbox(
                                        checked = true,
                                        onCheckedChange = { isChecked ->
                                            if (!isChecked) {
                                                Toast.makeText(myContext, "Task Marked as Incomplete", Toast.LENGTH_SHORT).show()
                                                taskList.add(item) // Add back to the task list
                                                taskWriteData(taskList, myContext)
                                                doneList.removeAt(index) // Remove from the completed list
                                                doneWriteData(doneList, myContext)
                                            }
                                        }
                                    )
                                    Spacer(modifier = Modifier.size(10.dp))
                                    Text(
                                        text = item,
                                        fontSize = 13.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Start,
                                        fontWeight = FontWeight.Bold,
                                        textDecoration = TextDecoration.LineThrough,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.weight(8F)
                                    )
                                    IconButton(
                                        onClick = {
                                            deleteDoneDialogStatus.value = true
                                            doneClickedItemIndex.intValue = index
                                        }, modifier = Modifier
                                            .background(
                                                color = MaterialTheme.colorScheme.errorContainer,
                                                shape = RoundedCornerShape(5.dp)
                                            )
                                            .size(33.dp)
                                    ) {
                                        Icon(
                                            Icons.Filled.Delete,
                                            contentDescription = "Delete",
                                            tint = MaterialTheme.colorScheme.onErrorContainer
                                        )
                                    }
                                }
                            }
                        }
                    )
                }

                if (taskDoneDialogStatus.value) {
                    AlertDialog(
                        onDismissRequest = { taskDoneDialogStatus.value = false },
                        text = { Text(text = doneClickedItem.value) },
                        shape = RoundedCornerShape(10),
                        containerColor = MaterialTheme.colorScheme.surface,
                        textContentColor = MaterialTheme.colorScheme.onSurface,
                        iconContentColor = MaterialTheme.colorScheme.primary,
                        confirmButton = {
                            TextButton(onClick = {
                                taskDoneDialogStatus.value = false
                            }) {
                                Text(text = "OK")
                            }
                        })
                }

                if (taskDialogStatus.value) {
                    AlertDialog(
                        onDismissRequest = { taskDialogStatus.value = false },
                        text = { Text(text = clickedItem.value) },
                        shape = RoundedCornerShape(10),
                        containerColor = MaterialTheme.colorScheme.surface,
                        textContentColor = MaterialTheme.colorScheme.onSurface,
                        iconContentColor = MaterialTheme.colorScheme.primary,
                        confirmButton = {
                            TextButton(onClick = {
                                taskDialogStatus.value = false
                            }) {
                                Text(text = "OK")
                            }
                        })
                }

                if (deleteDoneDialogStatus.value) {

                    AlertDialog(
                        onDismissRequest = { deleteDoneDialogStatus.value = false },
                        confirmButton = {
                            TextButton(onClick = {
                                doneList.removeAt(doneClickedItemIndex.intValue)
                                doneWriteData(doneList, myContext)
                                deleteDoneDialogStatus.value = false
                                Toast.makeText(myContext, "Deleted successfully", Toast.LENGTH_SHORT)
                                    .show()
                            }) {
                                Text(text = "YES")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { deleteDoneDialogStatus.value = false }) {
                                Text(text = "NO")
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.surface,
                        textContentColor = MaterialTheme.colorScheme.onSurface,
                        iconContentColor = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(10),
                        title = { Text(text = "Delete") },
                        text = { Text(text = "Are you sure you want to delete this task?") }
                    )

                }

                if (deleteDialogStatus.value) {

                    AlertDialog(
                        onDismissRequest = { deleteDialogStatus.value = false },
                        confirmButton = {
                            TextButton(onClick = {
                                taskList.removeAt(clickedItemIndex.intValue)
                                taskWriteData(taskList, myContext)
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
                        containerColor = MaterialTheme.colorScheme.surface,
                        textContentColor = MaterialTheme.colorScheme.onSurface,
                        iconContentColor = MaterialTheme.colorScheme.primary,
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
                                taskWriteData(taskList, myContext)
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
                        containerColor = MaterialTheme.colorScheme.surface,
                        textContentColor = MaterialTheme.colorScheme.onSurface,
                        iconContentColor = MaterialTheme.colorScheme.primary,
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


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun LightThemePreview() {
    val toggleTheme = remember { mutableStateOf(false) }
    TODOTheme(darkTheme = toggleTheme.value) {
        MainPage(toggleTheme = toggleTheme)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DarkThemePreview() {
    val toggleTheme = remember { mutableStateOf(true) }
    TODOTheme(darkTheme = toggleTheme.value) {
        MainPage(toggleTheme = toggleTheme)
    }
}

