package br.com.soulmove.controller.Dto;

import br.com.soulmove.model.UsuarioSoulMove;

public class UsuarioDtoMapper {

    public static UsuarioResponseDto toResponse(UsuarioSoulMove usuario){
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getDataCadastro(),
                usuario.getCarteira().getId(),
                usuario.getPontos(),
                usuario.getMissoesConcluidas(),
                usuario.getConquistasConcluidas()
        );
    }
}
