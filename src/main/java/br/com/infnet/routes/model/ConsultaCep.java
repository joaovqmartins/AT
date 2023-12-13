package br.com.infnet.routes.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultaCep {

    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private String estado;
}