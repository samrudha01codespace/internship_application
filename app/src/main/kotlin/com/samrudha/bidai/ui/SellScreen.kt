package com.samrudha.bidai.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.samrudha.bidai.R
import com.samrudha.bidai.ui.authFieldColors
import com.samrudha.bidai.ui.components.CategoryItem
import com.samrudha.bidai.ui.components.SectionHeader
import com.samrudha.bidai.ui.data.SampleHomeData
import com.samrudha.bidai.ui.theme.bidaiTheme

private val SellBlue = Color(0xFF0668E1)
private val UploadBoxBlue = Color(0xFFC5D8FF)
private val FieldOutline = Color(0xFFD1D5DB)

@Composable
fun SellScreen(
    modifier: Modifier = Modifier,
    uiState: SellUiState = SellUiState(categories = SampleHomeData.categories),
    onBack: () -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    onUpload: (
        photos: List<Uri>,
        title: String,
        description: String,
        brand: String,
        productType: String,
        location: String,
        price: String,
        sellAsBusiness: Boolean
    ) -> Unit = { _, _, _, _, _, _, _, _ -> },
    onErrorShown: () -> Unit = {},
    onSuccessShown: () -> Unit = {}
) {
    var selectedPhotos by remember { mutableStateOf(listOf<Uri>()) }
    var sellAsBusiness by remember { mutableStateOf(true) }
    var brands by remember { mutableStateOf("") }
    var productType by remember { mutableStateOf("") }
    var adTitle by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val closeLabel = stringResource(R.string.error_close)

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(5)
    ) { uris ->
        if (uris.isNotEmpty()) {
            selectedPhotos = (selectedPhotos + uris).distinctBy { it.toString() }.take(5)
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = closeLabel,
                duration = SnackbarDuration.Long,
                withDismissAction = true
            )
            onErrorShown()
        }
    }

    fun resetForm() {
        selectedPhotos = emptyList()
        sellAsBusiness = true
        brands = ""
        productType = ""
        adTitle = ""
        description = ""
        price = ""
        location = ""
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SellTopBar(onBack = onBack)

            Column {
                SectionHeader(title = stringResource(R.string.sell_select_categories))
                Spacer(modifier = Modifier.height(8.dp))
                val categories = uiState.categories
                val categoryRows = ((categories.size + 3) / 4).coerceAtLeast(1)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((categoryRows * 96 + 8).dp)
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.chunked(4).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { category ->
                                CategoryItem(
                                    category = category,
                                    selected = category.id == uiState.selectedCategoryId,
                                    onClick = { onCategoryClick(category.id) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            repeat(4 - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            PhotoUploadBox(
                onSelectPhotos = {
                    photoPicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )

            if (selectedPhotos.isNotEmpty()) {
                PhotoGrid(
                    photos = selectedPhotos,
                    onDelete = { uri ->
                        selectedPhotos = selectedPhotos.filterNot { it == uri }
                    }
                )
            }

            SellForm(
                sellAsBusiness = sellAsBusiness,
                onSellAsBusinessChange = { sellAsBusiness = it },
                brands = brands,
                onBrandsChange = { brands = it },
                productType = productType,
                onProductTypeChange = { productType = it },
                adTitle = adTitle,
                onAdTitleChange = { adTitle = it },
                description = description,
                onDescriptionChange = { description = it },
                price = price,
                onPriceChange = { price = it },
                location = location,
                onLocationChange = { location = it },
                isSubmitting = uiState.isSubmitting,
                onUpload = {
                    onUpload(
                        selectedPhotos,
                        adTitle,
                        description,
                        brands,
                        productType,
                        location,
                        price,
                        sellAsBusiness
                    )
                }
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 72.dp)
        )

        if (uiState.showSuccess) {
            CongratulationsDialog(
                onClose = {
                    onSuccessShown()
                    resetForm()
                }
            )
        }
    }
}

@Composable
private fun CongratulationsDialog(
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        icon = {
            Image(
                painter = painterResource(R.drawable.ic_congrats_doc),
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                contentScale = ContentScale.Fit
            )
        },
        title = {
            Text(
                text = stringResource(R.string.sell_congrats_title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = stringResource(R.string.sell_congrats_body),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = onClose,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.error_close),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

@Composable
private fun SellTopBar(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .padding(horizontal = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(40.dp)
                .clip(CircleShape)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.ic_chevron_back),
                contentDescription = stringResource(R.string.sell_back),
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = stringResource(R.string.sell_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun PhotoUploadBox(
    onSelectPhotos: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(260.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(UploadBoxBlue),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            val inset = 1.dp.toPx()
            drawRoundRect(
                color = SellBlue,
                topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                size = Size(size.width - inset * 2, size.height - inset * 2),
                cornerRadius = CornerRadius(12.dp.toPx() - inset),
                style = Stroke(
                    width = 1.5.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(10.dp.toPx(), 8.dp.toPx())
                    )
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_image_plus),
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                contentScale = ContentScale.Fit
            )
            Button(
                onClick = onSelectPhotos,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SellBlue,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 28.dp, vertical = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.sell_select_or_take_photo),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = stringResource(R.string.sell_upload_photos_help),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun PhotoGrid(
    photos: List<Uri>,
    onDelete: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        photos.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowItems.forEach { uri ->
                    PhotoCard(
                        uri = uri,
                        onDelete = { onDelete(uri) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun PhotoCard(
    uri: Uri,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(180.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(SellBlue)
            .padding(4.dp)
    ) {
        Text(
            text = stringResource(R.string.sell_photos),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(6.dp))
        ) {
            AsyncImage(
                model = uri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable(onClick = onDelete),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = stringResource(R.string.sell_delete_photo),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun SellForm(
    sellAsBusiness: Boolean,
    onSellAsBusinessChange: (Boolean) -> Unit,
    brands: String,
    onBrandsChange: (String) -> Unit,
    productType: String,
    onProductTypeChange: (String) -> Unit,
    adTitle: String,
    onAdTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    price: String,
    onPriceChange: (String) -> Unit,
    location: String,
    onLocationChange: (String) -> Unit,
    isSubmitting: Boolean,
    onUpload: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        FormLabel(stringResource(R.string.sell_as))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SellAsOption(
                label = stringResource(R.string.sell_vendor_business),
                selected = sellAsBusiness,
                onClick = { onSellAsBusinessChange(true) },
                modifier = Modifier.weight(1f)
            )
            SellAsOption(
                label = stringResource(R.string.sell_individual),
                selected = !sellAsBusiness,
                onClick = { onSellAsBusinessChange(false) },
                modifier = Modifier.weight(1f)
            )
        }

        FormField(
            label = stringResource(R.string.sell_brands),
            value = brands,
            onValueChange = onBrandsChange,
            placeholder = stringResource(R.string.sell_brands_hint),
            enabled = !isSubmitting
        )
        FormField(
            label = stringResource(R.string.sell_product_type),
            value = productType,
            onValueChange = onProductTypeChange,
            placeholder = stringResource(R.string.sell_product_type_hint),
            enabled = !isSubmitting
        )
        FormField(
            label = stringResource(R.string.sell_ad_title),
            value = adTitle,
            onValueChange = onAdTitleChange,
            placeholder = stringResource(R.string.sell_ad_title_hint),
            enabled = !isSubmitting
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            FormLabel(stringResource(R.string.sell_description))
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                minLines = 4,
                maxLines = 6,
                enabled = !isSubmitting,
                shape = RoundedCornerShape(8.dp),
                colors = authFieldColors(),
                modifier = Modifier.fillMaxWidth()
            )
        }
        FormField(
            label = stringResource(R.string.sell_price),
            value = price,
            onValueChange = onPriceChange,
            placeholder = stringResource(R.string.sell_price_hint),
            keyboardType = KeyboardType.Number,
            enabled = !isSubmitting
        )
        FormField(
            label = stringResource(R.string.sell_location),
            value = location,
            onValueChange = onLocationChange,
            placeholder = stringResource(R.string.sell_location_hint),
            enabled = !isSubmitting
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onUpload,
                enabled = !isSubmitting,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SellBlue,
                    contentColor = Color.White,
                    disabledContainerColor = SellBlue.copy(alpha = 0.6f)
                ),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 10.dp)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = stringResource(R.string.sell_upload),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun FormLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FormLabel(label)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            singleLine = true,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(8.dp),
            colors = authFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SellAsOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = FieldOutline,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = SellBlue,
                unselectedColor = SellBlue
            ),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SellScreenPreview() {
    bidaiTheme {
        SellScreen(
            uiState = SellUiState(categories = SampleHomeData.categories)
        )
    }
}
