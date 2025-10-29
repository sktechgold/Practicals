import java.util.*;

class Pass1 {
    // Structure for OPTAB
    static class OpInfo {
        String type; // IS, DL, AD
        int opcode;
        OpInfo(String type, int opcode) {
            this.type = type;
            this.opcode = opcode;
        }
    }

    // Symbol Table
    static class Symbol {
        String name;
        int address;
        Symbol(String name, int address) {
            this.name = name;
            this.address = address;
        }
    }

    // Literal Table
    static class Literal {
        String literal;
        int address;
        Literal(String literal, int address) {
            this.literal = literal;
            this.address = address;
        }
    }

    public static void main(String[] args) {
        // -------- OPTAB --------
        Map<String, OpInfo> OPTAB = new HashMap<>();
        OPTAB.put("STOP", new OpInfo("IS", 0));
        OPTAB.put("ADD", new OpInfo("IS", 1));
        OPTAB.put("SUB", new OpInfo("IS", 2));
        OPTAB.put("MULT", new OpInfo("IS", 3));
        OPTAB.put("MOVER", new OpInfo("IS", 4));
        OPTAB.put("MOVEM", new OpInfo("IS", 5));
        OPTAB.put("COMP", new OpInfo("IS", 6));
        OPTAB.put("BC", new OpInfo("IS", 7));
        OPTAB.put("DIV", new OpInfo("IS", 8));
        OPTAB.put("READ", new OpInfo("IS", 9));
        OPTAB.put("PRINT", new OpInfo("IS", 10));

        OPTAB.put("DC", new OpInfo("DL", 1));
        OPTAB.put("DS", new OpInfo("DL", 2));

        OPTAB.put("START", new OpInfo("AD", 1));
        OPTAB.put("END", new OpInfo("AD", 2));
        OPTAB.put("ORIGIN", new OpInfo("AD", 3));
        OPTAB.put("EQU", new OpInfo("AD", 4));
        OPTAB.put("LTORG", new OpInfo("AD", 5));

        // Register Table
        Map<String, Integer> REGTAB = new HashMap<>();
        REGTAB.put("AREG", 1);
        REGTAB.put("BREG", 2);
        REGTAB.put("CREG", 3);
        REGTAB.put("DREG", 4);

        List<Symbol> SYMTAB = new ArrayList<>();
        List<Literal> LITTAB = new ArrayList<>();
        List<Integer> POOLTAB = new ArrayList<>();
        POOLTAB.add(1); // First pool starts at literal #1

        List<String> IC = new ArrayList<>();

        // Input program
        String[] program = {
            "START 200",
            "MOVER AREG, ='5'",
            "ADD BREG, ALPHA",
            "ALPHA DC 2",
            "END"
        };

        int LC = 0; // Location Counter

        for (String line : program) {
            String[] parts = line.trim().split("[ ,]+");
            String label = "", opcode = "", op1 = "", op2 = "";

            // Parse label/opcode/operands
            if (OPTAB.containsKey(parts[0]) || parts[0].startsWith("=") || parts[0].matches("\\d+")) {
                opcode = parts[0];
                if (parts.length > 1) op1 = parts[1];
                if (parts.length > 2) op2 = parts[2];
            } else {
                label = parts[0];
                opcode = parts[1];
                if (parts.length > 2) op1 = parts[2];
                if (parts.length > 3) op2 = parts[3];
            }

            // Label handling
            if (!label.isEmpty()) {
                boolean exists = false;
                for (Symbol s : SYMTAB) {
                    if (s.name.equals(label)) {
                        s.address = LC;
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    SYMTAB.add(new Symbol(label, LC));
                }
            }

            // Opcode handling
            if (OPTAB.containsKey(opcode)) {
                OpInfo info = OPTAB.get(opcode);
                switch (info.type) {
                    case "AD":
                        if (opcode.equals("START")) {
                            LC = Integer.parseInt(op1);
                            IC.add("(AD," + info.opcode + ") (C," + op1 + ")");
                        } else if (opcode.equals("END")) {
                            for (Literal lit : LITTAB) {
    if (lit.address == -1) {
        lit.address = LC;
        LC++;
    }
}
IC.add("(AD," + info.opcode + ")");

                        }
                        break;

                    case "IS":
                        String regCode = "";
                        String operandCode = "";

                        if (!op1.isEmpty() && REGTAB.containsKey(op1)) {
                            regCode = "(" + REGTAB.get(op1) + ")";
                        }

                        if (!op2.isEmpty()) {
                            if (op2.startsWith("=")) {
                                boolean exists = false;
                                int litIndex = -1;
                                for (int i = 0; i < LITTAB.size(); i++) {
                                    if (LITTAB.get(i).literal.equals(op2)) {
                                        exists = true;
                                        litIndex = i + 1;
                                        break;
                                    }
                                }
                                if (!exists) {
                                    LITTAB.add(new Literal(op2, -1));
                                    litIndex = LITTAB.size();
                                }
                                operandCode = "(L," + litIndex + ")";
                            } else {
                                boolean exists = false;
                                int symIndex = -1;
                                for (int i = 0; i < SYMTAB.size(); i++) {
                                    if (SYMTAB.get(i).name.equals(op2)) {
                                        exists = true;
                                        symIndex = i + 1;
                                        break;
                                    }
                                }
                                if (!exists) {
                                    SYMTAB.add(new Symbol(op2, -1));
                                    symIndex = SYMTAB.size();
                                }
                                operandCode = "(S," + symIndex + ")";
                            }
                        }

                        IC.add("(IS," + info.opcode + ") " + regCode + " " + operandCode);
                        LC++;
                        break;

                    case "DL":
                        if (opcode.equals("DC")) {
                            IC.add("(DL," + info.opcode + ") (C," + op1 + ")");
                            LC++;
                        }
                        break;
                }
            }
        }

        // Output results
        System.out.println("INTERMEDIATE CODE:");
        for (String s : IC) {
            System.out.println(s);
        }

        System.out.println("\nSYMTAB:");
        for (int i = 0; i < SYMTAB.size(); i++) {
            System.out.println((i + 1) + "\t" + SYMTAB.get(i).name + "\t" + SYMTAB.get(i).address);
        }

        System.out.println("\nLITTAB:");
        for (int i = 0; i < LITTAB.size(); i++) {
            System.out.println((i + 1) + "\t" + LITTAB.get(i).literal + "\t" + LITTAB.get(i).address);
        }

        System.out.println("\nPOOLTAB:");
        for (int i = 0; i < POOLTAB.size(); i++) {
            System.out.println("#" + (i + 1) + ": starts at literal " + POOLTAB.get(i));
        }
    }
}

