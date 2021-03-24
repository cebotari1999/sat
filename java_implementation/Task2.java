// Copyright 2020
// Author: Matei Simtinică

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * In aceasta clasa se cauta o familie extinsa, datele problemei
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
 * extendedFamily - contine membrii unei familii extinse, daca aceasta exista;
 * oracleAnswer - raspunsul Oracolului pentru formula booleana(True/False);
 */
public class Task2 extends Task {
    private Integer numberOfFamilies;
    private Integer numberOfRelationships;
    private Integer sizeOfClique;
    private Map<Integer, ArrayList<Integer>> relationships = new HashMap<>();
    private ArrayList<Integer> extendedFamily = new ArrayList<>();
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
     * cu care aceastea prietenesc. Aceasta simuleaza o matrice de adiacenta.
     *
     * De asemnea se salveaza, numarul de relatii intre familii si dimensiunea
     * familiei extinse pe care o cautam.
     */
    @Override
    public void readProblemData() throws IOException {
        Scanner scanner = new Scanner(new File(inFilename));
        numberOfFamilies = scanner.nextInt();
        numberOfRelationships = scanner.nextInt();
        sizeOfClique = scanner.nextInt();

        for (int i = 1; i <= numberOfFamilies; i++) {
            relationships.put(i, new ArrayList<>());
        }

        for (int i = 0; i < numberOfRelationships; i++) {
            int family1 = scanner.nextInt();
            int family2 = scanner.nextInt();
            relationships.get(family1).add(family2);

            if (!relationships.get(family2).contains(family1)) {
                relationships.get(family2).add(family1);
            }
        }
    }

    /**
     * In aceasta metoda se realizeaza reducerea SAT potrivita problemei
     * si in paralel se face scrierea formulei booleane ce urmeaza sa fie
     * transmisa interpretorului.
     *
     * Sa ne imaginam un graf, in care o sa cautam o clica, aceasta find familia
     * extinsa.
     *
     * Se ia in calcul conditia care spune ca fiecare familie va fi reprezentata
     * in graful nostru, de un numar de noduri egal cu dimensiunea clicai pe care
     * o cautam. Cream cate o clauza cu variabilele care sunt atribuite pentru primul
     * nod al fiecarei familii, astfel procedam pentru sizeOfClique * numberOfFamilies
     * noduri.
     *
     * A doua conditie este ca intre nodurile care reprezinta aceiasi familie,
     * nu poate exista legatura. Astfel clauzele vor fi perechi de variabile
     * negate, ce reprezinta nodurile unei familii.
     *
     * Si a treia conditie este pentru familiile ce nu prietenesc. Astfel in graful
     * simulat intre nodurile care reprezinta aceste familii nu exita muchii.
     * Cream clauze cu perechi de variabile negate, o sa procedam astfel pentru toate
     * variabilele care sunt atribuite nodurilor celor doua familii.
     *
     */
    @Override
    public void formulateOracleQuestion() throws IOException {
        int position, family;
        int numberOfClauses = 0;
        FileWriter writer = new FileWriter(oracleInFilename);

        for (int i = 1; i <= numberOfFamilies; i++) {
            for (int j = 1; j <= numberOfFamilies; j++) {
                if (!relationships.get(i).contains(j) && i != j) {
                    numberOfClauses++;
                }
            }
        }

        numberOfClauses *= sizeOfClique * sizeOfClique;
        numberOfClauses += sizeOfClique * (1 + numberOfFamilies);
        writer.write("p cnf " + (numberOfFamilies * sizeOfClique) + " " + numberOfClauses + "\n");

        position = 1;

        for (int i = 1; i <= sizeOfClique; i++) {
            for (int j = 1; j <= numberOfFamilies; j++) {
                writer.write(position + " ");
                position++;
            }
            writer.write("0\n");
        }

        for (int i = 1; i <= numberOfFamilies; i++) {
            position = 1;
            family = 0;
            while (position < sizeOfClique) {
                for (int j = position; j < sizeOfClique; j++) {
                    writer.write("-" + (i + family) + " -" + (i + j * numberOfFamilies) + " 0\n");

                }
                family += numberOfFamilies;
                position++;
            }
        }

        for (int i = 1; i <= numberOfFamilies; i++) {
            for (int j = 1; j <= numberOfFamilies; j++) {
                if (!relationships.get(i).contains(j) && i != j) {
                    position = 0;
                    for (int k = 1; k <= sizeOfClique; k++) {
                        family = 0;
                        while (family < numberOfFamilies * sizeOfClique) {
                            writer.write("-" + (i + position) + " -" + (j + family) + " 0\n");
                            family = family + numberOfFamilies;
                        }
                        position = position + numberOfFamilies;
                    }
                }
            }
        }

        writer.close();
    }

    /**
     * Se salveaza solutia formulei booleane, daca aceasta este egala cu unu,
     * se interpreteaza sirul de variabile primit. Fiecare numar pozitiv, arata
     * ca o familie face parte din familieaextinsa. Iar pozitia acestuia pe un
     * interval de numberOfFamilies reprezinta numarul familiei.
     */

    @Override
    public void decipherOracleAnswer() throws IOException {
        int aux ;
        Scanner output = new Scanner(new File(oracleOutFilename));
        oracleAnswer = output.next();

        if (oracleAnswer.equals("True")) {
            output.nextInt();
            for (int i = 1; i <= sizeOfClique; i++) {
                for (int j = 1; j <= numberOfFamilies; j++) {
                    aux = output.nextInt();
                    if (aux > 0) {
                        extendedFamily.add(j);
                    }
                }
            }
        }
    }

    /**
     * Se redacteaza fisierul de output astfel: se afiseaza raspunsul Oracolului,
     * daca acesta este adevarat, atunci se afiseaza si numerele ce reprezinta
     * familiile care formeaza o familie extinsa.
     */
    @Override
    public void writeAnswer() throws IOException {
        FileWriter writer = new FileWriter(outFilename);
        writer.write(oracleAnswer + "\n");

        if (oracleAnswer.equals("True")) {
            for (int i = 0; i < extendedFamily.size(); i++) {
                writer.write(extendedFamily.get(i) + " ");
            }
            writer.write("\n");
        }

        writer.close();
    }
}
