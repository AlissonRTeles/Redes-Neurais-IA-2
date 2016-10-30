package neuronios;

import java.util.Random;

public class RedeNeural {
	CamadaNeural[] camadas;
	Integer 	   nCamadas;
	float minX = 0.0f;
	float maxX = 1.0f;
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
	
	private float geraPeso(){
		return rand.nextFloat() * (maxX - minX) + minX;
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
