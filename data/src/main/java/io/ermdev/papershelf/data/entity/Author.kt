package io.ermdev.papershelf.data.entity

import javax.persistence.*

@Entity
@Table(name = "tbl_author")
class Author(@Id
             @Column(name = "id")
             var id: String = "",

             @Column(name = "name")
             var name: String = "",

             @OneToMany(mappedBy = "author", cascade = [CascadeType.REMOVE])
             var books: MutableSet<Book> = HashSet())