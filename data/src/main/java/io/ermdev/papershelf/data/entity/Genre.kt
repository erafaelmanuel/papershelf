package io.ermdev.papershelf.data.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "tbl_genre")
class Genre(@Id
            @Column(name = "_id")
            var id: String = "",

            @Column(name = "_name")
            var name: String = "",

            @Column(name = "_description")
            var description: String = "")