package service;

import model.*;
import exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BibliotecaService {
    private final List<Item> itens = new ArrayList<>();

    public void cadastrar(Item item) {
        itens.add(item);
    }

    public void emprestar(Emprestavel item) throws Exception {
        item.emprestar();
    }


    public void devolver(Emprestavel item) throws Exception{
        item.devolver();
    }

    public Livro buscarPorISBN(String isbn) throws ItemNaoEncontradoException {
        return itens.stream()
                .filter(i -> i instanceof Livro l && l.getIsbn().equals(isbn))
                .map(i -> (Livro) i)
                .findFirst()
                .orElseThrow(() -> new ItemNaoEncontradoException("Livro não encontrado."));
    }

    public List<Revista> buscarPorEdicao(int edicao) {
        return itens.stream()
                .filter(i -> i instanceof Revista r && r.getEdicao() == edicao)
                .map(i -> (Revista) i)
                .collect(Collectors.toList());
    }

    public List<Item> buscarPorTermo(String termo) {
        return itens.stream()
                .filter(i -> i instanceof Catalogavel c && c.contemTermo(termo))
                .collect(Collectors.toList());
    }

    public List<Item> listarTodos() {
        return new ArrayList<>(itens);
    }
}

