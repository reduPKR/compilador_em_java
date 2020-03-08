package utils;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Tokens;
import pilha.Pilha;

public class AnaliseSintatica {
    private static ArrayList <String> codigo;
    private static ArrayList <Tokens> lista;

    public static void setCodigo(ArrayList<String> codigo) {
        AnaliseSintatica.codigo = codigo;
    }
    
    public static void IniciarAnalise(){        
        int i, j, tl;
        String[] items;
        String token;
        lista = new ArrayList();
        
        if(codigo != null){
            for (String linha : codigo) {
                items = new String[linha.length()];
               
                tl = 0; 
                for (i = 0; i < linha.length(); i++) {  
                    items[tl] = new String();
                    while(i < linha.length() && Split(linha.substring(i, i+1))){
                        if(!linha.substring(i, i+1).equals(" "))
                            items[tl] = items[tl].concat(linha.substring(i, i+1));
                        i++;
                    }
                    
                    if(i < linha.length()){
                        if(items[tl].length() > 0)
                            tl++;
                        
                        if(!linha.substring(i, i+1).equals(" ")){
                           items[tl] = new String();
                           items[tl] = items[tl].concat(linha.substring(i, i+1));
                           tl++;
                        }
                    }
                } 
                
                for(j = 0; j < linha.length() && items[j] != null; j++){
                    token = AnaliseLexica.getToken(items[j]);
                    lista.add(new Tokens(items[j],token));
                }
                lista.add(new Tokens("\n","\n"));
            }
            
            RemoverComentarios();
            AnalisarPrograma();
        }
        
    }
    
    private static Boolean Split(String letra){
        return !(letra.equals("(") || letra.equals(")") || 
                letra.equals("+") || letra.equals("-") || 
                letra.equals("*") || letra.equals("/") ||
                letra.equals("^") || letra.equals(" ") ||
                letra.equals(";"));
    }
    
    public static ObservableList<Tokens> getTabSimbolos(){
        if(lista != null){
            ArrayList <Tokens> aux = new ArrayList();
            
            for (Tokens t : lista){
                if(!t.getCadeia().equals("\n"))
                    aux.add(t);
            }
            
            return FXCollections.observableArrayList(aux);
        }   
        return null;
    }
    
    private static void RemoverComentarios(){
        for (int i = 0; i < lista.size(); i++) {
           if(lista.get(i).getToken().equals("tk_comentario")){
               lista.remove(i);
               while(!lista.get(i).getToken().equals("tk_comentario")){
                   lista.remove(i);
               }
           }
        }
    }
    
    private static void AnalisarPrograma(){
        Pilha p = Povoar(true);
        Pilha aux = Povoar(false);
        
        int x;
    }
    
    private static Pilha Povoar( boolean flag){
        Pilha p = new Pilha();
        
        int i, inc, fim;
        if(flag){
            inc = 1;
            i = 0;
            fim = lista.size();
        }else{
            inc = -1;
            i = lista.size()-1;
            fim = -1;
        }
        
        while(i != fim) {
            p.Push(lista.get(i).getCadeia());
            i = i+inc;
        }
        
        return p;
    }
}
