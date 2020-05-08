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

        if(!lista.get(i).getToken().contains("tk_prog_ini"))
            erro.add(new Log("Erro sintatico", " tk_prog_ini (<?) não encontrado", "Erro",lista.get(0).getLinha()+1,lista.get(0).getColuna()+1));
        else{
            erro.add(new Log("Analise sintatica", " sintaxe tk_prog_ini (<?) reconhecida","Log",lista.get(0).getLinha()+1,1));
            i++;
        }
        
        Boolean chave = LerLinha(i, false, ms);
        
        if(chave)
            erro.add(new Log("Erro sintatico", " tk_fecha_chav (}) a mais encontrado ","Erro",lista.get(lista.size()-2).getLinha()+1,1));
        
        if(!lista.get(lista.size()-2).getToken().contains("tk_prog_fim"))
            erro.add(new Log("Erro sintatico", " tk_prog_fim (?>) não encontrado","Erro",lista.get(lista.size()-2).getLinha()+1,lista.get(lista.size()-2).getColuna()+1));
        else
            erro.add(new Log("Analise sintatica", " sintaxe tk_prog_fim (?>) reconhecida","Log",lista.get(lista.size()-2).getLinha()+1,lista.get(lista.size()-2).getColuna()+1));
    }
    
    private static Boolean LerLinha(int i, Boolean bloco, MatrizSintatica ms){
        int j, k, next;
        String cadeia, aux, atual;
        ArrayList<String> ant;
        ArrayList <Log> logChave;
        Boolean flag, flgChave;
        Log l;
        Pilha p;
        
        k = 0;
        flgChave = false;
        ms.IniciarRegra();
        for(; i < lista.size() && !lista.get(i).getToken().equals("tk_prog_fim") && !flgChave; i++) {
            logChave = null;
            l = null;
            if(!lista.get(i).getCadeia().equals("\n")){
                cadeia = lista.get(i).getToken();
                ant = ms.getRotas(cadeia, true);//Retorna todos anteriores que levam diretamente a cadeia
                
                flag = false;
                for(j = 0; j < ant.size() && !flag && !flgChave; j++){
                    k = i;
                    
                    atual = ant.get(j);
                    p = Povoar(cadeia, ms);

                    aux = p.Pop();
                    if(aux.contains("tk_fecha_chav") && bloco){
                        flgChave = true;            
                    }else{
                        if(aux.contains(cadeia)){
                            k++;
                            while(k < lista.size() && !lista.get(k).getCadeia().equals("\n")){
                                cadeia = lista.get(k).getToken();
                                if(p.getTopo() != null){
                                    aux = p.Pop();
                                    if(aux.contains("tk_fecha_chav") && bloco){
                                        flgChave = true;                        
                                    }else{
                                        if(!aux.contains(cadeia)){
                                            if(!aux.equals("$"))
                                                l = new Log("Erro sintatico", " sintaxe incorreta do "+atual+" token esperado "+aux+" token usado "+cadeia,"Erro",lista.get(k).getLinha()+1,lista.get(k).getColuna()+1); 
                                            else
                                                l = new Log("Erro sintatico", " sintaxe incorreta do "+atual,"Erro",lista.get(k).getLinha()+1,lista.get(k).getColuna()+1); 

                                            k = lista.size();
                                        }   
                                        if(aux.contains("tk_comp")){
                                            if(lista.get(k+1).getToken().contains("tk_comp")){
                                                Pilha p2 = new Pilha();
                                                p2.Push(lista.get(k).getToken());
                                                p2.Push(lista.get(k+1).getToken());
                                                p2.Push(lista.get(k+2).getToken());
                                                
                                                if(!TratarComparacao(p2, ms)){
                                                    erro.add(new Log("Erro sintatico", " sintaxe de comparacao incorreta  ","Erro",lista.get(k).getLinha()+1,lista.get(k).getColuna()+1));
                                                }
                                                k+=2;
                                           }                                            
                                        }
                                    }
                                }else{
                                    l = new Log("Erro sintatico", " sintaxe incorreta do "+atual,"Erro",lista.get(k).getLinha()+1,lista.get(k).getColuna()+1);
                                    k = lista.size();
                                }

                                k++;
                            }

                            if(!flgChave){
                                if(p.getTopo() != null){
                                    aux = p.Pop();
                                }

                                if(k < lista.size()){
                                    if(lista.get(k).getCadeia().equals("\n") && (aux.contains("$") || aux.contains("tk_abre_chav") || aux.contains("bloco")) && !cadeia.contains("tk_prog_fim")){
                                        l = new Log("Analise sintatica", " sintaxe "+atual+" reconhecida","Log",lista.get(k).getLinha()+1,1);
                                        flag = true;
                                    }
                                }

                                /*Inicia bloco*/
                                if(cadeia.contains("tk_abre_chav") || aux.contains("tk_abre_chav")){
                                    int ctr = -1;
                                    /*Remove caso nao fosse o anterior*/
                                    logChave = new ArrayList();
                                    
                                    if(lista.get(k-1).getToken().contains("tk_abre_chav"))
                                        ctr = k-1;
                                    else
                                    if(lista.get(k).getToken().contains("tk_abre_chav"))
                                        ctr = k;
                                    
                                    
                                    /*Caso ja tenha atualizado*/
                                    if(ctr != -1){                                            
                                        logChave.add(new Log("Analise sintatica", " tk_abre_chav ({) reconhecida","Log",lista.get(ctr).getLinha()+1,1));
                                        
                                        if(lista.get(k).getCadeia().equals("\n"))
                                            k++;
                                        next = LerBloco(k, ms);
                                        if(next > 0){
                                            logChave.add(new Log("Analise sintatica", " tk_fecha_chav (}) reconhecida","Log",lista.get(next).getLinha()+1,1));
                                            k = next + 1;
                                        }else{
                                            logChave.add(new Log("Erro sintatico", " tk_fecha_chav (}) não encontrado","Erro",lista.get(next).getLinha()+1,1));
                                        }
                                    }else
                                        logChave.add(new Log("Erro sintatico", " tk_abre_chav ({) não encontrado apos o "+atual,"Erro",lista.get(k).getLinha()+1,1));
                                        
                                }
                            }
                        }else
                            l = new Log("Erro sintatico", " sintaxe inicial incorreta"+aux,"Erro",lista.get(k).getLinha()+1,lista.get(k).getColuna()+1);
                    }
                }
                i = k;
            }  
            if(l != null){
                erro.add(l);
                
                if(logChave != null){
                    for(Log item : logChave)
                        erro.add(item);
                }
            }
        }
        return flgChave;
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
                        }
                        else{
                            str = linha.get(i);
                            linha.remove(i);
                            linha.add(i, CorrigirArquivo(str, ms));
                            i--;
                        }                            
                    }
                }
                if(!cadeia.equals("oper_comparacao"))
                    ant = ms.getAnterior(ant);
                else
                    ant = "programa";
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
        
        if(linha.equals("comp_logica") && prox.size()> 1){//Evita deadlock
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
    
    private static int LerBloco(int i, MatrizSintatica ms){       
        if(!lista.get(i).getToken().contains("tk_fecha_chav")){
            if(LerLinha(i, true, ms)){
                while(!lista.get(i).getToken().contains("tk_fecha_chav"))
                    i++;
            }else
                return -1;
        }
        
        return i;
    }
    
    private static Boolean TratarComparacao(Pilha aux, MatrizSintatica ms){
        Boolean flag = true;//false = erro
        
        Pilha p = Povoar("oper_comparacao", ms);
        
        while(flag && !p.getTopo().equals("$") && aux.getTopo() != null){
            if(p.getTopo().getInfo().contains(aux.getTopo().getInfo())){
                p.Pop();
                aux.Pop();
            }else
                flag = false;
        }
        
        return flag;
    }
}
