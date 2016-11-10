package neuronios;

import java.util.Random;

import main.LeitorArquivos;

public class RedeNeural {
	CamadaNeural[] camadas;
	Integer 	   nCamadas;
	double minX = 0.0;
	double maxX = 1.0;
	Random rand = new Random();
	
	// -- constantes de aprendizado
	double taxaAprendizagem = 0.01;
	double momentum = 0.9;
	
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
			// -- pega os n�meros da linha
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
				// -- Valora a segunda camada com o somat�rio da primeira
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
		
			
			// -- Validar resultado
		
			boolean acertou = true;
			for (int j = 0; j < camadas[2].getnNeuronios(); j++) {			
				Neuronio neuronioAux = camadas[2].getNeuronios()[j];
				//System.out.println("Valor do Neur�nio: " + neuronioAux.getnValor());
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
			//
			testesPorNumero[DV]++;
			if(acertou){
				qtdAcertos++;
				acertosPorNumero[DV]++;
			}		
			
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

		}
		System.out.println("Quantidade de Testes: " + qtdTestes); 
		System.out.println("Quantidade de Acertos: " + qtdAcertos); 
		for (int i = 0; i < 10; i++) {
			System.out.println("Quantidade de Testes do N�mero " + i +": " + testesPorNumero[i] + " e Acertos: " + acertosPorNumero[i] );			
		}
		
		testarFile.fechaFile();
	}
	
	
	
	
	
	public void aprender(LeitorArquivos aprenderFile){
		
		//-- puxa primeira linha do arquivo
		String[] linhaFl = new String[9999];
		int qtdIteracoes = 0;
		int linhas = 0;
		while (qtdIteracoes <= 0){
		aprenderFile.criaBuffer(1);
		
		while(linhaFl != null){
			
		linhaFl = aprenderFile.retornaSplitLine(); 
		if (linhaFl.length == 0){
			break;
		}
		
		// -- pega os n�meros da linha
		double[] nNumbers = new double[linhaFl.length];
		for (int i = 0; i < linhaFl.length; i++) {
			nNumbers[i] = Double.valueOf(linhaFl[i]);
		}
		
		//testar valores lidos
		//for (int i = 0; i < nNumbers.length; i++) {
		//	System.out.println(nNumbers[i]);
		//}
		
		// -- valora a primeira camada com os numeros
		camadas[0].valorarNeuronios(nNumbers);
		
		
		// SIGMOIDAL NA CAMADA 1
		//for (int j = 0; j < camadas[0].getnNeuronios(); j++) {			
		//	Neuronio neuronioAux = camadas[0].getNeuronios()[j];
		//	neuronioAux.setnValor( this.sigmoidal(neuronioAux.getnValor()));
			
		//}
		
		
		
		
		// -- Salva o digito esperado da linha
		int DV = (int) Math.round(nNumbers[nNumbers.length-1]);
		
		// -- percorre primeira camada
		for (int j = 0; j < camadas[0].getnNeuronios(); j++) {
			Neuronio neuronioAux = camadas[0].getNeuronios()[j];
			
			// -- captura o valor do neuronio corrente
			// -- Valora a segunda camada com o somat�rio da primeira
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
			
			if (j == DV){
				neuronioAux.setnFatorErro(1-neuronioAux.getnValor());  
			}else{
				neuronioAux.setnFatorErro(0-neuronioAux.getnValor()); 
			}
					
		}
		
		// -- calcula erro da camada 3 
		for (int j = 0; j < camadas[2].getnNeuronios(); j++) {			
			Neuronio neuronioAux = camadas[2].getNeuronios()[j];
			neuronioAux.setnErro(calcErro(neuronioAux.getnValor(),neuronioAux.getnFatorErro()));							
		}
	
		
		// -- percorre a camada 3
		for (int j = 0; j < camadas[2].getnNeuronios(); j++) {
			Neuronio neuronioAux = camadas[2].getNeuronios()[j];
			
			// -- captura o erro do neuronio corrente
			double nAux = neuronioAux.getnErro();
			
			for (int i = 0; i < neuronioAux.getCamAntes().length; i++) {
				Neuronio neuronioAux2 = neuronioAux.getCamAntes()[i];
				
				neuronioAux2.setnFatorErro( neuronioAux2.getnFatorErro() + nAux * neuronioAux.getnVCamAntes()[i]);
				
			}			
		}
		
		
		// -- calcula erro da camada 2 
		for (int j = 0; j < camadas[1].getnNeuronios(); j++) {			
			Neuronio neuronioAux = camadas[1].getNeuronios()[j];
			neuronioAux.setnErro(calcErro(neuronioAux.getnValor(),neuronioAux.getnFatorErro()));							
		}
		
		
		//COLOCAR A CONSTANTE TAXA DE APRENDIZAGEM
		for (int j = 0; j < camadas[1].getnNeuronios(); j++) {	
			
			Neuronio neuronioCam2 = camadas[1].getNeuronios()[j];
			
			for (int k = 0; k < camadas[0].getnNeuronios(); k++) {	
				Neuronio neuronioCam1 = camadas[0].getNeuronios()[k];
				double novoPeso = this.momentum * neuronioCam2.getnVCamAntes()[k] + this.taxaAprendizagem * neuronioCam1.getnValor() * neuronioCam2.getnErro();
				//--atualiza os 2 caminhos, da camada 1 depois e da camada 2 antes.
				neuronioCam2.nVCamAntes[k] = novoPeso;
				neuronioCam1.nVCamDepois[j] = novoPeso;
				
			}				
		}
		
		for (int j = 0; j < camadas[2].getnNeuronios(); j++) {	
			Neuronio neuronioCam3 = camadas[2].getNeuronios()[j];
			for (int k = 0; k < camadas[1].getnNeuronios(); k++) {	
				Neuronio neuronioCam2 = camadas[1].getNeuronios()[k];
				double novoPeso = this.momentum * neuronioCam3.getnVCamAntes()[k] + this.taxaAprendizagem * neuronioCam2.getnValor() * neuronioCam3.getnErro();
				//--atualiza os 2 caminhos, da camada 2 depois e da camada 3 antes.
				neuronioCam3.nVCamAntes[k] = novoPeso;
				neuronioCam2.nVCamDepois[j] = novoPeso;
				
			}				
		}
		
		double fatoraux = 0;
		for (int j = 0; j < camadas[2].getnNeuronios(); j++) {	
			Neuronio neuronioCam3 = camadas[2].getNeuronios()[j];
			fatoraux += Math.abs(neuronioCam3.getnFatorErro());			
		}
		fatoraux = fatoraux/camadas[2].getnNeuronios();
		if(linhas == 0){
		
			System.out.println("M�dia do erro esperado da itera��o: " + fatoraux);
		}
		
		
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
		}//fecha while
		aprenderFile.fechaFile();
		qtdIteracoes++;
		}
		
	}
	
	
	
	public double calcErro(double saida, double fatorErro){
		
		return saida * (1-saida) * fatorErro;
		
	}
	
	public double sigmoidal(double somatorio){
	
		return  (1/(1+ Math.exp(-somatorio)));		
	
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
