package sintese;


import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Tokens;

public class GerarCodigoIntermediario {
    private ArrayList <Tokens> tabela;
    private static ArrayList <String> codigo = new ArrayList();;

    public GerarCodigoIntermediario() {
    }
    
    public void setTabela(ArrayList<Tokens> tabela) {
        this.tabela = tabela;
    }
    
    public void Gerar(){
        /*Se chegou aqui tem o prog ini e fim evita perguntas na recursividade*/
        RemoverEscopo();
        Registrador r = new Registrador();
        String salvar = Construir(tabela, r);
        geraArquivo(salvar);
    }
    
    private String Construir(ArrayList<Tokens> tabela, Registrador r){
        int i, j;
        Calculos cal = new Calculos();
        Condicionais con = new Condicionais();
        ArrayList<String> linha;
        ArrayList<String> aux;
        ArrayList<Tokens> bloco;
        ArrayList<String> cod;
        String salvar = "";
        
        for(i = 0; i < tabela.size(); i++){
            if(tabela.get(i).getToken().equals("tk_oper_atrib")){
                linha = new ArrayList();
                
                i--;
                String tipo = tabela.get(i).getTipo();
                for(; i < tabela.size() && !tabela.get(i).getToken().equals("tk_fim_lin"); i++)
                    linha.add(tabela.get(i).getCadeia());
                
                cal.setR(r);
                cod = cal.ExcutarCalculo(linha, tipo);
                r = cal.getR();
                
                for(j = 0; j < cod.size(); j++)
                    salvar += cod.get(j)+'\n';
            }else
            if(tabela.get(i).getToken().equals("tk_bloco_if") || tabela.get(i).getToken().equals("tk_bloco_while") || tabela.get(i).getToken().equals("tk_bloco_loop")){
                linha = new ArrayList();
                bloco = new ArrayList();
                
                i+=2;//pula para os dados
                for(; i < tabela.size() && !tabela.get(i).getToken().equals("tk_fecha_par"); i++){
                    linha.add(tabela.get(i).getCadeia());
                }
                
                while(tabela.get(i).getToken().equals("tk_fecha_par") || tabela.get(i).getToken().equals("tk_abre_chav") || tabela.get(i).getToken().equals("\n"))
                    i++;
                
                for(; i < tabela.size() && !tabela.get(i).getToken().equals("tk_fim_lin"); i++){
                    bloco.add(new Tokens(tabela.get(i).getCadeia(),tabela.get(i).getToken(), tabela.get(i).getTipo(), tabela.get(i).getDado() , 0, 0));
                }                
                
                /*Bloco*/
                aux = Converter(Construir(bloco, r));
                
                con.setR(r);
                if(tabela.get(i).getToken().equals("tk_bloco_if"))
                    cod = con.ExcutarIf(linha, aux);
                else
                if(tabela.get(i).getToken().equals("tk_bloco_while"))
                    cod = con.ExcutarWhile(linha, aux);
                else{
                    //loop só aceita 1 dado seja variavel ou inteiro
                    cod = con.ExcutarLoop(linha.get(0), aux);
                }
                r = con.getR();
                
                for(j = 0; j < cod.size(); j++)
                    salvar += cod.get(j)+'\n';
            }
        }
        
        return salvar;
    }
    
    private void geraArquivo(String codigo) {
        FileWriter arq;
        try {
            arq = new FileWriter("C:\\Users\\REDUA\\Downloads\\codigo.asm");
            PrintWriter gravarArq = new PrintWriter(arq);

            gravarArq.print(codigo);

            arq.close();
        } catch (IOException ex) {
            Logger.getLogger(GerarCodigoIntermediario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private ArrayList<String> Converter(String str){
        ArrayList<String> aux = new ArrayList();
      
        String[] items = str.split("\n");
        for (String item : items) 
           aux.add(item);
        
        return aux;
    }    
    
    private void RemoverEscopo(){
        while(tabela.get(0).getCadeia().equals("<?") || tabela.get(0).getCadeia().equals("\n"))
            tabela.remove(0);
        
        while(tabela.get(tabela.size()-1).getCadeia().equals("?>") || tabela.get(tabela.size()-1).getCadeia().equals("\n"))
            tabela.remove(tabela.size()-1);
    }
}
