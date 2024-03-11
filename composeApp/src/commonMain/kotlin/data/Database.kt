package data

import utils.security.BPMCipher
import utils.storage.FileManager
import javax.crypto.Cipher

class Database(val fileName: String, private val fileManager: FileManager) {
    val groups = mutableListOf<Group>()

    fun save() {

    }

    fun open(password: String) {
        val fileContent = fileManager.readFile(fileName)
        val decryptedContent = BPMCipher.decrypt(fileContent, password)
    }

    companion object {
        fun getDatabases(fileManager: FileManager): List<Database> = fileManager
            .listFiles().map { Database(it.name, fileManager) }

        fun newDatabase(fileName: String, password: String, fileManager: FileManager): Database {
            val encryptedContent = BPMCipher.encrypt("", password)
            fileManager.saveFile(fileName, encryptedContent)

            return Database("$fileName.bmp", fileManager)
        }
    }
}