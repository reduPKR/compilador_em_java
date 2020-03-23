package utils;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import matrizSintatica.MatrizSintatica;
import model.Log;
import model.Tokens;
import pilha.Pilha;

public class AnaliseSintatica {
    private static ArrayList <String> codigo;
    private static ArrayList <Log> erro;
    private static ArrayList <Tokens> lista;

    public static void setCodigo(ArrayList<String> codigo) {
        AnaliseSintatica.codigo = codigo;
    }
    
    public static void IniciarAnalise(){        
        boolean flagStr = false;
        boolean str = false;
        int i, j, tl, lin;
        String[] items;
        String token;
        lista = new ArrayList();
        
        if(codigo != null){
            lin = 0;
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
                    
                    if(token.equals("tk_string") && !flagStr){
                        str = true;
                        if(!items[j].endsWith("'"))
                            flagStr = true;
                    }else
                    if(flagStr)
                    {
                        token = "tk_string";
                        if(items[j].contains("'"))
                            flagStr = !flagStr;
                    }
                    
                    lista.add(new Tokens(items[j],token, lin, j));
                }
                lista.add(new Tokens("\n","\n",lin, j));
                lin++;
            }
            
            RemoverComentarios();
            UnirString();
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
    
    private static void UnirString(){
        String cadeia;
        for (int i = 0; i < lista.size(); i++) {
           if(lista.get(i).getToken().equals("tk_string")){
               cadeia = lista.get(i).getCadeia();
               while(lista.get(i+1).getToken().equals("tk_string")){
                   cadeia += " "+lista.get(i+1).getCadeia();
                   lista.remove(i+1);
               }
               lista.get(i).setCadeia(cadeia);
           }
        }
    }
    
    public static ArrayList<Log> getLog(){
        return erro;
    }
    
    private static void AnalisarPrograma(){
        int i = 0;        
        erro = new ArrayList(); 
        MatrizSintatica ms = new MatrizSintatica();

        if(!lista.get(i).getToken().equals("tk_prog_ini"))
            erro.add(new Log("Erro sintatico", " tk_prog_ini (<?) não encontrado", "Erro",lista.get(0).getLinha()+1,lista.get(0).getColuna()+1));
        else
            i++;
        
        LerLinha(i, ms);
        
        if(!lista.get(lista.size()-2).getToken().equals("tk_prog_fim"))
            erro.add(new Log("Erro sintatico", " tk_prog_fim (?>) não encontrado","Erro",lista.get(lista.size()-2).getLinha()+1,lista.get(lista.size()-2).getColuna()+1));
            
    }
    
    private static void LerLinha(int i, MatrizSintatica ms){
        int j, k;
        String cadeia, aux, atual;
        ArrayList<String> ant;
        Boolean flag;
        Log l = null;
        Pilha p;
        
        k = 0;
        for(; i < lista.size() && !lista.get(i).getToken().equals("tk_prog_fim"); i++) {
            if(!lista.get(i).getCadeia().equals("\n")){
                cadeia = lista.get(i).getToken();
                ant = ms.getRotas(cadeia);//Retorna todos anteriores que levam diretamente a cadeia
                
                flag = false;
                for(j = 0; j < ant.size() && !flag; j++){
                    k = i;
                    
                    atual = ant.get(j);
                    p = Povoar(cadeia, ms);

                    aux = p.Pop();
                    if(aux.contains(cadeia)){
                        k++;
                        while(k < lista.size() && !lista.get(k).getCadeia().equals("\n")){
                            cadeia = lista.get(k).getToken();
                            if(p.getTopo() != null){
                                aux = p.Pop();
                                
                                if(!aux.contains(cadeia)){
                                    if(!aux.equals("$"))
                                        l = new Log("Erro sintatico", " sintaxe incorreta do "+atual+" token esperado "+aux+" token usado "+cadeia,"Erro",lista.get(k).getLinha()+1,lista.get(k).getColuna()+1); 
                                    else
                                        l = new Log("Erro sintatico", " sintaxe incorreta do "+atual,"Erro",lista.get(k).getLinha()+1,lista.get(k).getColuna()+1); 

                                    k = lista.size();
                                } 
                            }else{
                                l = new Log("Erro sintatico", " sintaxe incorreta do "+atual,"Erro",lista.get(k).getLinha()+1,lista.get(k).getColuna()+1);
                                k = lista.size();
                            }

                            k++;
                        }
                        
                        if(k < lista.size()){
                            aux = p.Pop();
                            if(lista.get(k).getCadeia().equals("\n") && (aux.contains("$") || aux.contains("tk_abre_chav")) && !cadeia.contains("tk_prog_fim")){
                                l = new Log("Analise sintatica", " sintaxe "+atual+" reconhecida","Log",lista.get(k).getLinha()+1,1);
                                flag = true;
                            }
                            
                            /*Inicia bloco*/
                            if(aux.contains("tk_abre_chav")){
                                LerBloco(p, k, ms); 
                            }
                        }

                    }else
                        l = new Log("Erro sintatico", " sintaxe inicial incorreta"+aux,"Erro",lista.get(k).getLinha()+1,lista.get(k).getColuna()+1);
                }
                i = k;
            }  
            if(l != null)
                erro.add(l);
        }
    }
    
    private static Pilha Povoar(String cadeia, MatrizSintatica ms){
        String ant, str;
        ArrayList<String> linha;
        ArrayList<String> prox;
        Pilha p = new Pilha();
        Pilha aux = new Pilha();
 
        
        ant = ms.getAnterior(cadeia);
        //p.Push(ant);//empilha o tipo ou bloco
        if(ant != null){
            while(!ant.equals("programa")){
                if(!ant.contains("tk_") && !ant.equals("caracter") && !ant.equals("variavel") && !ant.equals("boolean") && !ant.equals("tipo") && !ant.equals("bloco")){
                    linha = ms.getLinha(ant);//atraves do token
                    
                    //p.Pop();//tipo_int -> tk_int variavel ...
                    for (int i = 0; i < linha.size(); i++){
                        if((linha.get(i).contains("tk_") && !linha.get(i).contains("caracter") && !linha.get(i).contains("boolean")) || linha.get(i).contains("bloco")){///Alterar depois o caracter
                            aux.Push(linha.get(i));
                        }else
                        if(!linha.get(i).contains("caracter") && !linha.get(i).contains("boolean")){
                            //j = i;
                            prox = ms.getLinha(linha.get(i));//atravez do pai do token
                            
                            linha.remove(i);
                            str = "";
                            for (String item : prox) {
                                str += item+" ";
                            }
                            linha.add(i, str);
                            i--;
                        }else{
                            str = linha.get(i);
                            linha.remove(i);
                            linha.add(i, CorrigirArquivo(str, ms));
                            i--;
                        }                            
                    }
                }
                ant = ms.getAnterior(ant);
            }
            
            p.Push("$");
            while(aux.getTopo() != null)
                p.Push(aux.Pop());
            
            if(cadeia.equals("tk_prog_fim"))
                p.Push("tk_prog_fim");
        }
        
        if(ant == null)
            erro.add(new Log("Erro do compilador"," montar a pilha","Erro",0,0));
        
        return p;
    }
    
    private static String CorrigirArquivo(String linha, MatrizSintatica ms){
        String str;
        ArrayList<String> prox;
        
        prox = ms.getLinha(linha);
        ArrayList<String> prox2;
        
        if(linha.equals("comp_logica")){//Evita deadlock
            prox.remove(0);
            prox.remove(1);
        }

        str = "";
        for (String item : prox) {
            
            if(item.contains("tk_"))
                str += item+" ";
            else
                str += CorrigirArquivo(item, ms);  
            /*
            prox2 = ms.getLinha(item);
            for (String item2 : prox2) {
                str += item2+" ";
            }*/
        }
        
        return str;
    }
    
    private static void LerBloco(Pilha p, int i, MatrizSintatica ms){
        
    }
}
