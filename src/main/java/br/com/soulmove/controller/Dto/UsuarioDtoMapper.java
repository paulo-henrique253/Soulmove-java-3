package br.com.soulmove.controller.Dto;

import br.com.soulmove.model.UsuarioSoulMove;

public class UsuarioDtoMapper {

    public static UsuarioResponseDto toResponse(UsuarioSoulMove usuario){
        String titulo;
        if(usuario.getTituloAtual() != null)
            titulo = usuario.getTituloAtual().getTitulo();
        else titulo = null;
        boolean isAdm = (usuario.getTipo().equalsIgnoreCase("admin"));

        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getDataCadastro(),
                titulo,
                usuario.getCarteira().getSaldo(),
                usuario.getPontos(),
                usuario.getMissoesConcluidas(),
                usuario.getConquistasConcluidas(),
                isAdm
        );
    }
}
