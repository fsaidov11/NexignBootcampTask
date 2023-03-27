import applicationManager.ApplicationManager;
import billCreator.CreateBill;
import billCreator.BillCreator;
import billPrinter.PrintBill;
import billPrinter.BillPrinter;
import builders.callBuilder.BuildCall;
import builders.callBuilder.CallBuilder;
import fileReader.CDRFileReader;
import fileReader.FileReader;
import builders.phoneNumberBuilder.BuildPhoneNumber;
import builders.phoneNumberBuilder.PhoneNumberBuilder;
import tools.numberStorage.StoreNumber;
import tools.numberStorage.NumberStorage;

import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(new Locale("US"));
        FileReader reader = new CDRFileReader("cdr.txt");
        BuildCall buildCall = new CallBuilder();
        BuildPhoneNumber numberBuilder = new PhoneNumberBuilder();
        StoreNumber numberStorage = new NumberStorage();
        PrintBill billPrinter = new BillPrinter();
        CreateBill billCreator = new BillCreator(billPrinter);
        ApplicationManager manager = new ApplicationManager(reader, buildCall, numberBuilder, numberStorage, billCreator);
        manager.manage();
    }
}
