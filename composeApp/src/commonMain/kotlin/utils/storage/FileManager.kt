package utils.storage

import java.io.File
import java.io.FileOutputStream

abstract class FileManager {
    /**
     * Directory for bpm files
     */
    protected abstract val bpmDirectory: File

    /**
     * Save file to bpm directory
     * @param fileName name of the file
     * @param content content of the file
     */
    fun saveFile(fileName: String, content: ByteArray) {
        val file = File(bpmDirectory, "$fileName.bpm")
        FileOutputStream(file).use {
            it.write(content)
        }
    }

    /**
     * Read file from bpm directory
     * @param fileName name of the file
     * @return file content
     */
    fun readFile(fileName: String): ByteArray {
        val file = File(bpmDirectory, fileName)

        return file.readBytes()
    }

    /**
     * List all files in bpm directory
     * @return list of files
     */
    fun listFiles(): List<File> {
        return bpmDirectory.listFiles { _, name -> name.endsWith(".bpm") }.orEmpty().toList()
    }

    /**
     * Delete file from bpm directory
     * @param fileName name of the file
     */
    fun deleteFile(fileName: String) {
        File(bpmDirectory, fileName).delete()
    }
}