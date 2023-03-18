package Homework3.OrgStructure;

import java.io.File;
import java.io.IOException;

public class OrgParserTest {
    public static void main(String[] args) throws IOException {
        File file = new File("src/Homework3/OrgStructure/structure.csv");
        OrgStructureParserImpl op = new OrgStructureParserImpl();
        System.out.println(op.parseStructure(file));
        System.out.println(op.getStructure());
    }
}
