public class BTree {
	private int ORDEM;
	private Nodo raiz = null;

	public BTree(int ordem, int[] valoresParaInserir) {
		this.ORDEM = ordem;

		// inicializa a raiz da arvore
		raiz = new Nodo(null);
		for (int i = 0; i < valoresParaInserir.length; i++) {
			inserir(valoresParaInserir[i], raiz);

			System.out.println("NIVEL -> " + (i + 1) + " |"+valoresParaInserir[i]+"|");
			raiz.imprimir();
			System.out.println("\n");
		}
		
		remover(21, raiz);
				
		System.out.println(".....................................");
		raiz.imprimir();
		System.out.println(".....................................");
		
		remover(20, raiz);
		
		System.out.println(".....................................");
		raiz.imprimir();
		System.out.println(".....................................");
		
		/*
		 * https://pt.wikipedia.org/wiki/%C3%81rvore_B
		 * FAZER O CASO 3
		 */
		
		remover(11, raiz);
		
		System.out.println(".....................................");
		raiz.imprimir();
		System.out.println(".....................................");
		
		System.out.println("FIMMMMMMMMMMMMMMMMMMMM");		
	}
	
	private void remover(int v, Nodo nodo) {
		System.out.println("passou pelo remover!<---------------");
		
		int posicaoEncontrado = -1;
		boolean encontrou = false;
		
		//procura nesse nodo
		if(nodo != null) {
			for (int i = 0; i < nodo.valor.length; i++) 
				if ( nodo.valor[i] != null && v == nodo.valor[i] ) {
					encontrou = true;
					posicaoEncontrado=i;
					//break; //ver se precisa disso
				}
		
			if( encontrou ) {
				//se encontrou remove
				System.out.println("ENCONTROU A BAGAÇA!!!!_____________________________________");
				
				if( nodo.temFilhos() == false ) {  
					if(nodo.quantidadeValores() > ORDEM/2) {
						removeValorEPuxaPraEsquerda(nodo, posicaoEncontrado);
					} else { //entao eh remocao na folha COM UNDERFLOW
						
						//remove o valor
						removeValorEPuxaPraEsquerda(nodo, posicaoEncontrado);
						
						Nodo irmaoEsquerda = null;
						Nodo irmaoDireita = null;
						int posicaoDesteNodoFilhoNoPai=0;
						
						//pega nodos irmaos
						Nodo pai = nodo.pai;
						for (int i = 0; i < pai.filho.length; i++) {
							if( pai.filho[i].valor[0] != null && pai.filho[i].valor[0] == nodo.valor[0] ) {
								System.out.println("PASSOU AQUII++++++++++++++++++++++++++");
								posicaoDesteNodoFilhoNoPai = i;
								if( i-1 >=0 ) irmaoEsquerda = pai.filho[i-1];
								if( i+1 <=3 ) irmaoDireita = pai.filho[i+1];
								break;
							}
						}
						
						//verifica se da pra fazer a juncao com o irmao da esquerda
						if( irmaoEsquerda.quantidadeValores() > ORDEM/2 ) {
							//insere valor do pai neste nodo
							nodo.inserir( pai.valor[posicaoDesteNodoFilhoNoPai-1] );
							
							//procura o valor no irmao
							Integer valorIrmaoErquerda = null;
							for (int i = 0; i < nodo.valor.length; i++) 
								if ( nodo.valor[i] == null ) {
									valorIrmaoErquerda = irmaoEsquerda.valor[i];
									irmaoEsquerda.valor[i] = null;	//apaga valor do irmao da esquerda!!
									break;
								}
							
							//coloca o valor no pai
							pai.valor[ posicaoDesteNodoFilhoNoPai-1 ] = valorIrmaoErquerda;
							
						} else if( irmaoDireita.quantidadeValores() > ORDEM/2 ) {  //se nao der pra fazer a juncao a esquerda ve se rola a direita
							//insere valor do pai neste nodo
							nodo.inserir( pai.valor[posicaoDesteNodoFilhoNoPai] );
							
							//procura o valor no irmao
							Integer valorIrmaoDireita = irmaoDireita.valor[0];
							//remove valor do irmao da esquerda!!
							removeValorEPuxaPraEsquerda(irmaoDireita, 0);
							
							//coloca o valor no pai
							pai.valor[ posicaoDesteNodoFilhoNoPai ] = valorIrmaoDireita;
						}
						
					}
				}
				
				System.out.println("SHOW");
			} else if( nodo.temFilhos() ) {
				//se nao encontrou e tem filhos, tem q ver em qual filho procurar
				int posicao = -1;
				for (int i = 0; i < nodo.valor.length; i++) 
					if ( nodo.valor[i] == null || v < nodo.valor[i] ) {
						posicao=i;
						break;
					}
				
				remover(v, nodo.filho[posicao]);
			} else {
				System.out.println("Valor não encontrado na BTree");
			}
		}
	}

	private void removeValorEPuxaPraEsquerda(Nodo nodo, int posicaoEncontrado) {
		nodo.valor[posicaoEncontrado] = null;
		//puxa todos os valores para a esquerda
		for (int k = posicaoEncontrado; k < nodo.valor.length; k++) {
			if( k+1 < 4 ) {
				Integer proximoNodo = nodo.valor[k+1];
				if(proximoNodo == null) nodo.valor[k] = null;
				else nodo.valor[k] = proximoNodo;
			}
		}
	}

	public void inserir(int v, Nodo nodo) {
		if( nodo.temFilhos() ) {
			// se tem filho, tem q descobir o nó q insere
				for (int i = 0; i < nodo.valor.length; i++) {
					if ( nodo.valor[i] == null || v < nodo.valor[i]) {
						inserir(v, nodo.filho[i]);
						break;
					}
				}
		} else {
			nodo.inserir(v);
		}
	}

	public static void main(String[] args) {
		int[] valoresParaInserir = { 3, 7, 10, 24, 14, 19, 21, 15, 1, 5, 2, 8, 9, 6, 11, 12, 17, 18, 20, 22, 23, 25};
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

		public Nodo inserir(int v) {
			Nodo retorno = null;
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
				retorno = split(v);

			ordenar();
			return retorno;
		}
		
		int quantidadeValores() {
			int quantidade = 0;
			for (int i = 0; i < valor.length; i++) {
				if( valor[i] != null ) quantidade++; 
			}
			return quantidade;
		}

		public Nodo split(int v) {
			System.out.println("----------------> RODOU SPLIT");
			Nodo retorno = null;

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
				limparNodo();
				valor[0] = aux[2];
				// setar filhos
				n1.pai = this;
				n2.pai = this;
				
				if(filho[0] != null) filho[0].pai = n1; 
				n1.inserirFilho(filho[0]);
				if(filho[1] != null) filho[1].pai = n1;
				n1.inserirFilho(filho[1]);
				if(filho[2] != null) filho[2].pai = n1;
				n1.inserirFilho(filho[2]);
				if(filho[3] != null) filho[3].pai = n2;
				n2.inserirFilho(filho[3]);
				if(filho[4] != null) filho[4].pai = n2;
				n2.inserirFilho(filho[4]);				
				
				limparFilhos();
				filho[0] = n1;
				filho[1] = n2;
				
				for(int i=0;i < n1.valor.length; i++) {
					if( n1.valor[i] != null && n1.valor[i] == v) 
						retorno = n1;
				}
				for(int i=0;i < n2.valor.length; i++) {
					if( n2.valor[i] != null && n2.valor[i] == v) 
						retorno = n2;
				}
				 
			} else {
				//pai.inserir(v);
				
				inserirNoPaiComFilhos(aux[metade], n1, n2);
			}
			return retorno;
		}

		public void inserirFilho( Nodo nodo ) {
			if( nodo != null ) {
				for (int i = 0; i < valor.length; i++) {
					if (valor[i] == null ) {
						filho[i] = nodo;
						break;
					} else if( nodo.valor[0] != null && nodo.valor[0] < valor[i] ){
						filho[i] = nodo;
						break;
					}
				}
			}
		}

		public void inserirNoPaiComFilhos(int v, Nodo n1, Nodo n2) {
			Nodo paiNovo = null;
			paiNovo = pai.inserir(v);
			
			if( paiNovo == null ) paiNovo = pai;
			
			if( paiNovo.filho[0] == null) {
				paiNovo.filho[0] = n1;
				n1.pai = pai;
				paiNovo.filho[1] = n2;
				n2.pai = pai;
			} else {
				for (int i = 0; i < paiNovo.filho.length; i++) {
					if (paiNovo.filho[i] != null && paiNovo.filho[i].valor[0] == null) {
						paiNovo.filho[i] = n1;
	
						int posicaoN2 = i + 1;
						if (paiNovo.filho[posicaoN2] != null) {
	
							for (int w = paiNovo.filho.length - 1; w >= posicaoN2; w--) {
								if (paiNovo.filho[w] != null)
									paiNovo.filho[w + 1] = paiNovo.filho[w];
							}
	
						}
						paiNovo.filho[posicaoN2] = n2;
						System.out.println("<++++++++++++++++++++++++++++++");
					}
					//TA COM BUG AQUI!!!
					//PQ JA TEM NODOS FILHOS. ELE PRECISARIA ENCONTRAR O PRIMEIRO ESPAÇO VAZIO E INSERIR.
				}
			}
			
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
		
		public void limparFilhos() {
			for (int i = 0; i < filho.length; i++)
				filho[i] = null;
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
