package pilha;

public class Pilha {
    private No topo;

    public Pilha() {
        topo = null;
    }
    
    public void Push(String info){
        if(topo == null)
            topo = new No(info, null);
        else{
            No no = new No(info, topo);
            topo = no;
        }
    }
    
    public String Pop(){
        if(topo != null){
            No no = topo;
            topo = topo.getProx();
            return no.getInfo();
        }
        return null;
    }
}
