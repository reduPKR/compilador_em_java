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
        }else
        if(!VerificaAndOr(linha)){
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
                if(linha.get(1).equals(">=") || linha.get(1).equals(">")){
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
        }else{
            ctr = 0;
            int pos;
            
            for(int j = 0; j < 2; j++){
                if(j == 0){
                    pos = 1;
                }
                else{
                    pos = 5;
                }
                if(linha.get(pos).equals("==") || linha.get(pos).equals("!=") || linha.get(pos).equals(">") || linha.get(pos).equals(">=")){
                    rv1 = r.getRegistrador(linha.get(pos-1));
                    rv2 = r.getRegistrador(linha.get(pos+1));
                    r1 = verificaRegistradores(linha.get(pos-1),temp);
                    r2 = verificaRegistradores(linha.get(pos+1),temp);

                    if(r1 == -1){//Nenhum registrador
                        if(j == 0){
                            if(rv1 == null)
                                cod.add("load r0, "+linha.get(pos-1));//Carrega val no registrador t1
                            else
                                cod.add("move r0, "+rv1);
                        }else{
                            if(rv1 == null)
                                cod.add("PT2: load r0, "+linha.get(pos-1));//Carrega val no registrador t1
                            else
                                cod.add("PT2: move r0, "+rv1);
                        }
                    }

                    if(r2 == -1){//Nenhum registrador
                        if(rv2 == null)
                            cod.add("load "+temp[ctr++]+", "+linha.get(pos+1));//Carrega val no registrador t1
                        else
                            cod.add("move "+temp[ctr++]+", "+rv2);
                        rv2 = temp[ctr-1];
                    }

                    if(linha.get(pos).equals(">")){
                        cod.add("load "+temp[ctr]+", -1");
                        cod.add("addi r0, r0,"+temp[ctr]);
                   }

                    if(linha.get(pos).equals("==")){
                        cod.add("jmpEQ "+rv2+"=r0, CTR"+(j+1));
                        cod.add("jmp FIM");
                    }else
                    if(linha.get(pos).equals("!=")){
                        cod.add("jmpEQ "+rv2+"=r0, FIM");
                    }else
                    if(linha.get(pos).equals(">=") || linha.get(pos).equals(">")){
                        cod.add("jmpLE "+rv2+"<=r0, CTR"+(j+1));
                        cod.add("jmp FIM");
                }
                }else{
                    if(linha.get(pos).equals("<=") || linha.get(pos).equals("<")){
                        rv1 = r.getRegistrador(linha.get(pos+1));
                        rv2 = r.getRegistrador(linha.get(pos-1));
                        r1 = verificaRegistradores(linha.get(pos+1),temp);
                        r2 = verificaRegistradores(linha.get(pos-1),temp);

                        if(r1 == -1){//Nenhum registrador
                            if(j == 0){
                                if(rv1 == null)
                                    cod.add("load r0, "+linha.get(pos+1));//Carrega val no registrador t1
                                else
                                    cod.add("move r0, "+rv1);
                            }else{
                                if(rv1 == null)
                                    cod.add("PT2: load r0, "+linha.get(pos+1));//Carrega val no registrador t1
                                else
                                    cod.add("PT2: move r0, "+rv1);
                            }
                        }

                        if(r2 == -1){//Nenhum registrador
                            if(rv2 == null)
                                cod.add("load "+temp[ctr++]+", "+linha.get(pos-1));//Carrega val no registrador t1
                            else{
                                cod.add("move "+temp[ctr++]+", "+rv2);
                            }
                            rv2 = temp[ctr-1];
                        }

                        if(linha.get(1).equals("<")){
                             cod.add("load "+temp[ctr]+", 1");
                             cod.add("addi "+temp[ctr-1]+","+temp[ctr-1]+","+temp[ctr]);
                        }

                        cod.add("jmpLE "+rv2+"<=r0, CTR"+(j+1));
                        cod.add("jmp FIM");
                    }
                }
            }            
            
            cod.add("CTR1: load r7, 1");
            cod.add("jmp PT2");
            cod.add("CTR2: load r0, 1");
            //cod.add("jmpEQ r7=r0,BLOCO");
            cod.add("BLOCO: "+bloco.get(0));
            for(i = 1; i < bloco.size(); i++)
                cod.add(bloco.get(i));
        }
        
        cod.add("FIM: load r0, 0");
        return cod;
    }
    
    public ArrayList<String> ExcutarWhile(ArrayList<String> linha, ArrayList<String> bloco){
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
            
            cod.add("WHILE: jmpEQ "+reg+" = r0, FIM");
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
                        cod.add("WHILE: load r0, "+linha.get(0));//Carrega val no registrador t1
                    else
                        cod.add("WHILE: move r0, "+rv1);
                    
                    if(linha.get(1).equals(">")){
                        cod.add("load ra, -1");
                        cod.add("addi r0, r0, ra");
                    }
                }

                if(r2 == -1){//Nenhum registrador
                    if(rv2 == null)
                        cod.add("load "+temp[ctr++]+", "+linha.get(2));//Carrega val no registrador t1
                    else
                        cod.add("move "+temp[ctr++]+", "+rv2);
                    rv2 = temp[ctr-1];
                }
                
                if(linha.get(1).equals("==")){
                    cod.add("jmpEQ "+rv2+"=r0, BLOCO");
                    cod.add("jmp FIM");
                }else
                if(linha.get(1).equals("!=")){
                    cod.add("jmpEQ "+rv2+"=r0, FIM");
                }else
                if(linha.get(1).equals(">=") || linha.get(1).equals(">")){
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
                            cod.add("WHILE: load r0, "+linha.get(2));//Carrega val no registrador t1
                        else
                            cod.add("WHILE: move r0, "+rv1);
                    
                        if(linha.get(1).equals("<")){
                            cod.add("load ra, -1");
                            cod.add("addi r0, r0, ra");
                        }
                    }

                    if(r2 == -1){//Nenhum registrador
                        if(rv2 == null)
                            cod.add("load "+temp[ctr++]+", "+linha.get(0));
                        else{
                            cod.add("move "+temp[ctr++]+", "+rv2);
                        }
                        rv2 = temp[ctr-1];
                    }
                    
                    cod.add("jmpLE "+rv2+"<=r0, BLOCO");
                    cod.add("jmp FIM");
                }
            }
            
            
            cod.add("BLOCO: "+bloco.get(0));
            for(i = 1; i < bloco.size(); i++)
                cod.add(bloco.get(i));
        }
        
        cod.add("jmp WHILE");
        cod.add("FIM: load r0, 0");
        return cod;
    }
    
    public ArrayList<String> ExcutarLoop(String linha, ArrayList<String> bloco){
       ArrayList<String> cod = new ArrayList();
       String reg;
       int i, aux;
       
       /*Verifica se a variavel tem registrador*/
        reg = r.getRegistrador(linha);
        cod.add("load r0, 0");
        if(reg == null){
            reg = "r"+(r.getMax()+1);
            r.Add(linha, reg);

            aux = Integer.parseInt(linha);
            if(aux < 0)
                cod.add("load rf, "+linha);
            else
                cod.add("load rf, -"+linha);            
        }else{
            cod.add("jmpLE "+reg+" <= r0, IGNORA");
            
            cod.add("move r0,"+reg);
            cod.add("load rc, -1");
            cod.add("load rd, 1");//faz uma repeticao a menos
            cod.add("load rf, 0");
            cod.add("NEGAR: addi rf, rf, rc");
            cod.add("addi r0, r0, rc");
            cod.add("jmpLE rd <= r0, NEGAR");
            cod.add("jmp INICIA");
            
            cod.add("IGNORA: move rf,"+reg);
        }
        
        cod.add("INICIA: load re, 1");
        cod.add("LOOP: jmpEQ rf = r0, FIM");
        cod.add("jmpLE rf <= r0, BLOCO");// R0 é sempre 0 e rf vai ser sempre negativo
        cod.add("BLOCO: "+bloco.get(0));
        for(i = 1; i < bloco.size(); i++)
            cod.add(bloco.get(i));
        
        cod.add("addi rf, rf, re");
        cod.add("jmp LOOP");
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
    
    private Boolean VerificaAndOr(ArrayList<String> linha){
        Boolean aux = false;
        for(int i = 0; i < linha.size(); i++){
            if(linha.get(i).equals("&&") || linha.get(i).equals("||"))
                aux = true;
        }
        return aux;
    }
}
