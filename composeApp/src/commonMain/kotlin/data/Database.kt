package data

import utils.security.BPMCipher
import utils.storage.FileManager
import javax.crypto.Cipher

class Database(val fileName: String, private val fileManager: FileManager) {
    val groups = mutableListOf<Group>()

    var isOpen: Boolean = false
        private set

    fun save() {

    }

    fun open(password: String) {
        val fileContent = fileManager.readFile(fileName)
        val decryptedContent = BPMCipher.decrypt(fileContent, password)

        isOpen = true
    }

    companion object {
        var databases: List<Database> = emptyList()
            private set

        /**
         * Update the list of databases without discarding the current instances (if they are valid), adding new ones
         * and removing the ones that are not in the file manager anymore
         * @param fileManager The file manager to use
         */
        fun updateDatabases(fileManager: FileManager) {
            val currentFilePaths = fileManager.listFiles().map { it.name }

            fileManager.listFiles().forEach { file ->
                if (databases.none { it.fileName == file.name }) {
                    databases = databases + Database(file.name, fileManager)
                }
            }

            databases = databases.filter { currentFilePaths.contains(it.fileName) }
        }

        fun newDatabase(fileName: String, password: String, fileManager: FileManager): Database {
            val encryptedContent = BPMCipher.encrypt("", password)
            fileManager.saveFile(fileName, encryptedContent)

            return Database("$fileName.bmp", fileManager)
        }
    }
}