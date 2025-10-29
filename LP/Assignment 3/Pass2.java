import java.util.*;

public class Pass2 {

    static class Symbol {
        String name;
        int address;
        Symbol(String name, int address) {
            this.name = name;
            this.address = address;
        }
    }

    static class Literal {
        String literal;
        int address;
        Literal(String literal, int address) {
            this.literal = literal;
            this.address = address;
        }
    }

    public static void main(String[] args) {
        List<Symbol> SYMTAB = new ArrayList<>();
        SYMTAB.add(new Symbol("ALPHA", 202));

        List<Literal> LITTAB = new ArrayList<>();
        LITTAB.add(new Literal("='5'", 203));

        List<String> IC = Arrays.asList(
            "(AD,1) (C,200)",
            "(IS,4) (1) (L,1)",
            "(IS,1) (2) (S,1)",
            "(DL,1) (C,2)",
            "(AD,2)"
        );

        int LC = 0;

        for (String line : IC) {
            line = line.trim();

            if (line.startsWith("(AD,")) {
                if (line.startsWith("(AD,1)")) {
                    String[] parts = line.split("[()]");
                    for (String part : parts) {
                        if (part.startsWith("C,")) {
                            LC = Integer.parseInt(part.substring(2));
                        }
                    }
                }
                continue;
            }

            else if (line.startsWith("(IS,")) {
                String[] parts = line.split(" ");
                int opcode = Integer.parseInt(parts[0].substring(4, parts[0].length() - 1));
                int reg = 0;
                if (parts.length > 1 && parts[1].startsWith("(")) {
                    reg = Integer.parseInt(parts[1].substring(1, parts[1].length() - 1));
                }
                int operandAddr = 0;
                if (parts.length > 2) {
                    String op = parts[2];
                    if (op.startsWith("(S,")) {
                        int symIndex = Integer.parseInt(op.substring(3, op.length() - 1));
                        operandAddr = SYMTAB.get(symIndex - 1).address;
                    }
                    else if (op.startsWith("(L,")) {
                        int litIndex = Integer.parseInt(op.substring(3, op.length() - 1));
                        operandAddr = LITTAB.get(litIndex - 1).address;
                    }
                    else if (op.startsWith("(C,")) {
                        operandAddr = Integer.parseInt(op.substring(3, op.length() - 1));
                    }
                }
                System.out.println(LC + " " + opcode + " " + reg + " " + operandAddr);
                LC++;
            }

            else if (line.startsWith("(DL,")) {
                String[] parts = line.split(" ");
                int val = 0;
                for (String part : parts) {
                    if (part.startsWith("(C,")) {
                        val = Integer.parseInt(part.substring(3, part.length() - 1));
                    }
                }
                System.out.println(LC + " 00 0 " + val);
                LC++;
            }
        }
    }
}

