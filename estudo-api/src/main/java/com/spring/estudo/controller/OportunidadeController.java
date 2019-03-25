package com.spring.estudo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.spring.estudo.model.Oportunidade;
import com.spring.estudo.repository.OportunidadeRepository;

@CrossOrigin
@RestController
@RequestMapping("/oportunidades")
public class OportunidadeController {

	@Autowired
	private OportunidadeRepository oportunidades;
	
	@GetMapping
	public List<Oportunidade> listar() {
		return oportunidades.findAll();
		/*
		 * Oportunidade oportunidade = new Oportunidade(); oportunidade.setId(1L);
		 * oportunidade.setNomeProspecto("Teste");
		 * oportunidade.setDescricao("Teste descricao"); oportunidade.setValor(new
		 * BigDecimal(10000));
		 * 
		 * return Arrays.asList(oportunidade);
		 */
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Oportunidade> buscar(@PathVariable Long id) {
		Optional<Oportunidade> oportunidade = oportunidades.findById(id);
		
		if(!oportunidade.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(oportunidade.get());
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Oportunidade incluir(@Validated @RequestBody Oportunidade oportunidade) {
		
		Optional<Oportunidade> oportunidadeExitente = oportunidades.
					findByDescricaoAndNomeProspecto(oportunidade.getDescricao(), oportunidade.getNomeProspecto());
		if(oportunidadeExitente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
					"Já exite uma oportunidade para este prospecto com a mesma descrição");
		}
		return oportunidades.save(oportunidade);
	}
}
