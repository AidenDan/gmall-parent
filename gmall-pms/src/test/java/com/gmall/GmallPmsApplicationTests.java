package com.gmall;

import com.gmall.pms.entity.Brand;
import com.gmall.pms.entity.Product;
import com.gmall.pms.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
class GmallPmsApplicationTests {

    @Autowired
    RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    ProductService productService;

    @Test
    void contextLoads() {
        Product byId = productService.getById(1);
        System.out.println(byId.getName());
    }

    @Test
    public void redis(){
        Brand brand = new Brand();
        brand.setName("查三");
        redisTemplate.opsForValue().set("abc", brand, 1, TimeUnit.DAYS);
        Brand abc = (Brand)redisTemplate.opsForValue().get("abc");
        System.err.println(abc.getName());
        System.out.println("////////////////////////////");
        List<Object> list = Arrays.asList("战三", "李四", "王五", brand);
        redisTemplate.opsForValue().set("def", list, 1, TimeUnit.DAYS);
        List<Object> def = (List<Object>)redisTemplate.opsForValue().get("def");
        for (Object s : def) {
            System.err.println(s);
        }
        System.err.println("完毕");
        List<Object> list2 = Arrays.asList("战三", "李四", "王五", brand);
        redisTemplate.opsForValue().set("qwe", list2);
    }
}
