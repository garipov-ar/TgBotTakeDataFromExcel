package settings;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class CommandsBot extends TelegramLongPollingBot {


    @Override
    public String getBotUsername() {
        return "korpk_bot";
    }

    @Override

    public String getBotToken() {
        return "5737882509:AAFawDR9V37rnVocTgVsoHAFEa3WI7mTz4U";

    }

    private static ReplyKeyboard replyKeyboard;

    public static File file = new File("/home/gpovaserv/Bot/TgBotTakeDataFromExcel/data.xlsx");
    private static FileInputStream inputStream;

    static {
        try {
            inputStream = new FileInputStream(file);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) {
            //log.debug("Update has no message. Skip processing.");
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
                case "СЦ г. Екатеринбург" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    inputStream = new FileInputStream(file);

                    Workbook workbook = WorkbookFactory.create(inputStream);



                    //Достаем платину
                    Row rowPlatina;
                    Cell cellPlatina;
                    Sheet sheetKPI = workbook.getSheet("KPI");
                    rowPlatina = sheetKPI.getRow(1);
                    cellPlatina = rowPlatina.getCell(2);
                    double platina = cellPlatina.getNumericCellValue();
                    platina = platina * 100;
                    platina = Math.round(platina * 10.0) / 10.0;


                    //Достаем Прочие
                    Row rowProchie;
                    Cell cellProchie;
                    rowProchie = sheetKPI.getRow(1);
                    cellProchie = rowProchie.getCell(1);
                    double prochie = cellProchie.getNumericCellValue();
                    prochie = prochie * 100;
                    prochie = Math.round(prochie * 10.0) / 10.0;

                    //Достаем Повторы
                    Row rowPovtor;
                    Cell cellPovtor;
                    rowPovtor = sheetKPI.getRow(1);
                    cellPovtor = rowPovtor.getCell(3);
                    double povtor = cellPovtor.getNumericCellValue();
                    povtor = povtor * 100;
                    povtor = Math.round(povtor * 10.0) / 10.0;

                    //Достаем Инсталлы
                    Row rowInstall;
                    Cell cellInstall;
                    rowInstall = sheetKPI.getRow(1);
                    cellInstall = rowInstall.getCell(4);
                    double install = cellInstall.getNumericCellValue();
                    install = install * 100;
                    install = Math.round(install * 10.0) / 10.0;
                    sendMessage(chatId, "\nSLA Платина: " + platina + "%" + "\nSLA Прочие: " + prochie + "%" +
                            "\nПовторы: " + povtor + "%"
                            + "\nИнсталляции с первого дня назначения: " + install + "%");
                    inputStream.close();

                }

                case "СЦ г. Нижний Тагил" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    inputStream = new FileInputStream(file);

                    Workbook workbook = WorkbookFactory.create(inputStream);



                    //Достаем платину
                    Row rowPlatina;
                    Cell cellPlatina;
                    Sheet sheetKPI = workbook.getSheet("KPI");
                    rowPlatina = sheetKPI.getRow(2);
                    cellPlatina = rowPlatina.getCell(2);
                    double platina = cellPlatina.getNumericCellValue();
                    platina = platina * 100;
                    platina = Math.round(platina * 10.0) / 10.0;


                    //Достаем Прочие
                    Row rowProchie;
                    Cell cellProchie;
                    rowProchie = sheetKPI.getRow(2);
                    cellProchie = rowProchie.getCell(1);
                    double prochie = cellProchie.getNumericCellValue();
                    prochie = prochie * 100;
                    prochie = Math.round(prochie * 10.0) / 10.0;

                    //Достаем Повторы
                    Row rowPovtor;
                    Cell cellPovtor;
                    rowPovtor = sheetKPI.getRow(2);
                    cellPovtor = rowPovtor.getCell(3);
                    double povtor = cellPovtor.getNumericCellValue();
                    povtor = povtor * 100;
                    povtor = Math.round(povtor * 10.0) / 10.0;

                    //Достаем Инсталлы
                    Row rowInstall;
                    Cell cellInstall;
                    rowInstall = sheetKPI.getRow(2);
                    cellInstall = rowInstall.getCell(4);
                    double install = cellInstall.getNumericCellValue();
                    install = install * 100;
                    install = Math.round(install * 10.0) / 10.0;
                    sendMessage(chatId, "\nSLA Платина: " + platina + "%" + "\nSLA Прочие: " + prochie + "%" +
                            "\nПовторы: " + povtor + "%"
                            + "\nИнсталляции с первого дня назначения: " + install + "%");
                    inputStream.close();

                }

                case "СЦ г. Богданович" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    inputStream = new FileInputStream(file);

                    Workbook workbook = WorkbookFactory.create(inputStream);



                    //Достаем платину
                    Row rowPlatina;
                    Cell cellPlatina;
                    Sheet sheetKPI = workbook.getSheet("KPI");
                    rowPlatina = sheetKPI.getRow(3);
                    cellPlatina = rowPlatina.getCell(2);
                    double platina = cellPlatina.getNumericCellValue();
                    platina = platina * 100;
                    platina = Math.round(platina * 10.0) / 10.0;


                    //Достаем Прочие
                    Row rowProchie;
                    Cell cellProchie;
                    rowProchie = sheetKPI.getRow(3);
                    cellProchie = rowProchie.getCell(1);
                    double prochie = cellProchie.getNumericCellValue();
                    prochie = prochie * 100;
                    prochie = Math.round(prochie * 10.0) / 10.0;

                    //Достаем Повторы
                    Row rowPovtor;
                    Cell cellPovtor;
                    rowPovtor = sheetKPI.getRow(3);
                    cellPovtor = rowPovtor.getCell(3);
                    double povtor = cellPovtor.getNumericCellValue();
                    povtor = povtor * 100;
                    povtor = Math.round(povtor * 10.0) / 10.0;

                    //Достаем Инсталлы
                    Row rowInstall;
                    Cell cellInstall;
                    rowInstall = sheetKPI.getRow(3);
                    cellInstall = rowInstall.getCell(4);
                    double install = cellInstall.getNumericCellValue();
                    install = install * 100;
                    install = Math.round(install * 10.0) / 10.0;
                    sendMessage(chatId, "\nSLA Платина: " + platina + "%" + "\nSLA Прочие: " + prochie + "%" +
                            "\nПовторы: " + povtor + "%"
                            + "\nИнсталляции с первого дня назначения: " + install + "%");
                    inputStream.close();

                }

                case "СЦ г. Ирбит" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    inputStream = new FileInputStream(file);

                    Workbook workbook = WorkbookFactory.create(inputStream);



                    //Достаем платину
                    Row rowPlatina;
                    Cell cellPlatina;
                    Sheet sheetKPI = workbook.getSheet("KPI");
                    rowPlatina = sheetKPI.getRow(4);
                    cellPlatina = rowPlatina.getCell(2);
                    double platina = cellPlatina.getNumericCellValue();
                    platina = platina * 100;
                    platina = Math.round(platina * 10.0) / 10.0;


                    //Достаем Прочие
                    Row rowProchie;
                    Cell cellProchie;
                    rowProchie = sheetKPI.getRow(4);
                    cellProchie = rowProchie.getCell(1);
                    double prochie = cellProchie.getNumericCellValue();
                    prochie = prochie * 100;
                    prochie = Math.round(prochie * 10.0) / 10.0;

                    //Достаем Повторы
                    Row rowPovtor;
                    Cell cellPovtor;
                    rowPovtor = sheetKPI.getRow(4);
                    cellPovtor = rowPovtor.getCell(3);
                    double povtor = cellPovtor.getNumericCellValue();
                    povtor = povtor * 100;
                    povtor = Math.round(povtor * 10.0) / 10.0;

                    //Достаем Инсталлы
                    Row rowInstall;
                    Cell cellInstall;
                    rowInstall = sheetKPI.getRow(4);
                    cellInstall = rowInstall.getCell(4);
                    double install = cellInstall.getNumericCellValue();
                    install = install * 100;
                    install = Math.round(install * 10.0) / 10.0;
                    sendMessage(chatId, "\nSLA Платина: " + platina + "%" + "\nSLA Прочие: " + prochie + "%" +
                            "\nПовторы: " + povtor + "%"
                            + "\nИнсталляции с первого дня назначения: " + install + "%");
                    inputStream.close();

                }
                case "СЦ г. Каменск-Уральский" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    inputStream = new FileInputStream(file);

                    Workbook workbook = WorkbookFactory.create(inputStream);



                    //Достаем платину
                    Row rowPlatina;
                    Cell cellPlatina;
                    Sheet sheetKPI = workbook.getSheet("KPI");
                    rowPlatina = sheetKPI.getRow(5);
                    cellPlatina = rowPlatina.getCell(2);
                    double platina = cellPlatina.getNumericCellValue();
                    platina = platina * 100;
                    platina = Math.round(platina * 10.0) / 10.0;


                    //Достаем Прочие
                    Row rowProchie;
                    Cell cellProchie;
                    rowProchie = sheetKPI.getRow(5);
                    cellProchie = rowProchie.getCell(1);
                    double prochie = cellProchie.getNumericCellValue();
                    prochie = prochie * 100;
                    prochie = Math.round(prochie * 10.0) / 10.0;

                    //Достаем Повторы
                    Row rowPovtor;
                    Cell cellPovtor;
                    rowPovtor = sheetKPI.getRow(5);
                    cellPovtor = rowPovtor.getCell(3);
                    double povtor = cellPovtor.getNumericCellValue();
                    povtor = povtor * 100;
                    povtor = Math.round(povtor * 10.0) / 10.0;

                    //Достаем Инсталлы
                    Row rowInstall;
                    Cell cellInstall;
                    rowInstall = sheetKPI.getRow(5);
                    cellInstall = rowInstall.getCell(4);
                    double install = cellInstall.getNumericCellValue();
                    install = install * 100;
                    install = Math.round(install * 10.0) / 10.0;
                    sendMessage(chatId, "\nSLA Платина: " + platina + "%" + "\nSLA Прочие: " + prochie + "%" +
                            "\nПовторы: " + povtor + "%"
                            + "\nИнсталляции с первого дня назначения: " + install + "%");
                    inputStream.close();

                }
                case "СЦ г. Кировград" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    inputStream = new FileInputStream(file);

                    Workbook workbook = WorkbookFactory.create(inputStream);



                    //Достаем платину
                    Row rowPlatina;
                    Cell cellPlatina;
                    Sheet sheetKPI = workbook.getSheet("KPI");
                    rowPlatina = sheetKPI.getRow(6);
                    cellPlatina = rowPlatina.getCell(2);
                    double platina = cellPlatina.getNumericCellValue();
                    platina = platina * 100;
                    platina = Math.round(platina * 10.0) / 10.0;


                    //Достаем Прочие
                    Row rowProchie;
                    Cell cellProchie;
                    rowProchie = sheetKPI.getRow(6);
                    cellProchie = rowProchie.getCell(1);
                    double prochie = cellProchie.getNumericCellValue();
                    prochie = prochie * 100;
                    prochie = Math.round(prochie * 10.0) / 10.0;

                    //Достаем Повторы
                    Row rowPovtor;
                    Cell cellPovtor;
                    rowPovtor = sheetKPI.getRow(6);
                    cellPovtor = rowPovtor.getCell(3);
                    double povtor = cellPovtor.getNumericCellValue();
                    povtor = povtor * 100;
                    povtor = Math.round(povtor * 10.0) / 10.0;

                    //Достаем Инсталлы
                    Row rowInstall;
                    Cell cellInstall;
                    rowInstall = sheetKPI.getRow(6);
                    cellInstall = rowInstall.getCell(4);
                    double install = cellInstall.getNumericCellValue();
                    install = install * 100;
                    install = Math.round(install * 10.0) / 10.0;
                    sendMessage(chatId, "\nSLA Платина: " + platina + "%" + "\nSLA Прочие: " + prochie + "%" +
                            "\nПовторы: " + povtor + "%"
                            + "\nИнсталляции с первого дня назначения: " + install + "%");
                    inputStream.close();

                }
                case "СЦ г. Первоуральск" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    inputStream = new FileInputStream(file);

                    Workbook workbook = WorkbookFactory.create(inputStream);



                    //Достаем платину
                    Row rowPlatina;
                    Cell cellPlatina;
                    Sheet sheetKPI = workbook.getSheet("KPI");
                    rowPlatina = sheetKPI.getRow(7);
                    cellPlatina = rowPlatina.getCell(2);
                    double platina = cellPlatina.getNumericCellValue();
                    platina = platina * 100;
                    platina = Math.round(platina * 10.0) / 10.0;


                    //Достаем Прочие
                    Row rowProchie;
                    Cell cellProchie;
                    rowProchie = sheetKPI.getRow(7);
                    cellProchie = rowProchie.getCell(1);
                    double prochie = cellProchie.getNumericCellValue();
                    prochie = prochie * 100;
                    prochie = Math.round(prochie * 10.0) / 10.0;

                    //Достаем Повторы
                    Row rowPovtor;
                    Cell cellPovtor;
                    rowPovtor = sheetKPI.getRow(7);
                    cellPovtor = rowPovtor.getCell(3);
                    double povtor = cellPovtor.getNumericCellValue();
                    povtor = povtor * 100;
                    povtor = Math.round(povtor * 10.0) / 10.0;

                    //Достаем Инсталлы
                    Row rowInstall;
                    Cell cellInstall;
                    rowInstall = sheetKPI.getRow(7);
                    cellInstall = rowInstall.getCell(4);
                    double install = cellInstall.getNumericCellValue();
                    install = install * 100;
                    install = Math.round(install * 10.0) / 10.0;
                    sendMessage(chatId, "\nSLA Платина: " + platina + "%" + "\nSLA Прочие: " + prochie + "%" +
                            "\nПовторы: " + povtor + "%"
                            + "\nИнсталляции с первого дня назначения: " + install + "%");
                    inputStream.close();

                }
                case "СЦ г. Серов" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    inputStream = new FileInputStream(file);

                    Workbook workbook = WorkbookFactory.create(inputStream);



                    //Достаем платину
                    Row rowPlatina;
                    Cell cellPlatina;
                    Sheet sheetKPI = workbook.getSheet("KPI");
                    rowPlatina = sheetKPI.getRow(8);
                    cellPlatina = rowPlatina.getCell(2);
                    double platina = cellPlatina.getNumericCellValue();
                    platina = platina * 100;
                    platina = Math.round(platina * 10.0) / 10.0;


                    //Достаем Прочие
                    Row rowProchie;
                    Cell cellProchie;
                    rowProchie = sheetKPI.getRow(8);
                    cellProchie = rowProchie.getCell(1);
                    double prochie = cellProchie.getNumericCellValue();
                    prochie = prochie * 100;
                    prochie = Math.round(prochie * 10.0) / 10.0;

                    //Достаем Повторы
                    Row rowPovtor;
                    Cell cellPovtor;
                    rowPovtor = sheetKPI.getRow(8);
                    cellPovtor = rowPovtor.getCell(3);
                    double povtor = cellPovtor.getNumericCellValue();
                    povtor = povtor * 100;
                    povtor = Math.round(povtor * 10.0) / 10.0;

                    //Достаем Инсталлы
                    Row rowInstall;
                    Cell cellInstall;
                    rowInstall = sheetKPI.getRow(8);
                    cellInstall = rowInstall.getCell(4);
                    double install = cellInstall.getNumericCellValue();
                    install = install * 100;
                    install = Math.round(install * 10.0) / 10.0;
                    sendMessage(chatId, "\nSLA Платина: " + platina + "%" + "\nSLA Прочие: " + prochie + "%" +
                            "\nПовторы: " + povtor + "%"
                            + "\nИнсталляции с первого дня назначения: " + install + "%");
                    inputStream.close();

                }
                case "УС г. Красноуфимск" -> {
                    sendMessage(chatId, "Подождите немного" + "\nАктуальность показателей от " + fileCreationDate);
                    inputStream = new FileInputStream(file);

                    Workbook workbook = WorkbookFactory.create(inputStream);



                    //Достаем платину
                    Row rowPlatina;
                    Cell cellPlatina;
                    Sheet sheetKPI = workbook.getSheet("KPI");
                    rowPlatina = sheetKPI.getRow(9);
                    cellPlatina = rowPlatina.getCell(2);
                    double platina = cellPlatina.getNumericCellValue();
                    platina = platina * 100;
                    platina = Math.round(platina * 10.0) / 10.0;


                    //Достаем Прочие
                    Row rowProchie;
                    Cell cellProchie;
                    rowProchie = sheetKPI.getRow(9);
                    cellProchie = rowProchie.getCell(1);
                    double prochie = cellProchie.getNumericCellValue();
                    prochie = prochie * 100;
                    prochie = Math.round(prochie * 10.0) / 10.0;

                    //Достаем Повторы
                    Row rowPovtor;
                    Cell cellPovtor;
                    rowPovtor = sheetKPI.getRow(9);
                    cellPovtor = rowPovtor.getCell(3);
                    double povtor = cellPovtor.getNumericCellValue();
                    povtor = povtor * 100;
                    povtor = Math.round(povtor * 10.0) / 10.0;

                    //Достаем Инсталлы
                    Row rowInstall;
                    Cell cellInstall;
                    rowInstall = sheetKPI.getRow(9);
                    cellInstall = rowInstall.getCell(4);
                    double install = cellInstall.getNumericCellValue();
                    install = install * 100;
                    install = Math.round(install * 10.0) / 10.0;
                    sendMessage(chatId, "\nSLA Платина: " + platina + "%" + "\nSLA Прочие: " + prochie + "%" +
                            "\nПовторы: " + povtor + "%"
                            + "\nИнсталляции с первого дня назначения: " + install + "%");
                    inputStream.close();

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
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("СЦ г. Екатеринбург");
        keyboard.add(row1);
        KeyboardRow row2 = new KeyboardRow();
        row2.add("СЦ г. Нижний Тагил");
        keyboard.add(row2);
        KeyboardRow row3 = new KeyboardRow();
        row2.add("СЦ г. Богданович");
        keyboard.add(row3);
        KeyboardRow row4 = new KeyboardRow();
        row3.add("СЦ г. Ирбит");
        keyboard.add(row4);
        KeyboardRow row5 = new KeyboardRow();
        row3.add("СЦ г. Каменск-Уральский");
        keyboard.add(row5);
        KeyboardRow row6 = new KeyboardRow();
        row4.add("СЦ г. Кировград");
        keyboard.add(row6);
        KeyboardRow row7 = new KeyboardRow();
        row4.add("СЦ г. Первоуральск");
        keyboard.add(row7);
        KeyboardRow row8 = new KeyboardRow();
        row5.add("СЦ г. Серов");
        keyboard.add(row8);
        KeyboardRow row9 = new KeyboardRow();
        row5.add("УС г. Красноуфимск");
        keyboard.add(row9);

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
