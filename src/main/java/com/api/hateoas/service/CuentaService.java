package com.api.hateoas.service;

import com.api.hateoas.exception.CuentaNotFoundException;
import com.api.hateoas.model.Cuenta;
import com.api.hateoas.repository.CuentaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CuentaService {
    @Autowired
    public CuentaRepository cuentaRepo;

    public List<Cuenta> listAll(){
        return cuentaRepo.findAll();
    }

    public Cuenta get(Integer id){
        return cuentaRepo.findById(id).get();
    }

    public Cuenta save(Cuenta cuenta){
        return cuentaRepo.save(cuenta);
    }

    public void delete (Integer id) throws CuentaNotFoundException {
        if(!cuentaRepo.existsById(id)){
            throw new CuentaNotFoundException("Cuenta no encontrada con el id: "+id);
        }
        cuentaRepo.deleteById(id);
    }

    public Cuenta depositar(float monto, Integer id){
        cuentaRepo.actualizarMonto(monto, id);
        return cuentaRepo.findById(id).get();
    }

    public Cuenta retirar(float monto, Integer id){
        cuentaRepo.actualizarMonto(-monto, id);
        return cuentaRepo.findById(id).get();
    }
}
