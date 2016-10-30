package main;

public class FuncoesExecucao{

	
	
	public double sigmoidal(double somatorio){
		
		return 1/(1+ Math.exp(-somatorio));		

		
	}
	
	public double calcErro(double saida, double fatorErro){
		
		
		return saida * (1-saida) * fatorErro;
		//double erro = saida * (1-saida) * fatorErro;
		//return erro;
		
	}
	
	public double ajustaPeso(double taxaAprendizagem, double erro, double saidaAnterior, double peso){
		
		return peso + taxaAprendizagem * saidaAnterior * erro;
		//double pesoAjustado = peso + taxa * saidaAnterior * erro;
		//return pesoAjustado;
	}
	
	/*
	public void calculaSaida(CamadaNeural camada){
		double saida = 0;
		for (int i = 0; i < camada.length; i++) {
			for (int j = 0; j < camada.length; j++) {
				
			}
			
		}
		
		
	}
	*/
	
		
}
