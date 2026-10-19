package br.com.soulmove.controller;

import br.com.soulmove.service.RecargaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recarga")
public class RecargaResource {
    private final RecargaService service;

    public RecargaResource(RecargaService service) {
        this.service = service;
    }

    @GetMapping("/verificar-codigo")
    public ResponseEntity<?> verificarCodigo(@RequestParam("codigo") String codigo){
        return ResponseEntity.ok(service.verificarCodigo(codigo));
    }

}
