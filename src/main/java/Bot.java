import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    private static final String SUNNY_EMOJI = EmojiParser.parseToUnicode(":sunny:");
    private static final String SWEAT_DROPS_EMOJI = EmojiParser.parseToUnicode(":sweat_drops:");
    private static final String CITY_SCAPE_EMOJI = EmojiParser.parseToUnicode(":cityscape:");
    private static final String HOUSE_WITH_GARDEN_EMOJI = EmojiParser.parseToUnicode(":house_with_garden:");
    private static final String RAINBOW_EMOJI = EmojiParser.parseToUnicode(":rainbow:");
    private static final String WORLD_MAP_MAP_EMOJI = EmojiParser.parseToUnicode(":world_map:");
    private static final String SUN_WITH_FACE_EMOJI = EmojiParser.parseToUnicode(":sun_with_face:");
    private static final String PENCIL_2_EMOJI = EmojiParser.parseToUnicode(":pencil2:");


    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }


    public void sendSticker(Message message) {
        SendSticker sendSticker = new SendSticker();
        sendSticker.setChatId(message.getChatId());
        sendSticker.setSticker("CAACAgIAAxkBAALzKV7qMaxw3mtVyJc-dWllD_LeGXf7AAIqAQACpkRIC5MPUTUxaZIgGgQ");
        try {
            execute(sendSticker);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }


    }

    public void setKeyboard(SendMessage sendMessage, Message message) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        if (message.getText().equals("/start")) {
            List<KeyboardRow> keyboardRowList = new ArrayList<>();
            KeyboardRow keyboardRow = new KeyboardRow();
            keyboardRow.add(new KeyboardButton("Find out the weather in my location" + RAINBOW_EMOJI));
            keyboardRowList.add(keyboardRow);
            replyKeyboardMarkup.setKeyboard(keyboardRowList);
        }
    }


    public void sendMessage(Message message, String string) {
        SendMessage sendMessage = new SendMessage();
        if (message.getText().equals("/start")) sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(string);
        try {
            setKeyboard(sendMessage, message);
            execute(sendMessage);
            if (message.getText().equals("/start")) {
                sendSticker(message);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            if (message.getText().equals("/start")) {
                sendMessage(message, "Hello! I'm weather bot" + SUNNY_EMOJI + " and can say, what the weather" + SWEAT_DROPS_EMOJI +
                        "is like  in your city" + CITY_SCAPE_EMOJI + " or village" + HOUSE_WITH_GARDEN_EMOJI);
            } else if (message.getText().equals("Find out the weather in my location" + RAINBOW_EMOJI)) {
                sendMessage(message, "Write your current location" + WORLD_MAP_MAP_EMOJI);
            } else if (!message.getText().equals("/start") || !message.getText().equals("Find out the weather in my location" + RAINBOW_EMOJI)) {
                try {
                    sendMessage(message, Weather.getWeather(message.getText(), model));
                    sendMessage(message, "If you want find out the weather" + SUN_WITH_FACE_EMOJI + " in another location, just write name"+PENCIL_2_EMOJI);
                } catch (IOException e) {
                    sendMessage(message, "Sorry, but your city do not found");
                }
            }
        }
    }


    public String getBotUsername() {
        return "MyF1rstWeatherBot";
    }

    public String getBotToken() {
        return "1151356916:AAGAoIgHk3bTTD3pVKwNfqxzph5iedUlK_k";
    }
}
