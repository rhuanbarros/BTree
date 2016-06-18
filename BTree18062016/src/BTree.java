public class BTree {
	private int ORDEM;
	private int[] valoresParaInserir;
	private Nodo raiz = null;
	
	public BTree(int ordem, int[] valoresParaInserir) {
		this.ORDEM = ordem;
		this.valoresParaInserir = valoresParaInserir;
		
		//inicializa a raiz da arvore
		raiz = new Nodo(ORDEM, null);
		for(int i=0;i<valoresParaInserir.length;i++)
			inserir(valoresParaInserir[i]);
	}
	
	public void inserir(int v) {
		//descobrir em qual nodo tem q ser inserido
		//daí mandar inserir no nodo
		//como nesse caso eh soh pra inserir na raiz
		raiz.inserir(v);
	}
	
	public void imprimeArvoreB() {
		//raiz.imprime2(0);
		//raiz.imprime();
		raiz.imprime4(0);
	}
	
	public static void main(String[] args) {
		int[] valoresParaInserir = {3, 7, 10, 24, 14, 19, 21, 15, 1, 5, 2, 8};//, 9, 6, 11, 12, 17};//, 18, 20, 22, 21, 23, 25};
		BTree arvoreb = new BTree(5, valoresParaInserir);
		arvoreb.imprimeArvoreB();
	}
	
}
