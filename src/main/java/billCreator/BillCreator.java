package billCreator;

import billPrinter.PrintBill;
import tools.data.Call;
import tools.data.PhoneNumber;
import tools.numberStorage.StoreNumber;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class BillCreator implements CreateBill {
    private final String LINE = "----------------------------------------------------------------------------\n";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" yyyy-MM-dd HH:mm:ss ");
    private final PrintBill billPrinter;

    public BillCreator(PrintBill billPrinter) {
        this.billPrinter = billPrinter;
    }

    @Override
    public void createBills(StoreNumber storage) {
        for (PhoneNumber i : storage.getNumbers()) {
            StringBuilder bill = new StringBuilder();
            bill.append(createHeader(i));
            List<Call> calls = storage.getCalls(i);
            Collections.sort(calls);
            double price = 0;
            for (Call j : calls) {
                bill.append(createLine(i, j));
                price += j.getCost();
            }
            if (i.getTariff() == 6) price += 100;
            bill.append(createFooter(price));
            billPrinter.printBill(i.getNumber(), bill.toString());
        }
    }

    private String createHeader(PhoneNumber i) {
        String header = "";
        header += "Tariff index: " + String.format("%02d", i.getTariff()) + "\n";
        header += LINE;
        header += "Report for phone number " + i.getNumber() + ":\n";
        header += LINE;
        header += "| Call Type |   Start Time        |     End Time        | Duration | Cost  |\n";
        header += LINE;
        return header;
    }

    private String createLine(PhoneNumber number, Call call) {
        String line = "|     " + String.format("%02d", call.getType()) + "    |";
        line += toStringFormat(call.getStartTime()) + "|";
        line += toStringFormat(call.getEndTime()) + "|";
        line += " " + call.getDuration() + " |";
        double curPrice = countPrice(number, call);
        call.setCost(curPrice);
        if (curPrice < 10) line += " ";
        line += " " + String.format("%4s", String.format("%.2f", curPrice)) + " |\n";
        return line;
    }

    private String toStringFormat(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }

    private double countPrice(PhoneNumber number, Call call) {
        switch (number.getTariff()) {
            case 3: {
                return call.getCost();
            }
            case 6: {
                if (number.getTariffMinutes() == 0) return call.getCost();
                double minutes = call.getCost();
                double tariffMinutes = number.getTariffMinutes();
                if (tariffMinutes >= minutes) {
                    tariffMinutes -= minutes;
                    number.setTariffMinutes((int) tariffMinutes);
                    return 0;
                } else {
                    double expensiveMinutes = minutes - tariffMinutes;
                    number.setTariffMinutes(0);
                    return expensiveMinutes;
                }
            }
            case 11: {
                if (call.getType() == 2) return 0;
                if (number.getTariffMinutes() == 0) return call.getCost();
                double minutes = call.getCost() / 1.5;
                double tariffMinutes = number.getTariffMinutes();
                if (tariffMinutes >= minutes) {
                    tariffMinutes -= minutes;
                    number.setTariffMinutes((int) tariffMinutes);
                    return 0.5 * minutes;
                } else {
                    double expensiveMinutes = minutes - tariffMinutes;
                    minutes -= expensiveMinutes;
                    number.setTariffMinutes(0);
                    return minutes * 0.5 + expensiveMinutes * 1.5;
                }
            }
            default: {
                return 0;
            }
        }
    }

    private String createFooter(double price) {
        String footer = LINE;
        footer += "|                                           Total Cost: |";
        footer += String.format("%10s", String.format("%.2f", price)) + " rubles |\n";
        footer += LINE;
        return footer;
    }
}
