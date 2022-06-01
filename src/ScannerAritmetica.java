import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
public class ScannerAritmetica{
    private Grafo automata;
    private ArrayList<Token> tokens;

    public ScannerAritmetica(){}
   public String ValidarAutomata(String cadena){
        String cadenaDeTokens="";
        String caracteresEncontrados="";
        Token tokenLocal = new Token();
        int nodoActual=0;
        NodoGrafo nodoEvaluando=null;
        for (int i=0; i<cadena.length();i++){
           caracteresEncontrados+=cadena.charAt(i);
           nodoEvaluando = automata.getNodo(nodoActual);
           boolean transitioned=false;
           for (int j=0; j<nodoEvaluando.getTransiciones().size();j++){
                if(nodoEvaluando.getTransiciones().get(j).getParametro().contains(Character.toString(cadena.charAt(i)))){
                    nodoActual = nodoEvaluando.getTransiciones().get(j).getObjetivo();
                    transitioned=true;
                }
           }
           if(!transitioned){
                if(nodoEvaluando.getIsFinal()){
                    cadenaDeTokens+=(nodoEvaluando.getToken()+", ");
                    caracteresEncontrados=caracteresEncontrados.substring(0, caracteresEncontrados.length()-1);                    tokenLocal = new Token(nodoEvaluando.getTokenNumber(),nodoEvaluando.getToken(),  caracteresEncontrados);
                    tokens.add(tokenLocal);
                    caracteresEncontrados="";
                    i--;
                    nodoActual=0;
                }else{
                   return "Error de entrada. Entrada no valida";
                }
            }
           if(nodoEvaluando.getIsFinal()&&nodoEvaluando.getTokenType()==0){
                cadenaDeTokens+=(nodoEvaluando.getToken()+", ");
                caracteresEncontrados=caracteresEncontrados.substring(0, caracteresEncontrados.length()-1);                tokenLocal = new Token(nodoEvaluando.getTokenNumber(),nodoEvaluando.getToken(),  caracteresEncontrados);
                tokens.add(tokenLocal);
                caracteresEncontrados="";
                i--;
                nodoActual=0;
            }
        }
        nodoEvaluando = automata.getNodo(nodoActual);
        if(nodoEvaluando.getIsFinal()){
        tokenLocal = new Token(nodoEvaluando.getTokenNumber(), nodoEvaluando.getToken(), caracteresEncontrados);        tokens.add(tokenLocal);        cadenaDeTokens+=(nodoEvaluando.getToken());
        }
        return cadenaDeTokens;
    }
    public ArrayList<Token> GenerarTokenString(String cadenaAEvaluar)
    {
        tokens = new ArrayList<>();
        generateAutoma();
        String cadenaDeRetorno = ValidarAutomata(cadenaAEvaluar);
        System.out.println("Respuesta de automata: "+cadenaDeRetorno);
        return tokens;
   }
    private void generateAutoma(){
       automata = new Grafo();
		automata.addNode(0, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
		automata.getNodo(automata.getCantidadNodos()-1).setIsFinal(true);
		automata.getNodo(automata.getCantidadNodos()-1).setToken("ident");
		automata.getNodo(automata.getCantidadNodos()-1).setTokenNumber(0);
		automata.getNodo(automata.getCantidadNodos()-1).setTokenType(1);
		automata.addNode(0, "0123456789");
		automata.getNodo(automata.getCantidadNodos()-1).setIsFinal(true);
		automata.getNodo(automata.getCantidadNodos()-1).setToken("number");
		automata.getNodo(automata.getCantidadNodos()-1).setTokenNumber(1);
		automata.getNodo(automata.getCantidadNodos()-1).setTokenType(1);
		automata.addNode(0, ";");
		automata.getNodo(automata.getCantidadNodos()-1).setIsFinal(true);
		automata.getNodo(automata.getCantidadNodos()-1).setToken(";");
		automata.getNodo(automata.getCantidadNodos()-1).setTokenNumber(2);
		automata.getNodo(automata.getCantidadNodos()-1).setTokenType(1);
		automata.addNode(0, ".");
		automata.getNodo(automata.getCantidadNodos()-1).setIsFinal(true);
		automata.getNodo(automata.getCantidadNodos()-1).setToken(".");
		automata.getNodo(automata.getCantidadNodos()-1).setTokenNumber(3);
		automata.getNodo(automata.getCantidadNodos()-1).setTokenType(1);
		automata.addNode(0, "+");
		automata.getNodo(automata.getCantidadNodos()-1).setIsFinal(true);
		automata.getNodo(automata.getCantidadNodos()-1).setToken("+");
		automata.getNodo(automata.getCantidadNodos()-1).setTokenNumber(4);
		automata.getNodo(automata.getCantidadNodos()-1).setTokenType(1);
		automata.addNode(0, "-");
		automata.getNodo(automata.getCantidadNodos()-1).setIsFinal(true);
		automata.getNodo(automata.getCantidadNodos()-1).setToken("-");
		automata.getNodo(automata.getCantidadNodos()-1).setTokenNumber(5);
		automata.getNodo(automata.getCantidadNodos()-1).setTokenType(1);
		automata.addNode(0, "*");
		automata.getNodo(automata.getCantidadNodos()-1).setIsFinal(true);
		automata.getNodo(automata.getCantidadNodos()-1).setToken("*");
		automata.getNodo(automata.getCantidadNodos()-1).setTokenNumber(6);
		automata.getNodo(automata.getCantidadNodos()-1).setTokenType(1);
		automata.addNode(0, "/");
		automata.getNodo(automata.getCantidadNodos()-1).setIsFinal(true);
		automata.getNodo(automata.getCantidadNodos()-1).setToken("/");
		automata.getNodo(automata.getCantidadNodos()-1).setTokenNumber(7);
		automata.getNodo(automata.getCantidadNodos()-1).setTokenType(1);
		automata.addNode(0, "(");
		automata.getNodo(automata.getCantidadNodos()-1).setIsFinal(true);
		automata.getNodo(automata.getCantidadNodos()-1).setToken("(");
		automata.getNodo(automata.getCantidadNodos()-1).setTokenNumber(8);
		automata.getNodo(automata.getCantidadNodos()-1).setTokenType(1);
		automata.addNode(0, ")");
		automata.getNodo(automata.getCantidadNodos()-1).setIsFinal(true);
		automata.getNodo(automata.getCantidadNodos()-1).setToken(")");
		automata.getNodo(automata.getCantidadNodos()-1).setTokenNumber(9);
		automata.getNodo(automata.getCantidadNodos()-1).setTokenType(1);
		automata.addNode(1, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
		automata.getNodo(automata.getCantidadNodos()-1).setIsFinal(true);
		automata.getNodo(automata.getCantidadNodos()-1).setToken("ident");
		automata.getNodo(automata.getCantidadNodos()-1).setTokenNumber(0);
		automata.getNodo(automata.getCantidadNodos()-1).setTokenType(1);
		automata.addNode(1, "0123456789");
		automata.getNodo(automata.getCantidadNodos()-1).setIsFinal(true);
		automata.getNodo(automata.getCantidadNodos()-1).setToken("ident");
		automata.getNodo(automata.getCantidadNodos()-1).setTokenNumber(0);
		automata.getNodo(automata.getCantidadNodos()-1).setTokenType(1);
		automata.addNode(2, "0123456789");
		automata.getNodo(automata.getCantidadNodos()-1).setIsFinal(true);
		automata.getNodo(automata.getCantidadNodos()-1).setToken("number");
		automata.getNodo(automata.getCantidadNodos()-1).setTokenNumber(1);
		automata.getNodo(automata.getCantidadNodos()-1).setTokenType(1);
		automata.addTransicion(11, 11, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
		automata.addTransicion(11, 12, "0123456789");
		automata.addTransicion(12, 11, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
		automata.addTransicion(12, 12, "0123456789");
		automata.addTransicion(13, 13, "0123456789");
    }}
