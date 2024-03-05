package utils.storage

import java.io.File

class DesktopFileManager : FileManager() {
    override val bpmDirectory: File by lazy {
        File("bpm").apply {
            if (!exists()) {
                mkdir()
            }
        }
    }
}