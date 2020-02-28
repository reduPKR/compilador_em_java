package utils;
 
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
    private static final String ATRIBUICAO_PATTERN = "=";/*"\\=|"+LETRAS_PATTERN+"\\=";*/
    private static final String ARITMETICO_PATTERN = "\\+|\\-|\\*|\\/";
    private static final String COMPARACAO_PATTERN = "\\==|\\!=|\\>|\\<|\\>=|\\<=";
    private static final String LOGICO_PATTERN = "\\&|\\||";
    private static final String LETRA_PATTERN = "a";
    
    private static final Pattern PATTERN = Pattern.compile(            
            "(?<PROGRAMA>" + PROGRAMA_PATTERN + ")" +
            "|(?<TIPO>" + TIPO_PATTERN + ")" +
            "|(?<BLOCO>" + BLOCO_PATTERN + ")" +
            "|(?<LIMITE>" + LIMITE_PATTERN + ")" +
            "|(?<ATRIBUICAO>" + ATRIBUICAO_PATTERN + ")" +
            "|(?<ARITMETICO>" + ARITMETICO_PATTERN + ")" +
            "|(?<COMPARACAO>" + COMPARACAO_PATTERN + ")" +
            "|(?<LOGICO>" + LOGICO_PATTERN + ")" +
            "|(?<LETRA>" + LETRA_PATTERN + ")"
    );
    
    public static CodeArea IniciarCodeArea(CodeArea ca){
        CodeArea code = ca;
        
        code.setPrefSize(986, 530);
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
                matcher.group("LETRA") != null ? "letra":
                matcher.group("LOGICO") != null ? "logico":
                null;
            
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}
