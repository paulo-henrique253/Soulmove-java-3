package br.com.soulmove.controller.Dto;

import br.com.soulmove.model.Missao;
import br.com.soulmove.model.type.TipoMissao;

import java.util.ArrayList;
import java.util.List;

public class MissaoDtoMapper {

    public static MissaoResponseDto toResponse(Missao missao){
        return new MissaoResponseDto(
                missao.getId(),
                missao.getTitulo(),
                missao.getTipo().getTipo(),
                missao.getDescricao(),
                missao.getPontos()
        );
    }

    public static List<MissaoResponseDto> toResponse(List<Missao> missoes){
        if (missoes == null) return null;
        List<MissaoResponseDto> missaoDtos = new ArrayList<>();
        for (Missao m : missoes){
            missaoDtos.add(toResponse(m));
        }
        return missaoDtos;
    }

    public static Missao toEntity(MissaoRequestDto missaoRequest){
        return new Missao(
                missaoRequest.titulo(),
                TipoMissao.getTipoMissao(missaoRequest.tipo()),
                missaoRequest.descricao(),
                missaoRequest.pontos()
        );
    }

    public static List<Missao> toEntity(List<MissaoRequestDto> missaoRequests) {
        if (missaoRequests == null) return null;
        List<Missao> missoes = new ArrayList<>();
        for (MissaoRequestDto mr : missaoRequests){
            missoes.add(toEntity(mr));
        }

        return missoes;
    }

}
