package br.com.infnet.routes.service;

import br.com.infnet.routes.model.ConsultaCep;
import br.com.infnet.routes.exception.ConsultaCepNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConsultaCepService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaCepService.class);

    private static final Map<String, ConsultaCep> consultaCepMap = new HashMap<>();
    private final ConsultaCepApi consultaCepApi;

    @Autowired
    public ConsultaCepService(ConsultaCepApi consultaCepApi) {
        this.consultaCepApi = consultaCepApi;
    }

    public ConsultaCep getConsultaCep(String cep) {
        ConsultaCep consultaCep = consultaCepMap.get(cep);
        if (consultaCep == null) {
            logger.warn("Consulta CEP não encontrada para o CEP: {}", cep);
            throw new ConsultaCepNotFoundException("Consulta CEP não encontrada para o CEP: " + cep);
        }
        return consultaCep;
    }

    public void adicionarConsultaCep(ConsultaCep consultaCep) {
        consultaCepMap.put(consultaCep.getCep(), consultaCep);
        logger.info("Consulta CEP adicionada com sucesso para o CEP: {}", consultaCep.getCep());
    }

    public void atualizarConsultaCep(String cep, ConsultaCep consultaCep) {
        if (!consultaCepMap.containsKey(cep)) {
            logger.warn("Consulta CEP não encontrada para o CEP: {}", cep);
            throw new ConsultaCepNotFoundException("Consulta CEP não encontrada para o CEP: " + cep);
        }
        consultaCepMap.put(cep, consultaCep);
        logger.info("Consulta CEP atualizada com sucesso para o CEP: {}", cep);
    }

    public void deletarConsultaCep(String cep) {
        if (!consultaCepMap.containsKey(cep)) {
            logger.warn("Consulta CEP não encontrada para o CEP: {}", cep);
            throw new ConsultaCepNotFoundException("Consulta CEP não encontrada para o CEP: " + cep);
        }
        consultaCepMap.remove(cep);
        logger.info("Consulta CEP removida com sucesso para o CEP: {}", cep);
    }

    public ConsultaCep consultarCepExterno(String cep) {
        try {
            ConsultaCep consultaCepExterno = consultaCepApi.consultarCepExterno(cep);
            adicionarConsultaCep(consultaCepExterno); // Adiciona à cache interna
            logger.info("Consulta CEP externo realizada com sucesso para o CEP: {}", cep);
            return consultaCepExterno;
        } catch (ConsultaCepNotFoundException e) {
            logger.warn("CEP não encontrado na API externa. CEP: {}", cep);
            throw e;
        } catch (Exception e) {
            logger.error("Erro ao consultar CEP externo. CEP: {}", cep, e);
            throw e;
        }
    }
}