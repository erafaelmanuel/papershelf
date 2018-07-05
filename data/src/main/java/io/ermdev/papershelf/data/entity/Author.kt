package io.ermdev.papershelf.data.entity

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