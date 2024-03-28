package utils.storage

import data.NewDatabase
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

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

    fun saveDatabase(database: NewDatabase) {
        val byteArray = serializeDatabase(database)

        saveFile(database.name, byteArray)
    }

    fun loadDatabase(fileName: String): NewDatabase {
        val byteArray = readFile(fileName)

        return deserializeDatabase(byteArray)
    }

    fun deleteDatabase(database: NewDatabase) {
        deleteFile("${database.name}.bpm")
    }

    private fun serializeDatabase(database: NewDatabase) = ByteArrayOutputStream().use {
        ObjectOutputStream(it).use { stream ->
            stream.writeObject(database)
            stream.flush()
            it.toByteArray()
        }
    }

    private fun deserializeDatabase(byteArray: ByteArray) = ByteArrayInputStream(byteArray).use {
        ObjectInputStream(it).use { stream -> stream.readObject() }
    }.cast<NewDatabase>()

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

    inline fun <reified T : Any> Any.cast(): T = this as T
}