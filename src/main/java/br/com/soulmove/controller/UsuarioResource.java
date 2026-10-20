package br.com.soulmove.controller;

import br.com.soulmove.controller.Dto.UsuarioDtoMapper;
import br.com.soulmove.controller.Dto.UsuarioRequestDto;
import br.com.soulmove.model.UsuarioSoulMove;
import br.com.soulmove.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioResource {

    private final UsuarioService service;

    @Autowired
    public UsuarioResource(UsuarioService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<?> cadastrar(@RequestBody UsuarioRequestDto dto){
        UsuarioSoulMove usuario = service.cadastrar(dto.nome(), dto.email(), dto.senha());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);

    }

    @GetMapping()
    public ResponseEntity<?> login(@RequestBody UsuarioRequestDto dto){
        UsuarioSoulMove usuario = service.logar(dto.email(), dto.senha());
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/titulo")
    public ResponseEntity<?> alterarTitulo(@RequestParam long usuarioId, @RequestParam long conquistaId){
        UsuarioSoulMove usuario = service.alterarTitulo(usuarioId, conquistaId);
        return ResponseEntity.status(HttpStatus.OK).body(UsuarioDtoMapper.toResponse(usuario));
    }



}
