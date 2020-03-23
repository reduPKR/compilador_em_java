package model;

public class Tokens {
    private String cadeia;
    private String token;
    private int linha;
    private int coluna;

    public Tokens(String cadeia, String token, int linha, int coluna) {
        this.cadeia = cadeia;
        this.token = token;
        this.linha = linha;
        this.coluna = coluna;
    }
    
    public Tokens() {
    }

    public void setCadeia(String cadeia) {
        this.cadeia = cadeia;
    }
    
    public String getCadeia() {
        return cadeia;
    }

    public String getToken() {
        return token;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }
    
    
}
