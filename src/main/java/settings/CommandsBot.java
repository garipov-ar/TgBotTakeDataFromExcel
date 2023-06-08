package settings;

import org.apache.poi.ss.usermodel.*;
import org.jetbrains.annotations.NotNull;
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
    } //тащим имя
    @Override
    public String getBotToken() {
        return settings.getBotToken();
    } //тащим токен
    private static ReplyKeyboard replyKeyboard;

    public static File file = new File("/root/bot/data.xlsx");
    //public static File file = new File("data.xlsx");

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
                    sendRfServiceKPI(chatId, "Пермский филиал", 3,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                    sendMessage(chatId, "Выберите Сервисный Центр");
                }
                case "Филиал в Тюменской и Курганской областях" -> {
// Отправить клавиатуру со списком СЦ
                    replyKeyboard  = getBranchesKeyboardFTK();
                    sendRfServiceKPI(chatId, "Филиал в Тюменской и Курганской областях", 4,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                    sendMessage(chatId, "Выберите Сервисный Центр");
                }
                case "Ханты-Мансийский филиал" -> {
// Отправить клавиатуру со списком СЦ
                    replyKeyboard  = getBranchesKeyboardHMF();
                    sendRfServiceKPI(chatId, "Ханты-Мансийский филиал", 5,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                    sendMessage(chatId, "Выберите Сервисный Центр");
                }
                case "Челябинский филиал" -> {
// Отправить клавиатуру со списком СЦ
                    replyKeyboard  = getBranchesKeyboardChF();
                    sendRfServiceKPI(chatId, "Челябинский филиал", 6,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                    sendMessage(chatId, "Выберите Сервисный Центр");
                }
                case "Ямало-Ненецкий филиал" -> {
// Отправить клавиатуру со списком СЦ
                    replyKeyboard  = getBranchesKeyboardYNF();
                    sendRfServiceKPI(chatId, "Ямало-Ненецкий филиал", 6,
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
                case "СЦ г.Ишим" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Ишим", 20,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г.Курган" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Курган", 21,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г. Тобольск" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Тобольск", 22,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г. Тюмень" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Тюмень", 23,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г. Шадринск" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Шадринск", 24,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г. Шумиха" -> {
                    sendSCServiceKPI(chatId, "СЦ г. Шумиха", 25,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ с. Кетово" -> {
                    sendSCServiceKPI(chatId, "СЦ с. Кетово", 26,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ Южный" -> {
                    sendSCServiceKPI(chatId, "СЦ Южный", 27,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СУ г.Белоярский" -> {
                    sendSCServiceKPI(chatId, "СУ г.Белоярский", 28,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СУ г.Лангепас" -> {
                    sendSCServiceKPI(chatId, "СУ г.Лангепас", 29,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СУ г.Радужный" -> {
                    sendSCServiceKPI(chatId, "СУ г.Радужный", 30,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СУ пгт.Березово" -> {
                    sendSCServiceKPI(chatId, "СУ пгт.Березово", 31,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СУ Сургутский район" -> {
                    sendSCServiceKPI(chatId, "СУ Сургутский район", 32,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г.Когалым" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Когалым", 33,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г.Нефтеюганск" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Нефтеюганск", 34,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г.Нижневартовск" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Нижневартовск", 35,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г.Нягань" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Нягань", 36,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г.Советский" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Советский", 37,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г.Сургут" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Сургут", 38,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г.Урай" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Урай", 39,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г.Ханты-Мансийск" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Ханты-Мансийск", 40,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "РУС г.Трехгорный" -> {
                    sendSCServiceKPI(chatId, "РУС г.Трехгорный", 41,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г.Аша" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Аша", 42,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г.Златоуст" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Златоуст", 43,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г.Карталы" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Карталы", 44,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г.Копейск" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Копейск", 45,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г.Магнитогорск" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Магнитогорск", 46,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "СЦ г.Миасс" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Миасс", 47,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "СЦ г.Чебаркуль" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Чебаркуль", 48,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "СЦ г.Челябинск" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Челябинск", 49,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }

                case "СЦ г.Южноуральск" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Южноуральск", 50,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СУ г.Губкинский" -> {
                    sendSCServiceKPI(chatId, "СУ г.Губкинский", 51,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СУ г.Лабытнанги" -> {
                    sendSCServiceKPI(chatId, "СУ г.Лабытнанги", 52,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СУ г.Муравленко" -> {
                    sendSCServiceKPI(chatId, "СУ г.Муравленко", 53,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СУ г.Тарко-Сале" -> {
                    sendSCServiceKPI(chatId, "СУ г.Тарко-Сале", 54,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СУ п.Тазовский" -> {
                    sendSCServiceKPI(chatId, "СУ п.Тазовский", 55,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СУ с.Красноселькуп" -> {
                    sendSCServiceKPI(chatId, "СУ с.Красноселькуп", 56,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СУ с.Мужи" -> {
                    sendSCServiceKPI(chatId, "СУ с.Мужи", 57,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СУ с.Яр-Сале" -> {
                    sendSCServiceKPI(chatId, "СУ с.Яр-Сале", 58,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г.Надым" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Надым", 59,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г.Новый Уренгой" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Новый Уренгой", 60,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г.Ноябрьск" -> {
                    sendSCServiceKPI(chatId, "СЦ г.Ноябрьск", 61,
                    new String[]{"Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                            "SLA сквозные платина", "Инсталляции с первого дня назначения"});
                }
                case "СЦ г.Салехард" -> {
                    sendSCServiceKPI(chatId, "", 62,
                            new String[]{ "Повторы", "SLA 3ЛТП платина", "SLA 3ЛТП прочие", "SLA сквозные прочие",
                                    "SLA сквозные платина", "Инсталляции с первого дня назначения"});
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
        return getReplyKeyboard(branches);
    }

    private ReplyKeyboard getBranchesKeyboardPF() {
        String[] branches = {"СУ г.Губаха", "СЦ г.Березники", "СЦ г. Кунгур", "СЦ г. Лысьва", "СЦ г. Очёр", "СЦ г. Пермь", "СЦ г. Чайковский", "СЦ г. Чернушка", "СЦ Прикамье"};
        return getReplyKeyboard(branches);
    }

    private ReplyKeyboard getBranchesKeyboardFTK() {
        String[] branches = {"СЦ г.Ишим", "СЦ г.Курган", "СЦ г. Тобольск", "СЦ г. Тюмень", "СЦ г. Шадринск", "СЦ г. Шумиха", "СЦ с. Кетово", "СЦ Южный"};
        return getReplyKeyboard(branches);
    }

    private ReplyKeyboard getBranchesKeyboardHMF() {
        String[] branches = {"СУ г.Белоярский", "СУ г.Лангепас", "СУ г.Радужный", "СУ пгт.Березово", "СУ Сургутский район", "СЦ г.Когалым", "СЦ г.Нефтеюганск", "СЦ г.Нижневартовск", "СЦ г.Нягань", "СЦ г.Советский", "СЦ г.Сургут", "СЦ г.Урай", "СЦ г.Ханты-Мансийск"};
        return getReplyKeyboard(branches);
    }

    private ReplyKeyboard getBranchesKeyboardChF() {
        String[] branches = {"РУС г.Трехгорный", "СЦ г.Аша", "СЦ г.Златоуст", "СЦ г.Карталы", "СЦ г.Копейск", "СЦ г.Магнитогорск", "СЦ г.Миасс", "СЦ г.Чебаркуль", "СЦ г.Челябинск", "СЦ г.Южноуральск"};
        return getReplyKeyboard(branches);
    }

    private ReplyKeyboard getBranchesKeyboardYNF() {
        String[] branches = {"СУ г.Губкинский", "СУ г.Лабытнанги", "СУ г.Муравленко", "СУ г.Тарко-Сале", "СУ п.Тазовский", "СУ с.Красноселькуп", "СУ с.Мужи", "СУ с.Яр-Сале", "СЦ г.Надым", "СЦ г.Новый Уренгой", "СЦ г.Ноябрьск", "СЦ г.Салехард"};
        return getReplyKeyboard(branches);
    }


    @NotNull
    private ReplyKeyboard getReplyKeyboard(String[] branches) {
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
