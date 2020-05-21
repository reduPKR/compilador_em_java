package sintese;

import java.util.ArrayList;

public class Condicionais {
    private Registrador r;
    
    public Condicionais() {
    }

    public Registrador getR() {
        return r;
    }

    public void setR(Registrador r) {
        this.r = r;
    }
    
    public ArrayList<String> ExcutarIf(ArrayList<String> linha, ArrayList<String> bloco){
       ArrayList<String> cod = new ArrayList();
       String reg, rv1;
       
       /*Verifica se a variavel tem registrador*/
        reg = r.getRegistrador(linha.get(0));
        if(linha.size() == 1){ // true ou false
            rv1 = r.getRegistrador(linha.get(2));//Verifica se é variavel
            
            if(reg == null){
                reg = "r"+(r.getMax()+1);
                r.Add(linha.get(0), reg);
            }  
            
            cod.add("load r0, 0");
            if(rv1 == null)
                cod.add("load "+reg+", "+linha.get(0));
            else
                cod.add("load "+reg+", "+rv1);
        }
        
        
        return cod;
    }
}
