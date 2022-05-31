import java.util.ArrayList;

public class ParserAritmetica {
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

        public ParserAritmetica(ArrayList<Token> tokens) {
        this.tokens = tokens;
        currentToken = tokens.get(0);
        currentIndex = 0;
    }

    public void Parse() {
Expr();
    }

   private void Expr (  ) { 
if (currentToken.getTokenType()==5||currentToken.getTokenType()==1||currentToken.getTokenType()==8||currentToken.getTokenType()==3) {
while (currentToken.getTokenType()==5||currentToken.getTokenType()==1||currentToken.getTokenType()==8) {
if (currentToken.getTokenType()==5||currentToken.getTokenType()==1||currentToken.getTokenType()==8) {
Stat();
Expect(2);
} 
} 
Expect(3);
}
}
   private void Stat (  ) { 
if (currentToken.getTokenType()==5||currentToken.getTokenType()==1||currentToken.getTokenType()==8) {
IntegerRef value=new IntegerRef();
Expression(  value);
System.out.println(value.getValue());
}
}
   private void Expression (  IntegerRef result ) { 
if (currentToken.getTokenType()==5||currentToken.getTokenType()==1||currentToken.getTokenType()==8) {
IntegerRef result1=new IntegerRef();IntegerRef result2=new IntegerRef();
Term(  result1);
while (currentToken.getTokenType()==4||currentToken.getTokenType()==5) {
if (currentToken.getTokenType()==4) {
Expect(4);
Term(  result2);
result1.add(result2.getValue());
} else if (currentToken.getTokenType()==5) {
Expect(5);
Term(  result2);
result1.substract(result2.getValue());
} 
} 
result.setValue(result1.getValue());
}
}
   private void Term (  IntegerRef result ) { 
if (currentToken.getTokenType()==5||currentToken.getTokenType()==1||currentToken.getTokenType()==8) {
IntegerRef result1=new IntegerRef();IntegerRef result2=new IntegerRef();
Factor( result1);
while (currentToken.getTokenType()==6||currentToken.getTokenType()==7) {
if (currentToken.getTokenType()==6) {
Expect(6);
Factor( result2);
result1.multiply(result2.getValue());
} else if (currentToken.getTokenType()==7) {
Expect(7);
Factor( result2);
result1.divide(result2.getValue());
} 
} 
result.setValue(result1.getValue());
}
}
   private void Factor (  IntegerRef result ) { 
if (currentToken.getTokenType()==5||currentToken.getTokenType()==1||currentToken.getTokenType()==8) {
int signo=1;
if (currentToken.getTokenType()==5) {
Expect(5);
signo = -1;
} 
if (currentToken.getTokenType()==1) {
Number( result);
} else if (currentToken.getTokenType()==8) {
Expect(8);
Expression(  result);
Expect(9);
} else {
	System.out.println("Error");
}
result.multiply(signo);
}
}
   private void Number (  IntegerRef result ) { 
if (currentToken.getTokenType()==1) {
Expect(1);
 result.setValue(Integer.parseInt(lastToken.getValue()));
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

}