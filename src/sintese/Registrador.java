package sintese;

import java.util.ArrayList;

public class Registrador {
    private ArrayList<No> lista = new ArrayList();

    public Registrador() {
    }
    
    public void Add(String cadeia, String registrador) {
        lista.add(new No(cadeia, registrador));
    }
    
    public int getMax() {
        return lista.size();
    }
    
    public String getRegistrador(String cadeia){
        String str = null;
        
        for(No no: lista){
            if(no.getCadeia().equals(cadeia))
                str = no.getRegistrador();
        }
        
        return str;
    }
    
    public String getCadeia(String reg){
        String str = null;
        
        for(No no: lista){
            if(no.getRegistrador().equals(reg))
                str = no.getCadeia();
        }
        
        return str;
    }
}
