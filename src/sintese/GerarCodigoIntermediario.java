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
        int i, j;
        ArvoreTresEnderecos t = new ArvoreTresEnderecos();
        ArrayList<String> linha;
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
                
                cod = t.Excutar(linha, tipo);
                for(j = 0; j < cod.size(); j++)
                    salvar += cod.get(j)+'\n';
            }
        }
        geraArquivo(salvar);
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
}
