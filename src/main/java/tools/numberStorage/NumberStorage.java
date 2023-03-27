package tools.numberStorage;

import tools.data.Call;
import tools.data.PhoneNumber;

import java.util.*;

public class NumberStorage implements StoreNumber {
    private final Map<PhoneNumber, List<Call>> storage;

    public NumberStorage() {
        storage = new HashMap<>();
    }

    @Override
    public boolean containsNumber(PhoneNumber number) {
        return storage.containsKey(number);
    }

    @Override
    public void putNumber(PhoneNumber number, List<Call> calls) {
        storage.put(number, calls);
    }

    @Override
    public void addCall(PhoneNumber number, Call call) {
        List<Call> calls = storage.get(number);
        calls.add(call);
        storage.put(number, calls);
    }

    @Override
    public Set<PhoneNumber> getNumbers() {
        return storage.keySet();
    }

    @Override
    public List<Call> getCalls(PhoneNumber number) {
        return storage.get(number);
    }
}
