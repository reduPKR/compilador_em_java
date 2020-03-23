package compiladorred;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import model.Log;
import model.Tokens;
import org.fxmisc.richtext.CodeArea;
import utils.AnaliseSintatica;

import utils.CodeAreaClass;
import utils.CodeAreaErro;

/**
 *
 * @author Aluno
 */
public class MainController implements Initializable {

    @FXML
    private Button btn_Compilar;
    @FXML
    private Button btn_Limpar;
    @FXML
    private AnchorPane a_pane_cod;
    @FXML
    private VBox a_pane_erro;
    @FXML
    private Button btn_Exemplo;
    @FXML
    private TableView<Tokens> tabela;
    @FXML
    private TableColumn<Tokens, String> tc_cadeia;
    @FXML
    private TableColumn<Tokens, String> tc_token;
    
    private CodeArea code;
    private CodeArea erro;    
    ArrayList<Integer> l;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tc_cadeia.setCellValueFactory(new PropertyValueFactory<Tokens, String>("cadeia"));
        tc_token.setCellValueFactory(new PropertyValueFactory<Tokens, String>("token"));
        iniciarCodeArea();
        carregarLista();
    }

    @FXML
    private void btn_Compilar(ActionEvent event) {
        ArrayList <String> texto = CodeAreaClass.getCode();
        
        if(l != null){
            for(Integer item: l)
                CodeAreaClass.LimparErros(code, item);
            l = null;
        }
        
        if(texto != null && texto.size() > 1){
            AnaliseSintatica.setCodigo(texto);
            AnaliseSintatica.IniciarAnalise();
            
            CodeAreaErro.SetLog( AnaliseSintatica.getLog());
            
            l = CodeAreaErro.getLinhas();
            for(Integer item: l)
                CodeAreaClass.setErro(code, item);
            
            carregarLista();
        }else{
            ArrayList<Log> e = new ArrayList();
            e.add(new Log("Erro sintatico", "Arquivo em branco","Erro",0,0));
            CodeAreaErro.SetLog(e);
        }
        
        erro = CodeAreaErro.getLog(erro);
    }

    @FXML
    private void btn_Limpar(ActionEvent event) {
        iniciarCodeArea();
        carregarLista();
    }

    @FXML
    private void btn_Exemplo(ActionEvent event) {
        iniciarCodeArea();
        
        code = CodeAreaClass.ExemploCodigo(code); 
        a_pane_cod.getChildren().add(code);
        carregarLista();
    }
    //-------------metodos------------------------    
    public void iniciarCodeArea(){        
        code = new CodeArea();
        erro = new CodeArea();
        
        code = CodeAreaClass.IniciarCodeArea(code);
        erro = CodeAreaErro.IniciarCodeArea(erro);
   
        a_pane_cod.getChildren().add(code);
        a_pane_erro.getChildren().add(erro);
    }
    
    public void carregarLista(){                
        ObservableList<Tokens> token = AnaliseSintatica.getTabSimbolos();
        if(token != null)
            tabela.setItems(token);
    }
}
