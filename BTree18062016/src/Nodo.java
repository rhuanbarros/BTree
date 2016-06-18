public class Nodo {
	private int ORDEM;
	private int qtdValores;
	
	private Integer valor[];
	private Nodo filho[];
	
	private Nodo pai;

	public Nodo(int ordem, Nodo pai) {
		this.ORDEM = ordem;
		this.pai = pai;
		this.qtdValores = ordem-1;
		
		valor = new Integer[qtdValores];
		for (int i = 0; i < valor.length; i++)
			valor[i] = null;
		
		filho = new Nodo[this.ORDEM];
		for (int i = 0; i < filho.length; i++)
			filho[i] = null;
	}
	
	

	public void inserir(int v) {
		boolean inseriu = false;

		if (temFilhos() == false) {
			// se não tem filho, insere nesse nó mesmo
			for (int i = 0; i < valor.length; i++) {
				if(valor[i]==null) {
					valor[i] = v;
					inseriu = true;
					break;
				}
			}
			if(inseriu == false )
				split(v);
		} else {
			// se tem filho, tem q descobir o nó q insere
			//System.out.println("oi");
			for (int i = 0; i < valor.length; i++) {
				if( valor[i] == null || v < valor[i]) {
					//System.out.println("->"+i);
					filho[i].inserir(v);
					break;
				}
			}
		}

		ordenar();
	}

	public void split(int v) {
		//cria array com um espaço a mais para divisao
		int aux[] = new int[qtdValores + 1];

		// copia para o array auxiliar e coloca o sexto valor no final
		for (int i = 0; i < aux.length - 1; i++)
			aux[i] = valor[i];
		aux[qtdValores] = v;

		aux = ordenarParaSplit(aux);

		int metade = qtdValores/2;

		Nodo n1 = new Nodo(ORDEM, this);
		for(int i=0;i<metade;i++)
			n1.inserir(aux[i]);

		Nodo n2 = new Nodo(ORDEM, this);
		for(int i=metade+1;i<aux.length;i++)
			n2.inserir(aux[i]);

		limparNodo();
		if (pai == null) {
			valor[0] = aux[2];
			// setar filhos
			filho[0] = n1;
			filho[1] = n2;
		} else {
			//ta dando pau aqui		<---------------ERRO
			pai.inserirNoPaiComFilhos(aux[2], n1, n2);
		}
	}
	
	public void inserirNoPaiComFilhos(int v, Nodo n1, Nodo n2) {
		boolean inseriu = false;
		
		for (int i = 0; i < valor.length; i++) {
			if(valor[i]==null) {
				//System.out.println("v->"+v);
				//System.out.println("i->"+i);
				//System.out.println(valor[i]);

				//ta dando pau aqui
				
				valor[i] = v;
				filho[i] = n1;
				filho[i+1] = n2;
				inseriu = true;
				break;
			}
		}
		//if(inseriu == false )
			//split(v);
	}
	
	public void inserirNoPai(int v) {
		boolean inseriu = false;
		
		for (int i = 0; i < valor.length; i++) {
			if(valor[i]==null) {
				valor[i] = v;
				inseriu = true;
				break;
			}
		}
		if(inseriu == false )
			split(v);
	}

	public void limparNodo() {
		for (int i = 0; i < valor.length; i++)
			valor[i] = null;
	}

	private int[] ordenarParaSplit(int[] vaux) {
		boolean trocou = false;
		do {
			trocou = false;
			for (int i = 0; i < qtdValores; i++) {
				if (vaux[i] > vaux[i + 1]) {
					int aux;
					if (i < qtdValores + 1) {
						aux = vaux[i];
						vaux[i] = vaux[i + 1];
						vaux[i + 1] = aux;
					} else
						System.out.println("\nInforme uma posicao menor que a ultima!");
					trocou = true;
				}
			}
		} while (trocou);
		return vaux;
	}

	public void ordenar() {
		boolean trocou = false;
		do {
			trocou = false;
			for (int i = 0; i < qtdValores - 1; i++) {
				if (valor[i] != null && valor[i + 1] != null) {
					if (valor[i] > valor[i + 1]) {
						int aux;
						aux = valor[i];
						valor[i] = valor[i + 1];
						valor[i + 1] = aux;
						trocou = true;
					}
				}
			}
		} while (trocou);

	}

	@Override
	public String toString() {
		String retorno = "[";
		for (int i = 0; i < qtdValores; i++) {
			if (valor[i] == null)
				retorno += "_";
			else
				retorno += valor[i];
			if (i != qtdValores - 1)
				retorno += "|";
		}
		retorno += "]";

		// começa a imprimir os nodos filhos
		for (int i = 0; i < qtdValores + 1; i++) {
			if (filho[i] != null)
				filho[i].imprime();
		}
		return retorno;
	}

	public boolean temFilhos() {
		boolean retorno = false;
		for (int i = 0; i < filho.length; i++) {
			if (filho[i] != null)
				retorno = true;
		}
		return retorno;
	}

	public void imprime() {
		System.out.println( toString() );
	}
	
	public void imprime3(int nivel) {
		int totalFilhos=0;
		
		if( temFilhos() ) {
			for(int j=filho.length-1;j>=0;j--) {
				if(filho[j] != null)
					totalFilhos++;
			}
			int filhoDoMeio = totalFilhos/2 +1;
			
			for(int i=filho.length-1;i>=0;i--) {
				if(filho[i] != null) {
					if(i==filhoDoMeio)
						imprimeApenasEsseNodo();
					
					filho[i].imprime2(nivel+1);
				}
			}
		}
		for(int i=0;i<nivel; i++)
			System.out.print("          ");
		imprimeApenasEsseNodo();
	}

	public void imprime2(int nivel) {
		int totalFilhos=0;
		
		if( temFilhos() ) {
			for(int i=filho.length-1;i>=0;i--) {
				if(filho[i] != null) {
					filho[i].imprime2(nivel+1);
				} else {
					imprimeNodoVazio(nivel+1);
				}
			}
		}
		for(int i=0;i<nivel; i++)
			System.out.print("          ");
		imprimeApenasEsseNodo();
	}
	
	public void imprime4(int nivel) {
		int totalFilhos=0;
		int filhoDoMeio=0;
		
		if( temFilhos() ) {
			
			for(int j=filho.length-1;j>=0;j--) {
				if(filho[j] != null)
					totalFilhos++;
			}
			filhoDoMeio = totalFilhos/2 +1;
			
			for(int i=filho.length-1;i>filhoDoMeio;i--) {
				if(filho[i] != null) {
					filho[i].imprime4(nivel+1);
				} else {
					imprimeNodoVazio(nivel+1);
				}
			}
		}
		for(int i=0;i<nivel; i++)
			System.out.print("          ");
		imprimeApenasEsseNodo();
		
		
		if( temFilhos() ) {
			for(int i=filhoDoMeio;i>=0;i--) {
				if(filho[i] != null) {
					filho[i].imprime4(nivel+1);
				} else {
					imprimeNodoVazio(nivel+1);
				}
			}
		}
		
		
		
	}
	
	public void imprimeNodoVazio(int nivel) {
		for(int i=0;i<nivel; i++)
			System.out.print("          ");
		
		String retorno = "[";
		for (int i = 0; i < qtdValores; i++) {
			retorno += "_";
			if (i != qtdValores - 1)
				retorno += "|";
		}
		retorno += "]";
		System.out.println(retorno);
	}
	
	public void imprimeApenasEsseNodo() {
		String retorno = "[";
		for (int i = 0; i < qtdValores; i++) {
			if (valor[i] == null)
				retorno += "_";
			else
				retorno += valor[i];
			if (i != qtdValores - 1)
				retorno += "|";
		}
		retorno += "]";
		System.out.println(retorno);
	}

}
