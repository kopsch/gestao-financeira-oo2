package com.gestaofinanceira.dao;

import java.util.List;
import java.util.UUID;

public interface GenericDAO<T> {
    
    void salvar(T entidade);

    void atualizar(T entidade);

    void deletar(UUID id);

    T buscarPorId(UUID id);

    List<T> listarTodos();
}