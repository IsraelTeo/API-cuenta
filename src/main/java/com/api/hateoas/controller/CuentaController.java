package com.api.hateoas.controller;

import com.api.hateoas.model.Cuenta;
import com.api.hateoas.model.Monto;
import com.api.hateoas.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaServ;

    @GetMapping("/")
    public ResponseEntity<List<Cuenta>> listarCuentas(){
        List<Cuenta> cuentas = cuentaServ.listAll();
        if(cuentas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        for(Cuenta cuenta: cuentas){
            cuenta.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuenta.getId())).withSelfRel());
            cuenta.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuenta.getId(), null)).withRel("depositos"));
            cuenta.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));
        }
        CollectionModel<Cuenta> modelo = CollectionModel.of(cuentas);
        modelo.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withSelfRel());
        return new ResponseEntity<>(cuentas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> listarCuenta(@PathVariable Integer id){
        try {
            Cuenta cuenta = cuentaServ.get(id);
            cuenta.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuenta.getId())).withSelfRel());
            cuenta.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuenta.getId(), null)).withRel("depositos"));
            cuenta.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));
            return new ResponseEntity<>(cuenta, HttpStatus.OK);
        }catch (Exception exception){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<Cuenta> guardarCuenta(@RequestBody Cuenta cuenta){
        Cuenta cuentaBD = cuentaServ.save(cuenta);
        cuentaBD.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaBD.getId())).withSelfRel());
        cuentaBD.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaBD.getId(), null)).withRel("depositos"));
        return ResponseEntity.created(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaBD.getId())).toUri()).body(cuentaBD);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<Cuenta> editarCuenta(@RequestBody Cuenta cuenta){
        Cuenta cuentaBD = cuentaServ.save(cuenta);
        cuentaBD.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuenta.getId())).withSelfRel());
        cuentaBD.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaBD.getId(), null)).withRel("depositos"));
        cuentaBD.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));
        return new ResponseEntity<>(cuentaBD, HttpStatus.OK);
    }

    //@PatchMapping: Actualizar algo en específico
    @PatchMapping("/{id}/deposito")
    public ResponseEntity<Cuenta> depositarDinero(@PathVariable Integer id, @RequestBody Monto monto){
        Cuenta cuentaBD = cuentaServ.depositar(monto.getMonto(), id);
        cuentaBD.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaBD.getId())).withSelfRel());
        cuentaBD.add(linkTo(methodOn(CuentaController.class).depositarDinero(cuentaBD.getId(), null)).withRel("depositos"));
        cuentaBD.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));
        return new ResponseEntity<>(cuentaBD, HttpStatus.OK);

    }

    @PatchMapping("/{id}/retiro")
    public ResponseEntity<Cuenta> retirarDinero(@PathVariable Integer id, @RequestBody Monto monto){
        Cuenta cuentaBD = cuentaServ.retirar(monto.getMonto(), id);
        cuentaBD.add(linkTo(methodOn(CuentaController.class).listarCuenta(cuentaBD.getId())).withSelfRel());
        cuentaBD.add(linkTo(methodOn(CuentaController.class).retirarDinero(cuentaBD.getId(), null)).withRel("retiros"));
        cuentaBD.add(linkTo(methodOn(CuentaController.class).listarCuentas()).withRel(IanaLinkRelations.COLLECTION));
        return new ResponseEntity<>(cuentaBD, HttpStatus.OK);

    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarCuenta(@PathVariable Integer id){
        try{
            cuentaServ.delete(id);
            return ResponseEntity.noContent().build();
        }catch (Exception exception){
            return ResponseEntity.notFound().build();

        }

    }

}
