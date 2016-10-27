package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class LeitorArquivos {

	public void leArquivo(){

		String nome = "Dados de Treinamento.txt";

		try {
			FileReader arq = new FileReader(nome);
			BufferedReader lerArq = new BufferedReader(arq);
		
			String linha = lerArq.readLine(); 
			
			while (linha != null) {
				linha = lerArq.readLine();
				String[] entradas = linha.split(",");
				for (int i = 0; i < entradas.length-1; i++) {
					//ENTRADA DO NEURONIO = entradas[i];					
				}
				
			}
		
			arq.close();
			} catch (IOException e) {
				e.getMessage();
			}
	}
		
	
}
