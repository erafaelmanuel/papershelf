package io.ermdev.papershelf.data.entity

import javax.persistence.*

@Entity
@Table(name = "tbl_book")
class Book(@Id
           @Column(name = "_id")
           var id: String = "",

           @Column(name = "_title")
           var title: String = "",

           @ManyToOne(cascade = [CascadeType.PERSIST])
           @JoinColumn(name = "author_id")
           var author: Author? = null,

           @ManyToMany(cascade = [CascadeType.ALL])
           @JoinTable(name = "tbl_book_genre", joinColumns = [JoinColumn(name = "book_id")],
                   inverseJoinColumns = [JoinColumn(name = "genre_id")])
           var genres: MutableSet<Genre> = HashSet())