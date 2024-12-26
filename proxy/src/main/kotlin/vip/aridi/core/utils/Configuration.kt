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
        try {
            if (!file.parentFile.exists() && !file.parentFile.mkdirs()) {
                plugin.logger.log(Level.SEVERE, "No se pudo crear el directorio: ${file.parentFile.path}")
            }

            if (!file.exists()) {
                val resourceStream = plugin.getResourceAsStream(file.name)

                if (resourceStream == null) {

                    if (!file.createNewFile()) {
                        plugin.logger.log(Level.SEVERE, "No se pudo crear el archivo: ${file.path}")
                    }
                } else {
                    Files.copy(resourceStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING)
                    plugin.logger.log(Level.INFO, "Archivo predeterminado copiado: ${file.path}")
                }
            }
        } catch (e: IOException) {
            plugin.logger.log(Level.SEVERE, "Error al crear el archivo: ${file.name}", e)
        } catch (e: Exception) {
            plugin.logger.log(Level.SEVERE, "Error desconocido al crear el archivo: ${file.name}", e)
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