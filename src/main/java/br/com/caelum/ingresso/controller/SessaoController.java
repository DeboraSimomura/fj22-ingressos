package br.com.caelum.ingresso.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.caelum.ingresso.dao.FilmeDao;
import br.com.caelum.ingresso.dao.SalaDao;
import br.com.caelum.ingresso.dao.SessaoDao;
import br.com.caelum.ingresso.model.form.SessaoForm;
import br.com.caelum.ingresso.modelo.Carrinho;
import br.com.caelum.ingresso.modelo.ImagemCapa;
import br.com.caelum.ingresso.modelo.Sessao;
import br.com.caelum.ingresso.modelo.TipoDeIngresso;
import br.com.caelum.ingresso.rest.ImdbClient;

@Controller
public class SessaoController {
	@Autowired
	private SalaDao salaDao;

	@Autowired
	private FilmeDao filmeDao;
	@Autowired
	private SessaoDao sessaoDao;
	@Autowired
	private ImdbClient client;
	@Autowired
	private Carrinho carrinho;

	@PostMapping("/admin/sessao")
	@Transactional
	public ModelAndView salva(@Valid SessaoForm form, BindingResult result) {
		if (result.hasErrors())
			return form(form.getSalaId(), form);
		ModelAndView mav = new ModelAndView("redirect:/admin/sala/" + form.getSalaId() + "/sessoes");
		Sessao sessao = form.toSessao(salaDao, filmeDao);
		sessaoDao.save(sessao);
		return mav;
	}
	
	@GetMapping("/sessao/{id}/lugares")
	public ModelAndView lugaresNaSessao(@PathVariable("id") Integer sessaoId){
		ModelAndView mav = new ModelAndView("sessao/lugares");
		
		Sessao sessao= sessaoDao.findOne(sessaoId);
		
		Optional<ImagemCapa> imagemCapa= client.request(sessao.getFilme(), ImagemCapa.class);
		
		mav.addObject("carrinho", carrinho);
		mav.addObject("sessao", sessao);
		mav.addObject("imagemCapa", imagemCapa.orElse(new ImagemCapa()));
		mav.addObject("tiposDeIngressos", TipoDeIngresso.values());
		return mav;
	}

	@GetMapping("/admin/sessao")	
	public ModelAndView form(@RequestParam("salaId") Integer salaId, SessaoForm form) {
		form.setSalaId(salaId);
		ModelAndView mav = new ModelAndView("sessao/sessao");
		mav.addObject("sala", salaDao.findOne(salaId));
		mav.addObject("filmes", filmeDao.findAll());
		mav.addObject("form", form);

		return mav;

	}

}
