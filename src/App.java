import java.util.Scanner;

public class App {
    public static void main(String args[]){
        Lector lector = new Lector();
        String filename;
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el nombre del archivo a leer");
        filename = sc.nextLine();
        if (lector.lexerExecute(filename)){
            System.out.println("Parser Generado con exito");
        }else{
            System.out.println("Error al generar Parser");
        }
        sc.close();

    }
}