package br.com.caelum.ingresso.model;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;

import org.junit.Assert;
import org.junit.Test;

import br.com.caelum.ingresso.modelo.Filme;
import br.com.caelum.ingresso.modelo.Sala;
import br.com.caelum.ingresso.modelo.Sessao;



public class SessaoTest {
	@Test
	public void oPrecoDaSessaoDeveSerIgualASomaDoPrecoDaSalaMaisOPrecoDoFilme(){
		Sala sala = new Sala("Eldorado - IMAX", new BigDecimal("22.50"));
		Filme filme= new Filme("Rogue One", Duration.ofMinutes(120),"SCI-FI", new BigDecimal("12.00"));
		
		BigDecimal somaDosPrecosDaSalaEFilme = sala.getPreco().add(filme.getPreco());
		Sessao sessao = new Sessao (LocalTime.parse("10:00:00"), filme, sala);
		Assert.assertEquals(somaDosPrecosDaSalaEFilme, sessao.getPreco());
		
	}

}
