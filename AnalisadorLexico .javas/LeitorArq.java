import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class LeitorArq {
	public BufferedReader br;
	
	public LeitorArq(String arq) { //construtor, so pra abrir o arquivo
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(arq)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//try catch caso de erro pra abrir
	}//LeitorArq
	
	public String lerProxLinha(){
		String linha=null;
		try {
			linha=br.readLine();//le a linha
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//try catch erro na hora de ler
		return linha;
	}//lerProxLinha

	public BufferedReader getBr() {
		return br;
	}//getIs

	public void setIs(BufferedReader br) {
		this.br = br;
	}//setIs
	
    public void fechaBuffer() {
        try {
            if (br != null) {
                br.close();
            }//fecha se tiver algo
        } catch (IOException e) {
            e.printStackTrace();
        }//try catch
    }//fechaBuffer
	
}//LeitorArq
