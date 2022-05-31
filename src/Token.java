public class Token {
    private int tokenType;
    private String tokenTypeName;
    private String value;

    public int getTokenType() {
        return this.tokenType;
    }

    public void setTokenType(int tokenType) {
        this.tokenType = tokenType;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Token() {
    }
    
    public Token(int tokenType,String tokenTypeName, String value) {
        this.tokenType = tokenType;
        this.value = value;
        this.tokenTypeName = tokenTypeName;
    }

    public String getTokenTypeName() {
        return this.tokenTypeName;
    }

    public void setTokenTypeName(String tokenTypeName) {
        this.tokenTypeName = tokenTypeName;
    }
    
}
