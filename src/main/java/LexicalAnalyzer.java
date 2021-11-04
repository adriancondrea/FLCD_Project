import utils.Pair;

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
    private boolean error;


    public LexicalAnalyzer(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        this.error = false;
    }

    public void scan(String filepath) {
        try {
            File myFile = new File(filepath);
            Scanner myReader = new Scanner(myFile);
            int lineNumber = 0;
            while (myReader.hasNextLine()) {
                lineNumber++;
                String line = myReader.nextLine();
                processLine(line, lineNumber);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred. File " + filepath + " not found!");
            e.printStackTrace();
        }
        if (!error) {
            System.out.println("Lexical analyzer has found no errors!");
        }
    }

    private void processLine(String line, int lineNumber) {
        // remove unnecessary whitespace
        line = line.trim();
        // split line by ( and ), adding the separators to the tokens array also
        String[] tokens = line.split("((?=\\(|\\))|(?<=\\(|\\)))");
        for (String token : tokens) {
            // if it is not a valid token, further break it down
            if (!processToken(token)) {
                // split token by <=, >=, ==, != - the compound tokens and ,
                String[] subtokens = token.split("((?=(<=)|(>=)|(==)|(!=)|,)|(?<=(<=)|(>=)|(==)|(!=)|,))");
                for (String subtoken : subtokens) {
                    // remove white space
                    subtoken = subtoken.trim();
                    // if it is not a valid token, further break it down
                    if (!processToken(subtoken)) {
                        // split by +,-,*,/,%,!,<,>,[,],,,.
                        String[] subsubtokens = subtoken.split("((?=\\+|-|\\*|/|%|!|<|>|\\[|]|\\.)|(?<=\\+|-|\\*|/|%|!|<|>|\\[|]|\\.))");
                        for (String subsubtoken : subsubtokens) {
                            subsubtoken = subsubtoken.trim();
                            if (!processToken(subsubtoken)) {
                                System.out.println("Lexical error on line " + lineNumber + ", token: " + subsubtoken);
                                this.error = true;
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean processToken(String token) {
        if (token.length() == 0) {
            return true;
        }
        if (RESERVED_WORDS.contains(token) || OPERATORS.contains(token) || SEPARATORS.contains(token)) {
            addToProgramInternalForm(token, -1);
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
        String regex = "^(-?[1-9][0-9]*)|([0]+)$";
        return token.matches(regex);
    }

    private boolean isArrayConstant(String token) {
        String regex = "^([.*])$";
        if (!token.matches(regex)) {
            return false;
        }

        // remove [ and ], in order to expose the elements separated by comma
        token = token.substring(1, token.length() - 1);
        // get the elements
        String[] subtokens = token.split(",");
        for (String subtoken : subtokens) {
            subtoken = subtoken.trim();
            if (!isIdentifier(subtoken) && !isConstant(subtoken)) {
                return false;
            }
        }
        return true;
    }

    private boolean isStringConstant(String token) {
        // a string constant must have at least 2 characters: opening and closing "
        String regex = "^(\".*\")$";
        return token.matches(regex);
    }

    private boolean isCharConstant(String token) {
        String regex = "^('[a-zA-Z0-9_ ]')$";
        return token.matches(regex);
    }

    private boolean isBooleanConstant(String token) {
        String regex = "^(\"(true)|(false)\")$";
        return token.matches(regex);
    }

    private boolean isIdentifier(String token) {
        String regex = "^([a-zA-Z]+[a-zA-Z0-9_]*)$";
        return token.matches(regex);
    }

    private void addToProgramInternalForm(String token, int symbolTablePosition) {
        programInternalForm.add(new Pair<>(token, symbolTablePosition));
    }

    public List<Pair<String, Integer>> getProgramInternalForm() {
        return this.programInternalForm;
    }
}
