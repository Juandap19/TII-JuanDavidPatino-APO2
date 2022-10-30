package ui;

import model.*;
import exceptions.*;

import java.io.File;
import java.util.Scanner;
import javax.swing.*;
import java.util.ArrayList;

public class Main {
    public static Scanner lc = new Scanner(System.in);
    public static Controladora ct = new Controladora();

    public static void main(String[] args) {

//        String path = "C:/Users/juand/OneDrive/Documentos/Tarea Integradora II";
//        File folder = new File(path);
//
//        String[] files = folder.list();
//        for(String name : files){
//            System.out.println(name);
//        }
//
//        try {
//            //Referenciar un ARCHIVO
//            File archivo = new File(path+"/miarchivo.txt");
//            System.out.println("exists: "+archivo.exists());
//
//            FileInputStream fis = new FileInputStream(archivo);
//            BufferedReader reader = new BufferedReader(
//                    new InputStreamReader(fis)
//            );
//            String line;
//            while(( line = reader.readLine()) != null){
//                System.out.println(line);
//            }
//            fis.close();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        File archivo;
        String ruta ="C:/Users/juand/OneDrive/Documentos/Tarea Integradora II";
        JFileChooser chooser = new JFileChooser(ruta);
        chooser.setCurrentDirectory(new java.io.File(".txt"));
        if((chooser.showDialog(null, "Abrir"))== JFileChooser.APPROVE_OPTION){
            archivo = chooser.getSelectedFile();
            if(archivo.canRead()){
                if(archivo.getName().endsWith(".txt")){
                    ruta =archivo.getAbsoluteFile().toString();
                }else{
                    JOptionPane.showMessageDialog(null, "Archivo con diferente extensión","Error al cargar el archivo",2);
                }
            }
        }

        int option = 0;
        do{
            option = menu();
            switch (option){
                case 1:
                    try {
                        calcularComando();
                    }catch (CommandWithOutLogic e){
                        System.out.println(e);
                    }
                    break;
                case 2:
                    importDataSQL();
                    break;
                case 3:
                    break;
            }

        }while(option != 3);
    }


    private static void calcularComando() throws CommandWithOutLogic {
        System.out.println("////Command console////\n" +
                "Put the Command:");
        String comando = lc.nextLine();
        ct.calculateCommand(comando);
    }

    public static void ordernarPaisesPor(String[] comandoArray) {
        ct.ordenarPaisesPor(comandoArray);
    }





    public static int menu() {
        System.out.println("////Menu////\n" +
                "[1]. Insertar Comando.\n" +
                "[2]. Importar datos desde archivo SQL.\n" +
                "[3]. Salir.\n");
        int option = lc.nextInt();
        lc.nextLine();
        return option;
    }


    public static void registrarPais(String comando){
        try{
            ct.validarComandos(comando);
        }
        catch (ComandoInvalidoException e){
            System.out.println(e);
        }
    }

    public static void importDataSQL(){
        File archivo;
        String ruta ="C:/Users/juand/OneDrive/Documentos/Tarea Integradora II";
        JFileChooser chooser = new JFileChooser(ruta);
        chooser.setCurrentDirectory(new java.io.File(".txt"));
        if((chooser.showDialog(null, "Abrir"))== JFileChooser.APPROVE_OPTION){
            archivo = chooser.getSelectedFile(); 
            if(archivo.canRead()){
                if(archivo.getName().endsWith(".txt")){
                    ruta =archivo.getAbsoluteFile().toString();
                }else{
                    JOptionPane.showMessageDialog(null, "Archivo con diferente extensión","Error al cargar el archivo",2);
                }
            }
        }

    }


}
