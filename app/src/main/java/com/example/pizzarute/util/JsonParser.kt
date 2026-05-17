package com.example.pizzarute.util

import com.example.pizzarute.domain.model.Pizza
import org.json.JSONArray
import org.json.JSONException

object JsonParser {

    fun parseCatalog(raw: String): List<Pizza> {
        return try {
            val items = JSONArray(raw)
            buildList {
                for (index in 0 until items.length()) {
                    val item = items.getJSONObject(index)
                    val ingredients = if (item.has("ingredients")) {
                        val ingArray = item.getJSONArray("ingredients")
                        buildList {
                            for (i in 0 until ingArray.length()) {
                                add(ingArray.getString(i))
                            }
                        }
                    } else {
                        emptyList()
                    }

                    add(
                        Pizza(
                            id = item.optString("id", (index + 1).toString()),
                            name = item.optString("name", "Pizza"),
                            description = item.optString("description", "Descrição não informada"),
                            price = item.optDouble("price", 0.0),
                            ingredients = ingredients
                        )
                    )
                }
            }
        } catch (_: JSONException) {
            emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }
}
