package com.vadims.application.bpm.utils.storage

import android.content.Context
import utils.storage.IFileManager
import java.io.File

class FileManager(private val context: Context) : IFileManager {
    override val bpmDirectory: File by lazy {
        File(context.getExternalFilesDir(null), "bpm").apply {
            if (!exists()) {
                mkdir()
            }
        }
    }

    override fun saveFile(fileName: String, content: String) {
        TODO("Not yet implemented")
    }

    override fun readFile(fileName: String): String {
        TODO("Not yet implemented")
    }

    override fun listFiles(): List<File> {
        TODO("Not yet implemented")
    }

    override fun deleteFile(fileName: String) {
        TODO("Not yet implemented")
    }
}