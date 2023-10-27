package ru.loudbar.gaishnik.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.loudbar.gaishnik.models.NumberPlate;
import ru.loudbar.gaishnik.repositories.NumberPlateRepository;

@Service
@RequiredArgsConstructor
public class NumberPlateService {
    private final NumberPlateRepository numberPlateRepository;
    private static final char[] VALID_LETTERS = {'А', 'В', 'Е', 'К', 'М', 'Н', 'О', 'Р', 'С', 'Т', 'У', 'Х'};
    private char[] currentLetters = {'А', 'А', 'А'};
    private int currentDigit = 0;
    private boolean rebootFlag = false;

    @Transactional
    public String getRandomNumber() {
        String randomNumber;
        do {
            randomLetter();
            randomDigit();
            randomNumber = generateNumber();
        } while (numberPlateRepository.existsByNumber(randomNumber));

        saveNumber(randomNumber);
        return randomNumber;
    }

    @Transactional
    public String getNextNumber() {
        String nextNumber;

        // Флаг сделан для того, чтобы получать состояние (последний сохраненный номер) после перезагрузки приложения.
        // С ним запрос в БД на чтение последнего состояния будет посылаться только один раз
        if (!rebootFlag) {
            NumberPlate lastSavedNumber = numberPlateRepository.findTopByOrderByIdDesc();
            if (lastSavedNumber != null) {
                String lastNumber = lastSavedNumber.getNumber();
                parseLastNumber(lastNumber);
            }
            rebootFlag = true;
        }
        do {
            nextDigit();
            nextNumber = generateNumber();
        } while (numberPlateRepository.existsByNumber(nextNumber));

        saveNumber(nextNumber);
        return nextNumber;
    }

    private void parseLastNumber(String lastNumber) {
        if (lastNumber != null) {
            // Извлекаем буквы последнего сохраненного номера
            currentLetters[0] = lastNumber.charAt(lastNumber.length() - 14);
            currentLetters[1] = lastNumber.charAt(lastNumber.length() - 10);
            currentLetters[2] = lastNumber.charAt(lastNumber.length() - 9);

            // Извлекаем цифры последнего сохраненного номера
            String lastDigits = lastNumber.substring(lastNumber.length() - 13, lastNumber.length() - 10);
            currentDigit = Integer.parseInt(lastDigits);
        }
    }

    private void saveNumber(String number) {
        numberPlateRepository.save(NumberPlate.builder().number(number).build());
    }

    private void randomLetter() {
        currentLetters[0] = VALID_LETTERS[(int) (Math.random() * VALID_LETTERS.length)];
        currentLetters[1] = VALID_LETTERS[(int) (Math.random() * VALID_LETTERS.length)];
        currentLetters[2] = VALID_LETTERS[(int) (Math.random() * VALID_LETTERS.length)];
    }

    private void randomDigit() {
        currentDigit = (int) (Math.random() * 1000);
    }

    private String generateNumber() {
        return String.format("%c%03d%c%c 116 RUS", currentLetters[0], currentDigit, currentLetters[1], currentLetters[2]);
    }

    private void nextDigit() {
        if (currentDigit == 999) {
            currentDigit = 0;
            nextLetter();
        } else {
            currentDigit++;
        }
    }

    private void nextLetter() {
        for (int i = 2; i >= 0; i--) {
            int letterIndex = new String(VALID_LETTERS).indexOf(currentLetters[i]);
            if (letterIndex == VALID_LETTERS.length - 1) {
                currentLetters[i] = VALID_LETTERS[0];
            } else {
                currentLetters[i] = VALID_LETTERS[letterIndex + 1];
                break;
            }
        }
    }
}
