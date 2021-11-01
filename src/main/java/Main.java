import utils.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Formatter;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable(10);
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(symbolTable);
        lexicalAnalyzer.scan("input/p1err.txt");
        writeSymbolTableToFile(symbolTable, "ST.out");
        List<Pair<String, Integer>> programInternalForm = lexicalAnalyzer.getProgramInternalForm();
        writeProgramInternalFormToFile(programInternalForm, "PIF.out");
    }

    private static void writeProgramInternalFormToFile(List<Pair<String, Integer>> programInternalForm, String filename) {
        Formatter formatter = new Formatter();
        formatter.format("%-32s%-32s\n", "Token:", "ST position:");
        programInternalForm.forEach(pair -> formatter.format("%-32s%-32s\n", pair.getFirst(), pair.getSecond()));

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(formatter.toString());

            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e);
        }
    }

    private static void writeSymbolTableToFile(SymbolTable symbolTable, String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(symbolTable.toString());

            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e);
        }
    }
}
