package br.com.biblioteca.biblioteca_api.usuario;

public enum Roles {
    ADMIN("ROLE_ADMIN"),
    CLIENTE("ROLE_CLIENTE");

    private String descricao;

    Roles(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
