package com.example.robot.handler;

import com.example.robot.Bean.BfvPlayer;
import com.google.gson.Gson;
import io.ktor.http.ContentType;
import love.forte.simbot.application.Application;
import love.forte.simbot.common.id.LongID;
import love.forte.simbot.component.onebot.v11.core.actor.OneBotFriend;
import love.forte.simbot.component.onebot.v11.core.api.SetFriendAddRequestApi;
import love.forte.simbot.component.onebot.v11.core.event.message.OneBotFriendMessageEvent;
import love.forte.simbot.component.onebot.v11.core.event.request.OneBotFriendRequestEvent;
import love.forte.simbot.component.onebot.v11.event.request.RawFriendRequestEvent;
import love.forte.simbot.event.ChatGroupMessageEvent;
import love.forte.simbot.event.Event;
import love.forte.simbot.message.Image;
import love.forte.simbot.message.Message;
import love.forte.simbot.quantcat.common.annotations.ContentTrim;
import love.forte.simbot.quantcat.common.annotations.Filter;
import love.forte.simbot.quantcat.common.annotations.Listener;
import love.forte.simbot.suspendrunner.reserve.SuspendReserves;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

@Component
public class MyEventHandlers {

    /**
     * 此处是一个标准库中通用的类型：聊天群消息事件
     */
    @Listener
    public void onGroupMessage(ChatGroupMessageEvent event) {
        System.out.println("ChatGroupMessageEvent: " + event);
    }

    /**
     * 此处监听的是OneBot组件中的专属类型：OneBot的好友消息事件
     * 并且过滤消息：消息中的文本消息去除前后空字符后，等于 '你好'
     */
    @Listener
    @ContentTrim
    @Filter(value = "你好")
    public CompletableFuture<?> onFriendMessage(OneBotFriendMessageEvent event) {

        System.out.println("OneBotFriendMessageEvent: " + event);
        return event.replyAsync("你也好");
        // 可以直接返回任意 CompletableFuture 类型
    }

    /**
     * 此处监听的是OneBot组件中的专属类型：OneBot的好友消息事件
     * 并且过滤消息：消息中的文本消息去除前后空字符后，等于 '你好'
     */
    @Listener
    @ContentTrim
    @Filter(value = "4733Aquarius")
    public CompletableFuture<?> onFriendMessageBFV(OneBotFriendMessageEvent event) {

        OkHttpClient okHttpClient = new OkHttpClient();
        Gson gson = new Gson();
        Request request = new Request.Builder().url("https://api.gametools.network/bfv/stats/?name=4733Aquarius").build();

        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String body = response.body().string(); // 使用string()方法
                response.body().close(); // 确保资源释放
                // 处理响应数据
                BfvPlayer player = gson.fromJson(body, BfvPlayer.class);
                String kpm = player.getKillsPerMinute();

                return event.replyAsync("kpm为："+kpm);
            } else {
                // 处理错误情况
                return event.replyAsync("处理数据失败: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 更好的异常处理，可能向用户报告错误
            return event.replyAsync("由于一个意外错误，获取数据失败");
        }
    }


        @Listener
        public void onFriendRequest1(OneBotFriendRequestEvent event) {
            RawFriendRequestEvent addFriendEvent = event.getSourceEvent();
            int userId = addFriendEvent.getUserId().toInt();
            String flag = addFriendEvent.getFlag();
            if (userId == 1393452805){
                event.acceptBlocking();
            }
        }
        @Listener
        public void onFriendRequest2(OneBotFriendRequestEvent event) {
            RawFriendRequestEvent addFriendEvent = event.getSourceEvent();
            int userId = addFriendEvent.getUserId().toInt();
            String flag = addFriendEvent.getFlag();
            if (userId == 939502270){
                System.out.println("OneBotFriendRequestEvent: " + event);
                event.acceptBlocking();
            }
        }
    }