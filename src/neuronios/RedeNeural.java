package neuronios;

import java.util.Random;
import java.util.Scanner;

import main.LeitorArquivos;

public class RedeNeural {
	CamadaNeural[] camadas;
	Integer 	   nCamadas;
	double minX = 0.1;
	double maxX = 0.7;
	Random rand = new Random();
	Scanner 	   in = new Scanner(System.in);
	
	// -- constantes de aprendizado
	double taxaAprendizagem = 0.5;
	double momentum = 1;
	
	public RedeNeural() {	
		
	}
	
	public void setNCamadas(Integer nCamadas){
		this.nCamadas = nCamadas;
		this.camadas  = new CamadaNeural[this.nCamadas];
		
		for (int i = 0; i < camadas.length; i++) {
			this.camadas[i] = new CamadaNeural();
		}
	}
	
	public void setNeuroniosCamada(Integer nNeuronio, Integer nCamada){
		this.camadas[nCamada].setNNeuronios(nNeuronio);
	}
	
	public CamadaNeural getCamada(Integer nIndex){
		return this.camadas[nIndex];
	}
	
	public void netCamada(Integer nIndex, CamadaNeural camada){
		this.camadas[nIndex] = camada;
	}
	
	private double geraPeso(){
		return rand.nextDouble() * (maxX - minX) + minX;
		//return 0.5;
	}
	public void criaRede(){
		
		for (int i = 0; i < camadas.length-1; i++) {
			CamadaNeural cam1= camadas[i];	
			CamadaNeural cam2= camadas[i+1];
			
			for (int j = 0; j < cam1.getnNeuronios(); j++) {
				Neuronio neuronio = cam1.getNeuronio(j);
				neuronio.montaCamDepois(cam2.getnNeuronios());
				
				for (int k = 0; k < cam2.getnNeuronios(); k++) {
					
					neuronio.getCamDEpois()[k] = cam2.getNeuronio(k);
					neuronio.getnVCamDepois()[k]=this.geraPeso();
				}
			
			}
			
			for (int j = 0; j < cam2.getnNeuronios(); j++) {
				Neuronio neuronio = cam2.getNeuronio(j);
				neuronio.montaCamAntes(cam1.getnNeuronios());
				
				for (int k = 0; k < cam1.getnNeuronios(); k++) {
					
					neuronio.getCamAntes()[k] = cam1.getNeuronio(k);
					neuronio.getnVCamAntes()[k]=cam1.getNeuronio(k).getnVCamDepois()[j];
					
				}
			}
			
		}
		
	}
	
	
	public void testarRede(LeitorArquivos testarFile){
		int qtdTestes = 0;
		int qtdAcertos = 0;
		int[] testesPorNumero = new int[10]; 
		int[] acertosPorNumero = new int[10]; 	
		
		testarFile.criaBuffer(0);
		
		String[] linhaFl = new String[9999];
		
		while (linhaFl != null) {
			//-- puxa primeira linha do arquivo
			linhaFl = testarFile.retornaSplitLine(); 
			if (linhaFl.length == 0){
				break;
			}
			
			qtdTestes++;
			// -- pega os nï¿½meros da linha
			double[] nNumbers = new double[linhaFl.length];
			for (int i = 0; i < linhaFl.length; i++) {
				nNumbers[i] = Double.valueOf(linhaFl[i]);
			}
		
			// -- valora a primeira camada com os numeros
			camadas[0].valorarNeuronios(nNumbers);
		
			// -- Salva o digito esperado da linha
			int DV = (int) Math.round(nNumbers[nNumbers.length-1]);

			// -- percorre primeira camada
			for (int j = 0; j < camadas[0].getnNeuronios(); j++) {
				Neuronio neuronioAux = camadas[0].getNeuronios()[j];
			
				// -- captura o valor do neuronio corrente
				// -- Valora a segunda camada com o somatï¿½rio da primeira
				double nAux = neuronioAux.getnValor();
			
				for (int i = 0; i < neuronioAux.getCamDEpois().length; i++) {
					Neuronio neuronioAux2 = neuronioAux.getCamDEpois()[i];
						
					neuronioAux2.setnValor( neuronioAux2.getnValor() + nAux * neuronioAux.getnVCamDepois()[i]);
				
				}
				
					
			}
				
			// -- agora faz sigmoidal na camada 2 
			for (int j = 0; j < camadas[1].getnNeuronios(); j++) {			
				Neuronio neuronioAux = camadas[1].getNeuronios()[j];
					
				neuronioAux.setnValor( this.sigmoidal(neuronioAux.getnValor()));
					
			}
			
				
			// -- percorre segunda camada
			for (int j = 0; j < camadas[1].getnNeuronios(); j++) {
				Neuronio neuronioAux = camadas[1].getNeuronios()[j];
					
				// -- captura o valor do neuronio corrente
				double nAux = neuronioAux.getnValor();
					
				for (int i = 0; i < neuronioAux.getCamDEpois().length; i++) {
					Neuronio neuronioAux2 = neuronioAux.getCamDEpois()[i];
					
					neuronioAux2.setnValor( neuronioAux2.getnValor() + nAux * neuronioAux.getnVCamDepois()[i]);
				
				}	
			}		
			// -- agora faz sigmoidal na camada 3 
			for (int j = 0; j < camadas[2].getnNeuronios(); j++) {			
				Neuronio neuronioAux = camadas[2].getNeuronios()[j];
		
				neuronioAux.setnValor( this.sigmoidal(neuronioAux.getnValor()));
			
			}
			
			// -- calcula fator erro da camada 3 
			for (int j = 0; j < camadas[2].getnNeuronios(); j++) {			
				Neuronio neuronioAux = camadas[2].getNeuronios()[j];
				
				//Valor esperado - valor obtido
				if (j == DV){
					neuronioAux.setnFatorErro(1-neuronioAux.getnValor());  
				}else{
					neuronioAux.setnFatorErro(0-neuronioAux.getnValor()); 
				}
						
			}
		
			
			// -- Validar resultado
			
			boolean acertou = true;
			for (int j = 0; j < camadas[2].getnNeuronios(); j++) {			
				Neuronio neuronioAux = camadas[2].getNeuronios()[j];
				//System.out.println("Valor do Neurônio: " + neuronioAux.getnValor());
				//System.out.println("Valor Arredondado: " + (int) Math.round(neuronioAux.getnValor()));
				if(j == DV){
					if( (int) Math.round(neuronioAux.getnValor()) != 1){
						acertou = false;
					}
				}else{
					if( (int) Math.round(neuronioAux.getnValor()) != 0){
						acertou = false;
					}
				}
			}
			/*
			boolean acertou = true;
			for (int j = 0; j < camadas[2].getnNeuronios(); j++) {			
				Neuronio neuronioAux = camadas[2].getNeuronios()[j];
				//System.out.println("Valor do Neurônio: " + neuronioAux.getnValor());
				//System.out.println("Valor Arredondado: " + (int) Math.round(neuronioAux.getnValor()));
				if(j == DV){
					if( neuronioAux.getnValor() < 0.499){
						acertou = false;
					}
				}else{
					if( neuronioAux.getnValor() > 0.499){
						acertou = false;
					}
				}
			}*/
			
			//
			testesPorNumero[DV]++;
			if(acertou){
				qtdAcertos++;
				acertosPorNumero[DV]++;
			}		
			
			
			//Zera os valores dos neurônios
			for (int n= 0; n < camadas[0].getnNeuronios(); n++) {
				Neuronio neuronioc1 = camadas[0].getNeuronios()[n];
				neuronioc1.setnValor(0.0);
				neuronioc1.setnFatorErro(0.0);
			}
			
			for (int m = 0; m < camadas[1].getnNeuronios(); m++) {
				Neuronio neuronioc2 = camadas[1].getNeuronios()[m];
				neuronioc2.setnValor(0.0);
				neuronioc2.setnFatorErro(0.0);
			}
			
			for (int o = 0; o < camadas[2].getnNeuronios(); o++) {
				Neuronio neuronioc3 = camadas[2].getNeuronios()[o];
				neuronioc3.setnValor(0.0);
				neuronioc3.setnFatorErro(0.0);
			}

		}
		System.out.println("Quantidade de Testes: " + qtdTestes); 
		System.out.println("Quantidade de Acertos: " + qtdAcertos); 
		for (int i = 0; i < 10; i++) {
			System.out.println("Quantidade de Testes do Número " + i +": " + testesPorNumero[i] + " e Acertos: " + acertosPorNumero[i] );			
		}
		
		testarFile.fechaFile();
	}
	

	
	
	
	
	public void aprender(LeitorArquivos aprenderFile){
		
		//-- puxa primeira linha do arquivo
		String[] linhaFl = new String[9999];
		int qtdIteracoes = 0;
		int linhas = 0;
		
		//ESSE WHILE SERVE PARA CONTROLAR QUANTIDADE DE LEITURAS DO PRIMEIRO ARQUIVO
		while (qtdIteracoes <= 150){ 
		aprenderFile.criaBuffer(1);
		double fatoraux = 0;
		//ESSE WHILE SERVE PARA LER TODAS AS LINHAS DO ARQUIVO
		while(linhaFl != null){
		//int nOpc = in.nextInt();	// TESTAR CADA LINHA
		linhaFl = aprenderFile.retornaSplitLine(); 
		if (linhaFl.length == 0){
			break;
		}
		
		// -- pega os nï¿½meros da linha
		double[] nNumbers = new double[linhaFl.length];
		for (int i = 0; i < linhaFl.length; i++) {
			nNumbers[i] = Double.valueOf(linhaFl[i]); //DIVIDIR AQUI POR 100 PRA TESTAR
		}
		

		// -- valora a primeira camada com os numeros
		camadas[0].valorarNeuronios(nNumbers);
		
		/*
		System.out.println("Valores da Camada Inicial.");	
		for (int i = 0; i < camadas[0].getnNeuronios(); i++) {
			Neuronio neuronioCam1 = camadas[0].getNeuronios()[i];
			System.out.println("Neuronio " + i + "  Valor:" + neuronioCam1.getnValor() + "   Peso para o primeiro neurônio da intermediária: " + neuronioCam1.getnVCamDepois()[0] );			
		}
		*/
		
		
		
		// -- Salva o digito esperado da linha
		int DV = (int) Math.round(nNumbers[nNumbers.length-1]);
		
		// -- propaga valores para a segunda camada
		for (int i = 0; i < camadas[0].getnNeuronios(); i++) {
			Neuronio neuronioCam1 = camadas[0].getNeuronios()[i];
			
			for (int j = 0; j < neuronioCam1.getCamDEpois().length; j++) {				
				Neuronio neuronioCam2 = neuronioCam1.getCamDEpois()[j];				
				neuronioCam2.setnValor( neuronioCam2.getnValor() + neuronioCam1.getnValor() * neuronioCam1.getnVCamDepois()[j]);
				
			}
			
		}
		
		/*
		System.out.println("\n\n");	
		System.out.println("Somatório dos valores da camada intermediária.");	
		for (int i = 0; i < camadas[1].getnNeuronios(); i++) {
			Neuronio neuronioCam2 = camadas[1].getNeuronios()[i];
			System.out.println("Neuronio " + i +"  Valor: " + neuronioCam2.getnValor());			
		}*/
		
		
		
		// -- agora faz sigmoidal na camada 2 	
		for (int j = 0; j < camadas[1].getnNeuronios(); j++) {			
			Neuronio neuronioCam2 = camadas[1].getNeuronios()[j];			
			neuronioCam2.setnValor( this.sigmoidal(neuronioCam2.getnValor()));			
		}
		/*
		System.out.println("\n\n");	
		System.out.println("Valores da Camada Intermediária após sigmoidal.");	
		for (int i = 0; i < camadas[1].getnNeuronios(); i++) {
			Neuronio neuronioCam2 = camadas[1].getNeuronios()[i];
			System.out.println("Neuronio " + i + "  Valor:" + neuronioCam2.getnValor() + "   Peso para o primeiro neurônio da camada de saída: " + neuronioCam2.getnVCamDepois()[0] );			
		}
		*/
		
		
		
		
		// -- propaga valores para a terceira camada
		for (int j = 0; j < camadas[1].getnNeuronios(); j++) {
			Neuronio neuronioCam2 = camadas[1].getNeuronios()[j];
			
			for (int k = 0; k < neuronioCam2.getCamDEpois().length; k++) {
				Neuronio neuronioCam3 = neuronioCam2.getCamDEpois()[k];
				neuronioCam3.setnValor( neuronioCam3.getnValor() + neuronioCam2.getnValor() * neuronioCam2.getnVCamDepois()[k]);
				
			}	
		}		
		
		/*
		System.out.println("\n\n");	
		System.out.println("Somatório dos valores da camada de saída.");	
		for (int i = 0; i < camadas[2].getnNeuronios(); i++) {
			Neuronio neuronioCam3 = camadas[2].getNeuronios()[i];
			System.out.println("Neuronio " + i +"  Valor: " + neuronioCam3.getnValor());			
		}*/
		
		
		
		// -- agora faz sigmoidal na camada 3 
		for (int k = 0; k < camadas[2].getnNeuronios(); k++) {			
			Neuronio neuronioCam3 = camadas[2].getNeuronios()[k];		
			neuronioCam3.setnValor(this.sigmoidal(neuronioCam3.getnValor()));
		}
		
		
		/*
		System.out.println("\n\n");	
		System.out.println("Valores da Camada de Saída após sigmoidal.");	
		for (int i = 0; i < camadas[2].getnNeuronios(); i++) {
			Neuronio neuronioCam3 = camadas[2].getNeuronios()[i];
			System.out.println("Neuronio " + i + "  Valor:" + neuronioCam3.getnValor());			
		}*/
			
		
		
		// -- calcula fator erro da camada 3 
		for (int j = 0; j < camadas[2].getnNeuronios(); j++) {			
			Neuronio neuronioAux = camadas[2].getNeuronios()[j];
			
			//Valor esperado - valor obtido
			if (j == DV){				
				neuronioAux.setnFatorErro(1-neuronioAux.getnValor());
			}else{				
				neuronioAux.setnFatorErro(0-neuronioAux.getnValor()); 
			}
					
		}
		
		
		/*
		System.out.println("\n\n");	
		System.out.println("Fator Erro da camada de saída.");	
		for (int k = 0; k < camadas[2].getnNeuronios(); k++) {
			Neuronio neuronioCam3 = camadas[2].getNeuronios()[k];
			System.out.println("Neuronio " + k + "  FatorErro:" + neuronioCam3.getnFatorErro());			
		}
		*/
		
		// -- calcula erro da camada 3 
		for (int k = 0; k < camadas[2].getnNeuronios(); k++) {			
			Neuronio neuronioCam3 = camadas[2].getNeuronios()[k];
			//erro da camada 3 = saida * (1-saida) * fatorErro
			neuronioCam3.setnErro(calcErro(neuronioCam3.getnValor(),neuronioCam3.getnFatorErro()));		
			//neuronioCam3.setnErro(neuronioCam3.getnFatorErro());
		}
			
		/*
		System.out.println("\n\n");	
		System.out.println("Erro da camada de saída. saida * (1-saida) * fatorErro");	
		for (int i = 0; i < camadas[2].getnNeuronios(); i++) {
			Neuronio neuronioCam3 = camadas[2].getNeuronios()[i];
			System.out.println("Neuronio " + i + "  Saída:" + neuronioCam3.getnValor()+ " FatorErro: " + neuronioCam3.getnFatorErro() );
			System.out.println("Neuronio " + i + "  Erro:" + neuronioCam3.getnErro()+ "   Peso para o primeiro neurônio da camada intermediária: " + neuronioCam3.getnVCamAntes()[0] );			
		}
		*/
		
		
		//fator erro da camada intermediária
		// -- percorre a camada 3 e propaga o erro pra camada 2
		for (int k = 0; k < camadas[2].getnNeuronios(); k++) {
			Neuronio neuronioCam3 = camadas[2].getNeuronios()[k];
			
			for (int j = 0; j < neuronioCam3.getCamAntes().length; j++) {
				Neuronio neuronioCam2 = neuronioCam3.getCamAntes()[j];				
				// Fator erro da camada 2 = fatorerro da camada2 (somatório) + Erro da camada3 * peso de 2 e 3  
				neuronioCam2.setnFatorErro(neuronioCam2.getnFatorErro() + neuronioCam3.getnErro() * neuronioCam2.getnVCamDepois()[k]);
				
			}			
		}
		
		/*
		System.out.println("\n\n");	
		System.out.println("Fator Erro da camada intermediária.");	
		for (int i = 0; i < camadas[1].getnNeuronios(); i++) {
			Neuronio neuronioCam2 = camadas[1].getNeuronios()[i];
			System.out.println("Neuronio " + i + "  FatorErro:" + neuronioCam2.getnFatorErro());			
		}
		*/

		
		// -- calcula erro da camada 2 
		for (int j = 0; j < camadas[1].getnNeuronios(); j++) {			
			Neuronio neuronioCam2 = camadas[1].getNeuronios()[j];
			neuronioCam2.setnErro(calcErro(neuronioCam2.getnValor(),neuronioCam2.getnFatorErro()));		
			//neuronioCam2.setnErro(neuronioCam2.getnFatorErro());
		}		
		
		/*
		System.out.println("\n\n");	
		System.out.println("Erro da camada intermediária");	
		for (int i = 0; i < camadas[1].getnNeuronios(); i++) {
			Neuronio neuronioCam2 = camadas[1].getNeuronios()[i];
			System.out.println("Neuronio " + i + "  Erro:" + neuronioCam2.getnErro()+ "   Peso para o primeiro neurônio da camada inicial: " + neuronioCam2.getnVCamAntes()[0] );			
		}*/
		
		
		//Atualiza pesos das conexões entre as camadas 1 e 2
		for (int j = 0; j < camadas[1].getnNeuronios(); j++) {			
			Neuronio neuronioCam2 = camadas[1].getNeuronios()[j];
			
			for (int i = 0; i < camadas[0].getnNeuronios(); i++) {	
				Neuronio neuronioCam1 = camadas[0].getNeuronios()[i];
				double novoPeso = this.momentum * neuronioCam2.getnVCamAntes()[i] + this.taxaAprendizagem * neuronioCam1.getnValor() * neuronioCam2.getnErro();
				//System.out.println("Peso da Conexão do Neuronio " + j + " da camada2 para " + k + " da camada1: ");
				//System.out.println("	"+this.momentum +" * "+ neuronioCam2.getnVCamAntes()[k] +" + " + this.taxaAprendizagem + " * " + neuronioCam1.getnValor() +" * " + neuronioCam2.getnErro() + "  = "+ novoPeso);
				//--atualiza os 2 caminhos, da camada 1 depois e da camada 2 antes.
				neuronioCam2.nVCamAntes[i] = novoPeso;
				neuronioCam1.nVCamDepois[j] = novoPeso;
				
			}				
		}
		
		//Atualiza pesos das conexçoes entre as camadas 2 e 3
		for (int k = 0; k < camadas[2].getnNeuronios(); k++) {	
			Neuronio neuronioCam3 = camadas[2].getNeuronios()[k];
			for (int j = 0; j < camadas[1].getnNeuronios(); j++) {	
				Neuronio neuronioCam2 = camadas[1].getNeuronios()[j];
				
				double novoPeso = this.momentum * neuronioCam3.getnVCamAntes()[j] + this.taxaAprendizagem * neuronioCam2.getnValor() * neuronioCam3.getnErro();
				/*if( k >= 7 && j == 0){
                    System.out.println("Peso Neuronio Cam3 " + k + "  para Cam2 " + j + " peso de " + neuronioCam3.getnVCamAntes()[j] + " para " + novoPeso);
				}*/
				//System.out.println("Peso da Conexão do Neuronio " + k + " da camada3 para " + j + " da camada2: ");
				//System.out.println("	"+this.momentum +" * "+ neuronioCam3.getnVCamAntes()[j] +" + " + this.taxaAprendizagem + " * " + neuronioCam2.getnValor() +" * " + neuronioCam3.getnErro() + "  = "+ novoPeso);
				//--atualiza os 2 caminhos, da camada 2 depois e da camada 3 antes.
				neuronioCam3.nVCamAntes[j] = novoPeso;
				neuronioCam2.nVCamDepois[k] = novoPeso;
				
			}				
		}

		/*
		double somaerros = 0.0;
		System.out.println("\n Dígito Esperado: "+ DV);
		System.out.println("FatorErro desta iteração da camada de saída:");
		for (int j = 0; j < camadas[2].getnNeuronios(); j++) {	
			Neuronio neuronioCam3 = camadas[2].getNeuronios()[j];
			System.out.println("Neuronio " + j + " Obtido: " + neuronioCam3.getnValor() + "  FatorErro: " + neuronioCam3.getnFatorErro());	
			somaerros += Math.abs(neuronioCam3.getnFatorErro());
		}
		System.out.println("Soma dos fatorerros: "+ somaerros + "\n");	
		System.out.println("\n OK, PRÓXIMA ITERAÇÃO... \n");*/	
//int nOpc1 = in.nextInt();
		//Calcula erro médio da iteração
		
		for (int j = 0; j < camadas[2].getnNeuronios(); j++) {	
			Neuronio neuronioCam3 = camadas[2].getNeuronios()[j];
			fatoraux += Math.abs(neuronioCam3.getnFatorErro());			
		}
		/*
		if(linhas == 0){
			fatoraux = fatoraux/camadas[2].getnNeuronios();
			System.out.println("Média do erro esperado da iteração: " + fatoraux);
		}/*
		/*
		if(linhas == 0){
			System.out.println("Verificando valores da linha: " + linhas + "  da camada 3.");
			int nOpc = in.nextInt();		
			for (int o = 0; o < camadas[2].getnNeuronios(); o++) {
				nOpc = in.nextInt();
			
				Neuronio neuronioc3 = camadas[2].getNeuronios()[o];
				System.out.println("Neuronio Saida " + o + "   Valor: " + neuronioc3.getnValor() + " Esperado: " + DV +"   FatorErro: " + neuronioc3.getnFatorErro());
					for (int p = 0; p < camadas[1].getnNeuronios(); p++) {
						System.out.println("Peso da conexão com neuronio " + p + " da camada anterior: " + neuronioc3.getnVCamAntes()[p]);
					}
			}
		}
		*/

		
/* Testes...	
		System.out.println("Verificando valores da linha: " + linhas + "  da camada 3.");
		int nOpc = in.nextInt();		
		for (int o = 0; o < camadas[2].getnNeuronios(); o++) {
			nOpc = in.nextInt();
		
			Neuronio neuronioc3 = camadas[2].getNeuronios()[o];
			System.out.println("Neuronio Saida " + o + "   Valor: " + neuronioc3.getnValor() + " Esperado: " + DV +"   FatorErro: " + neuronioc3.getnFatorErro());
				for (int p = 0; p < camadas[1].getnNeuronios(); p++) {
					System.out.println("Peso da conexão com neuronio " + p + " da camada anterior: " + neuronioc3.getnVCamAntes()[p]);
				}
		}
*/		
		//fatoraux = fatoraux/camadas[2].getnNeuronios();
		//System.out.println("Média do erro esperado da iteração: " + fatoraux);
		
		//zerar valor dos neuronios
		for (int n= 0; n < camadas[0].getnNeuronios(); n++) {
			Neuronio neuronioc1 = camadas[0].getNeuronios()[n];
			neuronioc1.setnValor(0.0);
			neuronioc1.setnErro(0.0);
			neuronioc1.setnFatorErro(0.0);
		}
		
		for (int m = 0; m < camadas[1].getnNeuronios(); m++) {
			Neuronio neuronioc2 = camadas[1].getNeuronios()[m];
			neuronioc2.setnValor(0.0);
			neuronioc2.setnErro(0.0);
			neuronioc2.setnFatorErro(0.0);
		}
		
		for (int o = 0; o < camadas[2].getnNeuronios(); o++) {
			Neuronio neuronioc3 = camadas[2].getNeuronios()[o];
			neuronioc3.setnValor(0.0);
			neuronioc3.setnErro(0.0);
			neuronioc3.setnFatorErro(0.0);
		}	
		
		
		
		
		linhas++;
		}//fecha while do leitor de arquivo
		aprenderFile.fechaFile();
		qtdIteracoes++;
		}
		
	}
	
	
	
	public double calcErro(double saida, double fatorErro){
		
		return saida * (1-saida) * fatorErro;
		
	}
	
	public double sigmoidal(double somatorio){
		//return  1 + (Math.exp(-somatorio));		
		return  (1/(1 + Math.exp(-1 * somatorio)));		
	
	}
	
	public void testar(LeitorArquivos testarFile){
		
		
	}
	
	
	public int converteDuDec(int[] vConverter){
		for (int i = 0; i < vConverter.length; i++) {
			if(vConverter[i]==1){
				return i;
			}
		}
		
		return 0;
	}
	
	public int[] converteDecDu(int vCOnverter){
		int[] nReturn = {0,0,0,0,0,0,0,0,0,0};
		nReturn[vCOnverter+1]=1;
		return nReturn;
	}
}
