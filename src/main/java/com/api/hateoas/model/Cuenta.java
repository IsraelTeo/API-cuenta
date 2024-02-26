package com.api.hateoas.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="cuentas")
//Clase RepresentationModel es de spring hateoas y provee de links para utilizar de retorno en nuestras Apis
public class Cuenta extends RepresentationModel<Cuenta> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, nullable = false, unique = false)
    private String numCuenta;

    private float monto;

    public Cuenta(Integer id, String numCuenta) {
        this.id = id;
        this.numCuenta = numCuenta;
    }

}
