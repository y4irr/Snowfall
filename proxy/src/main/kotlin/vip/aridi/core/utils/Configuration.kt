package vip.aridi.core.utils

import net.md_5.bungee.api.plugin.Plugin
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.logging.Level

/*
 * This project can't be redistributed without
 * authorization of the developer
 *
 * Project @ Snowfall
 * @author Yair © 2024
 * Date: 10 - nov
 */

class Configuration(
    private val plugin: Plugin,
    fileName: String,
    extension: String = "yml",
    folder: String = plugin.dataFolder.path
) {

    private val file: File = File(folder, "$fileName.$extension")
    var config: Map<String, Any> = createFile()

    private fun createFile(): Map<String, Any> {
        if (!file.exists()) {
            file.parentFile.mkdirs()
            val resourceStream = plugin.getResourceAsStream(file.name)
            if (resourceStream == null) {
                try {
                    file.createNewFile()
                } catch (e: IOException) {
                    plugin.logger.log(Level.SEVERE, "Failed to create new file ${file.name}", e)
                }
            } else {
                try {
                    Files.copy(resourceStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING)
                } catch (e: IOException) {
                    plugin.logger.log(Level.SEVERE, "Failed to save default file ${file.name}", e)
                }
            }
        }
        return loadConfig()
    }

    private fun loadConfig(): Map<String, Any> {
        return try {
            val yaml = org.yaml.snakeyaml.Yaml()
            file.inputStream().use { yaml.load(it) as? Map<String, Any> ?: emptyMap() }
        } catch (e: Exception) {
            plugin.logger.log(Level.SEVERE, "Failed to load configuration file ${file.name}", e)
            emptyMap()
        }
    }

    fun save() {
        try {
            val yaml = org.yaml.snakeyaml.Yaml()
            file.writer().use { writer ->
                yaml.dump(config, writer)
            }
        } catch (e: IOException) {
            plugin.logger.log(Level.SEVERE, "Could not save config file ${file.name}", e)
        }
    }

    fun reload() {
        config = loadConfig()
    }
}