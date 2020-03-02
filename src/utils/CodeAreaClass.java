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
    private static final String[] TIPOS = new String[]{"int", "double", "string", "bool"};
    private static final String[] BLOCOS = new String[]{"if", "else", "while", "loop"};
    
    private static final String PROGRAMA_PATTERN = "\\<\\?|\\?\\>";
    private static final String TIPO_PATTERN = "\\b(" + String.join("|", TIPOS) + ")\\b";
    private static final String BLOCO_PATTERN = "\\b(" + String.join("|", BLOCOS) + ")\\b";
    private static final String LIMITE_PATTERN = "\\(|\\)|\\{|\\}";
    private static final String ATRIBUICAO_PATTERN = "=";
    private static final String ARITMETICO_PATTERN = "\\+|\\-|\\/|\\*|\\^";
    private static final String COMPARACAO_PATTERN = "\\=\\=|\\!\\=|\\>|\\<|\\>\\=|\\<\\=";
    private static final String LOGICO_PATTERN = "\\&\\&|\\|\\|";
    private static final String LETRA_PATTERN = "[a-zA-Z]+";
    private static final String NUMEROS_PATTERN = "[0-9]+|\\.";
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
        
        code.setPrefSize(986, 540);
        code.setParagraphGraphicFactory(LineNumberFactory.get(code));
        
        code.textProperty().addListener((obs, oldText, newText) -> 
        {
            code.setStyleSpans(0, computeHighlighting(newText));
        });
        code.getStylesheets().add("style/Cores.css");
    
        return code;
    }
    
    private static StyleSpans<Collection<String>> computeHighlighting( String text)
    {
        boolean flag = false;
        ArrayList <String> cod = new ArrayList();
        ArrayList <Integer> len1 = new ArrayList();
        ArrayList <Integer> len2 = new ArrayList();
        
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        
        while (matcher.find()) 
        {
            String styleClass = 
                matcher.group("PROGRAMA") != null ? "programa":
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
            
                styleClass = (styleClass != null && styleClass.equals("atribuicao") && flag) ? "compracao" : styleClass;
                
                if(styleClass != null && (styleClass.equals("compracao") || styleClass.equals("atribuicao"))){
                    if(flag){
                        cod.set(cod.size()-1, styleClass);
                    }
                    
                    flag = !flag;
                }
                else
                    flag = false;
            
            assert styleClass != null;
            len1.add(matcher.start() - lastKwEnd);
            len2.add(matcher.end() - matcher.start());
            cod.add(styleClass);
            lastKwEnd = matcher.end();
        }
        for (int i = 0; i < cod.size(); i++) {
            spansBuilder.add(Collections.emptyList(), len1.get(i));
            spansBuilder.add(Collections.singleton(cod.get(i)),  len2.get(i));
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
    
    public static CodeArea ExemploCodigo(CodeArea ca){
        CodeArea code = ca;
        
        code.setPrefSize(986, 540);
        code.setParagraphGraphicFactory(LineNumberFactory.get(code));
        
        String exemplo = "<? \n"
                + "int i= 5;\n"
                + "int aux= i;\n"
                + "double d= 3.25;\n"
                + "string s= 'frase';\n"
                + "bool b= 1;\n"
                + "$ Comentario $\n"
                + "if(d <= 10 && d != aux || i >= aux){\n"
                + "     s = 'entrou no if'\n"
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
                + " s = 'entrou no else'\n"
                + "}\n"
                + "?>";
        code.replaceText(0, 0, exemplo);
        code.setStyleSpans(0, computeHighlighting(exemplo));
        code.getStylesheets().add("style/Cores.css");
    
        return code;
    }
}
