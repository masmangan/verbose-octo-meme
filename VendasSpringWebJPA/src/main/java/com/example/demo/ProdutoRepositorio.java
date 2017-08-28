package com.example.demo;

import java.util.List;

public interface ProdutoRepositorio {
	void inserir(Produto p);
	Produto consultarPorId(int id);
	List<Produto> consultarTodos();
}
