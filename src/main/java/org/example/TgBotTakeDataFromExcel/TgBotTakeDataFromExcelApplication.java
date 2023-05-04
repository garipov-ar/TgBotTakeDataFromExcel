package org.example.TgBotTakeDataFromExcel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import settings.CommandsBot;

@SpringBootApplication
public class TgBotTakeDataFromExcelApplication {

	public static void main(String[] args) throws TelegramApiException {
		SpringApplication.run(TgBotTakeDataFromExcelApplication.class, args);
		CommandsBot bot = new CommandsBot();
		try {
			TelegramBotsApi telegramBot = new TelegramBotsApi(DefaultBotSession.class);
			telegramBot.registerBot(bot);
		} catch (TelegramApiException e) {
			e.printStackTrace();

		}

	}
}