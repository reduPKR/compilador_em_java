package utils;
 
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

public class CodeAreaClass {
    private static final String[] KEYWORDS = new String[]{"boolean", "char", "double", "else", "false", "for", "if", "int", "mentira", "null", "nulo", "NULL", "program", "string", "true", "verdade", "while"};
    //private static final String[] SIMBOLOS = new String[]{"a, b, c, d, e, f"};
    
    //private static final String SIMBOLOS_PATTERN = "\\b(" + String.join("|", SIMBOLOS) + ")\\b";
    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String LETTERS_PATTERN = "[A-Za-z#`$._]+";
    private static final String SIMBOLOS_PATTERN = "\\{|\\}|\\[|\\]|\\(|\\)|\\%|\\;|\\<|\\>|\\*|\\=|\\?|\\:|\\||\\&|\\+|\\-|\\/|\\~|\\!|\\^";
    private static final String NUMBERS_PATTERN = "[0-9]+";
    private static final String CHAR_PATTERN = "\'[\\S+\\s]{1}\'";
    private static final String STRING_PATTERN = "\\\"([^\\\"\\\\\\\\]|\\\\\\\\.)*\\\"";
    private static final String COMENT_PATTERN = "\\@([^\\@\\\\\\\\]|\\\\\\\\.)*\\@";
    private static final String RESTO_PATTERN = "\\S+";
    
    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")" +
            "|(?<LETTERS>" + LETTERS_PATTERN + ")" +
            "|(?<SIMBOLOS>" + SIMBOLOS_PATTERN + ")" +
            "|(?<NUMBERS>" + NUMBERS_PATTERN + ")" +
            "|(?<CHAR>" + CHAR_PATTERN + ")" +
            "|(?<STRING>" + STRING_PATTERN + ")" +
            "|(?<COMENT>" + COMENT_PATTERN + ")" +
            "|(?<RESTO>" + RESTO_PATTERN + ")"
    );
    
    public static CodeArea IniciarCodeArea(CodeArea ca){
        CodeArea code = ca;
        
        code.setPrefSize(986, 530);
        code.setParagraphGraphicFactory(LineNumberFactory.get(code));
        code.opacityProperty().set(0.4);
        
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
                matcher.group("KEYWORD") != null ? "keyword": 
                matcher.group("LETTERS") != null ? "letters" :  
                matcher.group("SIMBOLOS") != null ? "simbolo" : 
                matcher.group("NUMBERS") != null ? "numeros" :
                matcher.group("CHAR") != null ? "char" :
                matcher.group("STRING") != null ? "string" :
                matcher.group("COMENT") != null ? "coment" : null;
            
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
}
