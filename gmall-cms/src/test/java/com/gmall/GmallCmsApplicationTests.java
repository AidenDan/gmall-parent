package com.gmall;

import com.gmall.cms.entity.Help;
import com.gmall.cms.service.HelpService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class GmallCmsApplicationTests {

    @Autowired
    HelpService helpService;

    @Test
    void contextLoads() {
        List<Help> list = helpService.list();
        list.forEach(var-> System.out.println(var));
    }

}
