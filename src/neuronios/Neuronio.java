package neuronios;

public class Neuronio {
	
	Neuronio[] camAntes;
	Float[]    nVCamAntes;
	Neuronio[] camDEpois;
	Float[]    nVCamDepois;
	
	Float nValor;
	Float nErro;
	Float nFatorErro;
	Integer nID;
	
	Neuronio(Integer nCamAntes, Integer nCamDepois){
		this.camAntes = new Neuronio[nCamAntes];
		this.nVCamAntes= new Float[nCamAntes];
		this.camDEpois = new Neuronio[nCamDepois];
		this.nVCamDepois= new Float[nCamDepois];
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
		this.nVCamAntes = new Float[nTam];
	}
	
	public void montaCamDepois(Integer nTam){
		this.camDEpois = new Neuronio[nTam];
		this.nVCamDepois = new Float[nTam];
	}


	public Float[] getnVCamAntes() {
		return nVCamAntes;
	}


	public void setnVCamAntes(Float[] nVCamAntes) {
		this.nVCamAntes = nVCamAntes;
	}

	public Float[] getnVCamDepois() {
		return nVCamDepois;
	}

	public void setnVCamDepois(Float[] nVCamDepois) {
		this.nVCamDepois = nVCamDepois;
	}
	
}
