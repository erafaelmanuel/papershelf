package com.rem.papershelf.data.role

import com.rem.papershelf.data.user.User
import javax.persistence.*

@Entity
@Table(name = "tbl_role")
class Role(@Id
           @Column(name = "_id")
           val id: String = "",

           @Column(name = "_name")
           val name: String = "",

           @ManyToMany(mappedBy = "roles")
           val users: MutableList<User> = ArrayList())