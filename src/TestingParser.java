import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TestingParser {

    public static void main(String[] args) throws IOException{
        Scanner sc = new Scanner(System.in);
        String filename;
        System.out.println("Ingrese el nombre del archivo a leer");
        filename = sc.nextLine();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        ScannerDouble scannerAritmetica = new ScannerDouble();
        ParserDouble parserAritmetica;
        ArrayList<Token> tokens = scannerAritmetica.GenerarTokenString(reader.readLine());
        parserAritmetica = new ParserDouble(tokens);
        parserAritmetica.Parse();
        sc.close();


    }
}
