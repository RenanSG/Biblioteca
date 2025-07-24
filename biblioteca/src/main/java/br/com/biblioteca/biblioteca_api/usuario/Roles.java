package br.com.biblioteca.biblioteca_api.usuario;

public enum Roles {
    ADMIN(1, "ROLE_ADMIN"),
    CLIENTE(2, "ROLE_CLIENTE");

    private int cod;
    private String descricao;

    Roles(int cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
    }

    public int getCod() {
        return cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Roles toEnum(Integer cod) {
        if (cod == null) {
            return null;
        }
        for (Roles x : Roles.values()) {
            if (cod.equals(x.getCod())) {
                return x;
            }
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}