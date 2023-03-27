package applicationManager;

import billCreator.CreateBill;
import builders.callBuilder.BuildCall;
import builders.phoneNumberBuilder.BuildPhoneNumber;
import fileReader.FileReader;
import tools.data.Call;
import tools.data.PhoneNumber;
import tools.numberStorage.StoreNumber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationManager {
    private final FileReader reader;
    private final BuildCall buildCall;
    private final BuildPhoneNumber numberBuilder;
    private final StoreNumber numberStorage;
    private final CreateBill billCreator;

    public ApplicationManager(FileReader reader, BuildCall buildCall, BuildPhoneNumber numberBuilder, StoreNumber numberStorage, CreateBill billCreator) {
        this.reader = reader;
        this.buildCall = buildCall;
        this.numberBuilder = numberBuilder;
        this.numberStorage = numberStorage;
        this.billCreator = billCreator;
    }

    public void manage() {
        if (!reader.init()) return;
        String buffer;
        try {
            while ((buffer = reader.nextLine()) != null) {
                String[] data = buffer.trim().split(", ");
                PhoneNumber number = numberBuilder.createPhoneNumber(data);
                Call call = buildCall.createCall(data);
                if (numberStorage.containsNumber(number)) numberStorage.addCall(number, call);
                else {
                    List<Call> calls = new ArrayList<>();
                    calls.add(call);
                    numberStorage.putNumber(number, calls);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error occurred while reading or closing a CDR file.");
            return;
        }
        billCreator.createBills(numberStorage);
    }
}
