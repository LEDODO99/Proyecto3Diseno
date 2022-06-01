import java.util.ArrayList;

public class ParserMyCOCOR {
    private ArrayList<Token> tokens;
    private Token currentToken;
    private Token lastToken;
    private int currentIndex;

    private void Expect(int tokenType) {
        if (currentToken.getTokenType() != tokenType) {
            System.out.println("Error en " + currentToken.getValue());
        }
        lastToken = tokens.get(currentIndex);
        currentIndex++;
        if (currentIndex < tokens.size())
            currentToken = tokens.get(currentIndex);
        else {
            currentToken.setTokenType(-1);
        }
    }

    public ParserMyCOCOR(ArrayList<Token> tokens) {
        this.tokens = tokens;
        currentToken = tokens.get(0);
        currentIndex = 0;
    }

    public void Parse() {
        MyCOCOR();
    }

    private void MyCOCOR() {
        if (currentToken.getTokenType() == 8) {
            StringRef CompilerName = new StringRef();
            StringRef EndName = new StringRef();
            Expect(8);
            Ident(CompilerName);
            System.out.println("Nombre Inicial del Compilador: " + CompilerName.getValue());
            if (currentToken.getTokenType() == 6) {
                Codigo();
            }
            Body();
            Expect(9);
            Ident(EndName);
            System.out.println("Nombre Final del Compilador:" + EndName.getValue());
        }
    }

    private void Body() {
        if (currentToken.getTokenType() == 10) {
            Characters();
            if (currentToken.getTokenType() == 15) {
                Keywords();
            }
            Tokens();
            Productions();
        }
    }

    private void Characters() {
        if (currentToken.getTokenType() == 10) {
            StringRef CharName = new StringRef();
            IntegerRef Counter = new IntegerRef();
            Expect(10);
            System.out.println("LEYENDO CHARACTERS");
            while (currentToken.getTokenType() == 0 || currentToken.getTokenType() == 12
                    || currentToken.getTokenType() == 13) {
                if (currentToken.getTokenType() == 0) {
                    Ident(CharName);
                    Counter.add(1);
                    System.out.println("Char Set " + Counter.getValue() + ": " + CharName.getValue());
                    Expect(11);
                    CharSet();
                    while (currentToken.getTokenType() == 12 || currentToken.getTokenType() == 13) {
                        if (currentToken.getTokenType() == 12) {
                            Expect(12);
                            CharSet();
                        } else if (currentToken.getTokenType() == 13) {
                            Expect(13);
                            CharSet();
                        }
                    }
                    Expect(14);
                }
            }
        }
    }

    private void Keywords() {
        if (currentToken.getTokenType() == 15) {
            StringRef KeyName = new StringRef();
            StringRef StringValue = new StringRef();
            IntegerRef Counter = new IntegerRef();
            Expect(15);
            System.out.println("LEYENDO KEYWORDS");
            while (currentToken.getTokenType() == 0) {
                if (currentToken.getTokenType() == 0) {
                    Ident(KeyName);
                    Counter.add(1);
                    System.out.println("KeyWord " + Counter.getValue() + ": " + KeyName.getValue());
                    Expect(11);
                    String(StringValue);
                    Expect(14);
                }
            }
        }
    }

    private void Tokens() {
        if (currentToken.getTokenType() == 16) {
            StringRef TokenName = new StringRef();
            int Counter = 0;
            Expect(16);
            System.out.println("LEYENDO TOKENS");
            while (currentToken.getTokenType() == 0 || currentToken.getTokenType() == 18) {
                if (currentToken.getTokenType() == 0) {
                    Ident(TokenName);
                    Counter++;
                    System.out.println("Token " + Counter + ": " + TokenName.getValue());
                    Expect(11);
                    TokenExpr();
                    if (currentToken.getTokenType() == 18) {
                        ExceptKeyword();
                    }
                    Expect(14);
                }
            }
        }
    }

    private void Productions() {
        if (currentToken.getTokenType() == 17) {
            int Counter = 0;
            Expect(17);
            StringRef ProdName = new StringRef();
            System.out.println("LEYENDO PRODUCTIONS");
            while (currentToken.getTokenType() == 0 || currentToken.getTokenType() == 26
                    || currentToken.getTokenType() == 6) {
                if (currentToken.getTokenType() == 0) {
                    Ident(ProdName);
                    Counter++;
                    System.out.println("Production " + Counter + ": " + ProdName.getValue());
                    if (currentToken.getTokenType() == 26) {
                        Atributos();
                    }
                    Expect(11);
                    if (currentToken.getTokenType() == 6) {
                        Codigo();
                    }
                    ProductionExpr();
                    Expect(14);
                }
            }
        }
    }

    private void ExceptKeyword() {
        if (currentToken.getTokenType() == 18) {
            Expect(18);
            Expect(15);
        }
    }

    private void ProductionExpr() {
        if (currentToken.getTokenType() == 1 || currentToken.getTokenType() == 2 || currentToken.getTokenType() == 0
                || currentToken.getTokenType() == 26 || currentToken.getTokenType() == 20
                || currentToken.getTokenType() == 22 || currentToken.getTokenType() == 24) {
            ProdTerm();
            while (currentToken.getTokenType() == 19) {
                if (currentToken.getTokenType() == 19) {
                    Expect(19);
                    ProdTerm();
                }
            }
        }
    }

    private void ProdTerm() {
        if (currentToken.getTokenType() == 1 || currentToken.getTokenType() == 2 || currentToken.getTokenType() == 0
                || currentToken.getTokenType() == 26 || currentToken.getTokenType() == 20
                || currentToken.getTokenType() == 22 || currentToken.getTokenType() == 24) {
            ProdFactor();
            while (currentToken.getTokenType() == 1 || currentToken.getTokenType() == 2
                    || currentToken.getTokenType() == 0 || currentToken.getTokenType() == 26
                    || currentToken.getTokenType() == 20 || currentToken.getTokenType() == 22
                    || currentToken.getTokenType() == 24) {
                if (currentToken.getTokenType() == 1 || currentToken.getTokenType() == 2
                        || currentToken.getTokenType() == 0 || currentToken.getTokenType() == 26
                        || currentToken.getTokenType() == 20 || currentToken.getTokenType() == 22
                        || currentToken.getTokenType() == 24) {
                    ProdFactor();
                }
            }
        }
    }

    private void ProdFactor() {
        if (currentToken.getTokenType() == 1 || currentToken.getTokenType() == 2 || currentToken.getTokenType() == 0
                || currentToken.getTokenType() == 26 || currentToken.getTokenType() == 20
                || currentToken.getTokenType() == 22 || currentToken.getTokenType() == 24) {
            if (currentToken.getTokenType() == 1 || currentToken.getTokenType() == 2 || currentToken.getTokenType() == 0
                    || currentToken.getTokenType() == 26) {
                SymbolProd();
            } else if (currentToken.getTokenType() == 20) {
                Expect(20);
                ProductionExpr();
                Expect(21);
            } else if (currentToken.getTokenType() == 22) {
                Expect(22);
                ProductionExpr();
                Expect(23);
            } else if (currentToken.getTokenType() == 24) {
                Expect(24);
                ProductionExpr();
                Expect(25);
            } else {
                System.out.println("Error");
            }
            if (currentToken.getTokenType() == 6) {
                Codigo();
            }
        }
    }

    private void SymbolProd() {
        if (currentToken.getTokenType() == 1 || currentToken.getTokenType() == 2 || currentToken.getTokenType() == 0
                || currentToken.getTokenType() == 26) {
            StringRef SV = new StringRef();
            StringRef IN = new StringRef();
            if (currentToken.getTokenType() == 1) {
                String(SV);
                System.out.println("String en Production: " + SV.getValue());
            } else if (currentToken.getTokenType() == 2) {
                Expect(2);
            } else if (currentToken.getTokenType() == 0) {
                Ident(IN);
                System.out.println("Identificador en Production: " + IN.getValue());
                if (currentToken.getTokenType() == 26) {
                    Atributos();
                }
            } else {
                System.out.println("Error");
            }
        }
    }

    private void Codigo() {
        if (currentToken.getTokenType() == 6) {
            Expect(6);
            while (currentToken.getTokenType() == 28) {
                if (currentToken.getTokenType() == 28) {
                    //ANY();
                }
            }
            Expect(7);
        }
    }

    private void Atributos() {
        if (currentToken.getTokenType() == 26) {
            Expect(26);
            while (currentToken.getTokenType() == 28) {
                if (currentToken.getTokenType() == 28) {
                    //ANY();
                }
            }
            Expect(27);
        }
    }

    private void TokenExpr() {
        if (currentToken.getTokenType() == 1 || currentToken.getTokenType() == 2 || currentToken.getTokenType() == 0
                || currentToken.getTokenType() == 20 || currentToken.getTokenType() == 22
                || currentToken.getTokenType() == 24) {
            TokenTerm();
            while (currentToken.getTokenType() == 19) {
                if (currentToken.getTokenType() == 19) {
                    Expect(19);
                    TokenTerm();
                }
            }
        }
    }

    private void TokenTerm() {
        if (currentToken.getTokenType() == 1 || currentToken.getTokenType() == 2 || currentToken.getTokenType() == 0
                || currentToken.getTokenType() == 20 || currentToken.getTokenType() == 22
                || currentToken.getTokenType() == 24) {
            TokenFactor();
            while (currentToken.getTokenType() == 1 || currentToken.getTokenType() == 2
                    || currentToken.getTokenType() == 0 || currentToken.getTokenType() == 20
                    || currentToken.getTokenType() == 22 || currentToken.getTokenType() == 24) {
                if (currentToken.getTokenType() == 1 || currentToken.getTokenType() == 2
                        || currentToken.getTokenType() == 0 || currentToken.getTokenType() == 20
                        || currentToken.getTokenType() == 22 || currentToken.getTokenType() == 24) {
                    TokenFactor();
                }
            }
        }
    }

    private void TokenFactor() {
        if (currentToken.getTokenType() == 1 || currentToken.getTokenType() == 2 || currentToken.getTokenType() == 0
                || currentToken.getTokenType() == 20 || currentToken.getTokenType() == 22
                || currentToken.getTokenType() == 24) {
            if (currentToken.getTokenType() == 1 || currentToken.getTokenType() == 2
                    || currentToken.getTokenType() == 0) {
                SimbolToken();
            } else if (currentToken.getTokenType() == 20) {
                Expect(20);
                TokenExpr();
                Expect(21);
            } else if (currentToken.getTokenType() == 22) {
                Expect(22);
                TokenExpr();
                Expect(23);
            } else if (currentToken.getTokenType() == 24) {
                Expect(24);
                TokenExpr();
                Expect(25);
            } else {
                System.out.println("Error");
            }
        }
    }

    private void SimbolToken() {
        if (currentToken.getTokenType() == 1 || currentToken.getTokenType() == 2 || currentToken.getTokenType() == 0) {
            StringRef IdentName = new StringRef();
            StringRef StringValue = new StringRef();
            if (currentToken.getTokenType() == 1) {
                String(StringValue);
            } else if (currentToken.getTokenType() == 2) {
                Expect(2);
            } else if (currentToken.getTokenType() == 0) {
                Ident(IdentName);
                System.out.println("Identificador en Token: " + IdentName.getValue());
            } else {
                System.out.println("Error");
            }
        }
    }

    private void CharSet() {
        if (currentToken.getTokenType() == 1 || currentToken.getTokenType() == 2 || currentToken.getTokenType() == 3
                || currentToken.getTokenType() == 4 || currentToken.getTokenType() == 28
                || currentToken.getTokenType() == 0) {
            StringRef IdentName = new StringRef();
            StringRef StringValue = new StringRef();
            if (currentToken.getTokenType() == 1) {
                String(StringValue);
            } else if (currentToken.getTokenType() == 2 || currentToken.getTokenType() == 3
                    || currentToken.getTokenType() == 4) {
                Char();
            } else if (currentToken.getTokenType() == 28) {
                Expect(28);
            } else if (currentToken.getTokenType() == 0) {
                Ident(IdentName);
                System.out.println("Identificador en CharSet: " + IdentName.getValue());
            } else {
                System.out.println("Error");
            }
        }
    }

    private void Char() {
        if (currentToken.getTokenType() == 2 || currentToken.getTokenType() == 3 || currentToken.getTokenType() == 4) {
            if (currentToken.getTokenType() == 2) {
                Expect(2);
            } else if (currentToken.getTokenType() == 3) {
                Expect(3);
            } else if (currentToken.getTokenType() == 4) {
                Expect(4);
            } else {
                System.out.println("Error");
            }
        }
    }

    private void String(StringRef S) {
        if (currentToken.getTokenType() == 1) {
            Expect(1);
            S.assignValue(lastToken.getValue());
        }
    }

    private void Ident(StringRef S) {
        if (currentToken.getTokenType() == 0) {
            Expect(0);
            S.assignValue(lastToken.getValue());
        }
    }

    class IntegerRef {
        private int integer;

        public IntegerRef() {
            this.integer = 0;
        }

        public IntegerRef(int integer) {
            this.integer = integer;
        }

        public void add(int integer) {
            this.integer += integer;
        }

        public void substract(int integer) {
            this.integer -= integer;
        }

        public void multiply(int integer) {
            this.integer *= integer;
        }

        public void divide(int integer) {
            this.integer /= integer;
        }

        public void setValue(int integer) {
            this.integer = integer;
        }

        public int getValue() {
            return this.integer;
        }
    }

    class DoubleRef {
        private double doble;

        public DoubleRef() {
            this.doble = 0;
        }

        public DoubleRef(double doble) {
            this.doble = doble;
        }

        public void add(double doble) {
            this.doble += doble;
        }

        public void substract(double doble) {
            this.doble -= doble;
        }

        public void multiply(double doble) {
            this.doble *= doble;
        }

        public void divide(double doble) {
            this.doble /= doble;
        }

        public void setValue(double doble) {
            this.doble = doble;
        }

        public double getValue() {
            return this.doble;
        }
    }

    class StringRef {
        private String string;

        public StringRef() {
            this.string = "";
        }

        public StringRef(String string) {
            this.string = string;
        }

        public void assignValue(String string) {
            this.string = string;
        }

        public String getValue() {
            return this.string;
        }
    }

}