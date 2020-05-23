package utils;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Log;
import model.Tokens;

public class AnaliseSemantica {
    private static ArrayList <Tokens> tabela;
    private static ArrayList <Log> log;
    private static int ctrErro;

    public static void setTabela(ArrayList<Tokens> tabela) {
        AnaliseSemantica.tabela = tabela;
    }

    public static ArrayList<Tokens> getTabela() {
        return tabela;
    }
    
    public static void setLog(ArrayList<Log> log) {
        AnaliseSemantica.log = log;
    }
    
    public static void AnalisarSemantica(){
        for (Tokens item : tabela) {
            if(item.getToken().contains("tk_variavel")){
                if(item.getTipo() == null){
                    for (Tokens aux : tabela) {
                        if(aux.getLinha() < item.getLinha() || (item.getLinha() == aux.getLinha() && aux.getColuna() < item.getColuna())){
                            if(item.getCadeia().equals(aux.getCadeia())){
                                item.setTipo(aux.getTipo());
                            }
                        }
                    }
                }
            }
        }
        setErrosTipo();
    }
    
    public static ObservableList<Tokens> getTabSimbolos(){
        if(tabela != null){
            ArrayList <Tokens> aux = new ArrayList();
            
            for (Tokens t : tabela){
                if(!t.getCadeia().equals("\n"))
                    aux.add(t);
            }
            
            return FXCollections.observableArrayList(aux);
        }   
        return null;
    }
    
    private static void setErrosTipo(){
        ArrayList<Log> erro = new ArrayList();
        setErrosDeclaracao(erro);
        setErrosAtribuicao(erro);
        mesclarLogs(erro);   
    }
    
    private static void setErrosDeclaracao(ArrayList<Log> erro){
        for (Tokens item : tabela) {
            if(item.getToken().contains("tk_variavel")){
                if(item.getTipo() == null){
                    erro.add(new Log("Erro semantico", " variavel "+item.getCadeia()+" nao foi declarada", "Erro",item.getLinha()+1,item.getColuna()+1));
                }
            }
        }
    }
    
     private static void setErrosAtribuicao(ArrayList<Log> erro){
        for (int i = 0; i < tabela.size(); i++) {
            if(i >= 1 && i < tabela.size() && tabela.get(i).getToken().contains("tk_oper_atrib")){
                if(tabela.get(i-1).getTipo() != null){                    
                    if(!tabela.get(i+1).getToken().contains(tabela.get(i-1).getTipo())){
                        if(!tabela.get(i+1).getTipo().equals(tabela.get(i-1).getTipo())){
                            if(!tabela.get(i+1).getToken().equals("tk_abre_par")){
                                if(tabela.get(i-1).getTipo().equals("double") && tabela.get(i+1).getToken().equals("tk_num_int"))
                                    erro.add(new Log("Alerta semantico", " double recebendo int", "Alerta",tabela.get(i).getLinha()+1,1));
                                else
                                    erro.add(new Log("Erro semantico", " variavel atribuicao em tipos incompativeis", "Erro",tabela.get(i).getLinha()+1,1));
                            }
                        }
                    }
                }
            }
        }   
    }
    
    private static void mesclarLogs(ArrayList<Log> erro){
        int pos, ctr;
        for(Log item: erro){
            pos = 0;
            ctr = -1;
            for(Log aux: log){
                if(item.getLinha() == aux.getLinha()){
                    ctr = pos;
                }
                pos++;
            }
            if(ctr != -1){
                log.add(ctr,item);
                log.remove(ctr+1);
            }
        }
    }
    
    public static int getErros(){
        ctrErro = 0;
        
        for(Log item: log){
            if(item.getTipo().equals("Erro")){
                ctrErro++;
            }
        }
        
        return ctrErro;
    }
}
