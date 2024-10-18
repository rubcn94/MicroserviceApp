package com.microcourse.accounts.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
// Esta anotación indica que `BaseEntity` no se mapeará como una tabla en la base de datos,
// pero sus propiedades se heredarán en las entidades que la extiendan.
@EntityListeners(AuditingEntityListener.class)
// `EntityListeners` con `AuditingEntityListener` permite la auditoría automática en esta entidad,
// es decir, registrar automáticamente quién creó o modificó un registro y cuándo lo hizo.
@Getter
@Setter
@ToString
// Anotaciones de Lombok para generar automáticamente los métodos getter, setter y el método `toString`.

public class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    // Este campo almacenará la fecha y hora en la que se creó el registro.
    // La anotación `@CreatedDate` permite que Spring Data JPA lo rellene automáticamente cuando se crea el registro.
    // `updatable = false` garantiza que este campo no se pueda modificar después de su creación.
    private LocalDateTime createdAt;

    @CreatedBy
    @Column(updatable = false)
    // Este campo almacena el usuario que creó el registro.
    // La anotación `@CreatedBy` es gestionada por el sistema de auditoría de Spring Data JPA.
    // `updatable = false` indica que este valor no debe cambiarse una vez establecido.
    private String createdBy;

    @LastModifiedDate
    @Column(insertable = false)
    // Este campo almacenará la fecha y hora en la que el registro fue modificado por última vez.
    // La anotación `@LastModifiedDate` permite que Spring Data JPA lo actualice automáticamente cada vez que se modifique el registro.
    // `insertable = false` asegura que este campo no se establezca al crear el registro, sino solo al modificarlo.
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Column(insertable = false)
    // Este campo almacena el usuario que modificó el registro por última vez.
    // La anotación `@LastModifiedBy` es gestionada automáticamente por Spring Data JPA durante la modificación.
    // `insertable = false` indica que este campo no se debe definir al crear el registro.
    private String updatedBy;
}
