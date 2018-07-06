package io.ermdev.papershelf.data.genre

import io.ermdev.papershelf.data.book.Book
import javax.persistence.*

@Entity
@Table(name = "tbl_genre")
class Genre(@Id
            @Column(name = "_id")
            var id: String = "",

            @Column(name = "_name")
            var name: String = "",

            @Column(name = "_description")
            var description: String = "",

            @ManyToMany(mappedBy = "genres")
            @OrderBy("title")
            var books: MutableList<Book> = ArrayList())