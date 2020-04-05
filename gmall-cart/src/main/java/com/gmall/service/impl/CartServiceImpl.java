package com.gmall.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gmall.cart.service.CartService;
import com.gmall.cart.vo.Cart;
import com.gmall.cart.vo.CartItem;
import com.gmall.cart.vo.CartResponse;
import com.gmall.cart.vo.UserCartKey;
import com.gmall.component.MemberComponet;
import com.gmall.constant.CartConstant;
import com.gmall.pms.entity.Product;
import com.gmall.pms.entity.SkuStock;
import com.gmall.pms.service.ProductService;
import com.gmall.pms.service.SkuStockService;
import com.gmall.ums.entity.Member;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Aiden
 * @version 1.0
 * @description
 * @date 2020-3-12 15:58:35
 */
@SuppressWarnings("all")
@Component
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    MemberComponet memberComponet;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedissonClient redissonClient;
    @Reference
    SkuStockService skuStockService;
    @Reference
    ProductService productService;

    /**
     * @param skuId 商品的sku属性id
     * @param cartKey 购物车的id
     * @param accessToken 登录用户的token
     * @param num 购物项中商品的数量
     * @description: 添加购物项
     * @return:
     * @author: Aiden
     * @time: 2020-3-12 16:15:58
     */
    @Override
    public CartResponse addToCart(Long skuId, Integer num, String cartKey, String accessToken) throws ExecutionException, InterruptedException {
        //String FINAL_KEY="";
        //1、判断当前用户有没有登陆过
        Member member = memberComponet.getMemberByAccessToken(accessToken);

        //2、先合并离线、在线购物车购物项再说，根据在线购物车的key--->用户id以及离线购物车的key--->cartKey进行购物车合并
        // 购物车里面存放购物项，计算出每个购物项中商品的的件数就可统计得到所有的商品总数count
        if(member!=null && !StringUtils.isEmpty(cartKey)){
            System.err.println("合并购物车。。。");
            mergeCart(member.getId(), cartKey);
        }

        //1、这个人登录了，购物车就用他的在线购物车 cart:user:1
        UserCartKey userCartKey = memberComponet.getCartKey(accessToken, cartKey);
        //if(member!=null){
            //登陆了购物车的key为用户id标识
            //FINAL_KEY = CartConstant.USER_CART_KEY_PREFIX+member.getId();
            /* *
            * 添加购物项到购物车
            * 1、考虑按照skuId找到sku的真正信息，用skuId来标识一件产品 每个用户都有cartKey
            * 2、给指定的购物车添加记录
            * 注意：如果已经有了这个skuId，那么只是count的增加
            *
            * */
            CartItem cartItem = addCartItemToCart(skuId, num,  userCartKey.getFinalKey());
            CartResponse cartResponse = new CartResponse();
            cartResponse.setCartItem(cartItem);
            //设置临时购物车的key，没有就设置为null
            cartResponse.setCartKey(userCartKey.getNewCartKey());
            //将当前购物车中的购物项也返回到前端....想要获取数据就获取数据
        cartResponse.setCart(listCart(cartKey, accessToken).getCart());

        //添加购物项时，默认就是选中了这个购物项，将选中状态维护到购物车的key,  checked
        checkItem(Arrays.asList(skuId), true,userCartKey.getFinalKey());
        return cartResponse;
        }
        //2、这个人没登录，用离线购物车 cart:temp:cartKey
/*        if(!StringUtils.isEmpty(cartKey)){
            FINAL_KEY = CartConstant.TEMP_CART_KEY_PREFIX+cartKey;
            CartItem cartItem = addCartItemToCart(skuId, num, FINAL_KEY);
            CartResponse cartResponse = new CartResponse();
            cartResponse.setCartItem(cartItem);
            return cartResponse;
        }*/
        //3、如果以上都有，说明刚来(第一次来)，分配一个临时购物车
/*
        String newCartKey = UUID.randomUUID().toString().replace("-", "");
        FINAL_KEY = CartConstant.TEMP_CART_KEY_PREFIX+newCartKey;
        CartItem cartItem = addCartItemToCart(skuId, num, FINAL_KEY);
        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartItem(cartItem);
        cartResponse.setCartKey(newCartKey);
        return cartResponse;
    }
*/

    /**
     *
     *
     * @description: 更新购物车中购物项的商品数量
     * @param null
     * @return:
     * @author: Aiden
     * @time: 2020-3-14 13:28:13
     */
    @Override
    public CartResponse updateCart(Long skuId, Integer num, String cartKey, String accessToken) {
        //1、获取购物车，根据skuId获取这个购物项
        UserCartKey userCartKey = memberComponet.getCartKey(accessToken, cartKey);
        String finalKey = userCartKey.getFinalKey();
        stringRedisTemplate.expire(finalKey, 30L, TimeUnit.DAYS);
        RMap<String, String> rMap = redissonClient.getMap(finalKey);
        String cartItemString = rMap.get(skuId.toString());
        CartItem cartItem = JSON.parseObject(cartItemString, CartItem.class);
        //修改这个购物项的商品数量
        System.err.println("期望值"+num);
        cartItem.setCount(num);
        //更新后，重新把数据保存到购物车
        rMap.put(skuId.toString(), JSON.toJSONString(cartItem));
        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartItem(cartItem);
        //将当前购物车中的购物项也返回到前端....想要获取数据就获取数据
        cartResponse.setCart(listCart(cartKey, accessToken).getCart());
        return cartResponse;
    }

    /**
     *
     *
     * @description: 查询购物车
     * @param null
     * @return:
     * @author: Aiden
     * @time: 2020-3-14 15:14:20
     */
    @Override
    public CartResponse listCart(String cartKey, String accessToken) {
        Cart cart = new Cart();
        CartResponse cartResponse = new CartResponse();
        ArrayList<CartItem> cartItemList = new ArrayList<>();
        //当用户登录的时候就合并购物车
        UserCartKey userCartKey = memberComponet.getCartKey(accessToken, cartKey);
        if(userCartKey.getIsLogin()){
            mergeCart(userCartKey.getId(), cartKey);
        }
        //获取购物车
        String finalKey = userCartKey.getFinalKey();
        //每次刷新购物车，给购物车自定续期30天
        stringRedisTemplate.expire(finalKey, 30L, TimeUnit.DAYS);
        RMap<String, String> rMap = redissonClient.getMap(finalKey);
        if(rMap!=null && !rMap.isEmpty()){
            Set<Map.Entry<String, String>> entries = rMap.entrySet();
            entries.forEach(entry->{
                //当购物车的key不为checked时我们才遍历出购物项
                if(!entry.getKey().equalsIgnoreCase(CartConstant.CART_CHECKED_KEY)){
                    String cartItemString = entry.getValue();
                    CartItem cartItem = JSON.parseObject(cartItemString, CartItem.class);
                    cartItemList.add(cartItem);
                }
            });
            cart.setCartItemList(cartItemList);
            cartResponse.setCart(cart);
        }else {
            //用户第一次刷购物车时，随机一个购物车key给用户
            String newCartKey = userCartKey.getNewCartKey();
            cartResponse.setCartKey(newCartKey);
        }
         return cartResponse;
    }

    /**
     *
     *
     * @description:  删除购物车购物项
     * @param null
     * @return:
     * @author: Aiden
     * @time: 2020-3-14 17:47:50
     */

    @Override
    public CartResponse delCartItem(Long skuId, String cartKey, String accessToken) {
        //1、判断离线购物车还是在线购物车--->获取购物车的key
        UserCartKey userCartKey = memberComponet.getCartKey(accessToken,cartKey);
        String finalKey = userCartKey.getFinalKey();
        //2、根据购物车的key获取购物车
        RMap<String, String> rMap = redissonClient.getMap(finalKey);
        //删除购物项时也要维护购物车中选中状态的key，改为false,移除
        checkItem(Arrays.asList(skuId), false, finalKey);
        //3、根据skuId获取当前选择的购物项，进行删除
        rMap.remove(skuId.toString());
        //4、返回更新后的购物车
        CartResponse cartResponse = listCart(cartKey, accessToken);
        return cartResponse;
    }

    /**
     *
     *
     * @description: 清空购物车
     * @param null
     * @return:
     * @author: Aiden
     * @time: 2020-3-14 18:13:47
     */

    @Override
    public CartResponse clearCart(String cartKey, String accessToken) {
        //1、获取购物车
        UserCartKey userCartKey = memberComponet.getCartKey(accessToken, cartKey);
        //2、获取购物车的key
        String finalKey = userCartKey.getFinalKey();
        //3、根据购物车的key货物购物车
        RMap<String, String> rMap = redissonClient.getMap(finalKey);
        //2、清空购物车
        rMap.clear();
        CartResponse cartResponse = new CartResponse();
        return cartResponse;
    }

    /**
     *
     *
     * @description: 修改购物车中购物项的选中状态
     * @param null
     * @return:
     * @author: Aiden
     * @time: 2020-3-15 09:58:40
     */
    @Override
    public CartResponse checkCart(String cartKey, String accessToken, String skuIds, Integer ops) {
        //获取封装所有的购物车id
        List<Long> skuIdsList = new ArrayList<>();
        //1、根据前端传递过来的skuIds以及ops来修改购物车选中状态
        UserCartKey userCartKey = memberComponet.getCartKey(accessToken, cartKey);
        String finalKey = userCartKey.getFinalKey();
        Boolean checked = ops==1?true:false;
        //获取购物车
        RMap<String, String> rMap = redissonClient.getMap(finalKey);
        if(!StringUtils.isEmpty(skuIds)){
            //skuIds格式为1,2,3,4，进行截取
            String[] splitSkuIds = skuIds.split(",");
            //修改skuId对应的购物项选中状态
            for (String splitSkuId : splitSkuIds) {
                skuIdsList.add(Long.parseLong(splitSkuId));
               if(rMap!=null&&!rMap.isEmpty()){
                   //获取购物项
                   String cartItemString = rMap.get(splitSkuId);
                   CartItem cartItem = JSON.parseObject(cartItemString, CartItem.class);
                   //修改选中状态
                   cartItem.setCheck(checked);
                   //又重新放回到购物车
                   rMap.put(splitSkuId, JSON.toJSONString(cartItem));
               }
            }
        }
        //为了快速找到那个购物项被选中了，我们单独维护一个数组 数组在map中key是CartConstant.CART_CHECKED_KEY
        //修改checked集合状态
        checkItem(skuIdsList, checked, finalKey);
        //修改完成后，返回更新后的购物车
        CartResponse cartResponse = listCart(cartKey, accessToken);
        return cartResponse;
    }

    //根据用户的accessToken获取用户的购物项信息
    @Override
    public List<CartItem> getMemberCartItem(String accessToken) {
        UserCartKey userCartKey = memberComponet.getCartKey(accessToken, null);
        //通过购物车的key获取购物车
        RMap<String, String> rMap = redissonClient.getMap(userCartKey.getFinalKey());
        //只取出状态为checked的购物项<通过购物项的key获取购物项，注意这里获取的为一个装skuId的数组
        String checkedSkuIds = rMap.get(CartConstant.CART_CHECKED_KEY);
        Set<Long> skuIds = JSON.parseObject(checkedSkuIds, new TypeReference<Set<Long>>() {
        });
        ArrayList<CartItem> list = new ArrayList<>();
        skuIds.forEach(skuId->{
            String cartItemString = rMap.get(skuId.toString());
            CartItem cartItem = JSON.parseObject(cartItemString, CartItem.class);
            list.add(cartItem);
        });
        return list;
    }

    //为了快速找到那个购物项被选中了，我们单独维护一个数组 数组在map中key是CartConstant.CART_CHECKED_KEY
    private void checkItem(List<Long> skuIdsList, Boolean checked, String finalKey) {
        RMap<String, String> rMap = redissonClient.getMap(finalKey);
        String checkedJson = rMap.get(CartConstant.CART_CHECKED_KEY);
        //直接转为set集合
        Set<Long> longSet = JSON.parseObject(checkedJson, new TypeReference<Set<Long>>() {
        });
        //如果longSet为null，那么就手动set一个，防止空指针
        if(longSet==null || longSet.isEmpty()){
            longSet = new HashSet<>();
        }
        //获取所有被选中的购物项
        if(checked){
            longSet.addAll(skuIdsList);
            System.out.println("被选中的商品是--->"+longSet);
        }else {
            //如果传过来购物项的都没有被选择中，那么就在这个数组中将这些skuId删除
            longSet.removeAll(skuIdsList);
            System.out.println("未被选中的商品是--->"+longSet);
        }
        //把被选中的商品在购物车中单独用一个key来进行维护
        rMap.put(CartConstant.CART_CHECKED_KEY, JSON.toJSONString(longSet));
    }

    /**
     *
     *
     * @description: 合并购物车
     * @param id 用户id 在线购物车的key
     * @param cartKey 离线购物车key
     * @return:
     * @author: Aiden
     * @time: 2020-3-12 17:34:06
     */
     private void mergeCart(Long id, String cartKey) {
         //1、获取离线购物车中的所有购物项
         String offLineKey = CartConstant.TEMP_CART_KEY_PREFIX+cartKey;
         RMap<String, String> rMap = redissonClient.getMap(offLineKey);
         if(rMap!=null && !rMap.isEmpty()){
             //当临时购物车存在，且里面有数据我们才进行合并
             //2、遍历出离线购物车每一个商品，添加到在线购物车
             Set<Map.Entry<String, String>> entries = rMap.entrySet();
             entries.forEach(entry->{
                 if(!entry.getKey().equalsIgnoreCase(CartConstant.CART_CHECKED_KEY)){
                     //离线购物车map的key为skuid
                     String skuId = entry.getKey();
                     //离线购物车map的value为购物项，购物车里面存的为购物项
                     String mapValue = entry.getValue();
                     //反序列化为离线购物项
                     CartItem cartItem = JSON.parseObject(mapValue, CartItem.class);
                     //获取在线购物车的key
                     String onLioneKey = CartConstant.USER_CART_KEY_PREFIX+id;
                     try {
                         //把老购物车中的购物项逐一添加到新的购物车
                         addCartItemToCart(Long.parseLong(skuId), cartItem.getCount(), onLioneKey);
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
             });
             //合并完后，要移除老购物车
             rMap.clear();
         }
     }

    /**
     *
     *
     * @description: 添加购物项到购物车
     * @param null
     * @return:
     * @author: Aiden
     * @time: 2020-3-12 17:16:28
     */
     private CartItem addCartItemToCart(Long skuId, Integer num, String FINAL_KEY) throws ExecutionException, InterruptedException {
         //0、根据商品skuId信息查询商品具体属性--->RPC远程调用
         CartItem newCartItem = new CartItem();

         /* *
         * thenAccept接受上一步的返回结果进行处理但不返回
         * thenApply接受上一步的返回结果进行处理返回一个新的结果
         *
         * */
         //开启异步任务
         CompletableFuture</*SkuStock*/Void> skuFuture = CompletableFuture.supplyAsync(() -> {
             SkuStock stock = skuStockService.getById(skuId);
             return stock;
         })./*whenComplete((result, e) -> {
             //准备好了sku信息
         });*//*thenApply((result)->{
             //这个result就是上一步返回的stock
         })*/thenAccept((result)->{
             //拿到上一步的商品id
             //还要给newCartItem设置name，skuId属性
             Product product = productService.getById(result.getProductId());
             newCartItem.setName(product.getName());
             newCartItem.setSkuId(result.getId());
             //默认购物项中件数为1
             newCartItem.setCount(num);
             BeanUtils.copyProperties(result, newCartItem);
         });
         /* *
         * 购物车集合key[skuId]是str，v[购物项]是json字符串
         * 另外，选中的k[checked]，v[1,2,3]
         *
         * */
         //1、获取分布式集合
         RMap<String, String> rMap = redissonClient.getMap(FINAL_KEY);
         //2、获取购物车中这个skuId对应的购物项，判断原先是否已经有了这个购物项，如果有了那么就只是增加数量
         String cartItemString = rMap.get(skuId.toString());
         //要等异步任务执行完毕才能set
         skuFuture.get();
         if(StringUtils.isEmpty(cartItemString)){
             //这个商品第一次添加
             String cartItemJsonString = JSON.toJSONString(newCartItem);
             rMap.put(skuId.toString(), cartItemJsonString);
         }else {
             //不是第一次添加
             CartItem oldItem = JSON.parseObject(cartItemString, CartItem.class);
             newCartItem.setCount(oldItem.getCount()+newCartItem.getCount());
             String newCartItemJsonString = JSON.toJSONString(newCartItem);
             //新数据覆盖老数据
             rMap.put(skuId.toString(), newCartItemJsonString);
         }
         //添加购物项是，默认就是选中了这个购物项，将选中状态维护到购物车的key,  checked
         checkItem(Arrays.asList(skuId), true, FINAL_KEY);
         return newCartItem;
    }
}















