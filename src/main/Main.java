package main;

import java.util.Scanner;

import neuronios.RedeNeural;

public class Main {
	Integer        nCamadas = 3;
	RedeNeural 	   rede;
	LeitorArquivos leitor;
	Scanner 	   in = new Scanner(System.in);
	
	public void init(){
		this.rede  = new RedeNeural();
		this.leitor= new LeitorArquivos();
		
		// -- monta rede neural
		rede.setNCamadas(3);
		rede.setNeuroniosCamada(2, 0);
		rede.setNeuroniosCamada(3, 1);
		rede.setNeuroniosCamada(2, 2);
	}
	
	public void exec(){	 
		
		//-- cria conexoes em rede neural 
		rede.criaRede();
		
		// -- monta o menu
		this.menuMain();
	}

	public void menuMain(){
		String  menuMsg  = "Qual opção voce deseja selecionar?";
		String[]menuMsgs = {"1-Treinar Rede Neural Artificial",
						   "2-Testar Rede Neural Artificial"};
		
		while (true){
			System.out.println();
			System.out.println(menuMsg);
			
			for (int i = 0; i < menuMsgs.length; i++) {
				System.out.println(menuMsgs[i]);
			}
			
			int nOpc = in.nextInt();
			
			switch (nOpc) {
			case 1:
				this.menu1();
				break;
			case 2:
				this.menu2();
				break;
			default:
				System.out.println("Bye!");
				return;
			}
		}
	}
	
	// -- executa aprendizado
	public void menu1(){
		
	}

	// -- executa teste
	public void menu2(){
		
	}
	public static void main(String[] args) {
		Main m = new Main();
		
		System.out.println("Trabalho de Redes Neurais Artificiais");
		System.out.println("Integrantes: Alisson, Gabriel e Priscila");
		
		m.init();
		m.exec();
		
	}
}
