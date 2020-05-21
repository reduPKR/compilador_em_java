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
        
        /*Se chegou aqui tem o prog ini e fim*/
        for(i = 2; i < tabela.size()-2; i++){
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
            if(tabela.get(i).getToken().equals("tk_bloco_if")){
                linha = new ArrayList();
                bloco = new ArrayList();
                
                i+=2;//pula para os dados
                for(; i < tabela.size() && !tabela.get(i).getToken().equals("tk_fecha_par"); i++){
                    linha.add(tabela.get(i).getCadeia());
                }
                i++;//pula {
                for(; i < tabela.size() && !tabela.get(i).getToken().equals("tk_fecha_chav"); i++){
                    bloco.add(new Tokens(tabela.get(i).getCadeia(),tabela.get(i).getToken() , 0, 0));
                }                
                
                /*Bloco*/
                aux = Converter(Construir(bloco, r));
                
                con.setR(r);
                cod = con.ExcutarIf(linha, aux);
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
}
