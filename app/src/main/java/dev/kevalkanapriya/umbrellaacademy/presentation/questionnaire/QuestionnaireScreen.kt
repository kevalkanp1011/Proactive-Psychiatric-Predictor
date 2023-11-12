package dev.kevalkanapriya.umbrellaacademy.presentation.questionnaire

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import dev.kevalkanapriya.umbrellaacademy.domain.model.QnA
import dev.kevalkanapriya.umbrellaacademy.presentation.home.HomeViewModel
import dev.kevalkanapriya.util.CardStack
import dev.kevalkanapriya.util.Item
import dev.kevalkanapriya.util.StoreDataToCSV
import kotlinx.serialization.json.Json
import org.koin.androidx.compose.koinViewModel
import java.io.FileInputStream
import java.io.FileNotFoundException

@Preview
@Composable
fun QuestionnaireScreen(

) {

    val homeViewModel: HomeViewModel = koinViewModel()
    val context = LocalContext.current

    // Read existing data from the file
    val filePath = "${context.filesDir}/reminder/1.json"
    val existingData = try {
        FileInputStream(filePath).bufferedReader().use { it.readText() }
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        ""
    }
    val data = Json.decodeFromString<MutableList<QnA>>(existingData)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (!homeViewModel.isEmptyCard.value) {
            CardStack(
                items = data,
                onEmptyStack = {
                    homeViewModel.setEmptyCardState()
                },
                onSwipeRight = { item ->
                    //store item in csv file
                    StoreDataToCSV(
                        listOf(listOf("msg", item.question, "4")),
                        context,
                        "qna_data.csv"
                    )
                },
                onSwipeLeft = { item ->
                    //store item in csv file
                    StoreDataToCSV(
                        listOf(listOf("msg", item.question, "1")),
                        context,
                        "qna_data.csv"
                    )
                },
                onClick2 = { item ->
                    //store item in csv file
                    StoreDataToCSV(
                        listOf(listOf("msg", item.question, "2")),
                        context,
                        "qna_data.csv"
                    )
                },
                onClick3 = { item ->
                    //store item in csv file
                    StoreDataToCSV(
                        listOf(listOf("msg", item.question, "3")),
                        context,
                        "qna_data.csv"
                    )
                }
            )
        } else {
            Text(text = "No more cards", fontWeight = FontWeight.Bold)
            //navigate to home screen
        }
    }
}
