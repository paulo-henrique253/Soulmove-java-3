package br.com.soulmove.controller;


import br.com.soulmove.controller.Dto.MissaoRequestDto;
import br.com.soulmove.controller.Dto.MissaoResponseDto;
import br.com.soulmove.controller.Dto.MissaoDtoMapper;
import br.com.soulmove.exceptions.BussinesRuleException;
import br.com.soulmove.model.Missao;
import br.com.soulmove.service.MissaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/imagem/{imagemId}")
    public ResponseEntity<?> buscarImagem(@PathVariable long imagemId){
        Map<String, Object> imagemInfos = service.buscarImagem(imagemId);

        byte[] dadosImagem = (byte[]) imagemInfos.get("dados_imagem");

        String tipoMime = (String) imagemInfos.get("tipo_mime");

        //Configura o cabeçalho HTTP para o front-end entender que é uma imagem
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, tipoMime);

        // (Opcional) Dizer ao navegador para fazer download ou apenas exibir na tela
        // "inline" exibe no navegador. "attachment; filename=..." faria o download automático.
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline");

        return new ResponseEntity<>(dadosImagem, headers, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> cadastrar(@RequestParam("titulo") String titulo,
                                       @RequestParam("nomeTipo") String nomeTipo,
                                       @RequestParam("descricao") String descricao,
                                       @RequestParam("pontos") int pontos,
                                       @RequestParam("meta") double meta,
                                       @RequestParam("periodicidade") int periodicidade,
                                       @RequestParam("prazo") int prazo,
                                       @RequestParam("arquivoImagem") MultipartFile arquivoImagem
    ){

        MissaoRequestDto missaoDto = new MissaoRequestDto(
                titulo, nomeTipo, descricao, pontos, meta, periodicidade, prazo, arquivoImagem
        );

        Missao novaMissao = service.cadastrar(missaoDto.pontos(), missaoDto.titulo(), missaoDto.nomeTipo(), missaoDto.descricao(), missaoDto.meta(), missaoDto.periodicidade(), missaoDto.prazo(), missaoDto.arquivoImagem());
        return ResponseEntity.status(201).body(MissaoDtoMapper.toResponse(novaMissao));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable long id, @RequestBody MissaoRequestDto missaoDto){
        Missao missao = MissaoDtoMapper.toEntity(missaoDto);
        service.editar(id, missaoDto.pontos(), missao.getTitulo(), missaoDto.descricao(), missao.getTipo().getNome());

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable long id){
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }


}
