public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable(5);
        symbolTable.addElement("aba");
        symbolTable.addElement("aab");
        symbolTable.addElement("2");
        symbolTable.addElement("a");
        System.out.println(symbolTable);
    }
}
