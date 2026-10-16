package br.com.soulmove.controller.Dto;

public record MissaoRequestDto(String titulo, String nomeTipo, String descricao, int pontos, double meta, int periodicidade, int prazo) {
}
