package utils

import data.NewDatabase
import utils.storage.FileManager

object DatabasesManager {
    var databases: List<NewDatabase> = emptyList()
        private set

    /**
     * Initializes the list of databases
     */
    fun initialize() {
        val fileManager = ServiceLocator.getService<FileManager>("FileManager")

        databases = fileManager.listFiles().map { NewDatabase(it.name) }
        databases += listOf("database1", "database2", "database3").map { NewDatabase(it) }
    }
}
