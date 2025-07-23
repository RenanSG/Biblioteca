package model;

import exceptions.ItemIndisponivelException;

public final class Revista extends Item implements Emprestavel, Catalogavel {
    private final int edicao;


    public Revista(String titulo, Autor autor, Categoria categoria, int edicao) {
        super(titulo, autor, categoria);
        this.edicao = edicao;
    }

    public int getEdicao() {
        return edicao;
    }

    @Override
    public void emprestar() throws ItemIndisponivelException {
        if (!disponivel) throw new ItemIndisponivelException("Revista já emprestada.");
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
            Revista: %s
            Gênero: %s
            Estilo: %s
            Tema: %s
            Autor: %s
            Edição: %s
            Disponível: %s
        """, titulo, categoria.genero(), categoria.estilo(), categoria.tema(), autor.nome(), edicao, disponivel ? "Sim" : "Não");
    }
}