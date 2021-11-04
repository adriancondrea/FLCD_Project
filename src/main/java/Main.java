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
            String symbolTableDocumentation = "The symbol table is implemented using hash table as data structure.\nIt has two fields: the size and an array of Strings.\nEvery key is added in the hash table based on the hash value obtained from the hash function.\nFor the hash function, I have used the sum of ascii characters % size (the basic hash function from the course)\n";
            writer.write(String.format("%s\n%s", symbolTableDocumentation, symbolTable.toString()));

            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e);
        }
    }
}
