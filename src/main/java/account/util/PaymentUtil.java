package account.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public final class PaymentUtil {

    public static String formatPeriodFromDateToString(LocalDate period) {
        return period.format(DateTimeFormatter.ofPattern("MMMM-yyyy"));
    }

    public static String formatSalaryFromLongToDollarsAndCents(Long salary) {
        return String.format("%d dollar(s) %d cent(s)",
                salary / 100,
                salary % 100);
    }

    public static LocalDate formatPeriodFromStringToDate(String period) {
        return YearMonth.parse(period, DateTimeFormatter.ofPattern("MM-yyyy")).atDay(1);
    }
}
