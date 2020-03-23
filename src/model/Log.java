package model;

public class Log {
    private String log;
    private String tipoLog;
    private String tipo;
    private int linha;
    private int coluna;

    public Log() {
    }

    public Log(String tipoLog, String log, String tipo , int linha, int coluna) {
        this.log = log;
        this.tipoLog = tipoLog;
        this.tipo = tipo;
        this.linha = linha;
        this.coluna = coluna;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getTipoLog() {
        return tipoLog;
    }

    public void setTipoLog(String tipoLog) {
        this.tipoLog = tipoLog;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
