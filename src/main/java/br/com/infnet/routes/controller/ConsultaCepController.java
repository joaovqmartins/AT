package br.com.infnet.routes.controller;

import br.com.infnet.routes.exception.ConsultaCepNotFoundException;
import br.com.infnet.routes.model.ConsultaCep;
import br.com.infnet.routes.service.ConsultaCepService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/consulta")
public class ConsultaCepController {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaCepController.class);

    @Autowired
    private ConsultaCepService service;

    @GetMapping("/{cep}")
    public ResponseEntity<ConsultaCep> getConsultaCep(@PathVariable("cep") String cep,
                                                      @RequestParam(value = "param1", required = false) String param1,
                                                      @RequestParam(value = "param2", required = false) String param2) {
        try {
            ConsultaCep consultaCep = service.getConsultaCep(cep);
            logger.info("Consulta CEP obtida com sucesso para o CEP: {}", cep);
            logger.info("Request 200 - Operação bem sucedida para o CEP: {}", cep);
            return ResponseEntity.ok(consultaCep);
        } catch (ConsultaCepNotFoundException e) {
            logger.warn("CEP não encontrado na API interna. CEP: {}", cep);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao buscar consulta CEP. CEP: {}", cep, e);
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/update")
    public ResponseEntity<List<ConsultaCep>> consultar(@RequestBody List<String> cepList) {
        List<ConsultaCep> result = cepList.stream()
                .map(cep -> {
                    try {
                        logger.info("", cep);
                        logger.info("", cep);
                        return service.getConsultaCep(cep);
                    } catch (ConsultaCepNotFoundException e) {
                        logger.warn("CEP não encontrado na API interna. CEP: {}", cep);
                        return null;
                    } catch (Exception e) {
                        logger.error("Erro ao consultar CEP. CEP: {}", cep, e);
                        return null;
                    }
                })
                .filter(consultaCep -> consultaCep != null)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @PutMapping("/consulta/{cep}")
    public ResponseEntity<String> atualizarConsultaCepExterno(@PathVariable("cep") String cep,
                                                              @RequestParam(value = "param1", required = false) String param1,
                                                              @RequestParam(value = "param2", required = false) String param2) {
        try {
            ConsultaCep consultaCepExterno = service.consultarCepExterno(cep);
            service.atualizarConsultaCep(cep, consultaCepExterno);
            logger.info("Consulta CEP externo atualizada com sucesso para o CEP: {}", cep);
            logger.info("Request 200 - Operação bem sucedida para o CEP: {}", cep);

            return ResponseEntity.ok("Consulta CEP externo atualizada com sucesso!");
        } catch (ConsultaCepNotFoundException e) {
            logger.warn("CEP não encontrado na API externa para atualização. CEP: {}", cep);
            return ResponseEntity.status(404).body("CEP não encontrado na API externa");
        } catch (Exception e) {
            logger.error("Erro ao atualizar consulta CEP externo. CEP: {}", cep, e);
            return ResponseEntity.status(500).body("Erro ao atualizar consulta CEP externo");
        }
    }

    @DeleteMapping("/consulta/{cep}")
    public ResponseEntity<String> deletarConsultaCepExterno(@PathVariable("cep") String cep,
                                                            @RequestParam(value = "param1", required = false) String param1,
                                                            @RequestParam(value = "param2", required = false) String param2) {
        try {
            service.deletarConsultaCep(cep);
            logger.info("Consulta CEP externo removida com sucesso para o CEP: {}", cep);
            return ResponseEntity.ok("Consulta CEP externo removida com sucesso!");
        } catch (ConsultaCepNotFoundException e) {
            logger.warn("CEP não encontrado na API externa para remoção. CEP: {}", cep);
            return ResponseEntity.status(404).body("CEP não encontrado na API externa");
        } catch (Exception e) {
            logger.error("Erro ao deletar consulta CEP externo. CEP: {}", cep, e);
            return ResponseEntity.status(500).body("Erro ao deletar consulta CEP");
        }
    }

    @GetMapping("/consulta-externa/{cep}")
    public ResponseEntity<ConsultaCep> consultarCepExterno(@PathVariable("cep") String cep) {
        try {
            ConsultaCep consultaCepExterno = service.consultarCepExterno(cep);
            logger.info("Consulta CEP externo obtida com sucesso para o CEP: {}", cep);
            return ResponseEntity.ok(consultaCepExterno);
        } catch (ConsultaCepNotFoundException e) {
            logger.warn("CEP não encontrado na API externa. CEP: {}", cep);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao consultar CEP externo. CEP: {}", cep, e);
            return ResponseEntity.status(500).build();
        }
    }
}