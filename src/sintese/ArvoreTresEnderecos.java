package sintese;

import java.util.ArrayList;

public class ArvoreTresEnderecos {
    private Registrador r = new Registrador();
    
    public ArvoreTresEnderecos() {
    }
    

    public ArrayList<String> Excutar(ArrayList<String> linha, String tipo){
       ArrayList<String> cod = new ArrayList();
       String reg;
       
       reg = r.getRegistrador(linha.get(0));
       if(linha.size() == 3){//x = n
            if(reg == null){
                reg = "r"+r.getMax();
                r.Add(linha.get(0), reg);
            }else{
                r.Add(linha.get(0), reg);
            }  
            cod.add("load "+reg+", "+linha.get(2));
       }else{
            String[] ordem = new String[linha.size()];
            String [][]operador = {{"(",""},{"*",""},{"/",""},{"+","add"},{"-",""}};
            int i, j, k, tl;
            
            tl = 0;
            for(i = 0; i < 5; i++){
                for(j = 0; j < linha.size(); j++){
                    if(linha.get(j).contains(operador[i][0])){
                        ordem[tl] = "";
                        if(i == 0){
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
                            
                            ordem[tl] += linha.get(j+1);
                            ordem[tl] += ", "+linha.get(j+3);
                            tl++;
                            j+=3;
                        }else{
                            ordem[tl] = operador[i][1];
                            
                            if(ordem[tl].equals("add")){
                                if(tipo.equals("int"))
                                    ordem[tl] += "i";
                                else
                                    ordem[tl] += "f";
                            }
                            
                            ordem[tl] += " "+linha.get(j-1);
                            ordem[tl] += ", "+linha.get(j+1);
                            tl++;
                            j+=1;
                        }
                    }
                }
            }
       }
       
       return cod;
   }
}
