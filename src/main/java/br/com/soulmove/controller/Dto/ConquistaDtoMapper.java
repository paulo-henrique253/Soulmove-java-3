package br.com.soulmove.controller.Dto;

import br.com.soulmove.model.Conquista;

import java.util.ArrayList;
import java.util.List;

public class ConquistaDtoMapper {
    public static ConquistaResponseDto toResponse(Conquista conquista){
        return new ConquistaResponseDto(
                conquista.getId(),
                conquista.getNome(),
                conquista.getTitulo(),
                conquista.getPontos(),
                conquista.getDescricao()
                );
    }

    public static List<ConquistaResponseDto> toResponse(List<Conquista> conquistas){
        List<ConquistaResponseDto> conquistaDtos = new ArrayList<>();
        for (Conquista c : conquistas){
            conquistaDtos.add(toResponse(c));
        }
        return conquistaDtos;
    }

    public static Conquista toEntity(ConquistaRequestDto conquistaDto){
        return new Conquista(
                conquistaDto.nome(),
                conquistaDto.titulo(),
                conquistaDto.pontos(),
                conquistaDto.descricao()
        );
    }

    public static List<Conquista> toEntity(List<ConquistaRequestDto> conquistaDtos){
        List<Conquista> conquistas = new ArrayList<>();
        for (ConquistaRequestDto cr : conquistaDtos){
            conquistas.add(toEntity(cr));
        }
        return conquistas;
    }

}
