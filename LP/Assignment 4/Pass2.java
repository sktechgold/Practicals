import java.util.*;

class Pass2 {

    static class MNTEntry {
        String name;
        int mdtIndex;

        public MNTEntry(String name, int mdtIndex) {
            this.name = name;
            this.mdtIndex = mdtIndex;
        }
    }

    static class Pass2MacroProcessor {

        List<MNTEntry> MNT;
        List<String> MDT;
        List<String> intermediateCode;
        List<Map<String, String>> ALA;

        public Pass2MacroProcessor(List<MNTEntry> MNT, List<String> MDT, List<String> IC, List<Map<String, String>> ALA) {
            this.MNT = MNT;
            this.MDT = MDT;
            this.intermediateCode = IC;
            this.ALA = ALA;
        }

        public void expandCode() {
            // Show Before/After first
            for (int m = 0; m < MNT.size(); m++) {
                Map<String, String> ala = ALA.get(m);
                for (Map.Entry<String, String> entry : ala.entrySet()) {
                    String formal = entry.getKey();   // &X
                    String positional = entry.getValue(); // #0
                    // Find actual argument from intermediate code
                    for (String line : intermediateCode) {
                        String[] parts = line.split("\\s+");
                        if (parts[0].equals(MNT.get(m).name)) {
                            String[] args = Arrays.copyOfRange(parts, 1, parts.length);
                            int index = Integer.parseInt(positional.substring(1));
                            if (index < args.length) {
                                System.out.println("Before: " + formal);
                                System.out.println("After : " + args[index]);
                            }
                        }
                    }
                }
            }

            System.out.println();
            System.out.println("Final Expanded Code:");

            for (String line : intermediateCode) {
                String[] parts = line.split("\\s+");
                String op = parts[0];

                MNTEntry macro = findMacro(op);
                if (macro != null) {
                    String[] args = Arrays.copyOfRange(parts, 1, parts.length);

                    for (int i = macro.mdtIndex - 1; i < MDT.size(); i++) {
                        String mdtLine = MDT.get(i);
                        if (mdtLine.equals("MEND")) break;
                        String expanded = mdtLine;
                        for (int a = 0; a < args.length; a++) {
                            expanded = expanded.replace("#" + a, args[a]);
                        }
                        System.out.println(expanded);
                    }
                } else {
                    System.out.println(line);
                }
            }
        }

        private MNTEntry findMacro(String name) {
            for (MNTEntry entry : MNT) {
                if (entry.name.equals(name)) {
                    return entry;
                }
            }
            return null;
        }
    }

    public static void main(String[] args) {
        List<MNTEntry> MNT = new ArrayList<>();
        MNT.add(new MNTEntry("INCR", 1));

        List<String> MDT = new ArrayList<>();
        MDT.add("MOVER AREG,#0");
        MDT.add("ADD AREG,ONE");
        MDT.add("MOVEM AREG,#0");
        MDT.add("MEND");

        List<String> IC = Arrays.asList(
            "START 100",
            "READ ALPHA",
            "INCR ALPHA",
            "PRINT ALPHA",
            "END"
        );

        // ALA from Pass1 (formal → positional)
        List<Map<String, String>> ALA = new ArrayList<>();
        Map<String, String> ala1 = new LinkedHashMap<>();
        ala1.put("&X", "#0");
        ALA.add(ala1);

        Pass2MacroProcessor pass2 = new Pass2MacroProcessor(MNT, MDT, IC, ALA);
        pass2.expandCode();
    }
      }
      
