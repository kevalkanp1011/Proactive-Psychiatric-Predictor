package dev.kevalkanapriya.util

import android.content.Context
import android.os.Environment
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter

fun StoreDataToCSV(data: List<List<String>>, context: Context, fileName: String) {

    val filePath = "${context.filesDir}/$fileName"

    try {
        val reader = CSVReader(FileReader(context.filesDir))
        val existingData = reader.readAll()
        reader.close()

        println(existingData.toString())

        val updatedData = existingData.map { it.toList() }.toMutableList()
        val newCsvData = data
        updatedData.addAll(newCsvData)
        println(updatedData.toString())

        // Create a CSV writer object.
        val csvWriter = CSVWriter(FileWriter(filePath))

        // Write the header row to the CSV file.
        csvWriter.writeNext(updatedData[0].toTypedArray())

        // Iterate over the data and write each row to the CSV file.
        updatedData.drop(1).forEach { csvWriter.writeNext(it.toTypedArray()) }

        // Close the CSV writer object.
        csvWriter.close()
    } catch (e: FileNotFoundException) {
        // Create a CSV writer object.
        val csvWriter = CSVWriter(FileWriter(filePath))
        println(data.toString())

        // Write the header row to the CSV file.
        csvWriter.writeNext(data[0].toTypedArray())

        // Iterate over the data and write each row to the CSV file.
        data.drop(1).forEach { csvWriter.writeNext(it.toTypedArray()) }

        // Close the CSV writer object.
        csvWriter.close()
    }

}