package com.rem.papershelf.data.author

import com.rem.papershelf.data.book.Book
import javax.persistence.*

@Entity
@Table(name = "tbl_author")
class Author(@Id
             @Column(name = "_id")
             var id: String = "",

             @Column(name = "_name")
             var name: String = "",

             @ManyToMany(mappedBy = "authors")
             var books: MutableSet<Book> = HashSet())