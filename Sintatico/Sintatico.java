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
				tokens.get(pos).getTipo()==TipoToken.PCImprimir) {
			listaCom();
		}//if volta pro listaCom ou vazio
	}//listaCom2

	
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
	
	
	public void comCondicao() {
	}//comCondicao
	
	
	public void comRep() {
	}//comRep
	
	
	public void subAlg() {
	}//subAlg
	
	
	public void expArit() {
	}//expArit
	
	
}//Sintatico