package br.com.soulmove.controller.Dto;

import org.springframework.web.multipart.MultipartFile;

public record MissaoRequestDto(String titulo, String nomeTipo, String descricao, int pontos, double meta, int periodicidade, int prazo, MultipartFile arquivoImagem) {
}
