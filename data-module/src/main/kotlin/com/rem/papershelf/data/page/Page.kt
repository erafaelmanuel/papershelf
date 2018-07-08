package com.rem.papershelf.data.page

import com.rem.papershelf.data.chapter.Chapter
import javax.persistence.*

@Entity
@Table(name = "tbl_page")
class Page(@Id
           @Column(name = "_id")
           var id: String = "",

           @Column(name = "_order")
           var order: Short = 0,

           @Column(name = "image_url")
           var imageUrl: String = "",

           @ManyToOne
           @JoinColumn(name = "chapter_id", nullable = false)
           var chapter: Chapter = Chapter())