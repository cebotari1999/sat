// Copyright 2020
// Author: Matei Simtinică

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
/**
 * In aceasta clasa se realizeaza repartizarea spionilor, datele problemei
 * find ajustate la o reducere de tip SAT, astfel se creiaza formula booleana,
 * pe care o transmitem Oracolului, apoi se interpreteaza raspunsul acestuia.
 *
 */

/**
 * numberOfFamilies - numarul de familii;
 * numberOfRelationships - numarul de relatii intre familii;
 * numberOfSpies - numarul de spioni;
 * relationships - tabela de dispersie, in care cheia reprezinta familia,
 * iar vectorul familiile cu care aceasta prieteneste;
 * distributionOfSpies - distributia spionilor;
 * oracleAnswer - raspunsul Oracolului pentru formula booleana(True/False);
 */
public class Task1 extends Task {

    private Integer numberOfFamilies;
    private Integer numberOfRelationships;
    private Integer numberOfSpies;
    private Map<Integer, ArrayList<Integer>> relationships = new HashMap<>();
    private ArrayList<Integer> distributionOfSpies = new ArrayList<>();
    private String oracleAnswer;


    @Override
    public void solve() throws IOException, InterruptedException {
        readProblemData();
        formulateOracleQuestion();
        askOracle();
        decipherOracleAnswer();
        writeAnswer();
    }

    /**
     * In aceasta metoda se citesc datele din fisierul de input(inFilename) si se
     * stocheaza.
     *
     * Intr-o structura de tip Map se retin relatiile dintre familii. Familiile fiind
     * numerotate de la 1 pana la numberOfFamilies, numarul lor va reprezenta cheia.
     * Vectorul corespunzator acestei chei, va contine numerele tuturor familiilor
     * cu care aceastea prietenesc.
     */
    @Override
    public void readProblemData() throws IOException {
        Scanner scanner = new Scanner(new File(inFilename));

        numberOfFamilies = scanner.nextInt();
        numberOfRelationships = scanner.nextInt();
        numberOfSpies = scanner.nextInt();

        for (int i = 1; i <= numberOfFamilies; i++) {
            relationships.put(i, new ArrayList<>());
        }

        for (int i = 0; i < numberOfRelationships; i++) {
            int family1 = scanner.nextInt();
            int family2 = scanner.nextInt();
            relationships.get(family1).add(family2);
        }
    }

    /**
     * In aceasta metoda se realizeaza reducerea SAT si in paralel se formateaza
     * fisierul ce urmeaza transmis Oracolului.
     *
     * Astfel se ia in calcul conditia, ca fiecare familie trebuie sa aiba,
     * cel putin un spion. Pentru ca ficare familie poate avea numberOfSpies de spioni,
     * clausele fiecarei familii vor contine numberOfSpies variabile. Position este
     * folosit pentru a itera printre aceste variabile.
     *
     * A doua conditie, este ca doua familii care prietenesc, sa nu fie infiltrate
     * de acelasi spion. Se ia fiecare familie, pentru familiile cu care aceasta
     * prieteneste  se neaga perechile de variabile corespunzatoare fiecarui spion.
     *
     */
    @Override
    public void formulateOracleQuestion() throws IOException {
        int position, family;

        FileWriter myWriter = new FileWriter(oracleInFilename);
        myWriter.write("p cnf " + numberOfFamilies * numberOfSpies + " "
                        + (numberOfFamilies + numberOfRelationships * numberOfSpies) + "\n");

        position = 0;

        for (int i = 1; i <= numberOfFamilies; i++) {
            for (int j = 0; j < numberOfSpies; j++) {
                myWriter.write((i + position + j) + " ");
            }
            position = position + numberOfSpies - 1;
            myWriter.write("0\n");
        }

        for (int i = 1; i <= numberOfFamilies; i++) {
            position = 1;
            family =  1 + (i - 1) * numberOfSpies;

            for (int j = 1; j <= numberOfFamilies; j++) {
                if (relationships.get(i).contains(j)) {
                    for (int k = 0; k < numberOfSpies; k++) {
                        myWriter.write("-" + (family + k) + " -" + (position + k) + " 0\n");
                    }
                }
                position = position + numberOfSpies;
            }
        }

        myWriter.close();
    }

    /**
     * Se interpreteaza fisierul de output primit de la Oracol.
     *
     * Se salveaza rezultatul obtinut pentru formula booleana. Daca formula poate fi rezolvata
     * atunci parcurgem sirul de variabile oferite in intervale egale cu numarul de spioni.
     *
     * Al catalea interval este la rand reprezinta familia, iar pozita variabilei pozitive pe
     * intervalul 1 - numberOfSpies reprezinta numarul spionului asignat pentru
     * familia respectiva.
     */
    @Override
    public void decipherOracleAnswer() throws IOException {
        int aux;
        Scanner scanner = new Scanner(new File(oracleOutFilename));
        oracleAnswer = scanner.next();

        if (oracleAnswer.equals("True")) {
            scanner.nextInt();
            for (int i = 1; i <= numberOfFamilies; i++) {
                for (int j = 1; j <= numberOfSpies; j++) {
                    aux = scanner.nextInt();
                    if (aux > 0) {
                        distributionOfSpies.add(j);
                    }
                }

            }
        }
    }

    /**
     * Se redacteaza fisierul de output astfel: se afiseaza raspunsul Oracolului,
     * daca acesta este adevarat, atunci se reprezinta si repartizarea spionilor
     * pe familii.
     */
    @Override
    public void writeAnswer() throws IOException {
        FileWriter writer = new FileWriter(outFilename);
        writer.write(oracleAnswer + "\n");

        if (oracleAnswer.equals("True")) {
            for (int i = 0; i < distributionOfSpies.size(); i++) {
                writer.write(distributionOfSpies.get(i) + " ");
            }
            writer.write("\n");
        }
        writer.close();
    }
}
