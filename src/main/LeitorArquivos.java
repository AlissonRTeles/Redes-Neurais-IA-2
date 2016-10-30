package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class LeitorArquivos {
	String nome = "Dados de Treinamento.txt";
	
	public void leArquivo(){
		
		try {
			FileReader arq = new FileReader(this.nome);
			BufferedReader lerArq = new BufferedReader(arq);

			String linha = lerArq.readLine(); 
			
			while (linha != null) {
				linha = lerArq.readLine();
				System.out.println(linha);
				if (linha != null){
					String[] entradas = linha.split(",");
					for (int i = 0; i < entradas.length-1; i++) {
						//valors das entradas dos neuronios
					}
					//validador da execu��o
				}

			}
		
			arq.close();
			} catch (IOException e) {
				e.getMessage();
			}
	}
		
	
}
