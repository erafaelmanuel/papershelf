package io.ermdev.papershelf.data.entity

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "tbl_chapter")
class Chapter(@Id
              @Column(name = "_id")
              var id: String = "",

              @Column(name = "_name")
              var name: String = "",

              @Column(name = "_order")
              var order: Short = 0,

              @Column(name = "upload_date")
              var uploadDate: Timestamp = Timestamp(System.currentTimeMillis()),

              @OneToMany(mappedBy = "chapter", cascade = [CascadeType.REMOVE])
              @OrderBy("order")
              var pages: MutableList<Page> = ArrayList(),

              @ManyToOne
              @JoinColumn(name = "book_id")
              var book: Book = Book())