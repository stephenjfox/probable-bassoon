package com.fox.library.k

import org.junit.jupiter.api.Assertions
import org.junit.platform.commons.annotation.Testable
import java.time.Year
import org.junit.jupiter.api.Test as test

@Testable
class LibraryTest {

    @test
    fun isAvailable() = with(StubLibrary()) {
        val hp1 = books("Harry").first()
        Assertions.assertTrue(isAvailable(hp1))
    }

    @test
    fun isNotAvailableDueToStock() = with(StubLibrary()) {
        Assertions.assertFalse({ isAvailable(fakeBook) },
                "shouldn't have a book that isn't in the library")
    }

    @test
    fun isNotAvailableDueToCheckOut() = StubLibrary().run {
        val harryPotter = books("Harry").first()
        checkOut(harryPotter)
        Assertions.assertFalse(isAvailable(harryPotter))
    }

    @test
    fun isAvailableDueToCheckIn() = testLibraryWithFirstCheckedOut().run {
        val book = books("Harry").first()
        println("[TEST] First Harry Potter found: $book")
        Assertions.assertFalse(isAvailable(book))

        checkIn(book)
        Assertions.assertTrue(isAvailable(book))
    }

    /**
     * this test is only useful because we know the library is full Harry Potter
     */
    @test
    fun bookSearchCount() {
        val library = StubLibrary()

        Assertions.assertTrue(library.books(startingWith = "Harry").count() <= 10)
    }

    ////////////////////////////////////////////////////////////////////////////////
    // HELPERS
    ////////////////////////////////////////////////////////////////////////////////

    private fun testLibraryWithFirstCheckedOut() = StubLibrary().apply {
        val firstHarryPotter = this.books("Harry").first()
        this.checkOut(firstHarryPotter)
        println("[SETUP] First Harry Potter found: $firstHarryPotter")
    }

    private val fakeBook: Book = Book(
            title = "not real",
            author = "Anonymous",
            publicationYear = Year.of(1)
    )

    inner class StubLibrary : Library {
        private val _books = mutableMapOf<Book, Boolean>(
                *(Book(
                        "Harry Potter #1",
                        author = "J.K. Rowling",
                        publicationYear = Year.now()
                ).let { harryPotter ->
                    Array(7, { harryPotter.copy(title = "Harry Potter #${it + 1}") to true })
                })
        )

        override fun checkOut(book: Book) {
            if (_books[book] == true) _books[book] = false
        }

        override fun checkIn(book: Book) {
            _books[book] = true
        }

        override fun books(startingWith: String, count: Int): Iterable<Book> = _books.keys.filter { it.title.startsWith(startingWith) }

        override fun isAvailable(book: Book) = _books[book] ?: false
    }
}