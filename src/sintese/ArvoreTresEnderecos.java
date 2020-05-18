package sintese;

import java.util.ArrayList;

public class ArvoreTresEnderecos {
    private Registrador r = new Registrador();
    
    public ArvoreTresEnderecos() {
    }
    

    public ArrayList<String> Excutar(ArrayList<String> linha, String tipo){
       ArrayList<String> cod = new ArrayList();
       String reg;
       
       /*Verifica se a variavel tem registrador*/
       reg = r.getRegistrador(linha.get(0));
       if(linha.size() == 3){//x = n
            if(reg == null){
                reg = "r"+r.getMax();
                r.Add(linha.get(0), reg);
            }  
            
            cod.add("load "+reg+", "+linha.get(2));
        }else{
            String[] ordem = new String[linha.size()];
            String [][]operador = {{"(",""},{"*",""},{"/",""},{"+","add"},{"-",""}};
            String []temp = {"ra","rb","rc","rd","re","rf"};
            Boolean ctr = false;
            int i, j, k, tl, ctrTemp, min;
            
            for(i = 0; i < linha.size(); i++){
                ordem[i] = "";
            }            
            
            tl = ctrTemp = 0;
            for(i = 0; i < 5; i++){
                for(j = 0; j < linha.size(); j++){
                    if(linha.get(j).contains(operador[i][0])){
                        if(i == 0){
                            min = verificaRegistradores(linha.get(j+1),linha.get(j+3),temp);
                            
                            if(min == -1){
                                ordem[tl++] = "load "+temp[ctrTemp++]+", "+linha.get(j+1);//Carrega val no registrador t1
                                ordem[tl++] = "load "+temp[ctrTemp++]+", "+linha.get(j+3);//Carrega val no registrador t2
                            }
                            
                            /*Parentes tem que ter recursao mas por hora*/
                            for(k = 0; k < 5; k++){
                                if(linha.get(j+2).contains(operador[k][0])){
                                    ordem[tl] = operador[k][1];
                                    
                                    if(ordem[tl].equals("add")){
                                        if(tipo.equals("int"))
                                            ordem[tl] += "i";
                                        else
                                            ordem[tl] += "f";
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
                                ctrTemp = 0;
                        }else{
                            min = verificaRegistradores(linha.get(j-1),linha.get(j+1),temp);
                            
                            if(min == -1){
                                ordem[tl++] = "load "+temp[ctrTemp++]+", "+linha.get(j-1);//Carrega val no registrador t1
                                ordem[tl++] = "load "+temp[ctrTemp++]+", "+linha.get(j+1);//Carrega val no registrador t2
                            }
                            
                            ordem[tl] = operador[i][1]; 
                            if(ordem[tl].equals("add")){
                                if(tipo.equals("int"))
                                    ordem[tl] += "i";
                                else
                                    ordem[tl] += "f";
                            }
                            
                            ordem[tl] += " "+temp[ctrTemp-2];//temp3
                            ordem[tl] += ", "+temp[ctrTemp-1];//temp2
                            ordem[tl] += ", "+temp[ctrTemp-2];//temp1
                            tl++;
                            
                            for(k = 0; k < 3; k++)
                                linha.remove(j-1);
                            linha.add(j-1,temp[ctrTemp-2]);
                            
                            if(min == -1)
                                ctrTemp -= 1;
                            else
                                ctrTemp = 0;
                        }
                        ctr = true;//Faz repetir denovo caso adicione;
                    }
                }
                if(ctr){
                    i--;
                    ctr = false;
                }
            }
            for(k = 0; k < tl; k++){
                cod.add(ordem[k]);
            }

            if(reg == null){
                reg = "r"+r.getMax();
                r.Add(linha.get(0), reg);
            }
            cod.add("move "+reg+", "+temp[ctrTemp-1]);
            ctrTemp = 0;
       }
       
       return cod;
   }
    
    public int verificaRegistradores(String r1, String r2, String []temp){
        int menor, aux1, aux2;
        menor = aux1 = aux2 = -1;
        for(int i = 0; i < 6; i++){
            if(r1.contains(temp[i]))
                aux1 = i;
            else
            if(r2.contains(temp[i]))
                aux2 = i;
        }
        if(aux1 != -1 && aux2 != -1)
            menor = Math.min(aux1, aux2);
        return menor;
    }
}
