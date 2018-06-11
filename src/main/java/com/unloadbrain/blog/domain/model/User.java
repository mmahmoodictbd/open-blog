package com.unloadbrain.blog.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@Entity
public class User extends BaseEntity {

    @Column(unique = true)
    @NotNull
    @Email
    private String email;

    @Column(unique = true)
    @NotNull
    @Length(min = 1, max = 255)
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String displayName;

    @Column
    private boolean enabled;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Role> roles = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="user_additional_properties", joinColumns=@JoinColumn(name="user_id"))
    @MapKeyColumn(name="property_key")
    @Column(name="property_value")
    private Map<String, String> additionalProperties = new HashMap<>();
}
