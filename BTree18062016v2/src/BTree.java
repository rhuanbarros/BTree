public class BTree {
	private int ORDEM;
	private Nodo raiz = null;

	public BTree(int ordem, int[] valoresParaInserir) {
		this.ORDEM = ordem;

		// inicializa a raiz da arvore
		raiz = new Nodo(null);
		for (int i = 0; i < valoresParaInserir.length; i++) {
			inserir(valoresParaInserir[i], raiz);

			System.out.println("NIVEL -> " + (i + 1));
			raiz.imprimir();
			System.out.println("\n");
		}
	}

	public void inserir(int v, Nodo nodo) {
		if (nodo.temFilhos()) {

			// se tem filho, tem q descobir o nó q insere
			for (int i = 0; i < nodo.valor.length; i++) {
				if (nodo.valor[i] == null || v < nodo.valor[i]) {
					nodo.filho[i].inserir(v);
					break;
				}
			}
		
		} else {
			nodo.inserir(v);
		}
	}

	public static void main(String[] args) {
		int[] valoresParaInserir = { 3, 7, 10, 24, 14, 19, 21, 15, 1, 5, 2, 8, 9, 6, 11, 12, 17};//, 18, 20, 22, 21, 23, 25};
		new BTree(5, valoresParaInserir);
	}

	class Nodo {
		private int qtdValores;

		private Integer valor[];
		private Nodo filho[];

		private Nodo pai;

		public Nodo(Nodo pai) {
			this.pai = pai;
			this.qtdValores = ORDEM - 1;

			valor = new Integer[qtdValores];
			for (int i = 0; i < valor.length; i++)
				valor[i] = null;

			filho = new Nodo[ORDEM];
			for (int i = 0; i < filho.length; i++)
				filho[i] = null;
		}

		public void inserir(int v) {
			boolean inseriu = false;

			for (int i = 0; i < valor.length; i++) {
				if (valor[i] == null) {
					valor[i] = v;
					inseriu = true;
					break;
				}
			}

			// PROCURA UM ESPAÇO VAZIO, SE NAO ACHAR
			// TEM Q FAZER SPLIT
			if (inseriu == false)
				split(v);

			ordenar();
		}

		public void split(int v) {
			System.out.println("----------------> RODOU SPLIT");
			
			// cria array com um espaço a mais para divisao
			int aux[] = new int[qtdValores + 1];

			// copia para o array auxiliar e coloca o sexto valor no final
			for (int i = 0; i < aux.length - 1; i++)
				aux[i] = valor[i];
			aux[qtdValores] = v;

			aux = ordenarParaSplit(aux);

			int metade = qtdValores / 2;

			Nodo n1 = new Nodo(pai);
			for (int i = 0; i < metade; i++)
				n1.inserir(aux[i]);

			Nodo n2 = new Nodo(pai);
			for (int i = metade + 1; i < aux.length; i++)
				n2.inserir(aux[i]);

			limparNodo();

			if (pai == null) {
				valor[0] = aux[2];
				// setar filhos
				n1.pai = this;
				n2.pai = this;
				filho[0] = n1;
				filho[1] = n2;
			} else {
				inserirNoPaiComFilhos(aux[metade], n1, n2);
			}
		}

		public void inserirNoPaiComFilhos(int v, Nodo n1, Nodo n2) {
			pai.inserir(v);

			/*if( pai.pai == null ) {
				Nodo n1aux = new Nodo(this);
				n1aux.valor[0] = valor[0];
				n1aux.valor[1] = valor[1];
				
				n1aux.filho[0] = filho[0];
				n1aux.filho[1] = filho[1];
				n1aux.filho[2] = filho[2];
				
 				Nodo n2aux = new Nodo(this);
				n2aux.valor[0] = valor[2];
				n2aux.valor[1] = valor[3];
				
				n2aux.filho[0] = filho[3];
				n2aux.filho[1] = filho[4];
				
				limparNodo();
				
				valor[0]=v;
				
				filho[0]=n1aux;
				filho[1]=n2aux;
				
			} else {
				*/for (int i = 0; i < pai.filho.length; i++) {
					if ( pai.filho[i] != null && pai.filho[i].valor[0] == null ) {
						pai.filho[i] = n1;
	
						int posicaoN2 = i + 1;
						if (pai.filho[posicaoN2] != null) {
	
							for (int w = pai.filho.length - 1; w >= posicaoN2; w--) {
								if (pai.filho[w] != null)
									pai.filho[w + 1] = pai.filho[w];
							}
	
						}
						pai.filho[posicaoN2] = n2;
					}
				}
			//}

		}

		public void inserirNoPai(int v) {
			boolean inseriu = false;

			for (int i = 0; i < valor.length; i++) {
				if (valor[i] == null) {
					valor[i] = v;
					inseriu = true;
					break;
				}
			}
			if (inseriu == false)
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

		public boolean temFilhos() {
			boolean retorno = false;
			for (int i = 0; i < filho.length; i++) {
				if (filho[i] != null)
					retorno = true;
			}
			return retorno;
		}

		public void imprimir() {
			imprime(0);
		}

		private void imprime(int nivel) {
			int totalFilhos = 0;
			int filhoDoMeio = 0;

			if (temFilhos()) {

				for (int j = filho.length - 1; j >= 0; j--) {
					if (filho[j] != null)
						totalFilhos++;
				}
				filhoDoMeio = totalFilhos / 2 + 1;

				for (int i = filho.length - 1; i > filhoDoMeio; i--) {
					if (filho[i] != null) {
						filho[i].imprime(nivel + 1);
					} else {
						imprimeNodoVazio(nivel + 1);
					}
				}
			}
			for (int i = 0; i < nivel; i++)
				System.out.print("          ");
			imprimeApenasEsseNodo();

			if (temFilhos()) {
				for (int i = filhoDoMeio; i >= 0; i--) {
					if (filho[i] != null) {
						filho[i].imprime(nivel + 1);
					} else {
						imprimeNodoVazio(nivel + 1);
					}
				}
			}

		}

		private void imprimeNodoVazio(int nivel) {
			for (int i = 0; i < nivel; i++)
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

		private void imprimeApenasEsseNodo() {
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

}
