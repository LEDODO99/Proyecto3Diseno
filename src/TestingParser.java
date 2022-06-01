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
        String lines="";
        String line=reader.readLine();
        lines+=line;
        line=reader.readLine();
        while (line!=null){
            lines+="\n"+line;
            line=reader.readLine();
        }
        
        ScannerDouble scanner = new ScannerDouble();
        ParserDouble parser;
        System.out.println("Lines: "+lines);
        ArrayList<Token> tokens = scanner.GenerarTokenString(lines);
        parser = new ParserDouble(tokens);
        parser.Parse();
        sc.close();
        reader.close();


    }
}
