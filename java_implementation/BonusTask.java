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
 * In aceasta clasa se identifica familiile ce trebuiesc arestate
 * folosind o reducere partiala Max-SAT cu clauze soft si hard.
 *
 */

/**
 * numberOfFamilies - numarul de familii;
 * numberOfRelationships - numarul de relatii intre familii;
 * relationships - tabela de dispersie, in care cheia reprezinta familia,
 * iar vectorul familiile cu care aceasta prieteneste;
 * distributionOfSpies - distributia spionilor;
 * arrestedFamilies - familiile ce trebuiesc arestate;
 */
public class BonusTask extends Task {
    private Integer numberOfFamilies;
    private Integer numberOfRelationships;
    private Map<Integer, ArrayList<Integer>> relationships = new HashMap<>();
    private ArrayList<Integer> arrestedFamilies = new ArrayList<>();

    @Override
    public void solve() throws IOException, InterruptedException {
        readProblemData();
        formulateOracleQuestion();
        askOracle();
        decipherOracleAnswer();
        writeAnswer();
    }

    /**
     * Se stocheaza datele din fisierul de input.
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
        }
    }

    /**
     * Se face reducerea partiala Max-SAT. Clauzele soft au ponderea unu
     * si reprezinta clauzele in care fiecare variabila ce reprezinta o familie
     * ar trebui sa aiba valoarea true. Clauzele hard sunt pentru famiile ce prietenesc,
     * astfel sunt perechi de variabile negate ce reprezinta legatura dintre doua familii,
     * cu ponderea egala cu 4.
     */
    @Override
    public void formulateOracleQuestion() throws IOException {
        FileWriter myWriter = new FileWriter(oracleInFilename);
        myWriter.write("p wcnf " + numberOfFamilies + " " + (numberOfFamilies
                                     + numberOfRelationships) + " " + (3) + "\n");

        for (int j = 1; j <= numberOfFamilies; j++) {
            myWriter.write((1) + " " + j + " 0\n");
        }

        for (int i = 1; i <= numberOfFamilies; i++) {
            for (int j = 1; j <= numberOfFamilies; j++) {
                if (relationships.get(i).contains(j) && i != j) {
                    myWriter.write((4) + " -" + (i) + " -" + (j) + " 0\n");

                }
            }
        }

        myWriter.close();
    }

    /**
     * Se interpreteaza raspunsul oracolului.
     */
    @Override
    public void decipherOracleAnswer() throws IOException {
        int aux;

        Scanner output = new Scanner(new File(oracleOutFilename));
        output.nextInt();
        output.nextInt();

        while (output.hasNextInt()) {
            aux = output.nextInt();
            if (aux < 0) {
                arrestedFamilies.add(aux * (-1));
            }
        }
    }

    /**
     * Se afiseaza familiile ce trebuiesc arestate.
     */
    @Override
    public void writeAnswer() throws IOException {
        FileWriter writer = new FileWriter(outFilename);

        for (int i = 0; i < arrestedFamilies.size(); i++) {
            writer.write(arrestedFamilies.get(i) + " ");
        }

        writer.close();
    }
}