package sintese;

import java.util.ArrayList;

public class Calculos {
    private Registrador r;
    
    public Calculos() {
    }

    public Registrador getR() {
        return r;
    }

    public void setR(Registrador r) {
        this.r = r;
    }

    /***********************Arvore de tres enderecos*******************************************************/
    public ArrayList<String> ExcutarCalculo(ArrayList<String> linha, String tipo){
       ArrayList<String> cod = new ArrayList();
       String reg, rv1, rv2;
       
       /*Verifica se a variavel tem registrador*/
        reg = r.getRegistrador(linha.get(0));
        if(linha.size() == 3){//x = n
            rv1 = r.getRegistrador(linha.get(2));//Verifica se é variavel
            
            if(reg == null){
                reg = "r"+(r.getMax()+1);
                r.Add(linha.get(0), reg);
            }  
            
            if(rv1 == null)
                cod.add("load "+reg+", "+linha.get(2));
            else
                cod.add("load "+reg+", "+rv1);
        }else{
            ArrayList<String> aux = new ArrayList();
            String[] ordem = new String[100];
            String [][]operador = {{"(",""},{"*","mult"},{"/","div"},{"+","add"},{"-","sub"}};
            String []temp = {"ra","rb","rc","rd","re","rf"};
            Boolean ctr = false;
            int i, j, k, tl, ctrTemp, r1, r2, pos;
            
            for(i = 0; i < 100; i++){
                ordem[i] = "";
            }            
            
            tl = ctrTemp = 0;
            for(i = 0; i < 5; i++){
                for(j = 0; j < linha.size(); j++){
                    if(linha.get(j).contains(operador[i][0])){
                        if(i == 0){
                           /* min = verificaRegistradores(linha.get(j+1),linha.get(j+3),temp);
                            
                            if(min == -1){
                                ordem[tl++] = "load "+temp[ctrTemp++]+", "+linha.get(j+1);//Carrega val no registrador t1
                                ordem[tl++] = "load "+temp[ctrTemp++]+", "+linha.get(j+3);//Carrega val no registrador t2
                            }
                            
                            
                            for(k = 0; k < 5; k++){
                                if(linha.get(j+2).contains(operador[k][0])){                                    
                                    if(operador[k][1].equals("add")){
                                        if(tipo.equals("int"))
                                            ordem[tl] += "addi";
                                        else
                                            ordem[tl] += "addf";
                                    }
                                    
                                    k = 5;
                                }
                            }
                            
                            ordem[tl] += " "+temp[ctrTemp-2];//temp3 por conta de reuso
                            ordem[tl] += ", "+temp[ctrTemp-1];//temp2
                            ordem[tl] += ", "+temp[ctrTemp-2];//temp1
                            tl++;
                            
                            for(k = 0; k < 5; k++)//( ate )
                                linha.remove(j);
                            linha.add(j,temp[ctrTemp-2]);
                            
                            if(min == -1)
                                ctrTemp -= 1;
                            else
                                ctrTemp = 0;*/
                        }else{
                            rv1 = r.getRegistrador(linha.get(j-1));
                            rv2 = r.getRegistrador(linha.get(j+1));
                            
                            r1 = verificaRegistradores(linha.get(j-1),temp);
                            if(r1 == -1){//Nenhum registrador
                                if(rv1 == null)
                                    ordem[tl++] = "load "+temp[ctrTemp++]+", "+linha.get(j-1);//Carrega val no registrador t1
                                else
                                    ordem[tl++] = "move "+temp[ctrTemp++]+", "+rv1;
                            }
                            r2 = verificaRegistradores(linha.get(j+1),temp);
                            if(r2 == -1){//Nenhum registrador
                                if(rv2 == null)
                                    ordem[tl++] = "load "+temp[ctrTemp++]+", "+linha.get(j+1);//Carrega val no registrador t2
                                else
                                    ordem[tl++] = "move "+temp[ctrTemp++]+", "+rv2;//Carrega val no registrador t2
                            }                                
                            
                            pos = ctrTemp;//como posso entrar no mult ou no add
                            if(operador[i][1].equals("mult") || operador[i][1].equals("div")){
                                ctrTemp -= 2;
                                
                                if(operador[i][1].equals("mult")){
                                    aux = mult(linha.get(j-1), linha.get(j+1), tipo, temp[ctrTemp++]);
                                }else
                                if(operador[i][1].equals("div")){
                                    aux = div(linha.get(j-1), linha.get(j+1), tipo, temp[ctrTemp++]);
                                }
                                
                                tl -= 2;
                                for(k = 0; k < aux.size(); k++){
                                    ordem[tl++] = aux.get(k);
                                }
                                pos = ctrTemp-1;
                                
                                if(r1!= -1 && r2 != -1)
                                    ctrTemp -= 2;
                                /*else
                                    ctrTemp -= 1;*/
                            }else
                            if(operador[i][1].equals("add") || operador[i][1].equals("sub")){
                                if(operador[i][1].equals("sub")){
                                    ordem[tl-1] = "load "+temp[ctrTemp-1]+", -"+linha.get(j+1);
                                }
                                
                                if(tipo.equals("int"))
                                    ordem[tl] += "addi";
                                else
                                    ordem[tl] += "addf";
                                
                                ordem[tl] += " "+temp[ctrTemp-2];//temp3
                                ordem[tl] += ", "+temp[ctrTemp-1];//temp2
                                ordem[tl] += ", "+temp[ctrTemp-2];//temp1
                                tl++;
                                pos = ctrTemp-2;
                                
                                if(r1!= -1 && r2 != -1)
                                    ctrTemp -= 2;
                                else
                                    ctrTemp -= 1;
                            }
                            
                            for(k = 0; k < 3; k++)
                                linha.remove(j-1);
                            linha.add(j-1,temp[pos]);
                        }
                        ctr = true;//Faz repetir denovo caso adicione;
                    }
                }
                if(ctr){
                    i--;//Controla pra só sair quando tiver excluido todas operacoes desse tipo
                    ctr = false;
                }
            }
            for(k = 0; k < tl; k++){
                cod.add(ordem[k]);
            }

            if(reg == null){
                reg = "r"+(r.getMax()+1);
                r.Add(linha.get(0), reg);
            }
            cod.add("move "+reg+", "+temp[ctrTemp-1]);
            ctrTemp = 0;
       }
       
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
    
    public ArrayList<String> mult(String val1, String val2, String tipo, String temp){
        ArrayList<String> cod = new ArrayList();
        String flag;
        
        if(tipo.equals("int"))
            flag = "i";
        else
            flag = "f";
        
        cod.add("load r0, 0");//contador
        cod.add("load ra, 1");//incremento
        cod.add("load rb, "+val1);//val 1
        cod.add("load rc, "+val2);//val 2
        cod.add("load rd, 0");//result
        cod.add("jmpEQ rc=r0, Fim");//mult por 0
        cod.add("incremento: addi rd, rd, rb");
        cod.add("add"+flag+" r0, r0, ra");
        cod.add("jmpEQ rc=r0, Fim");
        cod.add("jmp incremento");
        cod.add("Fim: move "+temp+", rd");// só pra pular a instrucao
        return cod;
    }
    
    public ArrayList<String> div(String val1, String val2, String tipo, String temp){
        ArrayList<String> cod = new ArrayList();
        String flag;
        
        if(tipo.equals("int"))
            flag = "i";
        else
            flag = "f";
        
        cod.add("load r0, 0");
        cod.add("load ra, 1");//incremento
        cod.add("load rb, "+val1);//val 1
        cod.add("load rc, -"+val2);//val 2
        cod.add("load rd, 0");//result
        cod.add("jmpEQ rb=r0, Fim");//mult por 0
        cod.add("div: addi rd, rd, ra");
        cod.add("add"+flag+" rb, rb, rc");
        cod.add("jmpEQ rb=r0, Fim");
        cod.add("jmp div");
        cod.add("Fim: move "+temp+", rd");// só pra pular a instrucao
        return cod;
    }
    
    /*********************************************************************************************************/
}
