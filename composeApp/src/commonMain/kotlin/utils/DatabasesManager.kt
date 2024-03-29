package utils

import data.Database
import utils.storage.FileManager

object DatabasesManager {
    var databases: List<Database> = emptyList()
        private set

    /**
     * Initializes the list of databases
     */
    fun initialize() {
        val fileManager = ServiceLocator.getService<FileManager>("FileManager")

        databases = fileManager.listFiles().map { Database(it.name) }
        databases += listOf("database1", "database2", "database3").map { Database(it) }
    }
}
