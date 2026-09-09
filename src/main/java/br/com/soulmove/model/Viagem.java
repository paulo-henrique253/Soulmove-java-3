package br.com.soulmove.model;

import br.com.soulmove.model.type.TipoVeiculo;

import java.time.LocalDate;

public class Viagem {
    private long id;
    private LocalDate data;
    private String origem;
    private String destino;
    private TipoVeiculo tipoVeiculo;
}
