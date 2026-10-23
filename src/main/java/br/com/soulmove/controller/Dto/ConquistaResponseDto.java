package br.com.soulmove.controller.Dto;

import java.time.LocalDate;

public record ConquistaResponseDto(long id, String nome, String titulo, int pontos, String descricao, double meta , String nomeTipo, double progressoAtual, LocalDate dataConclusao) {
}
