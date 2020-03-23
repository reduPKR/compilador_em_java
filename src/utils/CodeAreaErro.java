package utils;
 
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.Log;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

public class CodeAreaErro {
    private static int width = 813;
    private static int height = 406; 
    private static ArrayList <Log> erro;
    
    
    private static final Pattern PATTERN = Pattern.compile(            
            "(?<LOG>LOG)" +
            "|(?<LINHA>\\-)" +
            "|(?<SETA>\\>|\\*)" +
            "|(?<SINTATICO>Erro sintatico)" + 
            "|(?<SUCESSO>Analise sintatica)" +
            "|(?<INFO>Linha:|Coluna:|Erro:|Log:)" +
            "|(?<LETRA>[a-zA-Z_(){}?<>ãÃ]+)" +
            "|(?<NUMERO>[0-9]+)"
    );
    
    public static CodeArea IniciarCodeArea(CodeArea ca){        
        ca.setPrefSize(width, height);
        Head(ca, null);        
        ca.getStylesheets().add("style/Cores.css");
    
        return ca;
    }
    
    public static void SetLog(ArrayList<Log> erro){      
        CodeAreaErro.erro = erro;
    }
    
    public static ArrayList <Integer> getLinhas(){      
        ArrayList <Integer> l = new ArrayList();
        
        for(Log item : erro){
            if(item.getTipo().equals("Erro"))
                l.add(item.getLinha());
        }
        
        return l;
    }
    
    private static void Head(CodeArea ca, String str){
        String log = " LOG\n";
        for(int i = 0; i < 112; i++)
            log += "-";
        
        if(str == null)
            str = log;
        else{
            str = log.concat(str);
        }
        
        LogErros(ca, str);
    }
    
    public static CodeArea getLog(CodeArea ca){      
        if(erro != null){
            String str = "";
            ca.editableProperty().set(true);        
            for (Log item : erro) {
                str += "\n * "+item.getTipoLog()+" -> Linha: "+item.getLinha()+" Coluna: "+item.getColuna()+" "+item.getTipo()+": "+item.getLog();
            }
            
            Head(ca, str);
            ca.editableProperty().set(false);
        }
        
        return ca;
    }
    
    private static CodeArea LogErros(CodeArea ca, String str){      
        ca.clear();
        ca.replaceText(0, 0, str);        
        ca.setStyleSpans(0, computeHighlighting(str));
        ca.getStylesheets().add("style/Cores.css");
        
        return ca;
    }
    
    private static StyleSpans<Collection<String>> computeHighlighting( String text){        
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        
        Boolean flag = false;
        ArrayList <String> texto = new ArrayList();
        ArrayList <Integer> len1 = new ArrayList();
        ArrayList <Integer> len2 = new ArrayList();
        int a;
        while (matcher.find()) 
        {
            String styleClass = SelecionarClasse(matcher);
            if(styleClass.equals("erronumero"))
                a = 5;
            if(styleClass.equals("seta") && flag ){
                texto.set(texto.size()-1, "seta");
            }
            
            flag = styleClass.equals("aritmetico");
            
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
        return spansBuilder.create();
    }
    
    private static String SelecionarClasse(Matcher matcher){
        return matcher.group("LOG") != null ? "aritmetico":
               matcher.group("LINHA") != null ? "aritmetico": 
               matcher.group("SETA") != null ? "seta": 
               matcher.group("SINTATICO") != null ? "sintatico":
               matcher.group("SUCESSO") != null ? "aritmetico": 
               matcher.group("INFO") != null ? "info":
               matcher.group("LETRA") != null ? "erroletra":
               matcher.group("NUMERO") != null ? "erronumero":
               "outro";
    }
}
