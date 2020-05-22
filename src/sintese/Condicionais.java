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
       String reg, rv1, rv2;
       String []temp = {"ra","rb","rc","rd","re","rf"};
       int i, ctr, r1, r2;
       
       /*Verifica se a variavel tem registrador*/
        reg = r.getRegistrador(linha.get(0));
        if(linha.size() == 1){ // true ou false
            cod.add("load r0, 0");
            
            if(reg == null){
                reg = "r"+(r.getMax()+1);
                r.Add(linha.get(0), reg);
                
                cod.add("load "+reg+", "+linha.get(0));
            }
            
            cod.add("jmpEQ "+reg+" = r0, FIM");
            
            for(i = 0; i < bloco.size(); i++)
                cod.add(bloco.get(i));
        }else{
            ctr = 0;
            
            if(linha.get(1).equals("==") || linha.get(1).equals("!=") || linha.get(1).equals(">") || linha.get(1).equals(">=")){
                rv1 = r.getRegistrador(linha.get(0));
                rv2 = r.getRegistrador(linha.get(2));
                r1 = verificaRegistradores(linha.get(0),temp);
                r2 = verificaRegistradores(linha.get(2),temp);
                
                if(r1 == -1){//Nenhum registrador
                    if(rv1 == null)
                        cod.add("load r0, "+linha.get(0));//Carrega val no registrador t1
                    else
                        cod.add("move r0, "+rv1);
                }

                if(r2 == -1){//Nenhum registrador
                    if(rv2 == null)
                        cod.add("load "+temp[ctr++]+", "+linha.get(2));//Carrega val no registrador t1
                    else
                        cod.add("move "+temp[ctr++]+", "+rv2);
                    rv2 = temp[ctr-1];
                }

                if(linha.get(1).equals(">")){
                    cod.add("load "+temp[ctr]+", -1");
                    cod.add("addi r0, r0,"+temp[ctr]);
               }
                
                if(linha.get(1).equals("==")){
                    cod.add("jmpEQ "+rv2+"=r0, BLOCO");
                    cod.add("jmp FIM");
                }else
                if(linha.get(1).equals("!=")){
                    cod.add("jmpEQ "+rv2+"=r0, FIM");
                }else
                if(linha.get(1).equals(">=")){
                    cod.add("jmpLE "+rv2+"<=r0, BLOCO");
                    cod.add("jmp FIM");
                }
            }else{
                if(linha.get(1).equals("<=") || linha.get(1).equals("<")){
                    rv1 = r.getRegistrador(linha.get(2));
                    rv2 = r.getRegistrador(linha.get(0));
                    r1 = verificaRegistradores(linha.get(2),temp);
                    r2 = verificaRegistradores(linha.get(0),temp);
                    
                    if(r1 == -1){//Nenhum registrador
                        if(rv1 == null)
                            cod.add("load r0, "+linha.get(2));//Carrega val no registrador t1
                        else
                            cod.add("move r0, "+rv1);
                    }

                    if(r2 == -1){//Nenhum registrador
                        if(rv2 == null)
                            cod.add("load "+temp[ctr++]+", "+linha.get(0));//Carrega val no registrador t1
                        else{
                            cod.add("move "+temp[ctr++]+", "+rv2);
                        }
                        rv2 = temp[ctr-1];
                    }
                    
                    if(linha.get(1).equals("<")){
                         cod.add("load "+temp[ctr]+", 1");
                         cod.add("addi "+temp[ctr-1]+","+temp[ctr-1]+","+temp[ctr]);
                    }
                    
                    cod.add("jmpLE "+rv2+"<=r0, BLOCO");
                    cod.add("jmp FIM");
                }
            }
            
            
            cod.add("BLOCO: "+bloco.get(0));
            for(i = 1; i < bloco.size(); i++)
                cod.add(bloco.get(i));
        }
        
        
        cod.add("FIM: load r0, 0");
        return cod;
    }
    
    public int verificaRegistradores(String r1, String []temp){
        int  aux1;
        aux1 = -1;
        for(int i = 0; i < 6; i++){
            if(r1.contains(temp[i]))
                aux1 = i;
        }
        
        return aux1;
    }
    
}
