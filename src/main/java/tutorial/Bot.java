package tutorial;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

public class Bot extends TelegramLongPollingBot {
    // ключ = фамилия, а значение = номер телефона | ключ - это до ","
   // map - это как справочник, где можно быстро найти номер телефона по фамилии
   // phoneByLastName - это переменная, которая будет хранить нашу хэш-мапу
    Map<String, String> phoneByLastName = new HashMap<String, String>();

    public String getBotUsername() {
        return "TutorialBot";
    }

    @Override
    public String getBotToken() {
        return "7641801953:AAFNbzraA6O41AZPCdscuQQiSLdl--ZKAfs";
    }


    public void onUpdateReceived(Update update) {
        Message msg = update.getMessage();
        String message = msg.getText();//сообщение пользователя
        User user = msg.getFrom(); //сам пользователь
        Long id = user.getId(); // id пользователя
        String userName = user.getUserName(); //юзернейм пользователя

        //это ответ бота
        String answer = "";

        if (message.equalsIgnoreCase("/контакты")){
            answer = "Вот ваши контакты: " + phoneByLastName;
            //  проверяем, что сообщение пользователя начинается со слов /номер
        } else  if (message.startsWith("/номер")) {
            String[] commandAndLastName = message.split(" ");
            answer = phoneByLastName.get(commandAndLastName[1]);
        } else {
            //делим сообщение пользователя с помощью метода split на 2 отдельных слова (фамилия и отдельно номер телефона)
            // и превращаем в массив lastNameAndPhone
            String[] lastNameAndPhone = message.split(" ");
            phoneByLastName.put(lastNameAndPhone[0], lastNameAndPhone[1]);
        }


        //оставляем как есть
        SendMessage sm = SendMessage.builder()
                .chatId(id.toString())
                .text(answer) //что будет отвечать бот
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


}
