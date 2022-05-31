import java.util.ArrayList;

public class Plantilla {
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

    public Plantilla(ArrayList<Token> tokens) {
        this.tokens = tokens;
        currentToken = tokens.get(0);
        currentIndex = 0;
    }

    public void Parse() {
        //Punto medio
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
