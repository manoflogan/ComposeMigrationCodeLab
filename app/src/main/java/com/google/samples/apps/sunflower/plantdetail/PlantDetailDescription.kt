/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.sunflower.plantdetail

import android.content.Context
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.material.composethemeadapter.MdcTheme
import com.google.samples.apps.sunflower.R
import com.google.samples.apps.sunflower.data.Plant
import com.google.samples.apps.sunflower.viewmodels.PlantDetailViewModel

@Composable
fun PlantDetailDescription(viewModel: PlantDetailViewModel = viewModel()) {
    val plantName by viewModel.plant.observeAsState()
    Surface {

        plantName?.let {
            Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.margin_normal))) {
                PlantDetailName(it.name)
                PlantWaterHeader(plant = it)
                PlantDescription(it.description)
            }

        }
    }
}

@Composable
fun PlantDetailName(name: String, modifier: Modifier = Modifier) {
    Text(text = name, style = MaterialTheme.typography.h5, modifier = modifier
        .wrapContentHeight()
        .fillMaxWidth()
        .padding(horizontal = dimensionResource(id = R.dimen.margin_small))
        .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun PlantWaterHeader(modifier: Modifier = Modifier, plant: Plant) {
    Column(modifier = modifier.fillMaxWidth()) {
        val paddingModifier = modifier
            .padding(horizontal = dimensionResource(R.dimen.margin_small))
            .align(Alignment.CenterHorizontally)

        Text(
            text = stringResource(R.string.watering_needs_prefix),
            modifier = paddingModifier.padding(top = dimensionResource(R.dimen.margin_normal)),
            color = MaterialTheme.colors.primaryVariant,
            fontWeight = FontWeight.Bold
        )
        val waterIntervalText = LocalContext.current.resources.getQuantityString(R.plurals.watering_needs_suffix, plant.wateringInterval, plant.wateringInterval)
        Text(text = waterIntervalText, paddingModifier.padding(bottom = dimensionResource(R.dimen.margin_small)))
    }
}

@Composable
fun PlantDescription(description: String) {
    // Recompose everytime the value changes.
    val htmlFormattedText = remember(description) {
        HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }
    AndroidView(
        factory = { context: Context ->
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        update = {
            it.text = htmlFormattedText
        }
    )
}

@Preview
@Composable
fun PlantDetailDescriptonPreview() {
    MdcTheme() {
        PlantDetailDescription()
    }
}

@Preview
@Composable
fun PlantNamePreview() {
    MdcTheme() {
        PlantDetailName("Apple")
    }
}
