package br.com.soulmove.controller.Dto;



import java.time.LocalDate;

public record ViagemResponseDto(String origem, String destino, String veiculo, double km_percorrido, double carbono_economizado, double carbono_emitido, LocalDate data) {
}
