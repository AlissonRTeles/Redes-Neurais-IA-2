package neuronios;

public class Neuronio {
	Neuronio[] camAntes;
	Neuronio[] camDEpois;
	
	Float nValor;
	Float nErro;
	Float nFatorErro;
	Integer nID;
	
	Neuronio(Integer nCamAntes, Integer nCamDepois){
		this.camAntes = new Neuronio[nCamAntes];
		this.camDEpois = new Neuronio[nCamDepois];
		this.nFatorErro = (float) 0;
		this.nErro = (float) 0;
	}
	
	
	public Integer getnID() {
		return nID;
	}


	public void setnID(Integer nID) {
		this.nID = nID;
	}


	public Neuronio[] getCamAntes() {
		return camAntes;
	}
	
	public void setCamAntes(Neuronio[] camAntes) {
		this.camAntes = camAntes;
	}

	public Neuronio[] getCamDEpois() {
		return camDEpois;
	}

	public void setCamDEpois(Neuronio[] camDEpois) {
		this.camDEpois = camDEpois;
	}

	public Float getnValor() {
		return nValor;
	}

	public void setnValor(Float nValor) {
		this.nValor = nValor;
	}

	public Float getnErro() {
		return nErro;
	}

	public void setnErro(Float nErro) {
		this.nErro = nErro;
	}

	public Float getnFatorErro() {
		return nFatorErro;
	}

	public void setnFatorErro(Float nFatorErro) {
		this.nFatorErro = nFatorErro;
	}
	
	public void montaCamAntes(Integer nTam){
		this.camAntes = new Neuronio[nTam];
	}
	
	public void montaCamDepois(Integer nTam){
		this.camDEpois = new Neuronio[nTam];
	}
}
