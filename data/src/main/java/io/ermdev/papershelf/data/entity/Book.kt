package io.ermdev.papershelf.data.entity

import javax.persistence.*

@Entity
@Table(name = "tbl_book")
class Book(@Id
           @Column(name = "id")
           var id: String = "",

           @Column(name = "title")
           var title: String = "",

           @ManyToOne(cascade = [CascadeType.PERSIST])
           @JoinColumn(name = "author_id")
           var author: Author? = null)