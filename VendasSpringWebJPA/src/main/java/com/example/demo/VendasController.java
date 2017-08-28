package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class VendasController {
	private ProdutoRepositorioV2 pr;
	private VendaRepositorioV2 vr;
	
	@Autowired
	public VendasController(ProdutoRepositorioV2 pr, VendaRepositorioV2 vr) {
		this.pr = pr;
		this.vr = vr;
	}
	
	@GetMapping("/produtos")
	public Iterable<Produto> consultarProdutos() {
		return pr.findAll();
	}
		
	@GetMapping("/vendas")
	public Iterable<Venda> consultarVendas() {
		return vr.findAll();
	}
	
	@GetMapping("/vendas/{id}")
	public ResponseEntity<Venda> consultarVenda(@PathVariable("id") int id) {
		Venda v = vr.findOne(id);
		if(v != null) {
			return new ResponseEntity<>(v, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/vendas")
	public ResponseEntity<Void> adicionarVenda(@RequestBody Venda venda, UriComponentsBuilder uc) {
		if(venda.getId()!=0 && vr.exists(venda.getId())) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		Venda novaVenda = vr.save(venda);
		HttpHeaders cabecalho = new HttpHeaders();
		cabecalho.setLocation(uc.path("/venads/{id}").buildAndExpand(novaVenda.getId()).toUri());
		return new ResponseEntity<Void>(cabecalho, HttpStatus.CREATED);
	}

	@PutMapping("/vendas/{codVenda}/{codProduto}/{quant}")
	public ResponseEntity<Venda> adicionarProdutoVenda(@PathVariable("codVenda") int codVenda, @PathVariable("codProduto") int codProduto, @PathVariable("quant") int quantidade) {
		Venda v = vr.findOne(codVenda);
		if(v != null) {
			Produto p = pr.findOne(codProduto);
			if (p != null) {
				v.vender(p, quantidade);
				vr.save(v);
				return new ResponseEntity<Venda>(v, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
}
