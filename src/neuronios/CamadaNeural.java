package neuronios;

public class CamadaNeural {
	Neuronio[] neuronios;
	Integer nNeuronios;
	
	public CamadaNeural() {
		// TODO Auto-generated constructor stub
		this.neuronios = new Neuronio[0];
		nNeuronios = 0;
	}

	public void setNNeuronios(Integer nNeuronios){
		this.nNeuronios = nNeuronios;
		this.neuronios = new Neuronio[nNeuronios];
		this.criaNeuronios();
	}
	
	public void criaNeuronios(){
		for(int i = 0;i<this.nNeuronios;i++){
			this.neuronios[i] = new Neuronio(0, 0);
			this.neuronios[i].setnID(i);
		}
	}
	
	public Integer getnNeuronios (){
		return this.nNeuronios;
	}
	
	public Neuronio getNeuronio(Integer nIndex){
		return neuronios[nIndex];
	}
	
	public void setNeuronio (Integer nIndex, Neuronio neuronio){
		this.neuronios[nIndex] = neuronio;
	}
}
