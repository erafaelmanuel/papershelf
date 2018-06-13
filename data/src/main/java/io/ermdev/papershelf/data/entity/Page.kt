package io.ermdev.papershelf.data.entity

import javax.persistence.*

@Entity
@Table(name = "tbl_page")
class Page(@Id
           @Column(name = "_id")
           var id: String = "",

           @Column(name = "_number")
           var number: Short = 0,

           @Column(name = "_image")
           var image: String = "",

           @ManyToOne(cascade = [CascadeType.ALL])
           @JoinColumn(name = "chapter_id", nullable = false)
           var chapter: Chapter = Chapter())