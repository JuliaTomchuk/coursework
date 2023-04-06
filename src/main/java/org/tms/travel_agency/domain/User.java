package org.tms.travel_agency.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="accounts")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private UUID id;
    private String firstName;
    private String secondName;
    private String patronymic;
    @NaturalId
    private String username;
    private String password;
    private String passwordNumber;
    private Integer age;
    private Role role;




    public User(String firstName, String secondName, String username, String password, String passwordNumber, Integer age, String patronymic, Role role) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.username = username;
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
        return getUsername().equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return List.of( new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
