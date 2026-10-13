package br.com.soulmove.controller;

import br.com.soulmove.controller.Dto.CarteiraDtoMapper;
import br.com.soulmove.controller.Dto.CarteiraRequestDto;
import br.com.soulmove.model.CarteiraUsuario;
import br.com.soulmove.service.CarteiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carteira")
public class CarteiraResource {

    private final CarteiraService service;

    @Autowired
    public CarteiraResource(CarteiraService service) {
        this.service = service;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> alterarSaldo(@PathVariable long id, @RequestBody CarteiraRequestDto dto){
        CarteiraUsuario carteira = service.buscarCarteira(id);
        service.alterarSaldo(carteira, dto.quantidade());

        return ResponseEntity.ok(carteira);

    }

}
