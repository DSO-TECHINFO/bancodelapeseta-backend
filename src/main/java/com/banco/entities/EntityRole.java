package com.banco.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "roles", schema = "bancodelapeseta")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityRole {
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private Integer id;
    @JsonIgnore
    @Column
    private String roleName;
    @JsonIgnore
    @Column
    private String roleDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EntityRole entityRole = (EntityRole) o;

        if (!Objects.equals(id, entityRole.id)) return false;
        if (!Objects.equals(roleName, entityRole.roleName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (roleName != null ? roleName.hashCode() : 0);
        return result;
    }
}
