package br.com.soulmove.controller.Dto;

import br.com.soulmove.model.Conquista;
import br.com.soulmove.model.Missao;

import java.time.LocalDate;
import java.util.List;

public record UsuarioResponseDto(long id, String nome, String email, LocalDate dataCadastro, String titulo, long carteiraId, int pontos, List<Missao> missoesConcluidas, List<Conquista> conquistasConcluidas) {
}
