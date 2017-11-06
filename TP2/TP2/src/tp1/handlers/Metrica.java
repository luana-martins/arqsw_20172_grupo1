package tp1.handlers;

public class Metrica {

	private int lpm = 0;
	private int cpl = 0;
	private int tmm = 0;
	
	public void mediaTamanhoMetodos() {
		float somaDoTamanhoDosMetodos = 0;
		double somaDosCaracteres = 0;
		for(int i =0; i < SampleHandler.arrayDados.size();i++){
			somaDoTamanhoDosMetodos += SampleHandler.arrayDados.get(i).getQtdeDeLinhas();
			somaDosCaracteres += SampleHandler.arrayDados.get(i).getNumCaracteres();
		}
		lpm = (int)Math.ceil(somaDoTamanhoDosMetodos / SampleHandler.arrayDados.size());
		cpl = (int)Math.ceil(somaDosCaracteres / somaDoTamanhoDosMetodos);
		tmm = (int)Math.ceil(lpm*cpl);
	}
	
	public void tamanhoMetodo(){
		for(int i = 0; i < SampleHandler.arrayDados.size(); i++){
			SampleHandler.arrayDados.get(i).setPorcentagem((100*SampleHandler.arrayDados.get(i).getQtdeDeLinhas())/lpm);
		}
	}

	public int getLPM() {
		return lpm;
	}

	public int getCPL() {
		return cpl;
	}
	
	public int getTMM() {
		return tmm;
	}
	
	
	
}
