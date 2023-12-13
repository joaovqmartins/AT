package br.com.infnet.routes.service;

import br.com.infnet.routes.model.ConsultaCep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ConsultaCepApi {

    private static final String EXTERNAL_API_URL = "https://opencep.com/v1/28415000";

    public ConsultaCep  consultarCepExterno(String cep) {
        String apiUrl = EXTERNAL_API_URL + cep;
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<ConsultaCep> response = restTemplate.getForEntity(apiUrl, ConsultaCep.class);
            log.info("Status code da resposta da API externa: {}", response.getStatusCodeValue());

            ConsultaCep consultaCep = response.getBody();

            log.info("CEP: {}", consultaCep.getCep());
            log.info("Logradouro: {}", consultaCep.getLogradouro());
            log.info("Complemento: {}", consultaCep.getComplemento());
            log.info("Bairro: {}", consultaCep.getBairro());
            log.info("Localidade: {}", consultaCep.getLocalidade());
            log.info("UF: {}", consultaCep.getUf());
            log.info("IBGE: {}", consultaCep.getIbge());

            return consultaCep;

        } catch (Exception e) {
            log.error("Erro ao consultar o CEP na API externa", e);
            throw new RuntimeException("Erro ao consultar o CEP na API externa", e);
        }
    }
}
