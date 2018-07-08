package com.rem.papershelf.data.user

import com.rem.papershelf.data.role.Role
import javax.persistence.*

@Entity
@Table(name = "tbl_user")
class User(@Id
           @Column(name = "_id")
           val id: String = "",

           @Column(name = "_username")
           val username: String = "",

           @Column(name = "_password")
           val password: String = "",

           @ManyToMany
           @JoinTable(name = "tbl_user_role", joinColumns = [JoinColumn(name = "user_id")],
                   inverseJoinColumns = [JoinColumn(name = "role_id")])
           val roles: MutableList<Role> = ArrayList())