package br.com.soulmove.controller.Dto;

public record MissaoResponseDto(long id, String titulo, String nomeTipo, String descricao, int pontos, double meta, int periodicidade, int prazo, String linkImagem) {
}
