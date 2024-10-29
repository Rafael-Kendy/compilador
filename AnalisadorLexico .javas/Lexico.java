import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Lexico {
	LeitorArq learq;
	String linha_at="";
	int linha_num=0;
	int str_at;
	Map<String, Token> mapa_tok;

	
	public Lexico(String arq) {
		this.learq = new LeitorArq(arq);
		mapaTokens();
		str_at = 0;
	}//AnalLexico
	
	

	public String[] separaStr(String input) {
		//separa a string usando os simbolos como delimitador, usa tambem lookbehind, lookahead para separar melhor 
	    String[] partes = input.split("(?<=:|<|>|=|\"|\"|[+*/()\\s-])|(?=:|<|>|=|\"|\"|[+*/()\\s-])|(?<!\\d)\\.(?!\\d)|\\s+");
	    List<String> result = new ArrayList<>();

	    StringBuilder cadeia_aspas=new StringBuilder();
	    boolean dentro_aspas=false;
	    
	    for(String parte:partes) {	
	    	if(parte.equals("\"")) {
	    		if(!dentro_aspas) {
	    			dentro_aspas=true; //se ainda nao estiver dentro, seta a flag
	    			cadeia_aspas.append(parte);
	    		}else {
	    			dentro_aspas=false; //se estava true e achou outra aspas, deve ser o fechamento
	    			cadeia_aspas.append(parte);
	    			result.add(cadeia_aspas.toString());
	    			cadeia_aspas.setLength(0); //reseta pra proxima cadeia
	    		}//if(!dentro_aspas)
	    	}else if(dentro_aspas) {
	    		cadeia_aspas.append(parte);
	    	}else{ //adiciona tudo (incluindo espaco)
	            result.add(parte);
	        }//if (!parte.isBlank())
	    }//for(String parte:partes)
	    
	    if(dentro_aspas) { //if nao fechou as aspas, finaliza
	    	result.add(cadeia_aspas.toString());
	    	dentro_aspas=false;
	    }//if(dentro_aspas)

        //converte a lista de volta para array de strings e retorna
        return result.toArray(new String[0]);
    }//separaString
	
    
    
	public void mapaTokens() { //alternativa pro switch
		mapa_tok=new HashMap<>();
		mapa_tok.put("DEC", new Token("DEC", TipoToken.PCDec, -1));
		mapa_tok.put("PROG", new Token("PROG", TipoToken.PCProg, -1));
		mapa_tok.put("INT", new Token("INT", TipoToken.PCInt, -1));
		mapa_tok.put("REAL", new Token("REAL", TipoToken.PCReal, -1));
		mapa_tok.put("LER", new Token("LER", TipoToken.PCLer, -1));
		mapa_tok.put("IMPRIMIR", new Token("IMPRIMIR", TipoToken.PCImprimir, -1));
		mapa_tok.put("SE", new Token("SE", TipoToken.PCSe, -1));
		mapa_tok.put("SENAO", new Token("SENAO", TipoToken.PCSenao, -1));
		mapa_tok.put("ENTAO", new Token("ENTAO", TipoToken.PCEntao, -1));
		mapa_tok.put("ENQTO", new Token("ENQTO", TipoToken.PCEnqto, -1));
		mapa_tok.put("INI", new Token("INI", TipoToken.PCIni, -1));
		mapa_tok.put("FIM", new Token("FIM", TipoToken.PCFim, -1));
		
		mapa_tok.put("*", new Token("*", TipoToken.OpAritMult, -1));
		mapa_tok.put("/", new Token("/", TipoToken.OpAritDiv, -1));
		mapa_tok.put("+", new Token("+", TipoToken.OpAritSoma, -1));
		mapa_tok.put("-", new Token("-", TipoToken.OpAritSub, -1));		
		
		mapa_tok.put("<", new Token("<", TipoToken.OpRelMenor, -1));
		mapa_tok.put("<=", new Token("<=", TipoToken.OpRelMenorIgual, -1));
		mapa_tok.put(">", new Token(">", TipoToken.OpRelMaior, -1));
		mapa_tok.put(">=", new Token(">=", TipoToken.OpRelMaiorIgual, -1));
		mapa_tok.put("==", new Token("==", TipoToken.OpRelIgual, -1));
		mapa_tok.put("!=", new Token("!=", TipoToken.OpRelDif, -1));
		
		mapa_tok.put("E", new Token("E", TipoToken.OpBoolE, -1));
		mapa_tok.put("OU", new Token("OU", TipoToken.OpBoolOu	, -1));
		
		mapa_tok.put(":", new Token(":", TipoToken.Delim, -1));
		
		mapa_tok.put(":=", new Token(":=", TipoToken.Atrib, -1));
		
		mapa_tok.put("(", new Token("(", TipoToken.AbrePar, -1));
		mapa_tok.put(")", new Token(")", TipoToken.FechaPar, -1));
	}//mapaTokens
	
	
	
	public Token proximoToken() {
		while(true) {
			if(linha_at==null && str_at >= linha_at.length()) {
				return null;
			}//if acabou o arquivo
			
	        if(str_at >= linha_at.length()) { //ve se terminou a linha atual antes de passar pra proxima
	            linha_at = learq.lerProxLinha();
	            if(linha_at == null) { //acabou as linhas
	                return null;
	            }//if(linha_at == null)
	            linha_num++;
	            System.out.println("\nLinha " + linha_num + ": " + linha_at);
	            str_at = 0;
	        }//if(pos_at >= linha_at.length())
	        
	        //****
	        if (linha_at.trim().startsWith("#") || linha_at.isBlank()) {
	            linha_at = ""; //ignora o restante da linha
	            continue; //vai para a prox linha
	        }
			
			String[] substr = separaStr(linha_at);	
			
	        for(int i=str_at; i<substr.length; i++) {
	            String str = substr[i].trim(); //garante q nao tem espaco
	            str_at=i+1;
	            
	            //ignora comentarios dentro da linha
	            if (str.startsWith("#")) {
	                break; //ignora o restante da linha
	            }
	            
	            if (str.matches("^\".*\"$")) { //string que começa e termina com aspas
	                Token tok_cadeia = new Token(str, TipoToken.Cadeia, linha_num);
	                System.out.println("\t" + tok_cadeia);
	                return tok_cadeia;
	            } else if (str.startsWith("\"") && !str.endsWith("\"")) { //começa com aspas mas não fechou
	                System.out.println("\tERRO Léxico na linha " + linha_num + ": \"" + str + "\"");
	                System.exit(1);
	            } else if (str.endsWith("\"") && !str.startsWith("\"")) { //apenas aspas no final
	                System.out.println("\tERRO Léxico na linha " + linha_num + ": \"" + str + "\"");
	                System.exit(1);
	            }
	            
	            //detecta numeros
	            if (Pattern.matches("\\d*\\.\\d+", str) || Pattern.matches("\\d+\\.", str)) { //numero real
	                Token tok_real = new Token(str, TipoToken.NumReal, linha_num);
	                System.out.println("\t" + tok_real);
	                return tok_real;
	            } else if (Pattern.matches("\\d+", str)) { //numero int
	            	Token tok_int = new Token(str, TipoToken.NumInt, linha_num);
	                System.out.println("\t" + tok_int);
	                return tok_int;
	            }
	            
	            if((i+1)<=substr.length) { //se ainda nao ta no ultimo substr
	            	if((str.equals("<")||str.equals(">")||str.equals(":")||str.equals("!")||str.equals("=")) && substr[i+1].equals("=")) {
	            		i++;
	            		String op2=str+substr[i];
	            		if(mapa_tok.containsKey(op2)) { //if tem no mapa
	            			Token tok_op=mapa_tok.get(op2);
	            			tok_op.setLinha(linha_num);
	            			System.out.println("\t" + tok_op);
	            			str_at=i + 1; //att pra proxima string
	            			return tok_op;
	            		}//if(mapa_tok.containsKey(str))
	            	}//if algum operando de dois caracteres
	            }//if((i+1)<substr.length)
	            
	            if(str.isBlank()){
	            	continue;
	            }//if(str.isBlank())
	            
	            if(mapa_tok.containsKey(str)) { //if tem no mapa
	                Token token=mapa_tok.get(str);
	                token.setLinha(linha_num);
	                System.out.println("\t" + token);
	                str_at=i + 1; //att pra proxima string
	                return token;
	            }else if(Character.isUpperCase(str.charAt(0))){//se for maiusculo e nao e reservada, erro
	            	System.out.println("\tERRO Léxico na linha " + linha_num + ": \"" + str + "\"");
	            	System.exit(-1);
	            }else if(Character.isLowerCase(str.charAt(0))){
	            	Token tok_var=new Token(str, TipoToken.Var, linha_num);
	            	System.out.println("\t" + tok_var);
	            	return tok_var;
	            }else{ //algum simbolo diferente e que nao faz parte
	            	System.out.println("\tERRO Léxico na linha " + linha_num + ": \"" + str + "\"");
	            	System.exit(1);
	            }//if(mapa_tok.containsKey(str))
	        }//for(int i=pos_at; i<substr.length; i++)
	        str_at=linha_at.length(); //chegou no final
		}//while(true)
	}//proximoToken
}//Lexico
