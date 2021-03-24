// Copyright 2020
// Author: Matei Simtinică

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * In aceasta clasa se identifica familiile ce trebuiesc arestate,
 * astfel incat familiile ramase sa nu prieteneasca. Se va apela
 * metoda sat implementata la task-ul doi.
 *
 */

/**
 * numberOfFamilies - numarul de familii;
 * numberOfRelationships - numarul de relatii intre familii;
 * relationships - tabela de dispersie, in care cheia reprezinta familia,
 * iar vectorul familiile cu care aceasta prieteneste;
 * oracleAnswer - raspunsul Oracolului pentru formula booleana(True/False);
 * numberOfNearestFamilies - numarul familiilor nearestate
 */
public class Task3 extends Task {
    String task2InFilename;
    String task2OutFilename;
    private Integer numberOfFamilies;
    private Integer numberOfRelationships;
    private Map<Integer, ArrayList<Integer>> relationships = new HashMap<>();
    private Scanner oracleAnswer;
    private Integer namberOfNearestFamilies;

    /**
     * In aceasta metoda, se realizeaza mai multe cereri spre Oracol,
     * pana acesta poate solutioana formula booleana pentru a gasi
     * cea mai mare familie extinsa.
     */
    @Override
    public void solve() throws IOException, InterruptedException {
        task2InFilename = inFilename + "_t2";
        task2OutFilename = outFilename + "_t2";
        Task2 task2Solver = new Task2();
        task2Solver.addFiles(task2InFilename, oracleInFilename, oracleOutFilename, task2OutFilename);
        readProblemData();

        String status = "False";
        int size = numberOfFamilies;

        while (status.equals("False")) {
            reduceToTask2(size);
            task2Solver.readProblemData();
            task2Solver.formulateOracleQuestion();
            task2Solver.askOracle();
            task2Solver.decipherOracleAnswer();
            task2Solver.writeAnswer();
            extractAnswerFromTask2();
            status  = oracleAnswer.next();
            size--;
        }

        namberOfNearestFamilies = size + 1;
        writeAnswer();
    }

    /**
     * In acesta metoda se salveaza datele din fisierul de input si se stocheaza
     * in structurile corespunzatoare.
     */
    @Override
    public void readProblemData() throws IOException {
        Scanner scanner = new Scanner(new File(inFilename));
        numberOfFamilies = scanner.nextInt();
        numberOfRelationships = scanner.nextInt();

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

        for (int i = 1; i <= numberOfRelationships; i++) {
            System.out.println(relationships.get(i));
        }
    }

    /**
     * Se scrie fisierul de input pentru task-ul doi, pentru familiile
     * care nu prietenesc, se va considera ca prietenesc si invers, pentru
     * ca atunci cand Oracolul va gasi cea mai mare clica din familii care nu
     * prietenesc va trebui sa eliman familiile ramase.
     *
     * Astfel vor ramane doar familii ce nu prietenesc.
     */
    public void reduceToTask2(int size) throws IOException {
       int count = 0;
        FileWriter myWriter = new FileWriter(task2InFilename);
        for (int i = 1; i <= numberOfFamilies; i++) {
            for (int j = 1; j <= numberOfFamilies; j++) {
                if (!relationships.get(i).contains(j) && i != j) {
                    count++;
                }
            }
        }

        myWriter.write(numberOfFamilies + " " + count + " " + size + "\n");

        for (int i = 1; i <= numberOfFamilies; i++) {
            for (int j = 1; j <= numberOfFamilies; j++) {
                if (!relationships.get(i).contains(j) && i != j) {
                    myWriter.write(i + " " + j  + "\n");
                }
            }
        }
        myWriter.close();
    }

    /**
     * Se extrage raspunsul de la task-ul doi.
     */
    public void extractAnswerFromTask2() throws FileNotFoundException {
        oracleAnswer = new Scanner(new File(task2OutFilename));

    }

    /**
     * Se afiseaza familiile ce trebuiesc arestate.
     */
    @Override
    public void writeAnswer() throws IOException {
        FileWriter myWriter = new FileWriter(outFilename);

        ArrayList<Integer> nearestFamilies = new ArrayList<>();
        int aux;
        for (int i = 1; i <= namberOfNearestFamilies; i++) {
            aux = oracleAnswer.nextInt();

            nearestFamilies.add(aux);

        }

        for (int i = 1; i <= numberOfFamilies; i++) {
            if (!nearestFamilies.contains(i)) {
                myWriter.write(i + " ");
            }
        }
        myWriter.close();
    }
}
