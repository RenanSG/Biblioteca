package model;

public sealed abstract class Item permits Livro, Revista {
    protected final String titulo;
    protected final Autor autor;
    protected boolean disponivel = true;
    protected final Categoria categoria;

    public Item(String titulo, Autor autor, Categoria categoria) {
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;

    }


    public String getTitulo() { return titulo; }
    public Autor getAutor() { return autor; }

}
