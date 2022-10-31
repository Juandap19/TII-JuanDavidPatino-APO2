package ui;

import com.google.gson.Gson;
import model.*;
import exceptions.*;

import java.io.*;
import java.util.Scanner;
import javax.swing.*;
import java.util.ArrayList;

import static java.lang.Double.parseDouble;

public class Main {
    public static Scanner lc = new Scanner(System.in);
    public static Controladora ct = new Controladora();

    public static void main(String[] args) {
        File file = new File("C:/Users/juand/OneDrive/Documentos/Tarea Integradora II/CountryJson.txt");
        if (file.exists()) {
            ArrayList<Country> people = new ArrayList<>();
            try {
                FileInputStream fis = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

                String json = "";
                String line;
                if ((line = reader.readLine()) != null) {
                    json = line;
                }
                fis.close();

                Gson gson = new Gson();
                Country[] peopleFromJson = gson.fromJson(json, Country[].class);

                for (Country p : peopleFromJson) {
                    people.add(p);
                }
                ct.setCountries(people);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        file = new File("C:/Users/juand/OneDrive/Documentos/Tarea Integradora II/CityJson.txt");
        if(file.exists()){
            ArrayList<City> people = new ArrayList<>();
            try {
                FileInputStream fis = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

                String json = "";
                String line;
                if ((line = reader.readLine()) != null) {
                    json = line;
                }
                fis.close();

                Gson gson = new Gson();
                City[] peopleFromJson = gson.fromJson(json, City[].class);

                for (City p : peopleFromJson) {
                    people.add(p);
                }
                ct.setCities(people);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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
                    saveData();
                    break;
            }

        }while(option != 3);
    }

    private static void saveData() {
        ct.saveCountryData();
        ct.saveCityData();
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
        String ruta ="C:Users/juand/OneDrive/Documentos/Tarea Integradora II/comandos.SQL";
        File file=new File(ruta);
        try{
            FileInputStream fis=new FileInputStream(file);
            BufferedReader reader= new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line=reader.readLine())!=null){
                ct.validarComandos(line);
            }
        }catch(IOException e){
            e.printStackTrace();
        } catch (ComandoInvalidoException e){
            System.out.println(e);
        }

    }

}
