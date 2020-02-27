package compiladorred;

import java.awt.event.KeyEvent;
import java.beans.EventHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import org.fxmisc.richtext.CodeArea;

//import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        iniciarCodeArea();
    }    
    
    public void iniciarCodeArea()
    {        
        CodeArea code = new CodeArea();
        code = CodeAreaClass.IniciarCodeArea(code);
   
        a_pane.getChildren().add(code);
    }
}
