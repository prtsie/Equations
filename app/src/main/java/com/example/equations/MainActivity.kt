package com.example.equations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import kotlin.random.Random

var isStarted: MutableState<Boolean>? = null
var first: MutableState<Int>? = null
var second: MutableState<Int>? = null
var correct: MutableState<Int>? = null
var incorrect: MutableState<Int>? = null
var operation: MutableState<Op>? = null
var answer = 0
var input: MutableState<String>? = null
var parsedInput: MutableState<Int>? = null

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Greeting()
        }
    }
}

@Composable
fun Greeting() {
    isStarted = rememberSaveable { mutableStateOf(false) }
    first = rememberSaveable { mutableIntStateOf(0) }
    second = rememberSaveable { mutableIntStateOf(0) }
    correct = rememberSaveable { mutableIntStateOf(0) }
    incorrect = rememberSaveable { mutableIntStateOf(0) }
    parsedInput = rememberSaveable { mutableIntStateOf(0) }
    input = rememberSaveable { mutableStateOf("0") }
    operation = rememberSaveable { mutableStateOf(Op.Add) }

    fun generateEquation() {
        first!!.value = Random.nextInt(100)
        operation!!.value = Op.entries[Random.nextInt(Op.entries.count())]
        when (operation!!.value) {
            Op.Add -> {
                second!!.value = Random.nextInt(99 - first!!.value)
                answer = first!!.value + second!!.value
            }

            Op.Subtract -> {
                second!!.value = Random.nextInt(first!!.value + 1)
                answer = first!!.value - second!!.value
            }

            Op.Divide -> {
                var value = 1
                while (first!!.value % value != 0) {
                    value = Random.nextInt(first!!.value + 1)
                }
                second!!.value = value
                answer = first!!.value / second!!.value
            }

            Op.Multiple -> {
                second!!.value = Random.nextInt(99 / first!!.value)
                answer = first!!.value * second!!.value
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.height(50.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Итого решено примеров", fontSize = TextUnit(4f, TextUnitType.Em))
            Text(
                (correct!!.value + incorrect!!.value).toString(),
                fontSize = TextUnit(4f, TextUnitType.Em)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Правильно", fontSize = TextUnit(4f, TextUnitType.Em))
                Text(correct!!.value.toString(), fontSize = TextUnit(4f, TextUnitType.Em))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Неправильно", fontSize = TextUnit(4f, TextUnitType.Em))
                Text(incorrect!!.value.toString(), fontSize = TextUnit(4f, TextUnitType.Em))
            }
        }
        Text("0.00%", fontSize = TextUnit(10f, TextUnitType.Em))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(first!!.value.toString(), fontSize = TextUnit(7f, TextUnitType.Em))
            Text(operation!!.value.toString(), fontSize = TextUnit(7f, TextUnitType.Em))
            Text(second!!.value.toString(), fontSize = TextUnit(7f, TextUnitType.Em))
            Text("=", fontSize = TextUnit(7f, TextUnitType.Em))
            TextField(
                input!!.value,
                { i ->
                    if(i.count() < 3) {
                        input!!.value = i
                        if (i.isNotEmpty()) {
                            parsedInput!!.value = i.toInt()
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(fontSize = TextUnit(7f, TextUnitType.Em)),
                modifier = Modifier.width(70.dp)
            )
        }
        Column {
            Button(
                {
                    if (parsedInput!!.value == answer) {
                        correct!!.value++
                    } else {
                        incorrect!!.value++
                    }
                    input!!.value = ""
                    generateEquation()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isStarted!!.value && input!!.value.isNotEmpty()
            ) {
                Text("ПРОВЕРКА")
            }
            Button({
                generateEquation()
                isStarted!!.value = true
            }, modifier = Modifier.fillMaxWidth(), enabled = !isStarted!!.value) {
                Text("СТАРТ")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Greeting()
}