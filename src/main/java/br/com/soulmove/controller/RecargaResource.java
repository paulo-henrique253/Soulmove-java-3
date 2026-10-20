package br.com.soulmove.controller;

import br.com.soulmove.controller.Dto.RecargaRequestDto;
import br.com.soulmove.service.RecargaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recarga")
public class RecargaResource {
    private final RecargaService service;

    public RecargaResource(RecargaService service) {
        this.service = service;
    }

    @PostMapping("/pix")
    public ResponseEntity<?> recarregar(@RequestBody RecargaRequestDto dto){
        service.processarRecargaPix(dto.codigo(), dto.usuarioId());
        return ResponseEntity.ok("recargaEfetuada");
    }

}
