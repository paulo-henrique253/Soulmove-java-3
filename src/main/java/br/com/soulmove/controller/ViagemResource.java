package br.com.soulmove.controller;

import br.com.soulmove.controller.Dto.ViagemDtoMapper;
import br.com.soulmove.controller.Dto.ViagemRequestDto;
import br.com.soulmove.controller.Dto.ViagemResponseDto;
import br.com.soulmove.model.Viagem;
import br.com.soulmove.model.type.Veiculo;
import br.com.soulmove.service.ViagemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/viagem")
public class ViagemResource {
    private final ViagemService service;

    public ViagemResource(ViagemService service) {
        this.service = service;
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<?> obertHistorico(@PathVariable long usuarioId){
        List<ViagemResponseDto> dtos = ViagemDtoMapper.toResponse(service.obterHistorico(usuarioId));
        return ResponseEntity.ok(dtos);
    }


    @PostMapping("/simular/")
    public ResponseEntity<?> simularViagem(@RequestBody ViagemRequestDto dto){
        Viagem retorno = service.simularViagem(dto.origem(), dto.destino(), Veiculo.getTipoVeiculo(dto.veiculo()));
        return ResponseEntity.ok(retorno);
    }

    @PostMapping()
    public ResponseEntity<?> viajar(@RequestBody ViagemRequestDto dto){
        Viagem retorno = service.viajar(dto.origem(), dto.destino(), Veiculo.getTipoVeiculo(dto.veiculo()), dto.usuarioId());
        return ResponseEntity.status(HttpStatus.CREATED).body(retorno);
    }


}
