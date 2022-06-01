import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.naming.StringRefAddr;

public class Lector{
    private String EPSILON = "epsilon";
    private Grafo noDeterminista;
    private boolean isfirst=true;
    private int parenthesisDepth=0;
    private ArrayList<Integer> firstNodes;
    private ArrayList<Integer> lastNodes;
    private ArrayList<String> symbols;
    private ArrayList<Boolean> orDepth;

    private ArrayList<String> subconjuntosDeSimbolosCharacters;
    private ArrayList<String> keywordNames;
    private ArrayList<String> nombresDeCharacters;
    private String parserName;


    private Grafo subConjuntos;
    private int stage;

    private boolean isInComillas;
    private String characterFound;
    private boolean isFindingCharacter;

    private ArrayList<String> tokenNames;

    private boolean beforeEqual;
    private String curretProductionName;
    private boolean productionFindingParameters;
    private String productionParametes;

    private boolean betweenParenthesis;
    private boolean betweenParenthesisDot;
    private boolean betweenQuotes;
    private boolean betweenArrows;
    private boolean betweenBrackets;
    private boolean betweenCrurlyBackets;
    private boolean isFindingIdent;
    private char lastChar;
    private String newTokenString;
    private String parameterString;
    private String nameBeingFound;
    private String functionString;
    private String temporaryStringForFunc;
    private int currentStatement;
    private ArrayList<ArrayList<String>> listaDeListaDeFirstPos; 
    private ArrayList<ArrayList<String>> listaTemporalDeFirstPos;
    private ArrayList<String> nombresFunciones;
    private ArrayList<ArrayList<Integer>> listaDeFirstPosDeFunciones;
    private ArrayList<ArrayList<String>> listaDeFirstPosDeFuncionesString;
    private ArrayList<Integer> parentStatements;
    private ArrayList<Boolean> hasSectionFoundAllFirstPos;
    private ArrayList<String> parserCreationStrings;
    private String functionInicialName;
    private boolean hasFuncionInicial;

    private String parserCodeText;
    private String scannerCodeText;
    
    private void HardResetProductionVars(){
        functionInicialName="";
        hasFuncionInicial=false;
        betweenParenthesis=false;
        betweenParenthesisDot=false;
        betweenQuotes=false;
        betweenArrows=false;
        betweenBrackets=false;
        betweenCrurlyBackets=false;
        isFindingIdent=false;
        lastChar= ' ';
        newTokenString="";
        parameterString="";
        nameBeingFound="";
        functionString="";
        temporaryStringForFunc="";
        currentStatement=0;
        listaDeListaDeFirstPos=new ArrayList<>(); //a
        listaTemporalDeFirstPos=new ArrayList<>(); 
        nombresFunciones=new ArrayList<>(); //a
        listaDeFirstPosDeFunciones=new ArrayList<>(); //a
        listaDeFirstPosDeFuncionesString=new ArrayList<>(); //a
        parentStatements=new ArrayList<>(); 
        hasSectionFoundAllFirstPos=new ArrayList<>();
        parserCreationStrings=new ArrayList<>();//s
        scannerCodeText="";
        beforeEqual=true;
        curretProductionName="";
        productionFindingParameters=false;
        productionParametes="";
    }

    private void ResetProductionVars(){
        newTokenString="";
        parameterString="";
        nameBeingFound="";
        functionString="";
        currentStatement=0;
        listaTemporalDeFirstPos=new ArrayList<>();
        parentStatements=new ArrayList<>();
        hasSectionFoundAllFirstPos=new ArrayList<>();
        isFindingIdent=false;
        lastChar=' ';
        betweenArrows=false;
        betweenBrackets=false;
        betweenCrurlyBackets=false;
        betweenParenthesis=false;
        betweenParenthesisDot=false;
        betweenQuotes=false;
        beforeEqual=true;
        curretProductionName="";
        productionFindingParameters=false;
        productionParametes="";
    }

    public Lector() {
        noDeterminista = new Grafo();
        subConjuntos = new Grafo();
        stage = 0;
        nombresDeCharacters = new ArrayList<>();
        keywordNames = new ArrayList<>();
        subconjuntosDeSimbolosCharacters = new ArrayList<>();
        beforeEqual=true;
        symbols = new ArrayList<>();
        scannerCodeText="";
        tokenNames=new ArrayList<>();
        parserCodeText="";
    }

    
    public boolean lexerExecute (String filename){
        HardResetProductionVars();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine())!=null){
                cocolReader(line);
            }
            reader.close();
            generarTextoInicial();
            algoritmoSubconjuntos();
            scannerCodeText= scannerCodeText+"    }}\n";
            productionEnd();
            generarTextoParser();
            BufferedWriter writer = new BufferedWriter(new FileWriter("Scanner"+parserName+".java"));
            writer.write(scannerCodeText);
            writer.close();
            writer = new BufferedWriter(new FileWriter("Parser"+parserName+".java"));
            writer.write(parserCodeText);
            writer.close();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    private void cocolReader (String line){
        if(line.length()<1){
            return;
        }
            if (line.split(" ")[0].equals("COMPILER")){
                parserName = line.split(" ")[1];
                stage=0;
                return;
            }
            if (line.replaceAll("\\s+","").equals("CHARACTERS")){
                stage=1;
                return;
            }
            if (line.replaceAll("\\s+","").equals("KEYWORDS")){
                stage=2;
                return;
            }
            
            if (line.replaceAll("\\s+","").equals("TOKENS")){
                stage=3;
                return;
            }
            if (line.replaceAll("\\s+","").equals("PRODUCTIONS")){
                stage=4;
                return;
            }
            if (line.contains("END "+parserName)){
                stage=5;
                return;
            }
            if (stage==1){
                characterReader1(line);
                /*nombresDeCharacters.add(line.split(" ")[0]);
                characterReader(line.split(" ",3)[2]);*/
            }else if (stage==2){

                keywordReader(line);
                
            }else if (stage==3){
                tokenReader1(line);       
            }else if (stage==4){
                for (int i=0; i<line.length(); i++){
                    productionsReader(line.charAt(i));
                    lastChar=line.charAt(i);
                }
            }
    }
    private void constructAny(){

    }
    private void productionEnd(){
        parserCreationStrings.add(temporaryStringForFunc);
        for (int i = listaDeFirstPosDeFuncionesString.size()-1;i>=0;i--){
            for (int j=0;j<listaDeFirstPosDeFuncionesString.get(i).size();j++){
                if(tokenNames.contains(listaDeFirstPosDeFuncionesString.get(i).get(j))){
                    listaDeFirstPosDeFunciones.get(i).add(tokenNames.indexOf(listaDeFirstPosDeFuncionesString.get(i).get(j)));
                }else{
                    for (int k=0; k<listaDeFirstPosDeFunciones.get(nombresFunciones.indexOf(listaDeFirstPosDeFuncionesString.get(i).get(j))).size();k++){
                        if(!listaDeFirstPosDeFunciones.get(i).contains(listaDeFirstPosDeFunciones.get(nombresFunciones.indexOf(listaDeFirstPosDeFuncionesString.get(i).get(j))).get(k))){
                            listaDeFirstPosDeFunciones.get(i).add(listaDeFirstPosDeFunciones.get(nombresFunciones.indexOf(listaDeFirstPosDeFuncionesString.get(i).get(j))).get(k));
                        }
                    }
                }
            }
        }
        ArrayList<ArrayList<Integer>> lista=new ArrayList<>();
        for (int i = 0;i<listaDeListaDeFirstPos.size();i++){
            ArrayList<Integer> listaDeInts=new ArrayList<>();
            for (int j=0;j<listaDeListaDeFirstPos.get(i).size();j++){
                if(tokenNames.contains(listaDeListaDeFirstPos.get(i).get(j))){
                    listaDeInts.add(tokenNames.indexOf(listaDeListaDeFirstPos.get(i).get(j)));
                }else{
                    for (int k=0; k<listaDeFirstPosDeFunciones.get(nombresFunciones.indexOf(listaDeListaDeFirstPos.get(i).get(j))).size();k++){
                        if(!listaDeInts.contains(listaDeFirstPosDeFunciones.get(nombresFunciones.indexOf(listaDeListaDeFirstPos.get(i).get(j))).get(k))){
                            listaDeInts.add(listaDeFirstPosDeFunciones.get(nombresFunciones.indexOf(listaDeListaDeFirstPos.get(i).get(j))).get(k));
                        }
                    }
                }
            }
            lista.add(listaDeInts);
        }
        for (int i=0; i<parserCreationStrings.size()-1; i++){
            functionString+=parserCreationStrings.get(i);
            String temporaryStringForConditionals="currentToken.getTokenType()=="+lista.get(i).get(0);
            for (int j=1;j<lista.get(i).size();j++){
                temporaryStringForConditionals+="||currentToken.getTokenType()=="+lista.get(i).get(j);
            }
            functionString+=temporaryStringForConditionals+") {\n";
        }
        functionString+=parserCreationStrings.get(parserCreationStrings.size()-1);
    }

    private void productionsReader(char character){
        if (beforeEqual){
            if((character==' '||character=='\t'||character=='\n')&&!productionFindingParameters){
                return;
            }
            if (character=='='){
                beforeEqual=false;
                temporaryStringForFunc += "   private void "+curretProductionName +" ( "+productionParametes+" ) { \nif (";
                parserCreationStrings.add(temporaryStringForFunc);
                if(!hasFuncionInicial){
                    hasFuncionInicial=true;
                    functionInicialName=curretProductionName;
                }
                temporaryStringForFunc="";
                listaDeFirstPosDeFunciones.add(new ArrayList<>());
                nombresFunciones.add(curretProductionName);
                hasSectionFoundAllFirstPos.add(false);
                currentStatement=listaTemporalDeFirstPos.size();
                listaTemporalDeFirstPos.add(new ArrayList<>());
                parentStatements.add(-1);
            }
            if (character=='<'){
                productionParametes="";
                productionFindingParameters=true;
                return;
            }
            if (character=='>'){
                productionFindingParameters=false;
                return;
            }
            if(productionFindingParameters){
                productionParametes+=character;
            }else{
                if(character!='>'){
                    curretProductionName+=character;
                }
            }
        }else{
            if(betweenParenthesisDot){
                if(lastChar=='.'){
                    if(character==')'){
                        betweenParenthesis=false;
                        betweenParenthesisDot=false;
                        temporaryStringForFunc=temporaryStringForFunc.substring(0, temporaryStringForFunc.length()-1);
                        temporaryStringForFunc += "\n";
                    }else {
                        temporaryStringForFunc+=character;
                    }
                }else{
                    temporaryStringForFunc+=character;
                }
            }else if(isFindingIdent){
                if (character=='<'){
                    betweenArrows=true;
                    isFindingIdent=false;
                }else if(character=='.'||character=='('||character==')'||character=='{'||character=='}'||character=='['||character==']'||character=='\"'||character=='|'||character=='('){
                    if(tokenNames.contains(nameBeingFound)){
                        temporaryStringForFunc=temporaryStringForFunc+"Expect("+tokenNames.indexOf(nameBeingFound)+");\n";
                    }else{
                        temporaryStringForFunc=temporaryStringForFunc+nameBeingFound+"("+parameterString+");\n";
                    }
                    
                    if(!hasSectionFoundAllFirstPos.get(currentStatement)){
                        int evaluador=currentStatement;
                        while(evaluador>=0){
                            if(!listaTemporalDeFirstPos.get(evaluador).contains(nameBeingFound)&&!hasSectionFoundAllFirstPos.get(evaluador)){
                                listaTemporalDeFirstPos.get(evaluador).add(nameBeingFound);
                            }
                            evaluador=parentStatements.get(evaluador);
                        }
                    }
                    hasSectionFoundAllFirstPos.set(currentStatement, true);
                    nameBeingFound="";
                    parameterString="";
                    isFindingIdent=false;
                    productionsReader(character);
                }else if (character==' '||character=='\t'||character=='\n'){
                    if(tokenNames.contains(nameBeingFound)){
                        temporaryStringForFunc=temporaryStringForFunc+"Expect("+tokenNames.indexOf(nameBeingFound)+");\n";
                    }else{
                        temporaryStringForFunc=temporaryStringForFunc+nameBeingFound+"("+parameterString+");\n";
                    }
                    
                    if(!hasSectionFoundAllFirstPos.get(currentStatement)){
                        int evaluador=currentStatement;
                        while(evaluador>=0){
                            if(!listaTemporalDeFirstPos.get(evaluador).contains(nameBeingFound)&&!hasSectionFoundAllFirstPos.get(evaluador)){
                                listaTemporalDeFirstPos.get(evaluador).add(nameBeingFound);
                            }
                            evaluador=parentStatements.get(evaluador);
                        }
                    }
                    hasSectionFoundAllFirstPos.set(currentStatement, true);
                    nameBeingFound="";
                    parameterString="";
                    isFindingIdent=false;
                }else{
                    nameBeingFound+=character;
                    isFindingIdent=true;
                }
            }
            else if (betweenArrows){
                if(character=='>'){
                    betweenArrows=false;
                    temporaryStringForFunc=temporaryStringForFunc+nameBeingFound+"("+parameterString+");\n";
                    
                    if(!hasSectionFoundAllFirstPos.get(currentStatement)){
                        int evaluador=currentStatement;
                        while(evaluador>=0){
                            if(!listaTemporalDeFirstPos.get(evaluador).contains(nameBeingFound)&&!hasSectionFoundAllFirstPos.get(evaluador)){
                                listaTemporalDeFirstPos.get(evaluador).add(nameBeingFound);
                            }
                            evaluador=parentStatements.get(evaluador);
                        }
                    }
                    hasSectionFoundAllFirstPos.set(currentStatement,true);
                    parameterString="";
                    nameBeingFound="";
                }else{
                    parameterString+=character;
                }
                
            }else if(betweenQuotes){
                if (character=='\"'){
                    if(!tokenNames.contains(newTokenString))
                        tokenReader(newTokenString+" = \""+newTokenString+"\"");
                    temporaryStringForFunc+=("Expect("+tokenNames.indexOf(newTokenString)+");\n");
                    if(!hasSectionFoundAllFirstPos.get(currentStatement)){
                        int evaluador=currentStatement;
                        while(evaluador>=0){
                            if(!listaTemporalDeFirstPos.get(evaluador).contains(newTokenString)&&!hasSectionFoundAllFirstPos.get(evaluador)){
                                listaTemporalDeFirstPos.get(evaluador).add(newTokenString);
                            }
                            evaluador=parentStatements.get(evaluador);
                        }
                    }
                    if(currentStatement>=0)
                        hasSectionFoundAllFirstPos.set(currentStatement,true);
                    newTokenString="";
                    betweenQuotes=false;
                }else{
                    newTokenString+=character;
                }
            }else if(betweenParenthesis){
                betweenParenthesis=false;
                if(character=='.'){
                    lastChar=' ';
                    betweenParenthesisDot=true;
                }else{
                    temporaryStringForFunc+="if (";
                    parserCreationStrings.add(temporaryStringForFunc);
                    temporaryStringForFunc="";
                    parentStatements.add(currentStatement);
                    currentStatement=listaTemporalDeFirstPos.size();
                    listaTemporalDeFirstPos.add(new ArrayList<>());
                    productionsReader(character);
                    hasSectionFoundAllFirstPos.add(false);
                }
            }else{
                if(character=='|'){
                    temporaryStringForFunc+="} else if (";
                    parserCreationStrings.add(temporaryStringForFunc);
                    temporaryStringForFunc="";
                    currentStatement=listaTemporalDeFirstPos.size();
                    listaTemporalDeFirstPos.add(new ArrayList<>());
                    parentStatements.add(parentStatements.get(parentStatements.size()-1));
                    hasSectionFoundAllFirstPos.add(false);
                }else if(character=='('){
                    betweenParenthesis=true;
                }else if(character=='['){
                    temporaryStringForFunc+="if (";
                    parserCreationStrings.add(temporaryStringForFunc);
                    temporaryStringForFunc="";
                    parentStatements.add(currentStatement);
                    currentStatement=listaTemporalDeFirstPos.size();
                    listaTemporalDeFirstPos.add(new ArrayList<>());
                    hasSectionFoundAllFirstPos.add(false);
                }else if(character=='}'){
                    productionsReader(']');
                    temporaryStringForFunc+="} \n";
                    currentStatement = parentStatements.get(currentStatement);
                }else if(character==']'){
                    temporaryStringForFunc+="} \n";
                    currentStatement = parentStatements.get(currentStatement);
                }else if (character=='{'){
                    temporaryStringForFunc+="while (";
                    parserCreationStrings.add(temporaryStringForFunc);
                    temporaryStringForFunc="";
                    parentStatements.add(currentStatement);
                    currentStatement=listaTemporalDeFirstPos.size();
                    listaTemporalDeFirstPos.add(new ArrayList<>());
                    hasSectionFoundAllFirstPos.add(false);
                    productionsReader('[');
                }else if(character==')'){
                    temporaryStringForFunc+="} else {\n\tSystem.out.println(\"Error\");\n}\n";
                    currentStatement = parentStatements.get(currentStatement);
                    hasSectionFoundAllFirstPos.set(currentStatement, true);
                }else if (character=='.'){
                    if(lastChar=='('){
                        betweenParenthesisDot=true;
                    }else{
                        for (int i=0; i<listaTemporalDeFirstPos.size(); i++){
                            listaDeListaDeFirstPos.add(listaTemporalDeFirstPos.get(i));
                        }
                        temporaryStringForFunc+="}\n}\n";
                        listaDeFirstPosDeFuncionesString.add(listaTemporalDeFirstPos.get(0));
                        listaTemporalDeFirstPos=new ArrayList<>();
                        currentStatement=0;
                        betweenArrows=false;
                        isFindingIdent=false;
                        ResetProductionVars();
                    }
                }else if(character=='\"'){
                    betweenQuotes=true;
                    newTokenString="";
                }else if (character==' '||character=='\t'||character=='\n'){
                    return;
                }else{
                    isFindingIdent=true;
                    productionsReader(character);
                }
            }
        }
    }

    private void tokenReader1(String string){
        boolean isBeforeEqualSig=true;
        String tokenName="";
        isFindingCharacter=false;
        orDepth = new ArrayList<>();
        orDepth.add(false);
        noDeterminista.addNode(0, EPSILON);
        lastNodes = new ArrayList<>();
        firstNodes = new ArrayList<>();
        firstNodes.add(noDeterminista.getCantidadNodos()-1);
        lastNodes.add(noDeterminista.getCantidadNodos()-1);
        if(string.contains("IGNORE SET ")){
            String setToIgnore=string.substring(11);
            setToIgnore.replaceAll(" ", "");
            String cadenaDeCharacter;
            cadenaDeCharacter = subconjuntosDeSimbolosCharacters.get(nombresDeCharacters.indexOf(setToIgnore));
            noDeterminista.addNode(0, cadenaDeCharacter);
            noDeterminista.addTransicion(noDeterminista.getCantidadNodos()-1, 0, EPSILON);
            noDeterminista.addTransicion(0, noDeterminista.getCantidadNodos()-1, EPSILON);
            lastNodes.set(lastNodes.size()-1, noDeterminista.getCantidadNodos()-1);
            return;
        }
        for (int i=0; i<string.length();i++){
            if(isBeforeEqualSig){
                if(string.charAt(i)=='='){
                    isBeforeEqualSig=false;
                }else if(string.charAt(i)==' '){

                }else{
                    tokenName+=string.charAt(i);
                }
            }else{
                char characterActual=string.charAt(i);
                if (characterActual==' '){
                    if(isFindingCharacter){
                        if(characterFound.equals("EXCEPT")){
                            break;
                        }
                        characterFound();
                        isFindingCharacter=false;
                    }else if (isInComillas){
                        addSymbolToLanguage(characterActual);
                        thomsonChar(characterActual);
                    }
                }else if (characterActual=='('||characterActual=='{'||characterActual=='['){
                    if(isFindingCharacter){
                        characterFound();
                        isFindingCharacter=false;
                    }
                    if (isInComillas){
                        addSymbolToLanguage(characterActual);
                        thomsonChar(characterActual);
                    }else{
                        thomsonOpenPar();
                    }
                }else if(characterActual=='\"'){
                    if(isFindingCharacter){
                        characterFound();
                        isFindingCharacter=false;
                    }
                    if (isInComillas){
                        isInComillas=false;
                        thomsonClosePar();
                    }else{
                        isInComillas=true;
                        thomsonOpenPar();
                    }
                }else if(characterActual=='}'){
                    if(isFindingCharacter){
                        characterFound();
                        isFindingCharacter=false;
                    }
                    if (isInComillas){
                        addSymbolToLanguage(characterActual);
                        thomsonChar(characterActual);
                    }else{
                        thomsonClosePar();
                        thomsonStar();
                    }
                }else if(characterActual==']'){
                    if(isFindingCharacter){
                        characterFound();
                        isFindingCharacter=false;
                    }
                    if (isInComillas){
                        addSymbolToLanguage(characterActual);
                        thomsonChar(characterActual);
                    }else{
                        thomsonClosePar();
                        thomsonQuestion();
                    }
                }else if(characterActual==')'){
                    if(isFindingCharacter){
                        characterFound();
                        isFindingCharacter=false;
                    }
                    if (isInComillas){
                        addSymbolToLanguage(characterActual);
                        thomsonChar(characterActual);
                    }else{
                        thomsonClosePar();
                    }
                }else if (characterActual=='|'){
                    if(isFindingCharacter){
                        characterFound();
                        isFindingCharacter=false;
                    }
                    if (isInComillas){
                        addSymbolToLanguage(characterActual);
                        thomsonChar(characterActual);
                    }else{
                        thomsonOr();
                    }
                }else{
                    if (isInComillas){
                        addSymbolToLanguage(characterActual);
                        thomsonChar(characterActual);
                    }else{
                        if(!isFindingCharacter){
                            characterFound="";
                        }
                        isFindingCharacter = true;
                        characterFound+=characterActual;
                    }
                }
            }
        }
        if(orDepth.get(0)){
            noDeterminista.addTransicion(lastNodes.get(lastNodes.size()-1), firstNodes.get(firstNodes.size()), EPSILON);
            lastNodes.remove(lastNodes.size()-1);
        }
        noDeterminista.getNodo(lastNodes.get(lastNodes.size()-1)).setIsFinal(true);
        noDeterminista.getNodo(lastNodes.get(lastNodes.size()-1)).setTokenType(1);
        noDeterminista.getNodo(lastNodes.get(lastNodes.size()-1)).setToken(tokenName);
        noDeterminista.getNodo(lastNodes.get(lastNodes.size()-1)).setTokenNumber(tokenNames.size());
        tokenNames.add(tokenName);
        
    }
    private void tokenReader(String string){
        isFindingCharacter=false;
        orDepth = new ArrayList<>();
        orDepth.add(false);
        noDeterminista.addNode(0, EPSILON);
        lastNodes = new ArrayList<>();
        firstNodes = new ArrayList<>();
        firstNodes.add(noDeterminista.getCantidadNodos()-1);
        lastNodes.add(noDeterminista.getCantidadNodos()-1);
        String[] strings = string.split(" ");
        for (int i=0; i<strings[2].length();i++){
            char characterActual=strings[2].charAt(i);
            if (characterActual=='('||characterActual=='{'){
                if(isFindingCharacter){
                    characterFound();
                    isFindingCharacter=false;
                }
                if (isInComillas){
                    addSymbolToLanguage(characterActual);
                    thomsonChar(characterActual);
                }else{
                    thomsonOpenPar();
                }
            }else if(characterActual=='\"'){
                if(isFindingCharacter){
                    characterFound();
                    isFindingCharacter=false;
                }
                if (isInComillas){
                    isInComillas=false;
                    thomsonClosePar();
                }else{
                    isInComillas=true;
                    thomsonOpenPar();
                }
            }else if(characterActual=='}'){
                if(isFindingCharacter){
                    characterFound();
                    isFindingCharacter=false;
                }
                if (isInComillas){
                    addSymbolToLanguage(characterActual);
                    thomsonChar(characterActual);
                }else{
                    thomsonClosePar();
                    thomsonStar();
                }
            }else if(characterActual==')'){
                if(isFindingCharacter){
                    characterFound();
                    isFindingCharacter=false;
                }
                if (isInComillas){
                    addSymbolToLanguage(characterActual);
                    thomsonChar(characterActual);
                }else{
                    thomsonClosePar();
                }
            }else if (characterActual=='|'){
                if(isFindingCharacter){
                    characterFound();
                    isFindingCharacter=false;
                }
                if (isInComillas){
                    addSymbolToLanguage(characterActual);
                    thomsonChar(characterActual);
                }else{
                    thomsonOr();
                }
            }else{
                if (isInComillas){
                    addSymbolToLanguage(characterActual);
                    thomsonChar(characterActual);
                }else{
                    if(!isFindingCharacter){
                        characterFound="";
                    }
                    isFindingCharacter = true;
                    characterFound+=characterActual;
                }
            }
        }
        if(orDepth.get(0)){
            noDeterminista.addTransicion(lastNodes.get(lastNodes.size()-1), firstNodes.get(firstNodes.size()), EPSILON);
            lastNodes.remove(lastNodes.size()-1);
        }
        noDeterminista.getNodo(lastNodes.get(lastNodes.size()-1)).setIsFinal(true);
        noDeterminista.getNodo(lastNodes.get(lastNodes.size()-1)).setTokenType(1);
        noDeterminista.getNodo(lastNodes.get(lastNodes.size()-1)).setToken(strings[0]);
        noDeterminista.getNodo(lastNodes.get(lastNodes.size()-1)).setTokenNumber(tokenNames.size());
        tokenNames.add(strings[0]);
    }

    private void characterFound(){
        if (isfirst){
            firstNodes.add(noDeterminista.getCantidadNodos());
            isfirst=false;
        }else{
            firstNodes.set(firstNodes.size()-1, noDeterminista.getCantidadNodos());
        }
        String cadenaDeCharacter;
        cadenaDeCharacter = subconjuntosDeSimbolosCharacters.get(nombresDeCharacters.indexOf(characterFound));
        noDeterminista.addNode(lastNodes.get(lastNodes.size()-1), EPSILON);
        noDeterminista.addNode(noDeterminista.getCantidadNodos()-1, cadenaDeCharacter);
        noDeterminista.addNode(noDeterminista.getCantidadNodos()-1, EPSILON);
        lastNodes.set(lastNodes.size()-1, noDeterminista.getCantidadNodos()-1);
    }

    private void keywordReader(String string){
        noDeterminista.addNode(0, EPSILON);
        String[] strings = string.split(" ",3);
        String contenido = strings[2].replaceAll("\"","");
        if (contenido.charAt(contenido.length()-1)=='.'){
            contenido=contenido.substring(0, contenido.length()-1);
        }
        for (int i=0; i<contenido.length(); i++){
            noDeterminista.addNode(noDeterminista.getCantidadNodos()-1, Character.toString(contenido.charAt(i)));
            addSymbolToLanguage(contenido.charAt(i));
        }
        noDeterminista.getNodo(noDeterminista.getCantidadNodos()-1).setIsFinal(true);
        noDeterminista.getNodo(noDeterminista.getCantidadNodos()-1).setToken(strings[0]);
        noDeterminista.getNodo(noDeterminista.getCantidadNodos()-1).setTokenType(0);
        noDeterminista.getNodo(noDeterminista.getCantidadNodos()-1).setTokenNumber(tokenNames.size());
        keywordNames.add(strings[0]);
        tokenNames.add(strings[0]);
    }

    private void characterReader1(String string){
        String stringDeCharacter="";
        String nombreDeCharacter="";
        String stringIdents="";
        String stringQuotations="";
        String stringCharNumber="";
        boolean beforeTheEqual=true;
        boolean findingIdent=false;
        boolean findingQuotations=false;
        boolean findingCharNumber=false;
        boolean adding = true;
        boolean justPassedChar=false;
        boolean justPassedDotChar=false;
        int numberGatheredFromFirstChar=-1;

        for (int i=0; i<string.length();i++){
            if (beforeTheEqual){
                if(string.charAt(i)==' '){

                }else if (string.charAt(i)=='='){
                    nombresDeCharacters.add(nombreDeCharacter);
                    nombreDeCharacter="";
                    beforeTheEqual=false;
                    findingIdent=false;
                    findingQuotations=false;
                    findingCharNumber=false;
                    adding=true;
                    justPassedChar=false;
                    justPassedDotChar=false;
                    numberGatheredFromFirstChar=-1;
                    stringDeCharacter="";
                }else{
                    nombreDeCharacter+=string.charAt(i);
                }
            }else{
                if(findingIdent){
                    if(string.charAt(i)=='-'){
                        String stringQueSeUne="";
                        if(stringIdents.equals("ANY")){
                            for (int j=0;j<subconjuntosDeSimbolosCharacters.size();j++){
                                String cadenaAgregando=subconjuntosDeSimbolosCharacters.get(j);
                                for (int k=0;k<cadenaAgregando.length();k++){
                                    if(!stringQueSeUne.contains(Character.toString(cadenaAgregando.charAt(k)))){
                                        stringQueSeUne+=cadenaAgregando.charAt(k);
                                    }
                                }
                            }
                        }else{
                            stringQueSeUne=subconjuntosDeSimbolosCharacters.get(nombresDeCharacters.indexOf(stringIdents));
                        }
                        if(adding){
                            for (int j=0; j<stringQueSeUne.length(); j++){
                                if(!stringDeCharacter.contains(Character.toString(stringQueSeUne.charAt(j)))){
                                    stringDeCharacter+=stringQueSeUne.charAt(j);
                                }
                            }
                        }else{
                            for (int j=0; j<stringQueSeUne.length(); j++){
                                if(stringDeCharacter.contains(Character.toString(stringQueSeUne.charAt(j)))){
                                    stringDeCharacter.replaceAll(Character.toString(stringQueSeUne.charAt(j)), "");
                                }
                            }
                        }
                        stringIdents="";
                        findingIdent=false;
                        adding=false;
                    }else if(string.charAt(i)=='+'){
                        String stringQueSeUne="";
                        if(stringIdents.equals("ANY")){
                            for (int j=0;j<subconjuntosDeSimbolosCharacters.size();j++){
                                String cadenaAgregando=subconjuntosDeSimbolosCharacters.get(j);
                                for (int k=0;k<cadenaAgregando.length();k++){
                                    if(!stringQueSeUne.contains(Character.toString(cadenaAgregando.charAt(k)))){
                                        stringQueSeUne+=cadenaAgregando.charAt(k);
                                    }
                                }
                            }
                        }else{
                            stringQueSeUne=subconjuntosDeSimbolosCharacters.get(nombresDeCharacters.indexOf(stringIdents));
                        }
                        if(adding){
                            for (int j=0; j<stringQueSeUne.length(); j++){
                                if(!stringDeCharacter.contains(Character.toString(stringQueSeUne.charAt(j)))){
                                    stringDeCharacter+=stringQueSeUne.charAt(j);
                                }
                            }
                        }else{
                            for (int j=0; j<stringQueSeUne.length(); j++){
                                if(stringDeCharacter.contains(Character.toString(stringQueSeUne.charAt(j)))){
                                    stringDeCharacter.replaceAll(Character.toString(stringQueSeUne.charAt(j)), "");
                                }
                            }
                        }
                        stringIdents="";
                        findingIdent=false;
                        adding=true;
                    }else if(string.charAt(i)=='('){
                        if(stringIdents.equals("CHR")){
                            findingIdent=false;
                            stringIdents="";
                            stringCharNumber="";
                            findingCharNumber=true;
                        }
                    }else if(string.charAt(i)=='.'){
                        String stringQueSeUne="";
                        if(stringIdents.equals("ANY")){
                            for (int j=0;j<subconjuntosDeSimbolosCharacters.size();j++){
                                String cadenaAgregando=subconjuntosDeSimbolosCharacters.get(j);
                                for (int k=0;k<cadenaAgregando.length();k++){
                                    if(!stringQueSeUne.contains(Character.toString(cadenaAgregando.charAt(k)))){
                                        stringQueSeUne+=cadenaAgregando.charAt(k);
                                    }
                                }
                            }
                        }else{
                            stringQueSeUne=subconjuntosDeSimbolosCharacters.get(nombresDeCharacters.indexOf(stringIdents));
                        }
                        if(adding){
                            for (int j=0; j<stringQueSeUne.length(); j++){
                                if(!stringDeCharacter.contains(Character.toString(stringQueSeUne.charAt(j)))){
                                    stringDeCharacter+=stringQueSeUne.charAt(j);
                                }
                            }
                        }else{
                            for (int j=0; j<stringQueSeUne.length(); j++){
                                if(stringDeCharacter.contains(Character.toString(stringQueSeUne.charAt(j)))){
                                    stringDeCharacter.replace(Character.toString(stringQueSeUne.charAt(j)), "");
                                }
                            }
                        }
                        stringIdents="";
                        findingIdent=false;
                        subconjuntosDeSimbolosCharacters.add(stringDeCharacter);
                        symbols.add(stringDeCharacter);
                        nombreDeCharacter="";
                        beforeTheEqual=false;
                        findingIdent=false;
                        findingQuotations=false;
                        findingCharNumber=false;
                        adding=true;
                        justPassedChar=false;
                        justPassedDotChar=false;
                        numberGatheredFromFirstChar=-1;
                        stringDeCharacter="";
                        break;
                    }else if(string.charAt(i)==' '){
                        String stringQueSeUne="";
                        if(stringIdents.equals("ANY")){
                            for (int j=0;j<subconjuntosDeSimbolosCharacters.size();j++){
                                String cadenaAgregando=subconjuntosDeSimbolosCharacters.get(j);
                                for (int k=0;k<cadenaAgregando.length();k++){
                                    if(!stringQueSeUne.contains(Character.toString(cadenaAgregando.charAt(k)))){
                                        stringQueSeUne+=cadenaAgregando.charAt(k);
                                    }
                                }
                            }
                        }else{
                            stringQueSeUne=subconjuntosDeSimbolosCharacters.get(nombresDeCharacters.indexOf(stringIdents));
                        }
                        if(adding){
                            for (int j=0; j<stringQueSeUne.length(); j++){
                                if(!stringDeCharacter.contains(Character.toString(stringQueSeUne.charAt(j)))){
                                    stringDeCharacter+=stringQueSeUne.charAt(j);
                                }
                            }
                        }else{
                            for (int j=0; j<stringQueSeUne.length(); j++){
                                if(stringDeCharacter.contains(Character.toString(stringQueSeUne.charAt(j)))){
                                    stringDeCharacter.replaceAll(Character.toString(stringQueSeUne.charAt(j)), "");
                                }
                            }
                        }
                        stringIdents="";
                        findingIdent=false;
                    }else if(string.charAt(i)=='\"'){
                        String stringQueSeUne="";
                        if(stringIdents.equals("ANY")){
                            for (int j=0;j<subconjuntosDeSimbolosCharacters.size();j++){
                                String cadenaAgregando=subconjuntosDeSimbolosCharacters.get(j);
                                for (int k=0;k<cadenaAgregando.length();k++){
                                    if(!stringQueSeUne.contains(Character.toString(cadenaAgregando.charAt(k)))){
                                        stringQueSeUne+=cadenaAgregando.charAt(k);
                                    }
                                }
                            }
                        }else{
                            stringQueSeUne=subconjuntosDeSimbolosCharacters.get(nombresDeCharacters.indexOf(stringIdents));
                        }
                        if(adding){
                            for (int j=0; j<stringQueSeUne.length(); j++){
                                if(!stringDeCharacter.contains(Character.toString(stringQueSeUne.charAt(j)))){
                                    stringDeCharacter+=stringQueSeUne.charAt(j);
                                }
                            }
                        }else{
                            for (int j=0; j<stringQueSeUne.length(); j++){
                                if(stringDeCharacter.contains(Character.toString(stringQueSeUne.charAt(j)))){
                                    stringDeCharacter.replaceAll(Character.toString(stringQueSeUne.charAt(j)), "");
                                }
                            }
                        }
                        stringIdents="";
                        findingIdent=false;
                        findingQuotations=true;
                    }else{
                        stringIdents+=string.charAt(i);
                    }
                }else if (findingQuotations){
                    if (string.charAt(i)=='\"'){
                        if(adding){
                            for (int j=0; j<stringQuotations.length(); j++){
                                if(!stringDeCharacter.contains(Character.toString(stringQuotations.charAt(j)))){
                                    stringDeCharacter+=stringQuotations.charAt(j);
                                }
                            }
                        }else{
                            for (int j=0; j<stringQuotations.length(); j++){
                                if(stringDeCharacter.contains(Character.toString(stringQuotations.charAt(j)))){
                                    stringDeCharacter.replaceAll(Character.toString(stringQuotations.charAt(j)), "");
                                }
                            }
                        }
                        stringQuotations="";
                        findingQuotations=false;
                    }else{
                        stringQuotations+=string.charAt(i);
                    }
                }else if (findingCharNumber){
                    if (string.charAt(i)==')'){
                        if(numberGatheredFromFirstChar==-1){
                            numberGatheredFromFirstChar=Integer.parseInt(stringCharNumber);
                            justPassedChar=true;
                            findingCharNumber=false;
                            stringCharNumber="";
                        }else{
                            int valor1 = numberGatheredFromFirstChar;
                            int valor2 = Integer.parseInt(stringCharNumber);
                            String stringAgregar="";
                            for (int j = valor1; j<=valor2;j++){
                                stringAgregar = stringAgregar+ Character.toChars(j)[0];
                            }
                            if(adding){
                                for (int j=0; j<stringAgregar.length(); j++){
                                    if(!stringDeCharacter.contains(Character.toString(stringAgregar.charAt(j)))){
                                        stringDeCharacter+=stringAgregar.charAt(j);
                                    }
                                }
                            }else{
                                for (int j=0; j<stringAgregar.length(); j++){
                                    if(stringDeCharacter.contains(Character.toString(stringAgregar.charAt(j)))){
                                        stringDeCharacter.replaceAll(Character.toString(stringAgregar.charAt(j)), "");
                                    }
                                }
                            }
                            findingCharNumber=false;
                            stringCharNumber="";
                            numberGatheredFromFirstChar=-1;
                        }

                    }else{
                        stringCharNumber+=string.charAt(i);
                    }
                }else{
                    if(string.charAt(i)=='-'){
                        if(justPassedChar){                               
                            char stringAgregar= Character.toChars(numberGatheredFromFirstChar)[0];
                            if(adding){
                                if(!stringDeCharacter.contains(Character.toString(stringAgregar))){
                                    stringDeCharacter+=stringAgregar;
                                }
                                
                            }else{
                                if(stringDeCharacter.contains(Character.toString(stringAgregar))){
                                    stringDeCharacter.replaceAll(Character.toString(stringAgregar), "");
                                }
                                
                            }
                            justPassedChar=false;
                            numberGatheredFromFirstChar=-1;
                            stringCharNumber="";
                        }
                        adding=false;
                    }else if(string.charAt(i)=='+'){
                        if(justPassedChar){                               
                            char stringAgregar= Character.toChars(numberGatheredFromFirstChar)[0];
                            if(adding){
                                if(!stringDeCharacter.contains(Character.toString(stringAgregar))){
                                    stringDeCharacter+=stringAgregar;
                                }
                                
                            }else{
                                if(stringDeCharacter.contains(Character.toString(stringAgregar))){
                                    stringDeCharacter.replaceAll(Character.toString(stringAgregar), "");
                                }
                                
                            }
                            justPassedChar=false;
                            numberGatheredFromFirstChar=-1;
                            stringCharNumber="";
                        }
                        adding=true;
                    }else if(string.charAt(i)=='('||string.charAt(i)==')'){
                        
                    }else if(string.charAt(i)=='.'){
                        if(justPassedChar){
                            justPassedChar=false;
                            justPassedDotChar=true;
                        }else if(justPassedDotChar){
                            justPassedDotChar=false;
                        }else{
                            subconjuntosDeSimbolosCharacters.add(stringDeCharacter);
                            symbols.add(stringDeCharacter);
                            nombreDeCharacter="";
                            beforeTheEqual=false;
                            findingIdent=false;
                            findingQuotations=false;
                            findingCharNumber=false;
                            adding=true;
                            justPassedChar=false;
                            justPassedDotChar=false;
                            numberGatheredFromFirstChar=-1;
                            stringDeCharacter="";
                            break;
                        }
                    }else if(string.charAt(i)==' '){

                    }else if(string.charAt(i)=='\"'){
                        if(justPassedChar){                               
                            char stringAgregar= Character.toChars(numberGatheredFromFirstChar)[0];
                            if(adding){
                                if(!stringDeCharacter.contains(Character.toString(stringAgregar))){
                                    stringDeCharacter+=stringAgregar;
                                }
                                
                            }else{
                                if(stringDeCharacter.contains(Character.toString(stringAgregar))){
                                    stringDeCharacter.replaceAll(Character.toString(stringAgregar), "");
                                }
                                
                            }
                            justPassedChar=false;
                            numberGatheredFromFirstChar=-1;
                            stringCharNumber="";
                        }
                        findingQuotations=true;
                        stringQuotations="";
                    }else{
                        if(justPassedChar){                               
                            char stringAgregar= Character.toChars(numberGatheredFromFirstChar)[0];
                            if(adding){
                                if(!stringDeCharacter.contains(Character.toString(stringAgregar))){
                                    stringDeCharacter+=stringAgregar;
                                }
                                
                            }else{
                                if(stringDeCharacter.contains(Character.toString(stringAgregar))){
                                    stringDeCharacter.replaceAll(Character.toString(stringAgregar), "");
                                }
                                
                            }
                            justPassedChar=false;
                            numberGatheredFromFirstChar=-1;
                            stringCharNumber="";
                        }
                        findingIdent=true;
                        stringIdents+=string.charAt(i);
                    }
                }
            }
        }
        if(justPassedDotChar){                               
            char stringAgregar= Character.toChars(numberGatheredFromFirstChar)[0];
            if(adding){
                if(!stringDeCharacter.contains(Character.toString(stringAgregar))){
                    stringDeCharacter+=stringAgregar;
                }
                
            }else{
                if(stringDeCharacter.contains(Character.toString(stringAgregar))){
                    stringDeCharacter.replaceAll(Character.toString(stringAgregar), "");
                }
                
            }
            subconjuntosDeSimbolosCharacters.add(stringDeCharacter);
            symbols.add(stringDeCharacter);
            justPassedChar=false;
            justPassedDotChar=false;
            numberGatheredFromFirstChar=-1;
            stringCharNumber="";
        }
    }

    private void characterReader(String string){
        String conjuntoDeSimbolos = "";
        if(string.contains("\'+\'")){
            conjuntoDeSimbolos+="+";
        }
        string = string.replaceAll("\\s+","");
        String[] secciones =string.split("\\+");
        for (int i=0; i<secciones.length;i++){
            if (secciones[i].charAt(0)=='\"'){
                conjuntoDeSimbolos = conjuntoDeSimbolos + secciones[i].substring(1, secciones[i].indexOf("\"",1));
            }else if(secciones[i].charAt(0)=='\''){
                String[] seccionesDeString = string.split("..");
                if (seccionesDeString.length==1){
                    if (seccionesDeString[0].length()!=1)
                        conjuntoDeSimbolos = conjuntoDeSimbolos + seccionesDeString[0].charAt(1);
                }else{
                    int valor1 = (int)seccionesDeString[0].charAt(1);
                    int valor2 = (int)seccionesDeString[1].charAt(1);
                    for (int j = valor1; j<=valor2;j++){
                        conjuntoDeSimbolos = conjuntoDeSimbolos+ Character.toChars(j)[0];
                    }
                }

            }else if(secciones[i].contains("CHR(")){
                conjuntoDeSimbolos = conjuntoDeSimbolos + Character.toChars(Integer.parseInt(secciones[i].substring(secciones[i].indexOf("(")+1,secciones[i].indexOf(")"))))[0]; 
            }else{
                if (secciones[i].charAt(secciones[i].length()-1)=='.')
                    conjuntoDeSimbolos = conjuntoDeSimbolos + subconjuntosDeSimbolosCharacters.get(nombresDeCharacters.indexOf(secciones[i].substring(0, secciones[i].length()-1)));
                else
                    conjuntoDeSimbolos = conjuntoDeSimbolos + subconjuntosDeSimbolosCharacters.get(nombresDeCharacters.indexOf(secciones[i]));
            }
        }
        subconjuntosDeSimbolosCharacters.add(conjuntoDeSimbolos);
        symbols.add(conjuntoDeSimbolos);
    }

    

    private void algoritmoSubconjuntos(){
        ArrayList<ArrayList<Integer>> subconjuntos = new ArrayList<>();
        ArrayList<Integer> cadenaAgregar = new ArrayList<>();
        cadenaAgregar.add(0);
        cadenaAgregar=epsilonChain(cadenaAgregar);
        if (checkIfEnd(cadenaAgregar)){
            subConjuntos.getNodo(subConjuntos.getCantidadNodos()-1).setIsFinal(true);
            subConjuntos.getNodo(subConjuntos.getCantidadNodos()-1).setToken(tokenGetter(cadenaAgregar));
            subConjuntos.getNodo(subConjuntos.getCantidadNodos()-1).setTokenNumber(tokenNumberGetter(cadenaAgregar));
            subConjuntos.getNodo(subConjuntos.getCantidadNodos()-1).setTokenType(tokenTypeGetter(cadenaAgregar));
            scannerCodeText=scannerCodeText+"\t\tautomata.getNodo(automata.getCantidadNodos()-1).setIsFinal(true);\n";
            scannerCodeText=scannerCodeText+"\t\tautomata.getNodo(automata.getCantidadNodos()-1).setToken(\""+tokenGetter(cadenaAgregar)+"\");\n";
            scannerCodeText=scannerCodeText+"\t\tautomata.getNodo(automata.getCantidadNodos()-1).setTokenType("+Integer.toString(tokenTypeGetter(cadenaAgregar))+");\n";
            scannerCodeText=scannerCodeText+"\t\tautomata.getNodo(automata.getCantidadNodos()-1).setTokenNumber("+tokenNumberGetter(cadenaAgregar)+");\n";
        }
        subconjuntos.add(cadenaAgregar);
        for (int i=0; i<subconjuntos.size();i++){
            for (int j=0; j<symbols.size(); j++){
                cadenaAgregar=epsilonChain(characterChain(subconjuntos.get(i), symbols.get(j)));
                if (cadenaAgregar.size()>0){
                    int checkIn = checkIfChainBefore(subconjuntos, cadenaAgregar);
                    if (checkIn!=-1){
                        subConjuntos.addTransicion(i, checkIn, symbols.get(j));
                        scannerCodeText = scannerCodeText+"\t\tautomata.addTransicion("+i+", "+checkIn+", \""+symbols.get(j)+"\");\n";
                    }else{
                        subconjuntos.add(cadenaAgregar);
                        subConjuntos.addNode(i, symbols.get(j));
                        scannerCodeText=scannerCodeText+"\t\tautomata.addNode("+i+", \""+symbols.get(j)+"\");\n";
                        if (checkIfEnd(cadenaAgregar)){
                            subConjuntos.getNodo(subConjuntos.getCantidadNodos()-1).setIsFinal(true);
                            subConjuntos.getNodo(subConjuntos.getCantidadNodos()-1).setToken(tokenGetter(cadenaAgregar));
                            subConjuntos.getNodo(subConjuntos.getCantidadNodos()-1).setTokenType(tokenTypeGetter(cadenaAgregar));
                            subConjuntos.getNodo(subConjuntos.getCantidadNodos()-1).setTokenNumber(tokenNumberGetter(cadenaAgregar));
                            scannerCodeText=scannerCodeText+"\t\tautomata.getNodo(automata.getCantidadNodos()-1).setIsFinal(true);\n";
                            scannerCodeText=scannerCodeText+"\t\tautomata.getNodo(automata.getCantidadNodos()-1).setToken(\""+tokenGetter(cadenaAgregar)+"\");\n";
                            scannerCodeText=scannerCodeText+"\t\tautomata.getNodo(automata.getCantidadNodos()-1).setTokenNumber("+tokenNumberGetter(cadenaAgregar)+");\n";
                            scannerCodeText=scannerCodeText+"\t\tautomata.getNodo(automata.getCantidadNodos()-1).setTokenType("+Integer.toString(tokenTypeGetter(cadenaAgregar))+");\n";
                        }
                    }
                }
            }
        }
    }
    private int tokenNumberGetter(ArrayList<Integer> cadenaAgregar){
        for (int i=0; i<cadenaAgregar.size();i++){
            if(noDeterminista.getNodo(cadenaAgregar.get(i)).isIsFinal())
                return noDeterminista.getNodo(cadenaAgregar.get(i)).getTokenNumber();
        }
        return 0;
    }
    private int tokenTypeGetter(ArrayList<Integer> cadenaAgregar){
        for (int i=0; i<cadenaAgregar.size();i++){
            if(noDeterminista.getNodo(cadenaAgregar.get(i)).isIsFinal())
                return noDeterminista.getNodo(cadenaAgregar.get(i)).getTokenType();
        }
        return 0;
    }
    private String tokenGetter(ArrayList<Integer> cadenaAgregar){
        for (int i=0; i<cadenaAgregar.size();i++){
            if(noDeterminista.getNodo(cadenaAgregar.get(i)).isIsFinal())
                return noDeterminista.getNodo(cadenaAgregar.get(i)).getToken();
        }
        return "";
    }
    private boolean checkIfEnd(ArrayList<Integer> cadenaAgregar){
        for (int i=0; i<cadenaAgregar.size();i++){
            if(noDeterminista.getNodo(cadenaAgregar.get(i)).isIsFinal())
                return true;
        }
        return false;
    }
    private int checkIfChainBefore(ArrayList<ArrayList<Integer>> subconjuntos, ArrayList<Integer> cadenaAgregar){
        boolean isIn=true;
        for (int i=0; i<subconjuntos.size();i++){
            isIn=true;
            for (int j=0; j<cadenaAgregar.size();j++){
                if(!subconjuntos.get(i).contains(cadenaAgregar.get(j))){
                    isIn=false;
                }
            }
            if(isIn){
                return i;
            }
        }
        return -1;
    }
    private ArrayList<Integer> characterChain(ArrayList<Integer> lista, String character){
        ArrayList<Integer> estados = new ArrayList<>();
        for (int i=0; i<lista.size();i++){
            for (int j=0; j<noDeterminista.getNodo(lista.get(i)).getTransiciones().size();j++){
                if(subconjuntosDeSimbolosCharacters.contains(noDeterminista.getNodo(lista.get(i)).getTransiciones().get(j).getParametro())){
                    if (!noDeterminista.getNodo(lista.get(i)).getTransiciones().get(j).getParametro().equals(EPSILON)&&noDeterminista.getNodo(lista.get(i)).getTransiciones().get(j).getParametro().contains(character)){
                        int objetivo= noDeterminista.getNodo(lista.get(i)).getTransiciones().get(j).getObjetivo();
                        if(!estados.contains(objetivo)){
                            estados.add(objetivo);
                        }
                    }
                }
                else{
                    if (!noDeterminista.getNodo(lista.get(i)).getTransiciones().get(j).getParametro().equals(EPSILON)&&noDeterminista.getNodo(lista.get(i)).getTransiciones().get(j).getParametro().equals(character)){
                        int objetivo= noDeterminista.getNodo(lista.get(i)).getTransiciones().get(j).getObjetivo();
                        if(!estados.contains(objetivo)){
                            estados.add(objetivo);
                        }
                    }
                }
            }
        }
        return estados;
    }
    private ArrayList<Integer> epsilonChain(ArrayList<Integer> lista){
        ArrayList<Integer> estados = new ArrayList<>();
        for (int i=0; i<lista.size();i++){
            if(!estados.contains(lista.get(i))){
                estados.add(lista.get(i));
            }
            for (int j=0; j<noDeterminista.getNodo(lista.get(i)).getTransiciones().size();j++){
                if (noDeterminista.getNodo(lista.get(i)).getTransiciones().get(j).getParametro().equals(EPSILON)){
                    int objetivo= noDeterminista.getNodo(lista.get(i)).getTransiciones().get(j).getObjetivo();
                    if(!estados.contains(objetivo)){
                        estados.add(objetivo);
                    }
                    if(!lista.contains(objetivo)){
                        lista.add(objetivo);
                    }
                }
            }
        }
        return estados;
    }



    private void addSymbolToLanguage(char caracter){
        boolean isAlreadyThere = false;
        for (int i=0; i<symbols.size();i++){
            if (Character.toString(caracter).equals(symbols.get(i))){
                isAlreadyThere=true;
                break;
            }
        }
        if (!isAlreadyThere){
            symbols.add(Character.toString(caracter));
        }
    }
    private void thomsonOpenPar(){
        firstNodes.add(noDeterminista.getCantidadNodos());
        noDeterminista.addNode(lastNodes.get(lastNodes.size()-1), EPSILON);
        lastNodes.set(lastNodes.size()-1, noDeterminista.getCantidadNodos()-1);
        isfirst=true;
        orDepth.add(false);
        parenthesisDepth++;
    }
    private void thomsonClosePar(){
        if(parenthesisDepth>0){
            if (orDepth.get(orDepth.size()-1)){
                noDeterminista.addTransicion(lastNodes.get(lastNodes.size()-1), lastNodes.get(lastNodes.size()-2), EPSILON);
                lastNodes.remove(lastNodes.size()-1);
            }
            firstNodes.remove(firstNodes.size()-1);
            isfirst=true;
            orDepth.remove(orDepth.size()-1);
        }
    }
    private void thomsonStar(){
        noDeterminista.getNodo(firstNodes.get(firstNodes.size()-1)).addTransicion(lastNodes.get(lastNodes.size()-1), EPSILON);
        noDeterminista.getNodo(lastNodes.get(lastNodes.size()-1)).addTransicion(firstNodes.get(firstNodes.size()-1), EPSILON);
    }
    private void thomsonPlus(){
        noDeterminista.getNodo(lastNodes.get(lastNodes.size()-1)).addTransicion(firstNodes.get(firstNodes.size()-1), EPSILON);
        
    }
    private void thomsonOr(){
        isfirst=true;
        orDepth.set(orDepth.size()-1, true);
        firstNodes.remove(firstNodes.size()-1);
        lastNodes.add(firstNodes.get(firstNodes.size()-1));
    }
    private void thomsonQuestion(){
        noDeterminista.getNodo(firstNodes.get(firstNodes.size()-1)).addTransicion(lastNodes.get(lastNodes.size()-1), EPSILON);
        
    }
    private void thomsonChar(char caracter){
        if (isfirst){
            firstNodes.add(noDeterminista.getCantidadNodos());
            isfirst=false;
        }else{
            firstNodes.set(firstNodes.size()-1, noDeterminista.getCantidadNodos());
        }
        noDeterminista.addNode(lastNodes.get(lastNodes.size()-1), EPSILON);
        noDeterminista.addNode(noDeterminista.getCantidadNodos()-1, Character.toString(caracter));
        noDeterminista.addNode(noDeterminista.getCantidadNodos()-1, EPSILON);
        lastNodes.set(lastNodes.size()-1, noDeterminista.getCantidadNodos()-1);
    }
    
    private void generarTextoParser(){
        parserCodeText="import java.util.ArrayList;\n\n"+

        "public class Parser"+parserName+" {\n"+
        "    private ArrayList<Token> tokens;\n"+
        "    private Token currentToken;\n"+
        "    private Token lastToken;\n"+
        "    private int currentIndex;\n\n"+
        
        "    private void Expect(int tokenType) {\n"+
        "        if (currentToken.getTokenType() != tokenType) {\n"+
        "            System.out.println(\"Error en \" + currentToken.getValue());\n"+
        "        }\n"+
        "        lastToken = tokens.get(currentIndex);\n"+
        "        currentIndex++;\n"+
        "        if (currentIndex < tokens.size())\n"+
        "            currentToken = tokens.get(currentIndex);\n"+
        "        else {\n"+
        "            currentToken.setTokenType(-1);\n"+
        "        }\n"+
        "    }\n\n"+
        
        "        public Parser"+parserName+"(ArrayList<Token> tokens) {\n"+
        "        this.tokens = tokens;\n"+
        "        currentToken = tokens.get(0);\n"+
        "        currentIndex = 0;\n"+
        "    }\n\n"+
        "    public void Parse() {\n"+
                functionInicialName+"();\n"+
        "    }\n\n"+
        
                functionString+
        
        "    class IntegerRef {\n"+
        "        private int integer;\n\n"+
        
        "        public IntegerRef() {\n"+
        "            this.integer = 0;\n"+
        "        }\n\n"+
        
        "        public IntegerRef(int integer) {\n"+
        "            this.integer = integer;\n"+
        "        }\n\n"+
        
        "        public void add(int integer) {\n"+
        "            this.integer += integer;\n"+
        "        }\n\n"+
        
        "        public void substract(int integer) {\n"+
        "            this.integer -= integer;\n"+
        "        }\n\n"+
        
        "        public void multiply(int integer) {\n"+
        "            this.integer *= integer;\n"+
        "        }\n\n"+
        
        "        public void divide(int integer) {\n"+
        "            this.integer /= integer;\n"+
        "        }\n\n"+
        
        "        public void setValue(int integer) {\n"+
        "            this.integer = integer;\n"+
        "        }\n\n"+
        
        "        public int getValue() {\n"+
        "            return this.integer;\n"+
        "        }\n"+
        "    }\n\n"+
        
        "    class DoubleRef {\n"+
        "        private double doble;\n\n"+
        
        "        public DoubleRef() {\n"+
        "            this.doble = 0;\n"+
        "        }\n\n"+
        
        "        public DoubleRef(double doble) {\n"+
        "            this.doble = doble;\n"+
        "        }\n\n"+
        
        "        public void add(double doble) {\n"+
        "            this.doble += doble;\n"+
        "        }\n\n"+
        
        "        public void substract(double doble) {\n"+
        "            this.doble -= doble;\n"+
        "        }\n\n"+
        
        "        public void multiply(double doble) {\n"+
        "            this.doble *= doble;\n"+
        "        }\n\n"+
        
        "        public void divide(double doble) {\n"+
        "            this.doble /= doble;\n"+
        "        }\n\n"+
        
        "        public void setValue(double doble) {\n"+
        "            this.doble = doble;\n"+
        "        }\n\n"+
        
        "        public double getValue() {\n"+
        "            return this.doble;\n"+
        "        }\n"+
        "    }\n\n"+
        
        "}";
    }

    private void generarTextoInicial(){
        scannerCodeText = scannerCodeText+ 
        "import java.io.BufferedReader;\n"+
        "import java.io.FileReader;\n"+
        "import java.util.ArrayList;\n"+
        "import java.util.Scanner;\n"+
        
        "public class Scanner"+parserName+"{\n"+
            "    private Grafo automata;\n"+
            "    private ArrayList<Token> tokens;\n\n"+
            "    public Scanner"+parserName+"(){}\n"+
            "   public String ValidarAutomata(String cadena){\n"+
        "        String cadenaDeTokens=\"\";\n"+
        "        String caracteresEncontrados=\"\";\n"+
        "        Token tokenLocal = new Token();\n"+
        "        int nodoActual=0;\n"+
        "        NodoGrafo nodoEvaluando=null;\n"+
        "        for (int i=0; i<cadena.length();i++){\n"+
        "           caracteresEncontrados+=cadena.charAt(i);\n"+
        "           nodoEvaluando = automata.getNodo(nodoActual);\n"+
        "           boolean transitioned=false;\n"+
        "           for (int j=0; j<nodoEvaluando.getTransiciones().size();j++){\n"+
        "                if(nodoEvaluando.getTransiciones().get(j).getParametro().contains(Character.toString(cadena.charAt(i)))){\n"+
        "                    nodoActual = nodoEvaluando.getTransiciones().get(j).getObjetivo();\n"+
        "                    transitioned=true;\n"+
        "                }\n"+
        "           }\n"+
        "           if(!transitioned){\n"+
        "                if(nodoEvaluando.getIsFinal()){\n"+
        "                    cadenaDeTokens+=(nodoEvaluando.getToken()+\", \");\n"+
        "                    caracteresEncontrados=caracteresEncontrados.substring(0, caracteresEncontrados.length()-1);"+
        "                    tokenLocal = new Token(nodoEvaluando.getTokenNumber(),nodoEvaluando.getToken(),  caracteresEncontrados);\n"+
        "                    tokens.add(tokenLocal);\n"+
        "                    caracteresEncontrados=\"\";\n"+
        "                    i--;\n"+
        "                    nodoActual=0;\n"+
        "                }else{\n"+
        "                   return \"Error de entrada. Entrada no valida\";\n"+
        "                }\n"+
        "            }\n"+
        "           if(nodoEvaluando.getIsFinal()&&nodoEvaluando.getTokenType()==0){\n"+
        "                cadenaDeTokens+=(nodoEvaluando.getToken()+\", \");\n"+
        "                caracteresEncontrados=caracteresEncontrados.substring(0, caracteresEncontrados.length()-1);"+
        "                tokenLocal = new Token(nodoEvaluando.getTokenNumber(),nodoEvaluando.getToken(),  caracteresEncontrados);\n"+
        "                tokens.add(tokenLocal);\n"+
        "                caracteresEncontrados=\"\";\n"+
        "                i--;\n"+
        "                nodoActual=0;\n"+
        "            }\n"+
        "        }\n"+
		"        nodoEvaluando = automata.getNodo(nodoActual);\n"+
		"        if(nodoEvaluando.getIsFinal()){\n"+
        "        tokenLocal = new Token(nodoEvaluando.getTokenNumber(), nodoEvaluando.getToken(), caracteresEncontrados);"+
        "        tokens.add(tokenLocal);"+
		"        cadenaDeTokens+=(nodoEvaluando.getToken());\n"+
		"        }\n"+
        "        return cadenaDeTokens;\n"+
        "    }\n"+
        "    public ArrayList<Token> GenerarTokenString(String cadenaAEvaluar)\n"+
        "    {\n"+
        "        tokens = new ArrayList<>();\n"+
        "        generateAutoma();\n"+
        "        String cadenaDeRetorno = ValidarAutomata(cadenaAEvaluar);\n"+
        "        System.out.println(\"Respuesta de automata: \"+cadenaDeRetorno);\n"+
        "        return tokens;\n"+
        "   }\n"+
        
        "    private void generateAutoma(){\n"+
        "       automata = new Grafo();\n";        
    }
}