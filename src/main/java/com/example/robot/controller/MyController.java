package com.example.robot.controller;

import io.ktor.client.HttpClient;
import io.ktor.http.Url;
import love.forte.simbot.application.Application;
import love.forte.simbot.common.id.Identifies;
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBotFriendRelation;
import love.forte.simbot.component.onebot.v11.core.bot.OneBotBotManager;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    private final Application application;

    // 一个简单的返回值类型，假设始终返回空JSON {}
    public record Result() {
    }

    private static final Result OK = new Result();

    public MyController(Application application) {
        this.application = application;
    }

    /**
     * 通过 /xxx/event 接收事件，
     * xxx 为你bot配置的 uniqueBotId
     */
    @PostMapping("/1878673405/event")
    public Result onEvent(@PathVariable String botId, @RequestBody String body) {
        for (var botManager : application.getBotManagers()) {
            if (botManager instanceof OneBotBotManager obManager) {
                // 找到这个bot
                var bot = obManager.find(Identifies.of(botId));
                // 如果有就推送这个事件并退出寻找
                if (bot != null) {
                    // 推送事件并在异步中处理
                    // 你也可以参考上面有关 Flux 和 Collectable 的说明
                    // 来阻塞地等待事件处理完成后再返回
                    bot.pushAndLaunch(body);
                    break;
                }
            }
        }

        return OK;
    }
}
