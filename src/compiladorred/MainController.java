package compiladorred;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Tokens;
import org.fxmisc.richtext.CodeArea;
import utils.AnaliseSintatica;

import utils.CodeAreaClass;

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
    private TextArea ta_Mensagens;
    @FXML
    private AnchorPane a_pane;
    @FXML
    private Button btn_Exemplo;
    @FXML
    private TableView<Tokens> tabela;
    @FXML
    private TableColumn<Tokens, String> tc_cadeia;
    @FXML
    private TableColumn<Tokens, String> tc_token;
    
    CodeArea code;
    

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
        if(texto != null && texto.size() > 0){
            AnaliseSintatica.setCodigo(texto);
            AnaliseSintatica.IniciarAnalise();
            
            carregarLista();
            carregarLista();
        }
    }

    @FXML
    private void btn_Limpar(ActionEvent event) {
        iniciarCodeArea();
        carregarLista();
    }

    @FXML
    private void btn_Exemplo(ActionEvent event) {
        code = new CodeArea();
        code = CodeAreaClass.ExemploCodigo(code); 
        a_pane.getChildren().add(code);
        carregarLista();
    }
    //-------------metodos------------------------    
    public void iniciarCodeArea()
    {        
        code = new CodeArea();
        code = CodeAreaClass.IniciarCodeArea(code);
   
        a_pane.getChildren().add(code);
    }
    
    public void carregarLista(){                
        ObservableList<Tokens> token = AnaliseSintatica.getTabSimbolos();
        if(token != null)
            tabela.setItems(token);
    }
}
