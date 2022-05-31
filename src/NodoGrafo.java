import java.util.ArrayList;

public class NodoGrafo {
    private boolean isFinal;
    private ArrayList<Transicion> transiciones;
    private int numTransiciones=0;
    private String token;
    private int tokenType;
    private int tokenNumber;

    public NodoGrafo() {
        isFinal=false;
        transiciones = new ArrayList<>();
        numTransiciones=0;
        tokenType=0;
        tokenNumber=0;
    }

    public NodoGrafo(boolean isFinal, ArrayList<Transicion> transiciones, String token, int tokenNumber) {
        this.isFinal = isFinal;
        this.transiciones = transiciones;
        this.token=token;
        numTransiciones=transiciones.size();
    }
    public int getTokenNumber(){
        return this.tokenNumber;
    }

    public void setTokenNumber(int tokenNumber){
        this.tokenNumber=tokenNumber;
    }

    public boolean isIsFinal() {
        return this.isFinal;
    }

    public boolean getIsFinal() {
        return this.isFinal;
    }

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public ArrayList<Transicion> getTransiciones() {
        return this.transiciones;
    }

    public void setTransiciones(ArrayList<Transicion> transiciones) {
        this.transiciones = transiciones;
    }

    public int getNumTransiciones() {
        return this.numTransiciones;
    }

    public void setNumTransiciones(int numTransiciones) {
        this.numTransiciones = numTransiciones;
    }

    public void addTransicion(int nodo, String parametro){
        transiciones.add( new Transicion(parametro, nodo) );
        numTransiciones++;
    }
    public void setToken(String token){
        this.token=token;
    }
    public String getToken(){
        return this.token;
    }
    public void setTokenType(int tokenType){
        this.tokenType=tokenType;
    }
    public int getTokenType (){
        return this.tokenType;
    }
}
