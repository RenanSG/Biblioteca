package model;

import exceptions.ItemIndisponivelException;

public final class Livro extends Item implements Emprestavel, Catalogavel {
    private final String isbn;

    public Livro(String titulo, Autor autor, Categoria categoria, String isbn) {
        super(titulo, autor, categoria);
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    @Override
    public void emprestar() throws ItemIndisponivelException {
        if (!disponivel) throw new ItemIndisponivelException("Livro já emprestado.");
        disponivel = false;
    }

    @Override
    public void devolver() {
        disponivel = true;
    }

    @Override
    public boolean isDisponivel() {
        return disponivel;
    }

    @Override
    public boolean contemTermo(String termo) {
        return titulo.toLowerCase().contains(termo.toLowerCase()) ||
                autor.nome().toLowerCase().contains(termo.toLowerCase());

    }

    @Override
    public String toString() {
        return String.format("""
            Livro: %s
            Gênero: %s
            Estilo: %s
            Tema: %s
            Autor: %s
            ISBN: %s
            Disponível: %s
        """, titulo, categoria.genero(), categoria.estilo(), categoria.tema(), autor.nome(), isbn, disponivel ? "Sim" : "Não");
    }
}

