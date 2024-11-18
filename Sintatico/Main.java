//Rafael Kendy Naramoto Lopes e Hugo Antonio Massaro
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		System.out.println("\nOBJETIVO\n\tAbrir um arquivo .fonte e percorrer os lexemas para tokenizá-los\n");
		
		System.out.println("Análise Léxica:");
		Lexico lex=new Lexico(args[0]);
		ArrayList<Token>tokens=new ArrayList<>(); //lista para todos os tokens encontrados
		
		Token t=lex.proximoToken();
		while(t!=null) {
			Token novoTok=new Token(t.getLexema(), t.getTipo(), t.getLinha());
			System.out.println("\t"+novoTok);
			tokens.add(novoTok);
			t=lex.proximoToken();
		}//while
		
		System.out.println("\n\nAnálise Sintática:");
		Sintatico sin=new Sintatico(tokens);
		sin.prog();
		

	}//main
}//Main