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

    //public static File file = new File("/root/bot/data.xlsx");
    public static File file = new File("data.xlsx");

    long timestamp = file.lastModified();
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    Date date = new Date(timestamp);
    String fileCreationDate = sdf.format(date);

    private void sendSCServiceKPI(long chatId, String serviceName, int rowIndex, String[] kpiNames) {
        // Код для обработки СЦ
        sendMessage(chatId, "Показатели клиентского сервиса B2B" + "\n" + serviceName + "\nАктуально на " + fileCreationDate);
        try (InputStream inputStream = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheetKPI = workbook.getSheet("СЦ");
            double[] values = new double[kpiNames.length];

            for (int i = 1; i <= kpiNames.length; i++) {
                Row row = sheetKPI.getRow(rowIndex);
                Cell cell = row.getCell(i);
                double value = cell.getNumericCellValue() * 100;
                value = Math.round(value * 10.0) / 10.0;
                values[i - 1] = value;
            }

            StringBuilder messageBuilder = new StringBuilder();
            for (int i = 0; i < kpiNames.length; i++) {
                double value = values[i];
                String emoji = getEmojiForValue(kpiNames[i], value);
                messageBuilder.append(emoji).append(" ").append(kpiNames[i]).append(": ").append(value).append("%\n");
            }

            sendMessage(chatId, messageBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRfServiceKPI(long chatId, String serviceName, int rowIndex, String[] kpiNames) {
        // Код для обработки филиалов
        sendMessage(chatId, "Показатели клиентского сервиса B2B" + "\n" + serviceName + "\nАктуально на " + fileCreationDate);
        try (InputStream inputStream = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheetKPI = workbook.getSheet("РФ");
            double[] values = new double[kpiNames.length];

            for (int i = 1; i <= kpiNames.length; i++) {
                Row row = sheetKPI.getRow(rowIndex);
                Cell cell = row.getCell(i);
                double value = cell.getNumericCellValue() * 100;
                value = Math.round(value * 10.0) / 10.0;
                values[i - 1] = value;
            }

            StringBuilder messageBuilder = new StringBuilder();
            for (int i = 0; i < kpiNames.length; i++) {
                double value = values[i];
                String emoji = getEmojiForValue(kpiNames[i], value);
                messageBuilder.append(emoji).append(" ").append(kpiNames[i]).append(": ").append(value).append("%\n");
            }

            sendMessage(chatId, messageBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private String getEmojiForValue(String kpiName, double value) {
        return switch (kpiName) {
            case "SLA сквозные платина" -> value < 85 ? "\uD83D\uDD34" : "\uD83D\uDFE2";
            case "SLA сквозные прочие", "SLA 3ЛТП прочие" -> value < 88 ? "\uD83D\uDD34" : "\uD83D\uDFE2";
            case "SLA 3ЛТП платина" -> value < 84 ? "\uD83D\uDD34" : "\uD83D\uDFE2";
            case "Повторы" -> value > 4 ? "\uD83D\uDD34" : "\uD83D\uDFE2";
            case "Инсталляции с первого дня назначения" -> value < 80 ? "\uD83D\uDD34" : "\uD83D\uDFE2";
            case "SLA NTTM прочие сквозные", "SLA NTTM платина сквозные" -> value < 83 ? "\uD83D\uDD34" : "\uD83D\uDFE2";
            default -> "";
        };
    }



    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) {
            return;
        }


        long chatId = update.getMessage().getChatId();
        String messageText = update.getMessage().getText();

        try {
            switch (messageText) {

                case "/start", "Назад" -> {
                    replyKeyboard = getButtonsMRF();
                    sendMessage(chatId, "\nbot in test" + "\nДобрый день! Данный бот транслирует показатели клиентского сервиса B2B." +
                            "\nИсточник данных - дашборд БТИ" + "\nВыберите Филиал" );
                }

                case "Екатеринбургский филиал" -> {
// Отправить клавиатуру со списком СЦ
                    replyKeyboard  = getBranchesKeyboardEF();
                    sendRfServiceKPI(chatId, "Екатеринбургский филиал", 2,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});


                    sendMessage(chatId, "Выберите Сервисный Центр");
                }

                case "Пермский филиал" -> {
// Отправить клавиатуру со списком СЦ
                    replyKeyboard  = getBranchesKeyboardPF();
                    sendRfServiceKPI(chatId, "Екатеринбургский филиал", 3,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});


                    sendMessage(chatId, "Выберите Сервисный Центр");
                }



                case "СЦ г. Екатеринбург" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Екатеринбург", 4,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }



                case "СЦ г. Нижний Тагил" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Нижний Тагил", 8,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});

                }

                case "СЦ г. Богданович" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Богданович", 3,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});

                }

                case "СЦ г. Ирбит" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Ирбит", 5,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "СЦ г. Каменск-Уральский" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Каменск-Уральский", 6,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "СЦ г. Кировград" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Кировград", 7,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "СЦ г. Первоуральск" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Первоуральск", 9,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "СЦ г. Серов" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Серов", 10,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "СУ г. Красноуфимск" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Красноуфимск", 2,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "СУ г.Губаха" -> {
                    sendSCServiceKPI(chatId, "СУ г.Губаха", 11,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "СЦ г.Березники" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Березники", 12,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "СЦ г. Кунгур" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Кунгур", 13,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "СЦ г. Лысьва" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Лысьва", 14,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "СЦ г. Очёр" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Очёр", 15,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "СЦ г. Пермь" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Пермь", 16,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "СЦ г. Чайковский" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Чайковский", 17,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "СЦ г. Чернушка" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Чернушка", 18,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "СЦ Прикамье" -> {
                    sendSCServiceKPI(chatId, "СЦ Прикамье", 19,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
/*
                case "" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Екатеринбург", 19,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Екатеринбург", 20,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения", "SLA NTTM прочие сквозные", "SLA NTTM платина сквозные"});
                }

                case "" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Екатеринбург", 21,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения", "SLA NTTM прочие сквозные", "SLA NTTM платина сквозные"});
                }

                case "" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Екатеринбург", 22,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения", "SLA NTTM прочие сквозные", "SLA NTTM платина сквозные"});
                }

                case "" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Екатеринбург", 23,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения", "SLA NTTM прочие сквозные", "SLA NTTM платина сквозные"});
                }

                case "" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Екатеринбург", 24,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения", "SLA NTTM прочие сквозные", "SLA NTTM платина сквозные"});
                }

                case "" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Екатеринбург", 25,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения", "SLA NTTM прочие сквозные", "SLA NTTM платина сквозные"});
                }
*/

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

    private ReplyKeyboard getButtonsMRF() {
        String[] cities = {"Екатеринбургский филиал", "Пермский филиал", "Филиал в Тюменской и Курганской областях", "Ханты-Мансийский филиал", "Челябинский филиал", "Ямало-Ненецкий филиал"};
        int columns = 1;
        int rows = (int) Math.ceil((double) cities.length / columns);

        List<KeyboardRow> keyboard = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            KeyboardRow row = new KeyboardRow();
            for (int j = 0; j < columns; j++) {
                int index = i * columns + j;
                if (index < cities.length) {
                    row.add(cities[index]);
                }
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

    private ReplyKeyboard getBranchesKeyboardEF() {
        String[] branches = {"СЦ г. Екатеринбург", "СЦ г. Нижний Тагил", "СЦ г. Богданович", "СЦ г. Ирбит", "СЦ г. Каменск-Уральский", "СЦ г. Кировград", "СЦ г. Первоуральск", "СЦ г. Серов", "СУ г. Красноуфимск"};
        int columns = 3;
        int rows = (int) Math.ceil((double) branches.length / columns);

        List<KeyboardRow> keyboard = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            KeyboardRow row = new KeyboardRow();
            for (int j = 0; j < columns; j++) {
                int index = i * columns + j;
                if (index < branches.length) {
                    row.add(branches[index]);
                }
            }
            keyboard.add(row);
        }
        KeyboardRow backButtonRow = new KeyboardRow();
        backButtonRow.add("Назад");
        keyboard.add(backButtonRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    private ReplyKeyboard getBranchesKeyboardPF() {
        String[] branches = {"СУ г.Губаха", "СЦ г.Березники", "СЦ г. Кунгур", "СЦ г. Лысьва", "СЦ г. Очёр", "СЦ г. Пермь", "СЦ г. Чайковский", "СЦ г. Чернушка", "СЦ Прикамье"};
        int columns = 3;
        int rows = (int) Math.ceil((double) branches.length / columns);
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            KeyboardRow row = new KeyboardRow();
            for (int j = 0; j < columns; j++) {
                int index = i * columns + j;
                if (index < branches.length) {
                    row.add(branches[index]);
                }
            }
            keyboard.add(row);
        }
        KeyboardRow backButtonRow = new KeyboardRow();
        backButtonRow.add("Назад");
        keyboard.add(backButtonRow);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

}
