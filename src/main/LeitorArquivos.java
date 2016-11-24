package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LeitorArquivos {
	String nome   = "Dados de Treinamento.txt";
	FileReader arq;
	BufferedReader lerArq;
	
	public LeitorArquivos(String nome) {
		this.nome = nome;
	}
	
	public LeitorArquivos() {}
	
	public void criaFile(int i){
		try {
			if (i == 1){
				this.arq = new FileReader("Dados de Treinamento2.txt");	
			}else{
				this.arq = new FileReader("Dados de Teste.txt");
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void fechaFile(){
		try {
			this.arq.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void criaBuffer(int i){
		this.criaFile(i);
		
		this.lerArq = new BufferedReader(this.arq);
		
	
	}
	
	public String[] retornaSplitLine(){ 
		
		String[] entradas = {};
		
		try {
			String linha = lerArq.readLine();
			if (linha != null){
				entradas = linha.split(",");
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return entradas;
	}
	
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
		
	
}
