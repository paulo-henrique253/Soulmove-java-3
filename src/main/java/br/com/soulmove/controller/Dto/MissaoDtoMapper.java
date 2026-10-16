package br.com.soulmove.controller.Dto;

import br.com.soulmove.model.Missao;
import br.com.soulmove.model.TipoMissao;

import java.util.ArrayList;
import java.util.List;

public class MissaoDtoMapper {

    public static MissaoResponseDto toResponse(Missao missao){

        String linkImagem = "https://spousal-uptight-shiftless.ngrok-free.dev/api/v1/missao/imagem/" + missao.getImagemId();
        return new MissaoResponseDto(
                missao.getId(),
                missao.getTitulo(),
                missao.getTipo().getNome(),
                missao.getDescricao(),
                missao.getPontos(),
                missao.getMeta(),
                missao.getPeriodicidade(),
                missao.getPrazo(),
                linkImagem
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
                new TipoMissao(missaoRequest.nomeTipo()),
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
