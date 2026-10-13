package br.com.soulmove.controller;


import br.com.soulmove.controller.Dto.MissaoRequestDto;
import br.com.soulmove.controller.Dto.MissaoResponseDto;
import br.com.soulmove.controller.Dto.MissaoDtoMapper;
import br.com.soulmove.model.Missao;
import br.com.soulmove.service.MissaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/missao")
public class MissaoResource {

    private MissaoService service;

    @Autowired
    public MissaoResource(MissaoService service){this.service = service;}

    @GetMapping
    public ResponseEntity<?> buscarMissoes(){
        List<MissaoResponseDto> missaoDtos = MissaoDtoMapper.toResponse(service.buscarMissoes());
        return ResponseEntity.ok(missaoDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable long id){
        MissaoResponseDto missaoDto = MissaoDtoMapper.toResponse(service.buscar(id));
        return ResponseEntity.ok(missaoDto);
    }

    @GetMapping("/concluidas/{usuarioId}")
    public ResponseEntity<?> buscarConcluidas(@PathVariable long usuarioId){
        List<MissaoResponseDto> missaoDtos = MissaoDtoMapper.toResponse(service.buscarConcluidas(usuarioId));
        return ResponseEntity.ok(missaoDtos);
    }

    @PostMapping()
    public ResponseEntity<?> cadastrar(@RequestBody MissaoRequestDto missaoDto){
        Missao missao = MissaoDtoMapper.toEntity(missaoDto);
        Missao novaMissao = service.cadastrar(missao.getPontos(), missao.getTitulo(), missao.getTipo(), missaoDto.descricao());
        return ResponseEntity.status(201).body(MissaoDtoMapper.toResponse(novaMissao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable long id, @RequestBody MissaoRequestDto missaoDto){
        Missao missao = MissaoDtoMapper.toEntity(missaoDto);
        service.editar(id, missaoDto.pontos(), missao.getTitulo(), missaoDto.descricao(), missao.getTipo());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable long id){
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }


}
