package com.banco.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@jakarta.persistence.Entity
@Table(name = "entities")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Entity implements UserDetails {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private Long id;


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String surname;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Date birthday;

    @JsonIgnore
    @Column
    private String taxId;

    @JsonIgnore
    @Column
    private Date nationalIdExpiration;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String address;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String addressAdditionalInfo;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String postalCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String addressTown;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String addressCity;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String addressCountry;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private EntityGender gender;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String nationality;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String birthCity;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private EntityType type;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Date creationDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Boolean employee;

    @JsonIgnore
    @Column
    private String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String phoneNumber;

    @JsonIgnore
    @Column
    private Boolean locked;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String email;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Boolean emailConfirmed;

    @JsonIgnore
    @Column
    private String emailConfirmationCode;

    @JsonIgnore
    @Column
    private Timestamp emailConfirmationCodeExpiration;

    @JsonIgnore
    @Column
    private Integer score;

    @JsonIgnore
    @Column
    private String sign;

    @JsonIgnore
    @Column
    private Boolean signActivated;

    @JsonIgnore
    @Column
    private String emailChangeCode;

    @JsonIgnore
    @Column
    private String passwordChangeCode;

    @JsonIgnore
    @Column
    private String transactionApproveCode;

    @JsonIgnore
    @Column
    private Short loginAttempts;

    @JsonIgnore
    @Column
    private Date lastAttempt;

    @JsonIgnore
    @Column
    private EntityDebtType debtType; //TODO implement class EntityDebtType

    @JsonIgnore
    @Column
    private Date settingUpDate;

    @JsonIgnore
    @Column
    private String createdIpAddress;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getUsername() {
        return taxId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        if (!Objects.equals(id, entity.id)) return false;
        if (!Objects.equals(address, entity.address)) return false;
        if (!Objects.equals(addressCountry, entity.addressCountry)) return false;
        if (!Objects.equals(addressCity, entity.addressCity)) return false;
        if (!Objects.equals(postalCode, entity.postalCode)) return false;
        if (!Objects.equals(gender, entity.gender)) return false;
        if (!Objects.equals(nationality, entity.nationality)) return false;
        if (!Objects.equals(birthCity, entity.birthCity)) return false;
        if (!Objects.equals(birthday, entity.birthday)) return false;
        if (!Objects.equals(email, entity.email)) return false;
        if (!Objects.equals(type, entity.type)) return false;
        if (!Objects.equals(phoneNumber, entity.phoneNumber)) return false;
        if (!Objects.equals(name, entity.name)) return false;
        if (!Objects.equals(surname, entity.surname)) return false;
        if (!Objects.equals(password, entity.password)) return false;
        if (!Objects.equals(locked, entity.locked)) return false;
        if (!Objects.equals(emailConfirmed, entity.emailConfirmed))
            return false;
        if (!Objects.equals(emailConfirmationCode, entity.emailConfirmationCode))
            return false;
        if (!Objects.equals(emailConfirmationCodeExpiration, entity.emailConfirmationCodeExpiration))
            return false;
        if (!Objects.equals(taxId, entity.taxId)) return false;
        if (!Objects.equals(sign, entity.sign)) return false;
        if (!Objects.equals(signActivated, entity.signActivated)) return false;
        if (!Objects.equals(emailChangeCode, entity.emailChangeCode))
            return false;
        if (!Objects.equals(passwordChangeCode, entity.passwordChangeCode))
            return false;
        return Objects.equals(transactionApproveCode, entity.transactionApproveCode);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (taxId != null ? taxId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (locked != null ? locked.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (loginAttempts != null ? loginAttempts.hashCode() : 0);
        result = 31 * result + (emailConfirmed != null ? emailConfirmed.hashCode() : 0);
        result = 31 * result + (emailConfirmationCode != null ? emailConfirmationCode.hashCode() : 0);
        result = 31 * result + (emailConfirmationCodeExpiration != null ? emailConfirmationCodeExpiration.hashCode() : 0);
        result = 31 * result + (sign != null ? sign.hashCode() : 0);
        result = 31 * result + (signActivated != null ? signActivated.hashCode() : 0);
        result = 31 * result + (emailChangeCode != null ? emailChangeCode.hashCode() : 0);
        result = 31 * result + (passwordChangeCode != null ? passwordChangeCode.hashCode() : 0);
        result = 31 * result + (transactionApproveCode != null ? transactionApproveCode.hashCode() : 0);
        result = 31 * result + (debtType != null ? debtType.hashCode() : 0);
        return result;
    }
}
