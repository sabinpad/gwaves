package main;

import checker.CheckerConstants;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
// import java.util.Scanner;

/**
 * Use this if you want to test on a specific input file
 */
public final class Test {
    /**
     * for coding style
     */
    private Test() {
    }

    /**
     * @param args input files
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        File[] inputDir = directory.listFiles();

        if (inputDir != null) {
            Arrays.sort(inputDir);

            // Scanner scanner = new Scanner(System.in);
            // String fileName = scanner.next();
            // String fileName = "test00_etapa3_wrapped_one_user_one_artist.json";
            // String fileName = "test01_etapa3_wrapped_one_user_n_artist.json";
            // String fileName = "test02_etapa3_wrapped_n_user_one_artist.json";
            // String fileName = "test04_etapa3_monetization_premium.json";
            // String fileName = "test05_etapa3_monetization_free.json";
            // String fileName = "test06_etapa3_monetization_all.json";
            // String fileName = "test07_etapa3_notifications_simple.json";
            // String fileName = "test08_etapa3_notifications_more.json";
            // String fileName = "test09_etapa3_merch_buy.json";
            // String fileName = "test11_etapa3_basicPageNavigation.json";
            // String fileName = "test12_etapa3_recommendations.json";
            // String fileName = "test13_etapa3_basic_recommendation.json";
            // String fileName = "test14_etapa3_page_navigation.json";
            String fileName = "test15_etapa3_complex.json";
            for (File file : inputDir) {
                if (file.getName().equalsIgnoreCase(fileName)) {
                    // Main.action(file.getAbsolutePath(), CheckerConstants.OUT_FILE);
                    Main.action(file.getName(), CheckerConstants.OUT_FILE);
                    break;
                }
            }

            // scanner.close();
        }
    }
}
