package ru.otus.hw.controller.page;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.hw.services.MsgService;

@Controller
@RequiredArgsConstructor
public class MsgController {

    private final MsgService msgService;

    @PostMapping("/msg")
    public String startMsg() {

        System.out.println("/msg = ");

        msgService.generateAndSaveRandomMsgs(100);

        return "listbook";
    }

}