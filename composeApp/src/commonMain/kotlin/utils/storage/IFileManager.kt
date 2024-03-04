package utils.storage

import java.io.File

interface IFileManager {
    val bpmDirectory: File
    fun saveFile(fileName: String, content: String)
    fun readFile(fileName: String): String
    fun listFiles(): List<File>
    fun deleteFile(fileName: String)
}