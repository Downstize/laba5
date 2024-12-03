package com.example.laba5.utils

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream

object FileUtils {

    fun saveFileToExternalStorage(context: Context, content: String, fileName: String): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
                }
                val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
                uri?.let {
                    val outputStream: OutputStream? = resolver.openOutputStream(it)
                    outputStream?.use { stream ->
                        stream.write(content.toByteArray())
                    }
                }
            } else {
                val externalDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                val file = File(externalDir, fileName)
                file.writeText(content)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun isFileExists(context: Context, fileName: String): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ? AND ${MediaStore.MediaColumns.RELATIVE_PATH} = ?"
                val selectionArgs = arrayOf(fileName, "${Environment.DIRECTORY_DOCUMENTS}/")

                resolver.query(
                    MediaStore.Files.getContentUri("external"),
                    arrayOf(MediaStore.MediaColumns._ID),
                    selection,
                    selectionArgs,
                    null
                )?.use { cursor ->
                    cursor.moveToFirst()
                } ?: false
            } else {
                val externalDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                val file = File(externalDir, fileName)
                file.exists()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun deleteFileFromExternalStorage(context: Context, fileName: String): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ? AND ${MediaStore.MediaColumns.RELATIVE_PATH} = ?"
                val selectionArgs = arrayOf(fileName, "${Environment.DIRECTORY_DOCUMENTS}/")

                val rowsDeleted = resolver.delete(
                    MediaStore.Files.getContentUri("external"),
                    selection,
                    selectionArgs
                )
                rowsDeleted > 0
            } else {
                val externalDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                val file = File(externalDir, fileName)
                file.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun backupFile(context: Context, fileName: String): Boolean {
        return try {
            val externalDir = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver.query(
                    MediaStore.Files.getContentUri("external"),
                    arrayOf(MediaStore.MediaColumns._ID, MediaStore.MediaColumns.RELATIVE_PATH),
                    "${MediaStore.MediaColumns.DISPLAY_NAME} = ? AND ${MediaStore.MediaColumns.RELATIVE_PATH} = ?",
                    arrayOf(fileName, "${Environment.DIRECTORY_DOCUMENTS}/"),
                    null
                )?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                        MediaStore.Files.getContentUri("external").buildUpon().appendPath(id.toString()).build()
                    } else null
                }
            } else {
                val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
                if (file.exists()) file else null
            }

            externalDir?.let {
                val inputStream = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    context.contentResolver.openInputStream(it as android.net.Uri)
                } else {
                    FileInputStream(it as File)
                }
                val backupFile = File(context.filesDir, fileName)
                inputStream?.use { input ->
                    backupFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                true
            } ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun restoreFileFromBackup(context: Context, fileName: String): Boolean {
        return try {
            val backupFile = File(context.filesDir, fileName)

            if (!backupFile.exists()) {
                return false
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val resolver = context.contentResolver
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                    put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
                }
                val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
                uri?.let {
                    resolver.openOutputStream(it)?.use { outputStream ->
                        outputStream.write(backupFile.readText().toByteArray())
                    }
                    true
                } ?: false
            } else {
                val externalDir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
                externalDir.writeText(backupFile.readText())
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}