package neuronios;

import java.util.Random;

import main.LeitorArquivos;

public class RedeNeural {
	CamadaNeural[] camadas;
	Integer 	   nCamadas;
	double minX = 0.0;
	double maxX = 1.0;
	Random rand = new Random();

	
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
	
	public void aprender(LeitorArquivos aprenderFile){
		aprenderFile.criaBuffer();
		//-- puxa primeira linha do arquivo
		String[] linhaFl = aprenderFile.retornaSplitLine(); 
		
		// -- converte
		double[] nNumbers = new double[linhaFl.length];
		for (int i = 0; i < linhaFl.length; i++) {
			nNumbers[i] = Double.valueOf(linhaFl[i]);
		}
		
		camadas[0].valorarNeuronios(nNumbers);
		
		int DV = (int) Math.round(nNumbers[nNumbers.length-1]);
		
		// -- percorre primeira camada
		for (int j = 0; j < camadas[0].getnNeuronios(); j++) {
			Neuronio neuronioAux = camadas[0].getNeuronios()[j];
			
			// -- captura o valor do neuronio corrente
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
	
		
		aprenderFile.fechaFile();
		
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
