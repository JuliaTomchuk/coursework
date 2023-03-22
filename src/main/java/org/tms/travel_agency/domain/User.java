package org.tms.travel_agency.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name="accounts")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Setter(AccessLevel.NONE)
    private Integer id;
    private String firstName;
    private String secondName;
    private String patronymic;
    @NaturalId
    private String login;
    private char[] password;
    private String passwordNumber;
    private Integer age;

    private Role role;


    public User(String firstName, String secondName, String login, char[] password, String passwordNumber, Integer age, String patronymic, Role role) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.login = login;
        this.password = password;
        this.passwordNumber = passwordNumber;
        this.age = age;
        this.patronymic=patronymic;
        this.role=role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return getLogin().equals(user.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLogin());
    }
}
