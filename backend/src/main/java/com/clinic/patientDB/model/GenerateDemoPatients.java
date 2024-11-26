package com.clinic.patientDB.model;


import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class GenerateDemoPatients {

    private Set<String> filenames = new HashSet<String>();


    private static final String[] FIRST_NAMES = {
            "אורן", "ליאור", "נועה", "תומר", "דנה", "יונתן", "אביב", "שיר",
            "אלון", "איילת", "רון", "שני", "הילה", "אדם", "תמר", "אליאב",
            "מור", "עדי", "אופיר", "איתן", "רוני", "מעיין", "דניאל", "ניר",
            "אופק", "יעל", "יהונתן", "מיכל", "גל", "ענת", "דביר", "שרית",
            "בר", "עמית", "ליה", "יובל", "כרמל", "מאיה", "רז", "אפרת"
    };

    private static final String[] LAST_NAMES = {
            "כהן", "לוי", "מזרחי", "פרידמן", "גולדשטיין", "רבינוביץ'", "קפלן",
            "בלום", "אברהם", "שטרן", "ברק", "אילון", "סגל", "בן-דוד",
            "פרץ", "חזן", "זילברמן", "אוחנה", "כץ", "פישר", "הרשקוביץ",
            "פלדמן", "גרינברג", "רוזן", "מילר", "שפירא", "בן-ציון",
            "דקל", "שחם", "שמיר"
    };


    public static String generateRandomId(Random random) {
        String id = "_";

        for (int i = 0; i < 9; i++) {
            id += Integer.toString(random.nextInt(9));
        }
        return id;
    }

    public static String generateRandomDate(Random random) {
        Integer[] dates = {random.nextInt(30)+1, random.nextInt(11)+1,random.nextInt(26)};
        String date = "";
        for (Integer d : dates){
            String temp = d.toString();
            if (temp.length() == 1){
                temp = "0" + temp;
            }
            date += temp;
        }
        return date;
    }

    public static String generateRandomFullName(Random random) {
        int numOfNames = random.nextInt(2) + 1;


        String name = "_" + LAST_NAMES[random.nextInt(LAST_NAMES.length)];

        for (int i = 1; i <= numOfNames; i++) {
            name += "_" + FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        }
        return name;
    }

    public void setFilenames(Set<String> filenames) {
        this.filenames = filenames;
    }
    public void addFilenames(String filename) {
        this.filenames.add(filename);
    }

    public Set<String> getFilenames() {
        return filenames;
    }

    public String generateDemoFileName() throws IOException {
        String filename = "/Users/lilach/StudioProjects/patientDB/filenames.txt";
//        FileWriter fileWriter = new FileWriter();
        PrintWriter printWriter = new PrintWriter(filename, "UTF-8");
        int numOfPatients=10;
        String txt = "";
        Random random = new Random();
        for (int i = 0; i < numOfPatients; i++) {
            String name = generateRandomDate(random) + generateRandomFullName(random) + generateRandomId(random) + ".doc";
            addFilenames(name);
            System.out.println(name);
            txt += name + "\n";

            printWriter.println(name);
        }
        printWriter.close();
        return txt;
    }
}
