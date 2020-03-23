package matrizSintatica;

import java.util.ArrayList;

public class No {
    private int tl;
    private String ant;
    private ArrayList<String> prox;

    public No(String ant) {
        this.ant = ant;
        prox = new ArrayList();
    }
    
    public String getAnt() {
        return ant;
    }

    public void setAnt(String ant) {
        this.ant = ant;
    }
    
    public String getProx(int i) {
        return prox.get(i);
    }

    public ArrayList<String> getProx() {
        return prox;
    }
    
    public void setProx(String info) {
        tl++;
        this.prox.add(info);
    }
    
    public boolean Pertence(String cadeia){
        boolean flag = false;
        
        for (int i = 0; i < prox.size() && !flag; i++) {
            if(prox.get(i).equals(cadeia))
                flag = true;
        }
        
        return flag;
    }
}
