package com.example.medimapireland.helpers

import android.content.Context
import java.io.File

// Check whether the JSON file exists
fun exists(context: Context, fileName: String): Boolean {
    return File(context.filesDir, fileName).exists()
}

// Write JSON data to internal storage
fun write(context: Context, fileName: String, data: String) {
    val file = File(context.filesDir, fileName)
    file.writeText(data)
}

// Read JSON data from internal storage
fun read(context: Context, fileName: String): String {
    val file = File(context.filesDir, fileName)
    return file.readText()
}