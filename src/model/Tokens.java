package model;

public class Tokens {
    private String cadeia;
    private String token;

    public Tokens(String linha, String token) {
        cadeia = linha;
        this.token = token;
    }

    public Tokens() {
    }

    public String getCadeia() {
        return cadeia;
    }

    public String getToken() {
        return token;
    }
}
