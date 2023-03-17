package Homework2.SnilsValidator;

public class SnilsValidatorImpl implements SnilsValidator {
    @Override
    public boolean validate(String snils) {
        if(!checkInput(snils)){
            return false;
        }
        int sum = 0;
        int controlNum;
        for (int i = 0; i <= 9; i++) {
            sum += (snils.charAt(i) - '0') * (9 - i);
        }
        if (sum < 100) {
            controlNum = sum;
        } else if (sum == 100) {
            controlNum = 0;
        } else {
            controlNum = sum % 101 == 100 ? 0 : sum % 101;
        }
        return controlNum == Integer.parseInt(snils.substring(9));
    }

    public boolean checkInput(String snils){
        if (snils.length() != 11) {
            System.out.println("Некорректный ввод. СНИЛС состоит из 11 цифр");
            return false;
        }
        try {
            Long.parseLong(snils);
        } catch (NumberFormatException e) {
            System.out.println("СНИЛС может состоять только из цифр");
            return false;
        }
        return true;
    }
}
