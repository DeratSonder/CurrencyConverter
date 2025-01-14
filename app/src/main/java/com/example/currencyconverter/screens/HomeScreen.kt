package com.example.currencyconverter.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconverter.R
import com.example.currencyconverter.viewmodels.HomeScreenViewModel

val backgroundColor = Color(0xff090909)
val buttonColor = Color(0xff181818)
val focusColor = Color(0xFF98FB98)


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    var openCurrencyDialog by remember { mutableStateOf(false) }
    val currencyList by viewModel.currencies.collectAsState()
    val convertedValue by viewModel.convertedValue.
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("EUR") }
    var value by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(
                WindowInsets.systemBars.asPaddingValues()
            ),
        topBar = { TopBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
                .imePadding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CurrencyUnitButton(
                    currency = fromCurrency,
                    onClick = { openCurrencyDialog = true }
                )
                InputTextField(
                    onValueChange = { value = it }
                )
            }

            SwapButton(
                onClick = {
                    fromCurrency = toCurrency.also {
                        toCurrency = fromCurrency
                    }
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CurrencyUnitButton(
                    currency = toCurrency,
                    onClick = { openCurrencyDialog = true }
                )
                OutputTextField()
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.convert(value.toDouble(), fromCurrency, toCurrency)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Convert",
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }

    CurrencyDialog(
        currencyList = currencyList,
        isOpen = openCurrencyDialog,
        onDismiss = { openCurrencyDialog = false },
        onItemClick = { /*TODO*/ }
    )
}

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    Text(
        text = "Currency Converter",
        textAlign = TextAlign.Center,
        fontSize = MaterialTheme.typography.titleLarge.fontSize,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    )
}


@Composable
fun CurrencyUnitButton(
    modifier: Modifier = Modifier,
    currency: String = "USD",
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .wrapContentSize(align = Alignment.Center),
    ) {
        Text(
            text = currency,
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun SwapButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .wrapContentSize(align = Alignment.Center),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = focusColor,
            contentColor = Color.White
        )
    ) {
        Icon(
            painter = painterResource(id = R.drawable.exchange_icon),
            contentDescription = "Swap",
            tint = Color.White,
            modifier = Modifier.rotate(90f)
        )
    }
}

@Preview
@Composable
fun InputTextField(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {}
) {

    var input by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {

        TextField(
            value = input,
            onValueChange = {
                input = it
                onValueChange(it)
            },
            textStyle = TextStyle(color = Color.White),

            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .border(
                    width = 2.dp,
                    color = if (isFocused) focusColor else buttonColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = buttonColor,
                unfocusedContainerColor = buttonColor,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedPlaceholderColor = Color.White,
                unfocusedPlaceholderColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number // Đặt bàn phím là bàn phím số
            )

        )
    }
}


@Composable
fun OutputTextField(
    output: String = "",
    modifier: Modifier = Modifier
) {

    val isFocused by remember { mutableStateOf(output.isNotEmpty()) }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {

        TextField(
            value = output,
            onValueChange = { },
            textStyle = TextStyle(color = Color.White),
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .border(
                    width = 2.dp,
                    color = if (isFocused) focusColor else buttonColor,
                    shape = RoundedCornerShape(12.dp)
                ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = buttonColor,
                unfocusedContainerColor = buttonColor,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedPlaceholderColor = Color.White,
                unfocusedPlaceholderColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number // Đặt bàn phím là bàn phím số
            )
        )
    }
}

@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Search...",
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(color = Color.White),
        placeholder = {
            Text(
                text = placeholder,
                style = TextStyle(color = Color.Gray)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.Gray
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = Color.DarkGray,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.DarkGray,
            unfocusedContainerColor = Color.DarkGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun CurrencyDialog(
    modifier: Modifier = Modifier,
    currencyList: Map<String, String>,
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onItemClick: (String) -> Unit
) {
    if (isOpen) {

        var searchQuery by remember { mutableStateOf("") }

        Dialog(
            onDismissRequest = onDismiss
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(backgroundColor),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SearchTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .height(150.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(currencyList.size) {
                        val title =
                            "${currencyList.keys.elementAt(it)} - ${currencyList.values.elementAt(it)}"

                        Text(
                            text = title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { /*TODO*/ },
                            fontWeight = FontWeight.Medium,
                            color = focusColor
                        )
                    }
                }
            }
        }
    }
}