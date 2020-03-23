package matrizSintatica;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MatrizSintatica {
    private ArrayList<No> regra = new ArrayList();

    public MatrizSintatica() {
        int i;
        String linha;
        String[] cod;
        No no;
        
        try {
            FileReader arq = new FileReader("ArquivoSintatico.txt");
            BufferedReader lerArq = new BufferedReader(arq);
            
            linha = lerArq.readLine(); 
            while (linha != null) {
                if(!linha.equals("")){
                    cod = linha.split(" ");
                    no = new No(cod[0]);
                    for(i = 1; i < cod.length; i++){
                        if(!cod[i].equals("")){
                            no.setProx(cod[i]);
                        }
                    }
                    regra.add(no);
                }
                
                linha = lerArq.readLine(); 
            }
            arq.close();
        } catch (IOException e) {
            e.getMessage();
        }   
    }
    
    public String getAnterior(String cadeia){
        String ret = null;
        
        for (int i = 0; i < regra.size() && ret == null; i++) {
            if(regra.get(i).Pertence(cadeia))
                ret = regra.get(i).getAnt();
        }
        
        return ret;
    }
    
    public ArrayList<String> getRotas(String cadeia){
        ArrayList <String> ret = new ArrayList();
        
        for (int i = 0; i < regra.size(); i++) {
            if(regra.get(i).Pertence(cadeia) && !regra.get(i).getAnt().equals("caracter")){
                if(!regra.get(i).getAnt().equals("variavel"))
                    ret.add(regra.get(i).getAnt());
                else{
                    ret.addAll(getRotas(regra.get(i).getAnt()));
                }
            }
        }
        
        return ret;
    }
    
    public ArrayList<String> getLinha(String cadeia){
        ArrayList<String> ret = null;
        
        int i;
        for (i = 0; i < regra.size() && ret == null; i++) {
            if(regra.get(i).getAnt().equals(cadeia))
                ret = regra.get(i).getProx();
        }
        
        return ret;
    }
}