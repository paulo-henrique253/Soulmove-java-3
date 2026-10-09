package br.com.soulmove.controller;


import br.com.soulmove.controller.Dto.ConquistaDtoMapper;
import br.com.soulmove.controller.Dto.ConquistaRequestDto;
import br.com.soulmove.controller.Dto.ConquistaResponseDto;
import br.com.soulmove.model.Conquista;
import br.com.soulmove.service.ConquistaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/conquista")
public class ConquistaResource {

    private ConquistaService service;

    @Autowired
    public ConquistaResource(ConquistaService service){this.service = service;}

    @GetMapping
    public ResponseEntity<?> buscarConquistas(){
        List<ConquistaResponseDto> conquistaDtos = ConquistaDtoMapper.toResponse(service.buscarConquistas());
        return ResponseEntity.ok(conquistaDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable long id){
        ConquistaResponseDto conquistaDto = ConquistaDtoMapper.toResponse(service.buscar(id));
        return ResponseEntity.ok(conquistaDto);
    }

    @GetMapping("/concluidas/{usuarioId}")
    public ResponseEntity<?> buscarConcluidas(@PathVariable long usuarioId){
        List<ConquistaResponseDto> conquistaDtos = ConquistaDtoMapper.toResponse(service.buscarConcluidas(usuarioId));
        return ResponseEntity.ok(conquistaDtos);
    }

    @PostMapping()
    public ResponseEntity<?> cadastrar(@RequestBody ConquistaRequestDto conquistaDto){
        Conquista conquista = ConquistaDtoMapper.toEntity(conquistaDto);
        Conquista novaConquista = service.cadastrar(conquista.getPontos(), conquista.getNome(), conquista.getTitulo(), conquistaDto.descricao());
        return ResponseEntity.status(201).body(ConquistaDtoMapper.toResponse(novaConquista));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable long id, @RequestBody ConquistaRequestDto conquistaDto){
        Conquista conquista = ConquistaDtoMapper.toEntity(conquistaDto);
        service.editar(id, conquistaDto.pontos(), conquista.getNome(), conquista.getTitulo(), conquistaDto.descricao());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable long id){
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }


}
