package tp1.handlers;

public class Metrica {

	private int media = 0;
	
	public void mediaTamanhoMetodos() {
		float somaDoTamanhoDosMetodos = 0;
		for(int i =0; i < SampleHandler.arrayDados.size();i++){
			somaDoTamanhoDosMetodos += SampleHandler.arrayDados.get(i).getQtdeDeLinhas();
		}
		media = (int)Math.ceil(somaDoTamanhoDosMetodos / SampleHandler.arrayDados.size());
	}
	
	public void tamanhoMetodo(){
		for(int i = 0; i < SampleHandler.arrayDados.size(); i++){
			SampleHandler.arrayDados.get(i).setPorcentagem((100*SampleHandler.arrayDados.get(i).qtdeDeLinhas)/media);
		}
	}
}
