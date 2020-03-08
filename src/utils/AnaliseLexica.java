package utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnaliseLexica {
    
    private static final Pattern PATTERN = Pattern.compile(            
            "(?<PROGRAMAINICIO>\\<\\?)" +
            //"|(?<PROGRAMAFIM> \\?\\>)" +
            "|(?<TIPOINT>int)" +
            "|(?<TIPODOUBLE>double)" +
            "|(?<TIPOSTRING>string)" +
            "|(?<TIPOBOOL>bool)" +
            "|(?<BLOCOIF>if)" +
            "|(?<BLOCOELSE>else)" +
            "|(?<BLOCOWHILE>while)" +         
            "|(?<BLOCOLOOP>loop)" + 
            "|(?<ABREPARENTES>\\()" + 
            "|(?<FECHAPARENTES>\\))" +
            "|(?<ABRECHAVES>\\{)" +         
            "|(?<FECHACHAVES>\\})" + 
            "|(?<OPERATRIBUICAO>\\=)" + 
            "|(?<OPERSOMA>\\+)" +
            "|(?<OPERSUB>\\-)" +         
            "|(?<OPERDIV>\\/)" + 
            "|(?<OPERMULT>\\*)" + 
            "|(?<OPERPOT>\\^)" +
            //"|(?<COMPARACAOIGUAL>\\=\\=)" +         
            //"|(?<COMPARACAODIFERENTE> \\!\\=)" + 
            "|(?<COMPARACAOMAIOR>\\>)" +
            "|(?<COMPARACAOMENOR>\\<)" +
            //"|(?<COMPARACAOMAIORIGUAL>\\>\\=)" +         
            //"|(?<COMPARACAOMENORIGUAL>\\<\\=)" + 
            "|(?<LOGICOAND>\\&\\&)" +
            "|(?<LOGICOOR>\\|\\|)" + 
            "|(?<LETRA>[a-zA-Z]+)" +
            "|(?<NUMEROSINT>[0-9]+)" +
            //"|(?<NUMEROSDOUBLE>[0-9]+.[0-9]+)" +         
            "|(?<FIMLINHA>;)" + 
            "|(?<STRING>')" +
            "|(?<COMENTARIO>\\$)"         
    );
    
    public static String getToken(String text){
        Matcher matcher = PATTERN.matcher(text);
        String token = "outro";
        
        if(matcher.find())
            token = SelecionarClasse(matcher);
        
        if(text.length() == 2 && (!token.equals("tk_variavel") || !token.equals("tk_num_int"))){                      
            if(text.equals("?>"))
                token = "tk_prog_fim";
            else
            if(text.equals("=="))
                token = "tk_comp_igual";
            else
            if(text.equals("!="))
                token = "tk_comp_dif";
            else
            if(text.equals("<="))
                token = "tk_comp_men_igual";
            else
            if(text.equals(">="))
                token = "tk_comp_mai_igual";
        }else
        if(token.equals("tk_num_int") && text.contains(".")){
            int cont = 0;
            for (int i = 0; i < text.length(); i++) {
                if(text.charAt(i) == '.')
                    cont++;
            }
            
            if(cont == 1)
                token = "tk_num_double";
            else
                token = "Erro";
        }
        
        return token;
    }
    
    private static String SelecionarClasse(Matcher matcher){
        return matcher.group("PROGRAMAINICIO") != null ? "tk_prog_ini":
                //matcher.group("PROGRAMAFIM") != null ? "tk_prog_fim": 
                matcher.group("TIPOINT") != null ? "tk_tipo_int": 
                matcher.group("TIPODOUBLE") != null ? "tk_tipo_double":   
                matcher.group("TIPOSTRING") != null ? "tk_tipo_string":
                matcher.group("TIPOBOOL") != null ? "tk_tipo_bool": 
                matcher.group("BLOCOIF") != null ? "tk_bloco_if": 
                matcher.group("BLOCOELSE") != null ? "tk_bloco_else":
                matcher.group("BLOCOWHILE") != null ? "tk_bloco_while":
                matcher.group("BLOCOLOOP") != null ? "tk_bloco_loop":
                matcher.group("ABREPARENTES") != null ? "tk_abre_par":
                matcher.group("FECHAPARENTES") != null ? "tk_fecha_par":
                matcher.group("ABRECHAVES") != null ? "tk_abre_chav":
                matcher.group("FECHACHAVES") != null ? "tk_fecha_chav":
                matcher.group("OPERATRIBUICAO") != null ? "tk_oper_atrib":
                matcher.group("OPERSOMA") != null ? "tk_oper_soma":
                matcher.group("OPERSUB") != null ? "tk_oper_sub":
                matcher.group("OPERDIV") != null ? "tk_oper_div":
                matcher.group("OPERMULT") != null ? "tk_oper_mult":
                matcher.group("OPERPOT") != null ? "tk_oper_pot":
                //matcher.group("COMPARACAOIGUAL") != null ? "tk_comp_igual":
               // matcher.group("COMPARACAODIFERENTE") != null ? "tk_comp_dif":
                matcher.group("COMPARACAOMAIOR") != null ? "tk_comp_mai":
                matcher.group("COMPARACAOMENOR") != null ? "tk_comp_men":
                //matcher.group("COMPARACAOMAIORIGUAL") != null ? "tk_comp_mai_igual":
                //matcher.group("COMPARACAOMENORIGUAL") != null ? "tk_comp_men_igual":
                matcher.group("LOGICOAND") != null ? "tk_logico_and":
                matcher.group("LOGICOOR") != null ? "tk_logico_or":
                matcher.group("LETRA") != null ? "tk_variavel":
                matcher.group("NUMEROSINT") != null ? "tk_num_int":
                //matcher.group("NUMEROSDOUBLE") != null ? "tk_num_double":
                matcher.group("FIMLINHA") != null ? "tk_fim_lin":
                matcher.group("STRING") != null ? "tk_string":
                matcher.group("COMENTARIO") != null ? "tk_comentario":
                "outro";
    }
}
