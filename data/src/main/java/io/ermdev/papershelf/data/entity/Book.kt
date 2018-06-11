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
           var author: Author? = null)