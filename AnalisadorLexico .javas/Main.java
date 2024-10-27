//Rafael Kendy Naramoto Lopes e Hugo Antonio Massaro
import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		System.out.println("\nOBJETIVO\n\tAbrir um arquivo .fonte e percorrer os lexemas para tokenizá-los\n");
		
		Lexico lex=new Lexico(args[0]);
		ArrayList<Token>tokens=new ArrayList<>(); //lista para todos os tokens encontrados
		
		Token t=lex.proximoToken();
		while(t!=null) {
			tokens.add(t);
			t=lex.proximoToken();
		}//while

	}//main
}//Main