
public class Token {
	public String lexema;
	public TipoToken tipo;
	public int linha;
	
	public Token(String lexema, TipoToken tipo, int linha) {
		this.lexema = lexema;
		this.tipo = tipo;
		this.linha = linha;
	}//Token
	
	@Override
	public String toString() {
		return "<"+tipo+", "+lexema+", "+linha+">";
	}//toString
	
	public String getLexema() {
		return lexema;
	}//getLexema
	public void setLexema(String lexema) {
		this.lexema = lexema;
	}//setLexema
	
	public TipoToken getTipo() {
		return tipo;
	}//getTipo
	public void setTipo(TipoToken tipo) {
		this.tipo = tipo;
	}//setTipo
	
	public int getLinha() {
		return linha;
	}//getLinha
	public void setLinha(int linha) {
		this.linha = linha;
	}//setLinha
}
