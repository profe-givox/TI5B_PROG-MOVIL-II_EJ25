package com.example.inventory.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import com.example.inventory.InventoryApplication
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
    /*
     * The calls to addURI() go here for all the content URI patterns that the provider
     * recognizes. For this snippet, only the calls for table 3 are shown.
     */

    /*
     * Sets the integer value for multiple rows in table 3 to 1. Notice that no wildcard is used
     * in the path.
     */
    addURI("com.example.inventory.provider", "inventory", 1)

    /*
     * Sets the code for a single row to 2. In this case, the # wildcard is
     * used. content://com.example.app.provider/table3/3 matches, but
     * content://com.example.app.provider/table3 doesn't.
     */
    addURI("com.example.inventory.provider", "inventory/#", 2)

    addURI("com.example.inventory.provider", "inventory/*", 3)
}


class MiProveedorInventario  : ContentProvider() {

    override fun onCreate(): Boolean {
        return true
    }

    // Define un CoroutineScope para manejar las coroutines
    private val scope = CoroutineScope(Dispatchers.IO)

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor = MatrixCursor(arrayOf("id", "name", "price", "quantity"))
        val deferredResult = CompletableDeferred<Cursor?>()
        when(sUriMatcher.match(uri)) {
            // Lanza una coroutine para recolectar los datos del Flow
            1 ->  scope.launch {
                try {
                    Log.d("xxx123", "EN el content")
                    val inventario =
                        (context as InventoryApplication).container.itemsRepository.getAllItemsStream()

                    val itemList = inventario.first()
                    itemList.forEach { item ->
                        Log.d("xxx123", item.toString())
                        cursor.addRow(arrayOf(item.id, item.name, item.price, item.quantity))
                    }
                    Log.d("xxx123", cursor.toString())
                    deferredResult.complete(cursor)
                } catch (e: Exception) {
                    Log.e("xxx123", "Error collecting inventory items", e)
                    deferredResult.completeExceptionally(e)
                }
            }
        }
        // Espera el resultado de la coroutine
        return runBlocking {
            deferredResult.await()
        }
    }
    override fun getType(p0: Uri): String? {
        return when(sUriMatcher.match(p0)){
            1 ->  "vnd.android.cursor.dir/vnd.com.example.inventory.provider.inventory"
            2 ->  "vnd.android.cursor.item/vnd.com.example.inventory.provider.inventory"
            3 ->  "vnd.android.cursor.dir/vnd.com.example.inventory.provider.inventory"
            else -> "vnd.android.cursor.dir/vnd.com.example.inventory.provider.inventory"
        }
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        return  null
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        return  0
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        return  0
    }
}