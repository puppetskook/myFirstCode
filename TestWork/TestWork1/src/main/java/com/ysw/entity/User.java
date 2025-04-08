package com.ysw.entity;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "m_user")
public class User {
    @Id
    @KeySql(useGeneratedKeys=true)
    private Long id;
    private String name;
    private String sex;
    private Integer age;
    private String password;
}
