package com.gmall.service;

import com.gmall.config.AppJedisConfig;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-4 12:33:36
 */
@Service
public class HelloService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //注入jedis连接池
    @Autowired
    JedisPool jedisPool;

    //注入redissonClient客户端，获取分布式锁
    @Autowired
    RedissonClient redissonClient;

    public void useRedisssonForLock(){
        //获取一把锁，只要锁名一样那么就使用的同一把锁
        RLock lock = redissonClient.getLock("lock");
        try {
            //加锁,3秒自动解锁
            //这个加锁默认是阻塞的，哪个线程加的锁必须由哪个线程来解锁，否则会一直处于阻塞状态
            lock.lock(3, TimeUnit.SECONDS);
            Jedis jedis = jedisPool.getResource();
            String num = jedis.get("num");
            if(StringUtils.isEmpty(num)){
                num = "0";
            }
            Integer i = Integer.parseInt(num);
            i = i + 1;
            jedis.set("num", i.toString());
            jedis.close();
        } finally {
            //无论前面代码是否有异常，都必须解锁
            lock.unlock();
        }
    }

    /* *
    * 分布式场景下，以前的各种锁都没有用(他们是进程内锁，是在同一个jvm中的同一个工程内的锁)，因为分布式场景下是跨jvm的，jvm都已经互不相关了
    * 要使用分布式锁
    * 要想锁得住，每个线程必须用同一把锁
    * 分布式场景下，java提供的锁(synchronized, Lock, ReentrantLock)都锁不住，因为不同jvm中的锁对象肯定不同
    *
    *  //1、占坑。（原子性）
        //1）、先判断没有，2）、再给里面放值


        /**
         *
         * setnx->set if not exist：原子操作。判断带保存。
         *
         *1）、代码第一阶段；
         * public void hello(){
         *
         * //获取和设置值必须是原子的
         *   String lock =  getFromRedis("lock");//get("lock")
         *   if(lock == null){
         *       setRedisKey("lock","1");
         *       //执行业务
         *       delRedisKey("lock")
         *       return ;
         *   }else{
         *      hello();//自旋
         *   }
         * }
         * //问题：加锁的原子性
         *
         * 2、代码第二阶段
         * public void hello(){
         *     //1、获取到锁
         *     Integer lock = setnx("lock',"111"); //0代表没有保存数据，说明已经有人占了。1代表占可坑成功
         *     if(lock!=0){
         *         //执行业务逻辑
         *         //释放锁、删除锁
         *         del("lock")
         *     }else{
         *         //等待重试
         *         hello();
         *     }
         * }
         * //问题：如果由于各种问题（未捕获的异常、断电等）导致锁没释放。其他人永远获取不到锁。
         * //解决：加个过期时间。
         *
         * 3、代码第三阶段
         * public void hello(){
         *    //超时和加锁必须原子
         *     Integer lock = setnx("lock',"111");
         *     if(lock!=null){
         *         expire("lock",10s);
         *         //执行业务逻辑
         *         //释放锁
         *         del("lock')
         *     }else{
         *         hello();
         *     }
         *
         * }
         * 问题：刚拿到锁，机器炸了，没来得及设置超时。
         * 解决：加锁和加超时也必须是原子的。
         *
         *
         * 4、代码第四阶段：
         * public void hello(){
         *     String result = setnxex("lock","111",10s);
         *     if(result=="ok"){
         *         //加锁成功
         *         //执行业务逻辑
         *         del("lock")
         *     }else{
         *         hello();
         *     }
         * }
         * 问题：如果业务逻辑超时，导致锁自动删除，业务执行完又删除一遍。至少多个人都获取到了锁。
         *
         * 5、代码第五阶段。
         * public void hello(){
         *    String token = UUID;
         *    String result = setnxex("lock",token,10s);
         *    if(result == "ok"){
         *        //执行业务
         *
         *        //删锁，保证删除自己的锁
         *        if(get("lock")==token){
         *            del("lock")
         *        }
         *    }else{
         *        hello();
         *    }
         * }
         * 问题？：我们获取锁的时候，锁的值正在给我们返回。锁过期。redis删除了锁。
         * 但是我们拿到了值，而且对比成功（此时此刻正好有人又获取）。我们还删除了锁。至少两个线程又进入同一个代码。
         *  原因：？删锁不是原子。d
         *      lua脚本。
         *
         *  解决：
         *  String script =
         *      "if redis.call('get', KEYS[1]) == ARGV[1] then
         *              return redis.call('del', KEYS[1])
         *       else
         *              return 0
         *       end";
         *
         * jedis.eval(script, Collections.singletonList(key), Collections.singletonList(token));
         *
         *   lua脚本进行删除。
         *
         *
         * 1）、分布式锁的核心（保证原子性）
         *      1）、加锁。占坑一定要是原子的。（判断如果没有，就给redis中保存值）
         *      2）、锁要自动超时。
         *      3）、解锁也要原子。
         *
         *
         *  最终的分布式锁的代码：大家都去redis中占同一个坑。
    * */
    //分布式加锁

    public void  incrDistribute(){
        /*String token = UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", token, 3, TimeUnit.SECONDS);
        if(lock){
            String num = stringRedisTemplate.opsForValue().get("num");
            if(num !=null){
                Integer i = Integer.parseInt(num);
                i = i +1;
                stringRedisTemplate.opsForValue().set("num", i.toString());
            }
            //业务代码执行完毕后，删除锁
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            DefaultRedisScript<String> script1 = new DefaultRedisScript<>(script);
            stringRedisTemplate.execute(script1, Arrays.asList("lock"), token);
            System.out.println("删除锁完成...");
        }else {
            //自旋操作
            incrDistribute();
        }*/
        //用Jedis操作redis
       // Jedis jedis = new Jedis("localhost", 6379);
        //通过jedis连接池获取jedis客户端
        /* *
        * 考虑锁粒度，粒度要细：记录级别；各自服务给自锁，分析好锁粒度，不要锁住无关数据，一种数据一种锁，一条数据一条锁
        * 自旋次数、自旋超时
        * */
        Jedis jedis = jedisPool.getResource();
        try {
            String token = UUID.randomUUID().toString();
            //如果redis中没有lock的key，就设置一个，锁的过期时间3秒
            String lock = jedis.set("lock", token, SetParams.setParams().ex(3).nx());
            if(lock!=null && lock.equalsIgnoreCase("ok")){
                //业务方法开始
                String num = jedis.get("num");
                Integer i = Integer.parseInt(num);
                i = i+1;
                jedis.set("num", i.toString());
                //业务方法结束
                //删除锁 ，执行删除锁的脚本命令
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                jedis.eval(script, Collections.singletonList("lock"), Collections.singletonList(token));
                System.err.println("删除锁ok");
            }else{
                incrDistribute();
            }
        } finally {
            jedis.close();
        }
    }

    public  void incre(){
        stringRedisTemplate.opsForValue().increment("num");
    }



}

























