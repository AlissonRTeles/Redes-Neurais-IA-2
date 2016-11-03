package neuronios;

public class Neuronio {
	
	Neuronio[] camAntes;
	double[]    nVCamAntes;
	Neuronio[] camDEpois;
	double[]    nVCamDepois;
	
	double nValor;
	double nErro;
	double nFatorErro;
	Integer nID;
	
	Neuronio(Integer nCamAntes, Integer nCamDepois){
		this.camAntes = new Neuronio[nCamAntes];
		this.nVCamAntes= new double[nCamAntes];
		this.camDEpois = new Neuronio[nCamDepois];
		this.nVCamDepois= new double[nCamDepois];
		this.nFatorErro =  0.0;
		this.nErro = 0.0;
		this.nValor= 0.0;
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

	public double getnValor() {
		return nValor;
	}

	public void setnValor(double nValor) {
		this.nValor = nValor;
	}

	public double getnErro() {
		return nErro;
	}

	public void setnErro(double nErro) {
		this.nErro = nErro;
	}

	public double getnFatorErro() {
		return nFatorErro;
	}

	public void setnFatorErro(double nFatorErro) {
		this.nFatorErro = nFatorErro;
	}
	
	public void montaCamAntes(Integer nTam){
		this.camAntes = new Neuronio[nTam];
		this.nVCamAntes = new double[nTam];
	}
	
	public void montaCamDepois(Integer nTam){
		this.camDEpois = new Neuronio[nTam];
		this.nVCamDepois = new double[nTam];
	}


	public double[] getnVCamAntes() {
		return nVCamAntes;
	}


	public void setnVCamAntes(double[] nVCamAntes) {
		this.nVCamAntes = nVCamAntes;
	}

	public double[] getnVCamDepois() {
		return nVCamDepois;
	}

	public void setnVCamDepois(double[] nVCamDepois) {
		this.nVCamDepois = nVCamDepois;
	}
	
}
