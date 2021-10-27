import java.util.List;

public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable(100);
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(symbolTable);
        lexicalAnalyzer.scan("p2.txt");
    }
}
