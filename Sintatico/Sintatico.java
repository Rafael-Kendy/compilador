//Analisador descendente recursivo

import java.util.ArrayList;

public class Sintatico {
	int pos;
	ArrayList<Token>tokens;
	TipoToken tt;
	
	public Sintatico(ArrayList<Token> tokens) {
		this.tokens = tokens;
		this.pos=0;
	}//construtor Sintatico
	
	
	
	public void match(TipoToken tipo) {
		if(tokens.get(pos).getTipo()==tipo) {
			System.out.println("\t"+tokens.get(pos));
			pos++;
		}else {
			System.out.println("\tERRO Sintático na linha " + tokens.get(pos).getLinha());
			System.exit(1);
		}//if else erro
	}//match

	
	
	public void prog() { //Prog -> ‘:’ ‘DEC’ ListaDec ‘:’ ‘PROG’ ListaCom
		tt=TipoToken.Delim; //':'
		match(tt);
		tt=TipoToken.PCDec; //'DEC'
		match(tt);
		listaDec();
		tt=TipoToken.Delim; //':'
		match(tt);
		tt=TipoToken.PCProg; //'PROG'
		match(tt);
		listaCom();
	}//prog
	
	
	public void listaDec() { //ListaDec -> Dec ListaDec2
		dec();
		listaDec2();
	}//listaDec
	
	
	public void dec() { //Dec -> VARIAVEL ‘:’ TipoVar
		tt=TipoToken.Var; //'VARIAVEL'
		match(tt);
		tt=TipoToken.Delim; //':'
		match(tt);
		tipoVar();
	}//listaDec2
	
	
	public void tipoVar() { //TipoVar -> ‘INT | ‘REAL’
		if(tokens.get(pos).getTipo()==TipoToken.PCInt) {
			tt=TipoToken.PCInt;	//'INT'		
		}else {
			tt=TipoToken.PCReal; //'REAL'
		}//if int ou real
		match(tt);
	}//tipoVar
	
	
	public void listaDec2() { //ListaDec2 -> ListaDec | e
		if(tokens.get(pos).getTipo()==TipoToken.Var) { 
			listaDec();
		}//if ainda ta declarando
	}//listaDec2
	
	
	public void listaCom() { //ListaCom -> Com ListaCom2
		com();
		listaCom2();
	}//listaCom
	
	
	public void listaCom2() { //ListaCom2 -> ListaCom| e
		if(tokens.get(pos).getTipo()==TipoToken.Var || tokens.get(pos).getTipo()==TipoToken.PCLer ||
				tokens.get(pos).getTipo()==TipoToken.PCImprimir || tokens.get(pos).getTipo()==TipoToken.PCSe ||
				tokens.get(pos).getTipo()==TipoToken.PCEnqto || tokens.get(pos).getTipo()==TipoToken.PCIni) {
			listaCom();
		}//if volta pro listaCom ou vazio
	}//listaCom2
	//tem que estar dentro desse if

	
	public void com() { //Com -> ComAtri | ComEnt | ComSaida | ComCondicao | ComRep | SubAlg
		switch(tokens.get(pos).getTipo()) {
			case TipoToken.Var: //ComAtri 
				comAtri();
				break;
			case TipoToken.PCLer: //ComEnt
				comEnt();
				break;
			case TipoToken.PCImprimir: //ComEnt
				comSaida();
				break;
			case TipoToken.PCSe: //comCondicao
				comCondicao();
				break;
			case TipoToken.PCEnqto: //comRep
				comRep();
				break;
			case TipoToken.PCIni: //subAlg
				subAlg();
				break;
		}//switch
	}//com
	
	
	public void comAtri() { //ComAtri -> VARIAVEL ‘:=’ ExpArit
		tt=TipoToken.Var; //'VARIAVEL'
		match(tt);
		tt=TipoToken.Atrib; //':='
		match(tt);
		expArit();
	}//comAtri
	
	
	public void comEnt() { //ComEnt -> ‘LER’ VARIAVEL;
		tt=TipoToken.PCLer; //'LER'
		match(tt);
		tt=TipoToken.Var; //'VARIAVEL'
		match(tt);
	}//comEnt
	
	
	public void comSaida() { //ComSaida -> ‘IMPRIMIR’ ComSaida2;
		tt=TipoToken.PCImprimir; //'IMPRIMIR'
		match(tt);
		comSaida2();
	}//comSaida
	
	
	public void comSaida2() { //ComSaida2 -> VARIAVEL | CADEIA;
		if(tokens.get(pos).getTipo()==TipoToken.Var) {
			tt=TipoToken.Var; //'VARIAVEL'	
		}else {
			tt=TipoToken.Cadeia; //'CADEIA'
		}//if var ou cadeia
		match(tt);
	}//comSaida
	
	//////////////////////////////////////////////////////
	
	public void comCondicao() {//ComandoCondicao → 'SE' ExpressaoRelacional 'ENTAO' Comando ComandoCondicao2;
	    tt = TipoToken.PCSe; //'SE'
	    match(tt);
	    expRel(); 
	    tt = TipoToken.PCEntao; //'ENTAO'
	    match(tt);
	    com(); 
	    comCondicao2(); 
	}//comCondicao
	
	public void comCondicao2() { //ComandoCondicao2 → Comando 'SENAO' | e;
	    if (tokens.get(pos).getTipo() == TipoToken.PCSenao) {
	        tt = TipoToken.PCSenao; //'SENAO'
	        match(tt);
	        com(); 
	    } //se não houver 'SENAO', aceita vazio
	}//comCondicao2

	
	public void comRep() { //ComandoRepeticao → 'ENQTO' ExpressaoRelacional Comando;
	    tt = TipoToken.PCEnqto; //'ENQTO'
	    match(tt);
	    expRel(); 
	    com(); 
	}//comRep
	
	
	public void subAlg() { //SubAlgoritmo → 'INI' ListaComandos 'FIM';
	    tt = TipoToken.PCIni; //'INI'
	    match(tt);
	    listaCom(); 
	    tt = TipoToken.PCFim; //'FIM'
	    match(tt);
	}//subAlg
	
	
	public void expArit() { //ExpressaoAritmetica → TermoAritmetico  ExpressaoAritmetica2;
	    termoArit(); 
	    expArit2(); 
	}//expArit
	
	
	public void expArit2() { //ExpressaoAritmetica2 → '+' ExpressaoAritmetica | '-' ExpressaoAritmetica | e;
	    if (tokens.get(pos).getTipo() == TipoToken.OpAritSoma || tokens.get(pos).getTipo() == TipoToken.OpAritSub) {
	        tt = tokens.get(pos).getTipo(); //'+' ou '-'
	        match(tt);
	        expArit();
	    }
	}//expArit2

	
	public void termoArit() { //TermoAritmetico → FatorAritmetico TermoAritmetico2;
	    fatorArit(); 
	    termoArit2();
	}//termoArit

	
	public void termoArit2() { //TermoAritmetico2 → '*' FatorAritmetico TermoAritmetico2 | '/' FatorAritmetico TermoAritmetico2 | e;
	    if (tokens.get(pos).getTipo() == TipoToken.OpAritMult || tokens.get(pos).getTipo() == TipoToken.OpAritDiv) {
	        tt = tokens.get(pos).getTipo(); //'*' ou '/'
	        match(tt);
	        fatorArit(); 
	        termoArit2();
	    } 
	}//termoArit

	
	public void fatorArit() { //FatorAritmetico → NUMINT | NUMREAL | VARIAVEL | '(' ExpressaoAritmetica ')'
	    if (tokens.get(pos).getTipo() == TipoToken.NumInt || tokens.get(pos).getTipo() == TipoToken.NumReal || tokens.get(pos).getTipo() == TipoToken.Var) {
	        tt = tokens.get(pos).getTipo(); //NUMINT, NUMREAL ou VARIAVEL
	        match(tt);
	    } else if (tokens.get(pos).getTipo() == TipoToken.AbrePar) {
	        tt = TipoToken.AbrePar; //'('
	        match(tt);
	        expArit(); 
	        tt = TipoToken.FechaPar; //')'
	        match(tt);
	    }
	}//fatoArit
	

	public void expRel() { //ExpressaoRelacional → TermoRelacional ExpressaoRelacional2;
	    termoRel(); 
	    expRel2(); 
	}//expRel

	
	public void expRel2() { //ExpressaoRelacional2 → OperadorBooleano TermoRelacional ExpressaoRelacional2 | e;
	    if (tokens.get(pos).getTipo() == TipoToken.OpBoolE || tokens.get(pos).getTipo() == TipoToken.OpBoolOu) {
	        tt = tokens.get(pos).getTipo(); //'E' ou 'OU'
	        match(tt);
	        termoRel(); 
	        expRel2(); 
	    } 
	}//expRel2
	
	public void termoRel() { //TermoRelacional → ExpressaoAritmetica OP_REL ExpressaoAritmetica | '(' ExpressaoRelacional ')';
	    if (tokens.get(pos).getTipo() == TipoToken.AbrePar) {
	        tt = TipoToken.AbrePar; //'('
	        match(tt);
	        expRel(); 
	        tt = TipoToken.FechaPar; //')'
	        match(tt);
	    } else {
	    	expArit();
        if (tokens.get(pos).getTipo() == TipoToken.OpRelMenor ||
            tokens.get(pos).getTipo() == TipoToken.OpRelMaior ||
            tokens.get(pos).getTipo() == TipoToken.OpRelMenorIgual ||
            tokens.get(pos).getTipo() == TipoToken.OpRelMaiorIgual ||
            tokens.get(pos).getTipo() == TipoToken.OpRelIgual ||
            tokens.get(pos).getTipo() == TipoToken.OpRelDif) {
            tt = tokens.get(pos).getTipo(); //pega o operador relacional
            match(tt); 
        } 
        expArit(); 
    }
}//termoRel

}//Sintatico