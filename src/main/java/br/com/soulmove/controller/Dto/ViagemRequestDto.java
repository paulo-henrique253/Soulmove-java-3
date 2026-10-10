package br.com.soulmove.controller.Dto;

public record ViagemRequestDto(String origem, String destino, String veiculo, long usuarioId) {
}
