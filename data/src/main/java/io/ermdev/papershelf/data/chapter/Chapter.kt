package io.ermdev.papershelf.data.chapter

import io.ermdev.papershelf.data.book.Book
import io.ermdev.papershelf.data.page.Page
import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "tbl_chapter")
class Chapter(@Id
              @Column(name = "_id")
              var id: String = "",

              @Column(name = "_name")
              var name: String = "",

              @Column(name = "_level")
              var level: Double = 0.0,

              @Column(name = "upload_date")
              var uploadDate: Timestamp = Timestamp(System.currentTimeMillis()),

              @OneToMany(mappedBy = "chapter", cascade = [CascadeType.REMOVE])
              var pages: MutableSet<Page> = HashSet(),

              @ManyToOne
              @JoinColumn(name = "book_id")
              var book: Book = Book())