package com.gabriel.course.projectapi2.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingUtils {
    private static final double FIRST_15_MINUTES = 5.00;
    private static final double FIRST_60_MINUTES = 9.25;
    private static final double ADDITIONAL_15_MINUTES = 1.75;
    private static final double DESCOUNT_PERCENTUAL = 0.30;


    public static String createReceipt() {
        LocalDateTime date = LocalDateTime.now();
        String receipt = date.toString().substring(0,19);
        return  receipt.replace("-", "")
                .replace(":", "")
                .replace("T", "-");

    }

    public static BigDecimal calculateCost(LocalDateTime enterDate, LocalDateTime exitDate) {
        long minutes = enterDate.until(exitDate, ChronoUnit.MINUTES);
        double total = 0.0;
        int additionalCicles = 0;
        long totalMin = minutes;


        if (minutes <= 15) {
            total = FIRST_15_MINUTES;

        } else if (minutes <= 60) {
            total = FIRST_60_MINUTES;

        } else {
            while (totalMin > 60) {
                totalMin -= 15;
                additionalCicles++;
            }
            total = FIRST_60_MINUTES + ADDITIONAL_15_MINUTES * additionalCicles;

        }

        return new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal calcularDesconto(BigDecimal custo, long numberOfTimes) {

        String lastNumber = String.valueOf(numberOfTimes);
        // Complete o código com a sua lógica

        BigDecimal desconto = null;

        if(lastNumber.charAt(lastNumber.length() - 1) == '0' && lastNumber.length() > 1) {
            desconto = custo.multiply(new BigDecimal(DESCOUNT_PERCENTUAL));
        }else {
            desconto = new BigDecimal(0);
        }

        return desconto.setScale(2, RoundingMode.HALF_EVEN);
    }
}
