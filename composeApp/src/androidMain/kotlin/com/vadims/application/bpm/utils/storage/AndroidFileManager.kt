package com.vadims.application.bpm.utils.storage

import android.content.Context
import utils.storage.FileManager
import java.io.File

class AndroidFileManager(private val context: Context) : FileManager() {
    override val bpmDirectory: File by lazy {
        File(context.getExternalFilesDir(null), "bpm").apply {
            if (!exists()) {
                mkdir()
            }
        }
    }
}