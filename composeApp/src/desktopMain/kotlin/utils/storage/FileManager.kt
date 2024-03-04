package utils.storage

import java.io.File

class FileManager : IFileManager {
    override val bpmDirectory: File by lazy {
        File("bpm").apply {
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