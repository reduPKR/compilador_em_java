package compiladorred;

import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import org.fxmisc.richtext.CodeArea;

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
    
    CodeArea code;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        iniciarCodeArea();
    }    
    
    public void iniciarCodeArea()
    {        
        code = new CodeArea();
        code = CodeAreaClass.IniciarCodeArea(code);
   
        a_pane.getChildren().add(code);
    }

    @FXML
    private void btn_Compilar(ActionEvent event) {
    }

    @FXML
    private void btn_Limpar(ActionEvent event) {
        iniciarCodeArea();
    }

    @FXML
    private void btn_Exemplo(ActionEvent event) {
        code = new CodeArea();
        code = CodeAreaClass.ExemploCodigo(code); 
        a_pane.getChildren().add(code);
    }
}
