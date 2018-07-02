package io.ermdev.papershelf.data.entity

import javax.persistence.*

@Entity
@Table(name = "tbl_page")
class Page(@Id
           @Column(name = "_id")
           var id: String = "",

           @Column(name = "_order")
           var order: Short = 0,

           @Column(name = "_image")
           var image: String = "",

           @ManyToOne
           @JoinColumn(name = "chapter_id", nullable = false)
           var chapter: Chapter = Chapter())