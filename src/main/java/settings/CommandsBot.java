package settings;

import org.apache.poi.ss.usermodel.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;



public class CommandsBot extends TelegramLongPollingBot {

    private final Settings settings = new Settings();

    @Override
    public String getBotUsername() {
        return settings.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return settings.getBotToken();
    }

    private static ReplyKeyboard replyKeyboard;

    public static File file = new File("/root/bot/data.xlsx");



    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) {
            return;
        }

        long timestamp = file.lastModified();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date(timestamp);
        String fileCreationDate = sdf.format(date);

        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        try {
            switch (messageText) {

                case "/start" -> {
                    replyKeyboard = getButtons();
                    sendMessage(chatId, "\nbot in test" + "\nВыберите СЦ" );
                }

                case "Екатеринбургский филиал" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    try (InputStream inputStream = new FileInputStream(file);
                         Workbook workbook = WorkbookFactory.create(inputStream)) {

                        Sheet sheetKPI = workbook.getSheet("KPI");
                        double[] values = new double[6];

                        for (int i = 1; i <= 6; i++) {
                            Row row = sheetKPI.getRow(10);
                            Cell cell = row.getCell(i);
                            double value = cell.getNumericCellValue() * 100;
                            value = Math.round(value * 10.0) / 10.0;
                            values[i-1] = value;
                        }

                        sendMessage(chatId, "\nSLA Платина сквозные: " + values[1] + "%" + "\nSLA Прочие сквозные: " + values[0] + "%" +
                                "\nSLA Платина 3ЛТП: " + values[3] + "%" + "\nSLA Прочие 3ЛТП: " + values[2] + "%" +
                                "\nПовторы: " + values[4] + "%"
                                + "\nИнсталляции с первого дня назначения: " + values[5] + "%");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                case "СЦ г. Екатеринбург" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    try (InputStream inputStream = new FileInputStream(file);
                         Workbook workbook = WorkbookFactory.create(inputStream)) {

                        Sheet sheetKPI = workbook.getSheet("KPI");
                        double[] values = new double[6];

                        for (int i = 1; i <= 6; i++) {
                            Row row = sheetKPI.getRow(1);
                            Cell cell = row.getCell(i);
                            double value = cell.getNumericCellValue() * 100;
                            value = Math.round(value * 10.0) / 10.0;
                            values[i-1] = value;
                        }

                        sendMessage(chatId, "\nSLA Платина сквозные: " + values[1] + "%" + "\nSLA Прочие сквозные: " + values[0] + "%" +
                                "\nSLA Платина 3ЛТП: " + values[3] + "%" + "\nSLA Прочие 3ЛТП: " + values[2] + "%" +
                                "\nПовторы: " + values[4] + "%"
                                + "\nИнсталляции с первого дня назначения: " + values[5] + "%");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                case "СЦ г. Нижний Тагил" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    try (InputStream inputStream = new FileInputStream(file);
                         Workbook workbook = WorkbookFactory.create(inputStream)) {

                        Sheet sheetKPI = workbook.getSheet("KPI");
                        double[] values = new double[6];

                        for (int i = 1; i <= 6; i++) {
                            Row row = sheetKPI.getRow(2);
                            Cell cell = row.getCell(i);
                            double value = cell.getNumericCellValue() * 100;
                            value = Math.round(value * 10.0) / 10.0;
                            values[i-1] = value;
                        }

                        sendMessage(chatId, "\nSLA Платина сквозные: " + values[1] + "%" + "\nSLA Прочие сквозные: " + values[0] + "%" +
                                "\nSLA Платина 3ЛТП: " + values[3] + "%" + "\nSLA Прочие 3ЛТП: " + values[2] + "%" +
                                "\nПовторы: " + values[4] + "%"
                                + "\nИнсталляции с первого дня назначения: " + values[5] + "%");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                case "СЦ г. Богданович" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    try (InputStream inputStream = new FileInputStream(file);
                         Workbook workbook = WorkbookFactory.create(inputStream)) {

                        Sheet sheetKPI = workbook.getSheet("KPI");
                        double[] values = new double[6];

                        for (int i = 1; i <= 6; i++) {
                            Row row = sheetKPI.getRow(3);
                            Cell cell = row.getCell(i);
                            double value = cell.getNumericCellValue() * 100;
                            value = Math.round(value * 10.0) / 10.0;
                            values[i-1] = value;
                        }

                        sendMessage(chatId, "\nSLA Платина сквозные: " + values[1] + "%" + "\nSLA Прочие сквозные: " + values[0] + "%" +
                                "\nSLA Платина 3ЛТП: " + values[3] + "%" + "\nSLA Прочие 3ЛТП: " + values[2] + "%" +
                                "\nПовторы: " + values[4] + "%"
                                + "\nИнсталляции с первого дня назначения: " + values[5] + "%");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                case "СЦ г. Ирбит" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    try (InputStream inputStream = new FileInputStream(file);
                         Workbook workbook = WorkbookFactory.create(inputStream)) {

                        Sheet sheetKPI = workbook.getSheet("KPI");
                        double[] values = new double[6];

                        for (int i = 1; i <= 6; i++) {
                            Row row = sheetKPI.getRow(4);
                            Cell cell = row.getCell(i);
                            double value = cell.getNumericCellValue() * 100;
                            value = Math.round(value * 10.0) / 10.0;
                            values[i-1] = value;
                        }

                        sendMessage(chatId, "\nSLA Платина сквозные: " + values[1] + "%" + "\nSLA Прочие сквозные: " + values[0] + "%" +
                                "\nSLA Платина 3ЛТП: " + values[3] + "%" + "\nSLA Прочие 3ЛТП: " + values[2] + "%" +
                                "\nПовторы: " + values[4] + "%"
                                + "\nИнсталляции с первого дня назначения: " + values[5] + "%");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                case "СЦ г. Каменск-Уральский" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    try (InputStream inputStream = new FileInputStream(file);
                         Workbook workbook = WorkbookFactory.create(inputStream)) {

                        Sheet sheetKPI = workbook.getSheet("KPI");
                        double[] values = new double[6];

                        for (int i = 1; i <= 6; i++) {
                            Row row = sheetKPI.getRow(5);
                            Cell cell = row.getCell(i);
                            double value = cell.getNumericCellValue() * 100;
                            value = Math.round(value * 10.0) / 10.0;
                            values[i-1] = value;
                        }

                        sendMessage(chatId, "\nSLA Платина сквозные: " + values[1] + "%" + "\nSLA Прочие сквозные: " + values[0] + "%" +
                                "\nSLA Платина 3ЛТП: " + values[3] + "%" + "\nSLA Прочие 3ЛТП: " + values[2] + "%" +
                                "\nПовторы: " + values[4] + "%"
                                + "\nИнсталляции с первого дня назначения: " + values[5] + "%");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                case "СЦ г. Кировград" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    try (InputStream inputStream = new FileInputStream(file);
                         Workbook workbook = WorkbookFactory.create(inputStream)) {

                        Sheet sheetKPI = workbook.getSheet("KPI");
                        double[] values = new double[6];

                        for (int i = 1; i <= 6; i++) {
                            Row row = sheetKPI.getRow(6);
                            Cell cell = row.getCell(i);
                            double value = cell.getNumericCellValue() * 100;
                            value = Math.round(value * 10.0) / 10.0;
                            values[i-1] = value;
                        }

                        sendMessage(chatId, "\nSLA Платина сквозные: " + values[1] + "%" + "\nSLA Прочие сквозные: " + values[0] + "%" +
                                "\nSLA Платина 3ЛТП: " + values[3] + "%" + "\nSLA Прочие 3ЛТП: " + values[2] + "%" +
                                "\nПовторы: " + values[4] + "%"
                                + "\nИнсталляции с первого дня назначения: " + values[5] + "%");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                case "СЦ г. Первоуральск" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    try (InputStream inputStream = new FileInputStream(file);
                         Workbook workbook = WorkbookFactory.create(inputStream)) {

                        Sheet sheetKPI = workbook.getSheet("KPI");
                        double[] values = new double[6];

                        for (int i = 1; i <= 6; i++) {
                            Row row = sheetKPI.getRow(7);
                            Cell cell = row.getCell(i);
                            double value = cell.getNumericCellValue() * 100;
                            value = Math.round(value * 10.0) / 10.0;
                            values[i-1] = value;
                        }

                        sendMessage(chatId, "\nSLA Платина сквозные: " + values[1] + "%" + "\nSLA Прочие сквозные: " + values[0] + "%" +
                                "\nSLA Платина 3ЛТП: " + values[3] + "%" + "\nSLA Прочие 3ЛТП: " + values[2] + "%" +
                                "\nПовторы: " + values[4] + "%"
                                + "\nИнсталляции с первого дня назначения: " + values[5] + "%");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                case "СЦ г. Серов" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    try (InputStream inputStream = new FileInputStream(file);
                         Workbook workbook = WorkbookFactory.create(inputStream)) {

                        Sheet sheetKPI = workbook.getSheet("KPI");
                        double[] values = new double[6];

                        for (int i = 1; i <= 6; i++) {
                            Row row = sheetKPI.getRow(8);
                            Cell cell = row.getCell(i);
                            double value = cell.getNumericCellValue() * 100;
                            value = Math.round(value * 10.0) / 10.0;
                            values[i-1] = value;
                        }

                        sendMessage(chatId, "\nSLA Платина сквозные: " + values[1] + "%" + "\nSLA Прочие сквозные: " + values[0] + "%" +
                                "\nSLA Платина 3ЛТП: " + values[3] + "%" + "\nSLA Прочие 3ЛТП: " + values[2] + "%" +
                                "\nПовторы: " + values[4] + "%"
                                + "\nИнсталляции с первого дня назначения: " + values[5] + "%");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                case "СУ г. Красноуфимск" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    try (InputStream inputStream = new FileInputStream(file);
                         Workbook workbook = WorkbookFactory.create(inputStream)) {

                        Sheet sheetKPI = workbook.getSheet("KPI");
                        double[] values = new double[6];

                        for (int i = 1; i <= 6; i++) {
                            Row row = sheetKPI.getRow(9);
                            Cell cell = row.getCell(i);
                            double value = cell.getNumericCellValue() * 100;
                            value = Math.round(value * 10.0) / 10.0;
                            values[i-1] = value;
                        }
                        sendMessage(chatId, "\nSLA Платина сквозные: " + values[1] + "%" + "\nSLA Прочие сквозные: " + values[0] + "%" +
                                "\nSLA Платина 3ЛТП: " + values[3] + "%" + "\nSLA Прочие 3ЛТП: " + values[2] + "%" +
                                "\nПовторы: " + values[4] + "%"
                                + "\nИнсталляции с первого дня назначения: " + values[5] + "%");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage(chatId, "Ошибка.\nПопробуйте ещё раз.");
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(replyKeyboard).build();

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboard getButtons() {
        String[] cities = {"Екатеринбургский филиал", "СЦ г. Екатеринбург", "СЦ г. Нижний Тагил", "СЦ г. Богданович", "СЦ г. Ирбит", "СЦ г. Каменск-Уральский", "СЦ г. Кировград", "СЦ г. Первоуральск", "СЦ г. Серов", "СУ г. Красноуфимск"};
        int rows = cities.length / 2 + 1;
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            KeyboardRow row = new KeyboardRow();
            int index1 = i * 2;
            int index2 = index1 + 1;
            if (index1 < cities.length) {
                row.add(cities[index1]);
            }
            if (index2 < cities.length) {
                row.add(cities[index2]);
            }
            keyboard.add(row);
        }
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
