package main;

import neuronios.RedeNeural;

public class Main {
	Integer nCamadas = 3;
	
	public static void main(String[] args) {
		RedeNeural rede = new RedeNeural();
		rede.setNCamadas(3);
		rede.setNeuroniosCamada(2, 0);
		rede.setNeuroniosCamada(3, 1);
		rede.setNeuroniosCamada(2, 2);
		rede.criaRede();
		System.out.println();		
		System.out.println("Trabalho de Redes Neurais Artificiais");
		System.out.println("Integrantes: Alisson, Gabriel e Priscila");

	}
}
