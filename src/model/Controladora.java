package model;

import exceptions.ComandoInvalidoException;
import exceptions.IncorrectFormatException;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import exceptions.*;

import com.google.gson.Gson;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static ui.Main.*;

public class Controladora {

    private ArrayList<Country> countries;
    private ArrayList<City> cities;

    public Controladora() {
        cities = new ArrayList<>();
        countries = new ArrayList<>();
    }

    public void addPais(String[] terceraParte) {
        double entero = parseDouble(terceraParte[2]);
        countries.add(new Country(terceraParte[0],terceraParte[1],entero,terceraParte[3]));
        System.out.println(countries.get(0).toString());
    }

    public void addCity(String[] terceraParte) {
        String [] Valor1 = terceraParte[terceraParte.length-1].split("\\)");
        double entero = parseDouble(Valor1[0]);
        try {
            Country pais = searchCountry(terceraParte[2]);
            if (pais != null) {
                try {
                    verificarFormato(terceraParte[0], terceraParte[1], terceraParte[3], terceraParte[2]);
                    cities.add(new City(terceraParte[0],terceraParte[1],terceraParte[2], entero ));
                    System.out.println("Ciudad Registrada Exitosamente.");
                } catch (IncorrectFormatException e) {
                    System.out.println();
                }
            } else {
                throw new CountryNotFoundException("The Country with the ID:"+terceraParte[2]+" haven`t registered yet.");
            }
        }catch (CountryNotFoundException e){
            System.out.println( e);
        }
    }
    private  Country searchCountry(String countryID) {
        for (int i = 0; i < countries.size(); i++) {
            if(countries.get(i).getId().equalsIgnoreCase(countryID)){
                return countries.get(i);
            }
        }
        return  null;
    }

    public String arrayPaisYaSort(ArrayList<Country> aux, String factorDeOrdenamiento) {
        switch (factorDeOrdenamiento){
            case "name" -> {
                aux = sortByName(aux);
            }
            case "id" -> {
                aux = sortById(aux);
            }
            case "population" -> {
                aux = sortByPop(aux);
            }
            case "countryCode" -> {
                aux = sortByCountryCode(aux);
            }

        }
        String message = "//ORDER BY: "+factorDeOrdenamiento.toUpperCase()+"//\n";
        for (Country aux1:aux) {
            message += aux1.getName()+"\n";
        }
        return message;
    }

    private ArrayList<Country> sortByName(ArrayList<Country> aux) {
        Collections.sort(aux, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return aux;
    }
    private ArrayList<Country> sortByPop(ArrayList<Country> aux) {
        Collections.sort(aux, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                if(o1.getPopulation() > o2.getPopulation()){
                    return -1;
                } else if (o1.getPopulation() < o2.getPopulation()) {
                    return 1;
                }else{
                    return 0;
                }
            }
        });
        return aux;

    }
    private ArrayList<Country> sortByCountryCode(ArrayList<Country> aux) {
        Collections.sort(aux, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                return o1.getCountryCode().compareTo(o2.getCountryCode());
            }
        });

        return aux;
    }

    private ArrayList<Country> sortById(ArrayList<Country> aux) {
        Collections.sort(aux, new Comparator<Country>() {
            @Override
            public int compare(Country o1, Country o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });

        return aux;
    }

    public void validarComandos(String comando) throws ComandoInvalidoException{
        comando = comando.replace("(", " ");
        comando = comando.replace(")", " ");
        String [] valores = comando.split("VALUES");
        if (valores.length == 2){
            String [] primeraParte = valores[0].split(" ");
            String [] segundaParte = valores[1].split(" ");
            String me = "";
            for (int i = 0; i < segundaParte.length; i++) {
                me += segundaParte[i];
            }
            String [] terceraParte = me.split(",");
            if (primeraParte.length == 7 && segundaParte.length == 6){
                if(primeraParte[0].equalsIgnoreCase("insert")){
                    if (primeraParte[1].equalsIgnoreCase("INTO") ){
                        if(primeraParte[2].equalsIgnoreCase("countries")) {
                            if(!IDRepetido(terceraParte[0])) {
                                addPais(terceraParte);
                                System.out.println("Pais Registrado Exitosamente.");
                            }else{
                                throw new ComandoInvalidoException("The Id is already registered with an another Country.");
                            }
                        }else if(primeraParte[2].equalsIgnoreCase("cities")){
                                addCity(terceraParte);
                        }else {
                            throw new ComandoInvalidoException("palabra cities o country no encontrada");
                        }
                    }else {
                        throw new ComandoInvalidoException("palabra into no encontrada ");
                    }
                }else{
                    throw new ComandoInvalidoException("Insert no encontrado");
                }
            }else {
                throw new ComandoInvalidoException("Comando con un length invalido");
            }
        }else {
            throw new ComandoInvalidoException("comando invalido");
        }
    }

    private boolean IDRepetido(String id) {
        for (int i = 0; i < countries.size(); i++) {
            if (id.equals(countries.get(i).getId())){
                return true;
            }
        }
        return false;
    }

    private void verificarFormato(String id, String name, String pop, String countryCode) throws IncorrectFormatException {
       if(id.charAt(0) == '\'' && id.charAt(id.length()-1) == '\''){
           if(name.charAt(0) == '\'' && name.charAt(name.length()-1) == '\''){
               if(countryCode.charAt(0) == '\'' && countryCode.charAt(countryCode.length()-1) == '\''){
                   if(pop.charAt(0) != '\'' && pop.charAt(pop.length()-1) != '\''){

                   }else
                       throw new IncorrectFormatException("The pop has an Incorrect syntax, it mustn`t has \' at the beginning or end \nYour input pop:"+pop);
               }else
                   throw new IncorrectFormatException("The countryCode has an Incorrect syntax, it must has \' at the beginning  and end\nYour input countryCode:"+countryCode);
           }else
               throw new IncorrectFormatException("The name has an Incorrect syntax, it must has \' at the beginning and end \nYour input name:"+name);
       }else
           throw new IncorrectFormatException("The id has an Incorrect syntax , it must has \' at the beginning and end \nYour input id:"+id);

    }

    public boolean ordenamientoValidacion(String[] comandoArray) {
        if (comandoArray.length == 11) {
            if (comandoArray[0].equalsIgnoreCase("select")) {
                if (comandoArray[1].equalsIgnoreCase("*")) {
                    if (comandoArray[2].equalsIgnoreCase("from")) {
                        if (comandoArray[3].equalsIgnoreCase("countries")||comandoArray[3].equalsIgnoreCase("cities")) {
                            if (comandoArray[4].equals("WHERE")) {
                                if (comandoArray[5].equalsIgnoreCase("name")|| comandoArray[5].equalsIgnoreCase("id") || comandoArray[5].equalsIgnoreCase("population") || comandoArray[5].equalsIgnoreCase("countryCode") || comandoArray[5].equalsIgnoreCase("countryID")) {
                                    if(comandoArray[6].equals(">") || comandoArray[6].equals("<") || comandoArray[6].equals("=")){
                                        if(comandoArray[8].equals("ORDER")){
                                            if(comandoArray[9].equals("BY")){
                                                return true;
                                            } else
                                                return false;
                                        } else
                                            return false;
                                    } else
                                        return false;
                                } else
                                    return false;
                            } else
                                return false;
                        } else
                            return false;
                    } else
                        return false;
                } else
                    return false;
            } else
                return false;
        }else
            return false;
    }

    public ArrayList<Country> busquedaYFIltradoPaises(String[] array) throws ComandoInvalidoException{
        ArrayList<Country> aux = new ArrayList<>();
        if(array.length == 8 || array.length == 11){
            String restriccion = array[7];
            switch (array[5]) {
                case "id" -> {
                    for (int i = 0; i < countries.size(); i++) {
                        if (countries.get(i).getId().equalsIgnoreCase(restriccion))
                            aux.add(countries.get(i));
                    }
                }
                case "name" -> {
                    for (int i = 0; i <countries.size(); i++) {
                        if (countries.get(i).getName().equalsIgnoreCase(restriccion))
                            aux.add(countries.get(i));
                    }
                }
                case "population" -> {
                    String restriccion2 = array[6];
                    switch (restriccion2) {
                        case ">":
                            for (int i = 0; i < countries.size(); i++) {
                                if (countries.get(i).getPopulation() > parseInt(restriccion))
                                    aux.add(countries.get(i));
                            }
                            break;
                        case "<":
                            for (int i = 0; i < countries.size(); i++) {
                                if (countries.get(i).getPopulation() < parseInt(restriccion))
                                    aux.add(countries.get(i));
                            }
                            break;
                        case "=":
                            for (int i = 0; i < countries.size(); i++) {
                                if (countries.get(i).getPopulation() == parseInt(restriccion))
                                    aux.add(countries.get(i));
                            }
                            break;
                    }
                }
                case "countryCode" -> {
                    for (int i = 0; i <countries.size(); i++) {
                        if (countries.get(i).getName().equalsIgnoreCase(restriccion)) {
                            aux.add(countries.get(i));
                        }
                    }
                }
                default -> {
                    throw new ComandoInvalidoException("Paramentro no valido");
                }
            }

        }else{
            aux =  countries;
        }
        return aux;
    }

    public boolean delteValidacion(String[] comandoArray) {
        if(comandoArray.length == 7){
            if (comandoArray[0].equals("DELETE")){
                if(comandoArray[1].equals("FROM")){
                    if (comandoArray[2].equals("countries") || comandoArray[2].equals("cities")){
                        if (comandoArray[3].equals("WHERE")){
                            if(comandoArray[4].equalsIgnoreCase("name")|| comandoArray[4].equalsIgnoreCase("id") || comandoArray[4].equalsIgnoreCase("population") || comandoArray[4].equalsIgnoreCase("countryCode") || comandoArray[4].equalsIgnoreCase("countryID")){
                                if(comandoArray[5].equals(">")||comandoArray[5].equals("=")|| comandoArray[6].equals("<")){
                                    return true;
                                }else
                                    return false;
                            }else
                                return false;
                        }else
                            return false;
                    }else
                        return false;
                }else
                    return false;
            }else
                return false;
        }else
            return false;
    }

    public boolean isCountryByString(String countryOrCity) {
        if (countryOrCity.equals("countries")){
            return true;
        }else
            return false;
    }

    public void deleteCountry(String restriccion, String operadorLogico, String comparador) throws ComandoInvalidoException {
        ArrayList<Country> aux = new ArrayList<>();
        switch (restriccion) {
            case "id" -> {
                switch (operadorLogico) {
                    case ">"-> {
                        for (int i = 0; i < countries.size(); i++) {
                            if (countries.get(i).getId().compareTo(comparador) >= 1) {
                                aux.add(countries.get(i));
                            }
                        }
                        countries = aux;
                    }
                    case "<" -> {
                        for (int i = 0; i < countries.size(); i++) {
                            if (countries.get(i).getId().compareTo(comparador) <= -1) {
                                aux.add(countries.get(i));
                            }
                        }
                        countries = aux;
                    }
                    case "=" -> {
                        for (int i = 0; i < countries.size(); i++) {
                            if (countries.get(i).getId().compareTo(comparador) == 0) {
                                countries.remove(i);
                            }
                        }
                    }
                }

            }
            case "name" -> {
                switch (operadorLogico) {
                    case ">"-> {
                        for (int i = 0; i < countries.size(); i++) {
                            if (countries.get(i).getName().compareTo(comparador) >= 1) {
                                aux.add(countries.get(i));
                            }
                        }
                        countries = aux;
                    }
                    case "<" -> {
                        for (int i = 0; i < countries.size(); i++) {
                            if (countries.get(i).getName().compareTo(comparador) <= -1) {
                                aux.add(countries.get(i));
                            }
                        }
                        countries = aux;
                    }
                    case "=" -> {
                        for (int i = 0; i < countries.size(); i++) {
                            if (countries.get(i).getName().compareTo(comparador) == 0) {
                                countries.remove(i);
                            }
                        }
                    }
                }

            }
            case "population" -> {
                switch (operadorLogico) {
                    case ">" -> {
                        for (int i = 0; i < countries.size(); i++) {
                            if (countries.get(i).getPopulation() > parseInt(comparador)) {
                                aux.add(countries.get(i));
                            }
                        }
                        countries = aux;
                    }
                    case "<" -> {
                        for (int i = 0; i < countries.size(); i++) {
                            if (countries.get(i).getPopulation() < parseInt(comparador)) {
                                aux.add(countries.get(i));
                            }
                        }
                        countries = aux;
                    }
                    case "=" -> {
                        for (int i = 0; i < countries.size(); i++) {
                            if (countries.get(i).getPopulation() == parseInt(comparador)) {
                                countries.remove(i);
                            }
                        }
                        countries = aux;
                    }
                }
            }
            case "countryCode" -> {
                switch (operadorLogico) {
                    case ">"-> {
                        for (int i = 0; i < countries.size(); i++) {
                            if (countries.get(i).getCountryCode().compareTo(comparador) >= 1) {
                                aux.add(countries.get(i));
                            }
                        }
                        countries = aux;
                    }
                    case "<" -> {
                        for (int i = 0; i < countries.size(); i++) {
                            if (countries.get(i).getCountryCode().compareTo(comparador) <= -1) {
                                aux.add(countries.get(i));
                            }
                        }
                        countries = aux;
                    }
                    case "=" -> {
                        for (int i = 0; i < countries.size(); i++) {
                            if (countries.get(i).getCountryCode().compareTo(comparador) == 0) {
                                aux.add(countries.get(i));
                            }
                        }
                        countries = aux;
                    }
                }
            }
            default -> {
                throw new ComandoInvalidoException("Parametro no valido");
            }
        }
    }

    //Separacion de city - Pais

    public String arrayCityYaSort(ArrayList<City> aux, String factorDeOrdenamiento) {
        switch (factorDeOrdenamiento){
            case "name" -> {
                System.out.println("nameueu");
                sortByNameCity(aux);
            }
            case "id" -> {
                sortByIdCity(aux);
            }
            case "population" -> {
                aux = sortByPOPCity(aux);
            }
            case "countryID" -> {
                sortByContryIDCity(aux);
            }

        }
        String message = "//ORDER BY: "+factorDeOrdenamiento.toUpperCase()+"//\n";
        for (City aux1:aux) {
            message += aux1.getName()+"\n";
        }
        return message;
    }

    private void sortByNameCity(ArrayList<City> aux) {
        Collections.sort(aux, new Comparator<City>() {
            @Override
            public int compare(City o1, City o2) {
                return o1.getName().compareTo(o2.getName());
            }

        });

    }
    private ArrayList<City> sortByPOPCity(ArrayList<City> aux) {
        Collections.sort(aux, new Comparator<City>() {
            @Override
            public int compare(City o1, City o2) {
                if(o1.getPopulation() < o2.getPopulation()){
                    return -1;
                } else if (o1.getPopulation() > o2.getPopulation()) {
                    return 1;
                }else{
                    return 0;
                }
            }
        });
        return aux;

    }
    private void sortByContryIDCity(ArrayList<City> aux) {
        Collections.sort(aux, new Comparator<City>() {
            @Override
            public int compare(City o1, City o2) {
                return o1.getCountryID().compareTo(o2.getCountryID());
            }
        });

    }

    private void sortByIdCity(ArrayList<City> aux) {
        Collections.sort(aux, new Comparator<City>() {
            @Override
            public int compare(City o1, City o2) {
               return o1.getId().compareTo(o2.getId());
            }
        });

    }


    public ArrayList<City> busquedaYFIltradoCities(String[] array) {
        ArrayList<City> aux = new ArrayList<>();
        if(array.length == 8 || array.length == 11){
            String restriccion = array[6];
            String restriccion2 = array[5];
            String comparador = array[7];
            switch (restriccion2) {
                case "id" -> {
                    switch (restriccion) {
                        case ">"-> {
                            for (int i = 0; i < cities.size(); i++) {
                                if (cities.get(i).getId().compareTo(comparador) >= 1) {
                                    aux.add(cities.get(i));
                                }
                            }
                        }
                        case "<" -> {
                            for (int i = 0; i < cities.size(); i++) {
                                if (cities.get(i).getId().compareTo(comparador) <= -1) {
                                    aux.add(cities.get(i));
                                }
                            }
                        }
                        case "=" -> {
                            for (int i = 0; i < cities.size(); i++) {
                                if (cities.get(i).getId().compareTo(comparador) == 0) {
                                    aux.add(cities.get(i));
                                }
                            }
                        }
                    }

                }
                case "name" -> {
                    switch (restriccion) {
                        case ">"-> {
                            for (int i = 0; i < cities.size(); i++) {
                                if (cities.get(i).getName().compareTo(comparador) >= 1) {
                                    aux.add(cities.get(i));
                                }
                            }
                        }
                        case "<" -> {
                            for (int i = 0; i < cities.size(); i++) {
                                if (cities.get(i).getName().compareTo(comparador) <= -1) {
                                    aux.add(cities.get(i));
                                }
                            }
                        }
                        case "=" -> {
                            System.out.println("here");
                            for (int i = 0; i < cities.size(); i++) {
                                if (cities.get(i).getName().compareTo(comparador) == 0) {
                                    aux.add(cities.get(i));
                                }
                            }
                        }
                    }

                }
                case "population" -> {
                    switch (restriccion) {
                        case ">" -> {
                            for (int i = 0; i < cities.size(); i++) {
                                if (cities.get(i).getPopulation() > parseInt(comparador)) {
                                    aux.add(cities.get(i));
                                }
                            }
                        }
                        case "<" -> {
                            for (int i = 0; i < cities.size(); i++) {
                                if (cities.get(i).getPopulation() < parseInt(comparador)) {
                                    aux.add(cities.get(i));
                                }
                            }
                        }
                        case "=" -> {
                            for (int i = 0; i < countries.size(); i++) {
                                if (countries.get(i).getPopulation() == parseInt(comparador)) {
                                    aux.add(cities.get(i));
                                }
                            }
                        }
                    }
                }
                case "countryID" -> {
                    switch (restriccion) {
                        case ">"-> {
                            for (int i = 0; i < cities.size(); i++) {
                                if (cities.get(i).getCountryID().compareTo(comparador) >= 1) {
                                    aux.add(cities.get(i));
                                }
                            }
                        }
                        case "<" -> {
                            for (int i = 0; i < cities.size(); i++) {
                                if (cities.get(i).getCountryID().compareTo(comparador) <= -1) {
                                    aux.add(cities.get(i));
                                }
                            }
                        }
                        case "=" -> {
                            for (int i = 0; i < cities.size(); i++) {
                                if (cities.get(i).getCountryID().compareTo(comparador) == 0) {
                                    aux.add(cities.get(i));
                                }
                            }
                        }
                    }
                }
                default -> {
                    System.out.println("exception parametro no valido");
                }
            }
        }else{
            aux =  cities;
        }
        return aux;
    }


    public void deleteCity(String restriccion, String operadorLogico, String comparador) {
        ArrayList<City> aux = new ArrayList<>();
        switch (restriccion) {
            case "id" -> {
                switch (operadorLogico) {
                    case ">"-> {
                        for (int i = 0; i < cities.size(); i++) {
                            if (cities.get(i).getId().compareTo(comparador) >= 1) {
                                aux.add(cities.get(i));
                            }
                        }
                        cities = aux;
                    }
                    case "<" -> {
                        for (int i = 0; i < cities.size(); i++) {
                            if (cities.get(i).getId().compareTo(comparador) <= -1) {
                                aux.add(cities.get(i));
                            }
                        }
                        cities = aux;
                    }
                    case "=" -> {
                        for (int i = 0; i < cities.size(); i++) {
                            if (cities.get(i).getId().compareTo(comparador) == 0) {
                                cities.remove(i);
                            }
                        }
                    }
                }

            }
            case "name" -> {
                switch (operadorLogico) {
                    case ">"-> {
                        for (int i = 0; i < cities.size(); i++) {
                            if (cities.get(i).getName().compareTo(comparador) >= 1) {
                                aux.add(cities.get(i));
                            }
                        }
                        cities = aux;
                    }
                    case "<" -> {
                        for (int i = 0; i < cities.size(); i++) {
                            if (cities.get(i).getName().compareTo(comparador) <= -1) {
                                aux.add(cities.get(i));
                            }
                        }
                        cities = aux;
                    }
                    case "=" -> {
                        for (int i = 0; i < cities.size(); i++) {
                            if (cities.get(i).getName().compareTo(comparador) == 0) {
                                cities.remove(i);
                            }
                        }
                    }
                }

            }
            case "population" -> {
                switch (operadorLogico) {
                    case ">" -> {
                        for (int i = 0; i < cities.size(); i++) {
                            if (cities.get(i).getPopulation() > parseInt(comparador)) {
                                aux.add(cities.get(i));
                            }
                        }
                        cities = aux;
                    }
                    case "<" -> {
                        for (int i = 0; i < cities.size(); i++) {
                            if (cities.get(i).getPopulation() < parseInt(comparador)) {
                                aux.add(cities.get(i));
                            }
                        }
                        cities = aux;
                    }
                    case "=" -> {
                        for (int i = 0; i < countries.size(); i++) {
                            if (countries.get(i).getPopulation() == parseInt(comparador)) {
                                countries.remove(i);
                            }
                        }
                        cities = aux;
                    }
                }
            }
            case "countryCode" -> {
                switch (operadorLogico) {
                    case ">"-> {
                        for (int i = 0; i < cities.size(); i++) {
                            if (cities.get(i).getCountryID().compareTo(comparador) >= 1) {
                                aux.add(cities.get(i));
                            }
                        }
                        cities = aux;
                    }
                    case "<" -> {
                        for (int i = 0; i < cities.size(); i++) {
                            if (cities.get(i).getCountryID().compareTo(comparador) <= -1) {
                                aux.add(cities.get(i));
                            }
                        }
                        cities = aux;
                    }
                    case "=" -> {
                        for (int i = 0; i < cities.size(); i++) {
                            if (cities.get(i).getCountryID().compareTo(comparador) == 0) {
                                aux.add(cities.get(i));
                            }
                        }
                        cities = aux;
                    }
                }
            }
            default -> {
                System.out.println("exception parametro no valido");
            }
        }
    }

    //Getter and Setters:

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    public void calculateCommand(String comando) throws CommandWithOutLogic {
        String [] comandoArray = comando.split(" ");
        if(comandoArray[0].equalsIgnoreCase("insert") && comandoArray[1].equalsIgnoreCase("into")){
            registrarPais(comando);
        } else if (comandoArray[0].equalsIgnoreCase("select") && comandoArray[1].equalsIgnoreCase("*") && comandoArray[2].equalsIgnoreCase("from") && comandoArray.length == 8 || comandoArray.length == 4 && comandoArray[3].equalsIgnoreCase("countries") || comandoArray[3].equalsIgnoreCase("cities")) {
            if(ct.isCountryByString(comandoArray[3])){
                try {
                    ArrayList<Country> aux = ct.busquedaYFIltradoPaises(comandoArray);
                    for (Country obj : aux) {
                        System.out.println(obj.getName());
                    }
                }catch (ComandoInvalidoException e){
                    System.out.println(e);
                }
            }else {
                ArrayList<City> aux =  ct.busquedaYFIltradoCities(comandoArray);
                for (City obj : aux){
                    System.out.println(obj.getName());
                }
            }
        } else if (ct.ordenamientoValidacion(comandoArray)) {
            if(ct.isCountryByString(comandoArray[3])){
                ordernarPaisesPor(comandoArray);
            }else{
                ordenarPaisesPor(comandoArray);
            }

        }else if(ct.delteValidacion(comandoArray)){
            if(isCountryByString(comandoArray[2])){
                try {
                    ArrayList<Country> aux1 = ct.getCountries();
                    deleteCountry(comandoArray[4], comandoArray[5], comandoArray[6]);
                    ct.setCountries(aux1);
                }catch (ComandoInvalidoException e){
                    System.out.println(e);
                }
            }else{
                ArrayList<City> aux1= ct.getCities();
                ct.deleteCity(comandoArray[4],comandoArray[5],comandoArray[6]);
                ct.setCities(aux1);
            }
        } else{
            throw new CommandWithOutLogic("The commad doesn`t follow a Logic Syntax");
        }
    }

    public void ordenarPaisesPor(String[] comandoArray) {
        String factorDeOrdenamiento = comandoArray[comandoArray.length-1];
        if(ct.isCountryByString(comandoArray[3])) {
            try {
                ArrayList<Country> aux = ct.busquedaYFIltradoPaises(comandoArray);
                for (int i = 0; i < aux.size(); i++) {
                    System.out.print(aux.get(i).toString());
                }
                System.out.println(ct.arrayPaisYaSort(aux, factorDeOrdenamiento));
            }catch (ComandoInvalidoException e){
                System.out.println(e);
            }
        }else{
            System.out.println("bigie");
            ArrayList<City> aux = ct.busquedaYFIltradoCities(comandoArray);
            for (int i = 0; i < aux.size(); i++) {
                System.out.print(aux.get(i).toString());
            }
            System.out.println(ct.arrayCityYaSort(aux,factorDeOrdenamiento));
        }
    }

    public void saveCountryData() {
        Gson gson = new Gson();
        String json = gson.toJson(countries);
        System.out.print(json);

        try {
            FileOutputStream fos = new FileOutputStream(new File("CountryJson.txt"));
            fos.write( json.getBytes(StandardCharsets.UTF_8) );
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void saveCityData() {
        Gson gson = new Gson();
        String json = gson.toJson(cities);
        System.out.print(json);

        try {
            FileOutputStream fos = new FileOutputStream(new File("CityJson.txt"));
            fos.write(json.getBytes(StandardCharsets.UTF_8));
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
