package com.fox.library.k

import java.time.Year

typealias Author = String

data class Book(
        val title: String,
        val author: Author,
        val publicationYear: Year
)


interface Library {
    fun checkOut(book: Book)
    fun checkIn(book: Book)
    fun books(startingWith: String, count: Int = 10): Iterable<Book>
    fun isAvailable(book: Book): Boolean
}
