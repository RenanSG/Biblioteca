package model;

public interface Emprestavel {
    void emprestar() throws Exception;
    void devolver();
    boolean isDisponivel();
}
