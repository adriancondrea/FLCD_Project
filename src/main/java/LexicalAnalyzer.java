import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LexicalAnalyzer {
    private static final List<String> RESERVED_WORDS = Arrays.asList("startProgram", "input", "declare", "typeCheck",
            "typeDefine", "check", "else", "output", "endProgram", "assign", "loop", "breakLoop", "integer", "boolean",
            "string", "char", "array");
    private static final List<String> OPERATORS = Arrays.asList("+", "-", "*", "/", "%", "<=", ">=", "==", "!=", "!",
            "<", ">");
    private static final List<String> SEPARATORS = Arrays.asList(" ", ",", "\"", "'", ".", "\n", "[", "]", "(", ")", "<", ">");
    private final List<Pair<String, Integer>> programInternalForm = new ArrayList<>();
    private SymbolTable symbolTable;

    public LexicalAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public void scan(String filename) {
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                processLine(line);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void processLine(String line) {
        String[] tokens = line.trim().split(" |\t");
        for (String token : tokens) {
            if (!processToken(token)) {
                String[] subtokens = token.split("((?=(<=)|(>=)|(==)|(!=))|(?<=(<=)|(>=)|(==)|(!=)))");
                for (String subtoken : subtokens) {
                    if (!processToken(subtoken)) {
                        String[] subsubtokens = token.split("((?=\\+|-|\\*|/|%|!|<|>|\\[|]|\\(|\\)|,|\"|'|\\.)|(?<=\\+|-|\\*|/|%|!|<|>|\\[|]|\\(|\\)|,|\"|'|\\.))");
                        for (String subsubtoken : subsubtokens) {
                            if (!processToken(subsubtoken)) {
                                System.out.println("Lexical error: " + subsubtoken);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean processToken(String token) {
        if (RESERVED_WORDS.contains(token) || OPERATORS.contains(token) || SEPARATORS.contains(token)) {
            addToProgramInternalForm(token, 0);
            return true;
        } else if (isIdentifier(token) || isConstant(token)) {
            int index = symbolTable.addElement(token);
            addToProgramInternalForm(token, index);
            return true;
        }
        return false;
    }

    private boolean isConstant(String token) {
        return isNumberConstant(token) || isBooleanConstant(token) || isCharConstant(token) || isStringConstant(token) || isArrayConstant(token);
    }

    private boolean isNumberConstant(String token) {
        if (token.length() == 0) {
            return false;
        }
        for (int i = 0; i < token.length(); i++) {
            if (!Character.isDigit(token.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isArrayConstant(String token) {
        //TODO: implement array constant
        return false;
    }

    private boolean isStringConstant(String token) {
        if (token.length() < 2) {
            return false;
        }
        if (token.charAt(0) != '"' || token.charAt(token.length() - 1) != '"') {
            return false;
        }
        for (int i = 1; i < token.length() - 1; i++) {
            char c = token.charAt(i);
            if (!Character.isLetter(c) && !Character.isDigit(c) && c != '_' && c != ' ') {
                return false;
            }
        }
        return true;
    }

    private boolean isCharConstant(String token) {
        if (token.length() != 3) {
            return false;
        }
        if (token.charAt(0) != '\'' && token.charAt(2) != '\'') {
            return false;
        }
        char c = token.charAt(1);
        return Character.isLetter(c) || Character.isDigit(c) || c == '_' || c == ' ';
    }

    private boolean isBooleanConstant(String token) {
        return token.equals("true") || token.equals("false");
    }

    private boolean isIdentifier(String token) {
        if(token.length() == 0){
            return false;
        }
        if (!Character.isLetter(token.charAt(0))) {
            return false;
        }
        for (int i = 1; i < token.length(); i++) {
            char c = token.charAt(i);
            if (!Character.isLetter(c) && !Character.isDigit(c) && c != '_') {
                return false;
            }
        }
        return true;
    }

    private void addToProgramInternalForm(String token, int symbolTablePosition) {
        programInternalForm.add(new Pair<>(token, symbolTablePosition));
    }

    private static class Pair<T1, T2> {
        private T1 first;
        private T2 second;

        public Pair(T1 first, T2 second) {
            this.first = first;
            this.second = second;
        }

        public T1 getFirst() {
            return this.first;
        }

        public void setFirst(T1 first) {
            this.first = first;
        }

        public T2 getSecond() {
            return this.second;
        }

        public void setSecond(T2 second) {
            this.second = second;
        }
    }
}
