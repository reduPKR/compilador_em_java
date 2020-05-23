package utils;
 
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

public class CodeAreaClass {
    private static int width = 801;
    private static int height = 538;    
    private static ArrayList <String> codigo;
    private static String codOrig;
    
    private static final String[] TIPOS = new String[]{"int", "double", "string", "bool"};
    private static final String[] BLOCOS = new String[]{"if", "else", "while", "loop"};
    
    private static final String PROGRAMA_PATTERN = "\\<\\?|\\?\\>";
    private static final String TIPO_PATTERN = "\\b(" + String.join("|", TIPOS) + ")\\b";
    private static final String BLOCO_PATTERN = "\\b(" + String.join("|", BLOCOS) + ")\\b";
    private static final String LIMITE_PATTERN = "\\(|\\)|\\{|\\}";
    private static final String ATRIBUICAO_PATTERN = "\\=";
    private static final String ARITMETICO_PATTERN = "\\+|\\-|\\/|\\*|\\^";
    private static final String COMPARACAO_PATTERN = "\\=\\=|\\!\\=|\\>|\\<|\\>\\=|\\<\\=";
    private static final String LOGICO_PATTERN = "\\&\\&|\\|\\|";
    private static final String LETRA_PATTERN = "[a-zA-Z]+";
    private static final String NUMEROS_PATTERN = "[0-9]+| [0-9]+.[0-9]+";
    private static final String FIM_LINHA_PATTERN = ";";
    private static final String STRING_PATTERN = "'";
    private static final String COMENTARIO_PATTERN = "\\$";
    
    private static final Pattern PATTERN = Pattern.compile(            
            "(?<PROGRAMA>" + PROGRAMA_PATTERN + ")" +
            "|(?<TIPO>" + TIPO_PATTERN + ")" +
            "|(?<BLOCO>" + BLOCO_PATTERN + ")" +
            "|(?<LIMITE>" + LIMITE_PATTERN + ")" +
            "|(?<ATRIBUICAO>" + ATRIBUICAO_PATTERN + ")" +
            "|(?<ARITMETICO>" + ARITMETICO_PATTERN + ")" +
            "|(?<COMPARACAO>" + COMPARACAO_PATTERN + ")" +
            "|(?<LOGICO>" + LOGICO_PATTERN + ")" +
            "|(?<LETRA>" + LETRA_PATTERN + ")" +
            "|(?<NUMEROS>" + NUMEROS_PATTERN + ")" +
            "|(?<FIMLINHA>" + FIM_LINHA_PATTERN + ")" +
            "|(?<STRING>" + STRING_PATTERN + ")" +
            "|(?<COMENTARIO>" + COMENTARIO_PATTERN + ")"     
    );
    
    public static CodeArea IniciarCodeArea(CodeArea ca){
        CodeArea code = ca;
        
        code.setPrefSize(width, height);
        code.setParagraphGraphicFactory(LineNumberFactory.get(code));
        
        code.textProperty().addListener((obs, oldText, newText) -> 
        {
            code.setStyleSpans(0, computeHighlighting(newText));
        });
        code.getStylesheets().add("style/Cores.css");
    
        return code;
    }
    
    private static StyleSpans<Collection<String>> computeHighlighting( String text){
        boolean flagComp = false;
        boolean flagStr = false;
        boolean flagComent = false;
        
        ArrayList <String> texto = new ArrayList();
        ArrayList <Integer> len1 = new ArrayList();
        ArrayList <Integer> len2 = new ArrayList();
        
        setCode(text);
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        
        while (matcher.find()) 
        {
            String styleClass = SelecionarClasse(matcher);
                
            if(styleClass != null && styleClass.equals("comentario"))
                flagComent = !flagComent;               
            styleClass = (flagComent) ? "comentario" : styleClass;

            if(styleClass != null && styleClass.equals("string"))
                flagStr = !flagStr;               
            styleClass = (flagStr) ? "string" : styleClass;

            styleClass = (styleClass != null && styleClass.equals("atribuicao") && flagComp) ? "compracao" : styleClass;
            if(styleClass != null && (styleClass.equals("compracao") || styleClass.equals("atribuicao"))){
                if(flagComp)
                    texto.set(texto.size()-1, styleClass);

                flagComp = !flagComp;
            }
            else
                flagComp = false;
            
            assert styleClass != null;
            len1.add(matcher.start() - lastKwEnd);
            len2.add(matcher.end() - matcher.start());
            texto.add(styleClass);
            lastKwEnd = matcher.end();
        }
        for (int i = 0; i < texto.size(); i++) {
            spansBuilder.add(Collections.emptyList(), len1.get(i));
            spansBuilder.add(Collections.singleton(texto.get(i)),  len2.get(i));
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
    
    private static String SelecionarClasse(Matcher matcher){
        return matcher.group("PROGRAMA") != null ? "programa":
                matcher.group("TIPO") != null ? "tipo": 
                matcher.group("BLOCO") != null ? "bloco": 
                matcher.group("LIMITE") != null ? "limite":   
                matcher.group("ATRIBUICAO") != null ? "atribuicao":
                matcher.group("ARITMETICO") != null ? "aritmetico": 
                matcher.group("COMPARACAO") != null ? "compracao": 
                matcher.group("LOGICO") != null ? "logico":
                matcher.group("LETRA") != null ? "letra":
                matcher.group("NUMEROS") != null ? "numero":
                matcher.group("FIMLINHA") != null ? "fim_linha":
                matcher.group("STRING") != null ? "string":
                matcher.group("COMENTARIO") != null ? "comentario":
                null;
    }
    
    public static CodeArea ExemploCodigo(CodeArea ca){
        CodeArea code = ca;
        
        code.setPrefSize(width, height);
        code.setParagraphGraphicFactory(LineNumberFactory.get(code));
        
        String exemplo = "<?\n"
                + "int i = 5;\n"
                + "int aux = i;\n"
                + "double d = 3.25;\n"
                + "string s = 'frase';\n"
                + "bool b = 1;\n"
                + "$ Comentario $\n"
                + "if(d <= 10 && d != aux || i >= aux){\n"
                + "     s = 'entrou no if';\n"
                + "     while(b){\n"
                + "         loop(i){\n"
                + "             aux = aux+1;\n"
                + "         }\n"
                + "         b = 0;\n"
                + "     }\n"
                + "     aux = i*5 + aux^2 - 4;\n"
                + "     if(d == aux){\n"
                + "         d = d/2;\n"
                + "     }\n"
                + "}\n"
                + "else{\n"
                + " s = 'entrou no else';\n"
                + "}\n"
                + "?>";
        code.replaceText(0, 0, exemplo);        
        code.setStyleSpans(0, computeHighlighting(exemplo));
        
        code.textProperty().addListener((obs, oldText, newText) -> 
        {
            code.setStyleSpans(0, computeHighlighting(newText));
        });
        code.getStylesheets().add("style/Cores.css");
    
        return code;
    }
    
    
    private static void setCode(String texto){
        texto = texto.replaceAll("\t", " ");
        
        codOrig = texto;
        String[] items = texto.split("\n");
        codigo = new ArrayList<String>();
        
        for (String item : items) {
            codigo.add(item);
        }
    }
    
    public static ArrayList getCode(){
        return codigo;
    }
    
    public static void setErro(CodeArea ca, int linha){
        linha--;
        ArrayList<String> e = new ArrayList();
        e.add("erro");
        ca.setParagraphStyle(linha,e);
    }
    
     public static void LimparErros(CodeArea ca, int linha){
        linha--;
        ArrayList<String> e = new ArrayList();
        e.add("erroCorrigido");
        ca.setParagraphStyle(linha,e);
    }
}
