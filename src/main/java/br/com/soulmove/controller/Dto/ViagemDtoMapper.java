package br.com.soulmove.controller.Dto;

import br.com.soulmove.model.Viagem;

import java.util.ArrayList;
import java.util.List;

public class ViagemDtoMapper {

    public static ViagemResponseDto toResponse(Viagem viagem){
        return new ViagemResponseDto(
                viagem.getOrigem(),
                viagem.getDestino(),
                viagem.getTipoVeiculo().getVeiculo(),
                viagem.getKmPercorrido(),
                viagem.getCarbonoEconomizado(),
                viagem.getCarbonoEmitido(),
                viagem.getData()
        );
    }

    public static List<ViagemResponseDto> toResponse(List<Viagem> viagens){
        List<ViagemResponseDto> viagemDtos = new ArrayList<>();
        for (Viagem v : viagens){
            viagemDtos.add(toResponse(v));
        }
        return viagemDtos;
    }

}
