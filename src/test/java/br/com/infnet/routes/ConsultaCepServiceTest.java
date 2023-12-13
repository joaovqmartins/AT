package br.com.infnet.routes;

import br.com.infnet.routes.exception.ConsultaCepNotFoundException;
import br.com.infnet.routes.model.ConsultaCep;
import br.com.infnet.routes.service.ConsultaCepApi;
import br.com.infnet.routes.service.ConsultaCepService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConsultaCepServiceTest {

    private ConsultaCepService consultaCepService = new ConsultaCepService(new ConsultaCepApi());

    @Test
    public void testGetConsultaCep_WithValidCep_ShouldReturnConsultaCep() {

        ConsultaCep consultaCep = new ConsultaCep();
        consultaCep.setCep("12345678");
        consultaCepService.adicionarConsultaCep(consultaCep);

        ConsultaCep result = consultaCepService.getConsultaCep("12345678");

        assertNotNull(result);
        assertEquals(consultaCep, result);
    }

    @Test
    public void testGetConsultaCep_WithInvalidCep_ShouldThrowNotFoundException() {
        assertThrows(ConsultaCepNotFoundException.class, () -> {
            consultaCepService.getConsultaCep("99999999");
        });
    }

    @Test
    public void testAdicionarConsultaCep_ShouldAddConsultaCepToMap() {
        ConsultaCep consultaCep = new ConsultaCep();
        consultaCep.setCep("98765432");

        consultaCepService.adicionarConsultaCep(consultaCep);

        ConsultaCep result = consultaCepService.getConsultaCep("98765432");
        assertNotNull(result);
        assertEquals(consultaCep, result);
    }

    @Test
    public void testAtualizarConsultaCep_WithExistingCep_ShouldUpdateConsultaCepInMap() {
        ConsultaCep existingConsultaCep = new ConsultaCep();
        existingConsultaCep.setCep("11111111");
        consultaCepService.adicionarConsultaCep(existingConsultaCep);

        ConsultaCep updatedConsultaCep = new ConsultaCep();
        updatedConsultaCep.setCep("11111111");
        updatedConsultaCep.setLogradouro("Rua Atualizada");

        consultaCepService.atualizarConsultaCep("11111111", updatedConsultaCep);

        ConsultaCep result = consultaCepService.getConsultaCep("11111111");
        assertNotNull(result);
        assertEquals("Rua Atualizada", result.getLogradouro());
    }

    @Test
    public void testAtualizarConsultaCep_WithNonexistentCep_ShouldThrowNotFoundException() {
        ConsultaCep nonExistingConsultaCep = new ConsultaCep();
        nonExistingConsultaCep.setCep("99999999");

        assertThrows(ConsultaCepNotFoundException.class, () -> {
            consultaCepService.atualizarConsultaCep("99999999", nonExistingConsultaCep);
        });
    }

    @Test
    public void testDeletarConsultaCep_WithExistingCep_ShouldRemoveConsultaCepFromMap() {
        ConsultaCep existingConsultaCep = new ConsultaCep();
        existingConsultaCep.setCep("22222222");
        consultaCepService.adicionarConsultaCep(existingConsultaCep);

        consultaCepService.deletarConsultaCep("2222222");
        assertThrows(ConsultaCepNotFoundException.class, () -> {
            consultaCepService.getConsultaCep("22222222");
        });
    }

    @Test
    public void testDeletarConsultaCep_WithNonexistentCep_ShouldThrowNotFoundException() {
        assertThrows(ConsultaCepNotFoundException.class, () -> {
            consultaCepService.deletarConsultaCep("99999999");
        });
    }
}
