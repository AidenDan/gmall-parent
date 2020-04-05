package com.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gmall.pms.entity.*;
import com.gmall.pms.mapper.*;
import com.gmall.pms.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gmall.vo.PageInfoVo;
import com.gmall.vo.product.PmsProductParam;
import com.gmall.vo.product.PmsProductQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author djy
 * @since 2020-02-27
 */
@Slf4j
@Service
@Component
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Autowired
    ProductMapper productMapper;
    @Autowired
    ProductAttributeValueMapper productAttributeValueMapper;
    @Autowired
    ProductFullReductionMapper productFullReductionMapper;
    @Autowired
    ProductLadderMapper productLadderMapper;
    @Autowired
    SkuStockMapper skuStockMapper;

    /**
     *
     *
     * @description:  分页查询商品信息
     * @param null
     * @return:
     * @author: Aiden
     * @time: 2020-2-28 16:10:07
     */
    @Override
    public PageInfoVo productPageInfo(PmsProductQueryParam param) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(param.getKeyword())){
            wrapper.like("name", param.getKeyword());
        }
        if(param.getBrandId() !=null){
            wrapper.eq("brand_id", param.getBrandId());
        }
        if(param.getProductCategoryId()!=null){
            wrapper.eq("product_category_id", param.getProductCategoryId());
        }
        if(param.getPublishStatus()!=null){
            wrapper.eq("publish_status", param.getPublishStatus());
        }
        if(param.getVerifyStatus()!=null){
            wrapper.eq("verify_status", param.getVerifyStatus());
        }
        if(!StringUtils.isEmpty(param.getProductSn())){
            wrapper.like("product_sn", param.getProductSn());
        }
        IPage<Product> page = productMapper.selectPage(new Page<Product>(param.getPageNum(), param.getPageSize()), wrapper);
        return new PageInfoVo(page.getTotal(), page.getPages(), param.getPageSize(), page.getRecords(), page.getCurrent());
    }

    @Override
    public void updateProductPublishStatus(List<Long> ids, Integer publishStatus) {
        //1、更新数据库中商品的上架下架状态
        //2、更新全文检索服务器中商品的上下架状态？有无必要？
        ids.forEach(id->{
            Product product = new Product();
            product.setId(id);
            product.setPublishStatus(publishStatus);
            productMapper.updateById(product);
        });
    }


    /* *
    * 1、什么时候回滚，全部回滚还是部分回滚 ----》事物的传播行为
    * 2、抽取出共享变量，产品id，用ThreadLocal来保存同一个线程中的共享变量安全，可以避免线程安全问题
    * 3、把每一组操作单独抽取为一个方法
    * 4、多线程共享变量有数据安全问题，单线程（同一个线程内部）共享变量没有安全问题
    * 5、同一次调用，只要上面方法的数据，下面要用，我们就可以用ThreadLocal共享
    * */
    ThreadLocal<Long> threadLocal = new ThreadLocal<>();
        /* *
        * ThreadLocal的底层原理
        HashMap<Thread, Long> hashMap = new HashMap<>();
        */

    /**
     * 大保存...
     * @param productParam
     *
     * 考虑事务....
     * 1）、哪些东西是一定要回滚的、哪些即使出错了不必要回滚的。
     *      商品的核心信息（基本数据、sku）保存的时候，不要受到别的无关信息的影响。
     *      无关信息出问题，核心信息也不用回滚的。
     *
     * 2）、事务的传播行为;propagation:当前方法的事务[是否要和别人公用一个事务]如何传播下去（里面的方法如果用事务，是否和他公用一个事务）
     *
     *
     *      Propagation propagation() default Propagation.REQUIRED;
     *
     *
     *
     *      REQUIRED:(必须):
     *          Support a current transaction, create a new one if none exists.
     *          如果以前有事务，就和之前的事务公用一个事务，没有就创建一个事务；
     *      REQUIRES_NEW（总是用新的事务）:
     *           Create a new transaction, and suspend the current transaction if one exists.
     *          创建一个新的事务，如果以前有事务，暂停前面的事务。
     *
     *      SUPPORTS（支持）:
     *          Support a current transaction, execute non-transactionally if none exists.
     *          之前有事务，就以事务的方式运行，没有事务也可以；
     *
     *      MANDATORY（强制）:没事务就报错
     *          Support a current transaction, throw an exception if none exists
     *          一定要有事务，如果没事务就报错
     *
     *
     *      NOT_SUPPORTED（不支持）:
     *          Execute non-transactionally, suspend the current transaction if one exists
     *          不支持在事务内运行，如果已经有事务了，就挂起当前存在的事务
     *
     *      NEVER（从不使用）:
     *           Execute non-transactionally, throw an exception if a transaction exists.
     *           不支持在事务内运行，如果已经有事务了，抛异常
     *
     *
     *      NESTED:
     *          Execute within a nested transaction if a current transaction exists,
     *          开启一个子事务（MySQL不支持），需要支持还原点功能的数据库才行；
     *
     *
     * 一家人带着老王去旅游；
     *      一家人：开自己的车还是坐老王的车
     *
     *      Required：坐老王车
     *      Requires_new：一定得开车，开新的
     *
     *      SUPPORTS：用车，有车就用，没车走路；
     *      MANDATORY：用车，没车就骂街。。。
     *
     *      NOT_SUPPORTED：不支持用车。有车放那不用
     *      NEVER：从不用车，有车抛异常
     *
     *
     *
     *
     * 外事务{
     *
     *     A();//事务.Required：跟着回滚
     *
     *     b();//事务.Requires_new：不回滚
     *
     *     //自己给数据库插入数据
     *
     *     int i = 10/0;
     *
     * }
     *
     * Required_new
     * 外事务{
     *
     *     A（）；Required; A
     *     B（）;Requires_new B
     *     try{
     *         C();Required; C
     *     }catch(Exception e){
     *         //c出异常？
     *     }
     *
     *     D();Requires_new; D
     *
     *     //给数据库存 --外
     *
     *    // int i = 10/0;
     *
     * }
     *
     * 场景1：
     *      A方法出现了异常；由于异常机制导致代码停止，下面无法执行，数据库什么都没有
     * 场景2：
     *     C方法出现异常；A回滚，B成功，C回滚，D无法执行，外无法执行
     * 场景3：
     *      外成了后，int i = 10/0; B,D成功。A,C,外都执行了但是必须回滚
     * 场景4：
     *     D炸；抛异常。外事务感知到异常。A,C回滚，外执行不到，D自己回滚，B成功
     * 场景5：
     *     C用try-catch执行；C出了异常回滚，由于异常被捕获，外事务没有感知异常。A,B,D都成，C自己回滚
     *
     * 总结：
     *      传播行为过程中，只要Requires_new被执行过就一定成功，不管后面出不出问题。异常机制还是一样的，出现异常代码以后不执行。
     * Required只要感觉到异常就一定回滚。和外事务是什么传播行为无关。
     *
     * 传播行为总是来定义，当一个事务存在的时候，他内部的事务该怎么执行。
     *
     *
     *
     *
     *
     *
     * 如何让某些可以不回滚
     *
     *
     * 事务Spring中是怎么做的？
     * TransactionManager；
     * AOP做；
     *
     * 动态代理。
     *  hahaServiceProxy.saveBaseInfo();
     *
     *  A{
     *      A(){
     *          B(); //1,2,3
     *          C(); //4,5,6
     *          D(); //7,8,9
     *      }
     *  }
     *
     *  自己类调用自己类里面的方法，就是一个复制粘贴。归根到底，只是给
     *  controller{
     *      serviceProxy.a();
     *  }
     *  对象.方法()才能加上事务。
     *
     *
     *  A(){
     *      //1,2,3,4,5,6,7,8,9
     *      //
     *  }
     *
     *  A{
     *      A(){
     *          hahaService.B();
     *          hahaService.C();
     *          hahaService.D();
     *
     *      }
     *  }
     *
     *  事务的问题：
     *      Service自己调用自己的方法，无法加上真正的自己内部调整的各个事务
     *      解决：如果是  对象.方法()那就好了
     *       1）、要是能拿到ioc容器，从容器中再把我们的组件获取一下，用对象调方法。
     *
     *
     *
     * 复习：事务传播行为，
     * ====================================================================
     * 隔离级别：解决读写加锁问题的（数据底层的方案）。  可重复读（快照）；
     *
     * 读未提交：
     * 读已提交：
     * 可重复读：
     * 串行化：
     *
     * ===========================================================
     * 异常回滚策略
     * 异常：
     *      运行时异常（不受检查异常）
     *          ArithmeticException ......
     *      编译时异常（受检异常）
     *            FileNotFound；1）要么throw要么try- catch
     *
     * 运行的异常默认是一定回滚
     * 编译时异常默认是不回滚的；
     *      rollbackFor：指定哪些异常一定回滚的。
     *      noRollbackFor: 指定哪些异常不回滚
     *
     *
     *
     *
     */

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    @Override
    public void saveProduct(PmsProductParam productParam) {
        //获取当前类的代理对象，必须用代理对象调用事物方法，这样事物才能生效
        ProductServiceImpl proxy = (ProductServiceImpl)AopContext.currentProxy();

        //要把传过来的参数拆开，分别存入以下几张表中，先插入商品表，获取自增的商品id，然后插入这个商品对应的属性等信息表
        //1、pms_product 保存商品基本信息
        proxy.saveProductBaseInfo( productParam);

        //5、pms_sku_stock 库存表
        proxy.saveSkuStock(productParam);

        //2、pms_product_attribute_value 保存这个商品对应的所有属性的值
        proxy.saveProductAttributeValue( productParam);

        //3、pms_product_full_reduction 保存商品的满减信息
        proxy.saveProductFullReduction(productParam);

        //4、pms_product_ladder  商品阶梯价格表
        proxy.saveProductLadder(productParam);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSkuStock(PmsProductParam productParam) {
        List<SkuStock> skuStockList = productParam.getSkuStockList();
        for (int i = 0; i < skuStockList.size(); i++) {
            SkuStock skuStock = skuStockList.get(i);
            //skuCode不能为空
            if(StringUtils.isEmpty(skuStock.getSkuCode())){
                skuStock.setSkuCode(threadLocal.get()+"_"+i);
            }
            skuStock.setProductId(threadLocal.get());
            skuStockMapper.insert(skuStock);
            log.debug("skuStock:{}", skuStock);
        }
        log.debug("当前线程：{}", Thread.currentThread().getName());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductLadder(PmsProductParam productParam) {
        List<ProductLadder> productLadderList = productParam.getProductLadderList();
        productLadderList.forEach(productLadder -> {
            productLadder.setProductId(threadLocal.get());
            productLadderMapper.insert(productLadder);
            log.debug("productLadder:{}", productLadder);
        });
        log.debug("当前线程：{}", Thread.currentThread().getName());
        //int i = 10/0;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductFullReduction(PmsProductParam productParam) {
        List<ProductFullReduction> productFullReductionList = productParam.getProductFullReductionList();
        productFullReductionList.forEach(productFullReduction -> {
            productFullReduction.setProductId(threadLocal.get());
            productFullReductionMapper.insert(productFullReduction);
            log.debug("productFullReduction:{}", productFullReduction);
        });
        log.debug("当前线程：{}", Thread.currentThread().getName());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductAttributeValue(PmsProductParam productParam) {
        List<ProductAttributeValue> productAttributeValueList = productParam.getProductAttributeValueList();
        //把上面自增生成的主键商品id添加到每一个商品属性值记录中，从线程中取值
        productAttributeValueList.forEach(productAttributeValue -> {
            productAttributeValue.setProductId(threadLocal.get());
            productAttributeValueMapper.insert(productAttributeValue);
            log.debug("productAttributeValue:{}", productAttributeValue);
        });
        log.debug("当前线程：{}", Thread.currentThread().getName());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveProductBaseInfo(PmsProductParam productParam) {
        Product product = new Product();
        BeanUtils.copyProperties(productParam, product);
        productMapper.insert(product);
        //把当前线程的共享变量产品id存入threadLocal，那么只有当前线程才能获取这个数据，可以保证线程安全
        threadLocal.set(product.getId());
        log.debug("product:{}", product);
        log.debug("当前线程：{}", Thread.currentThread().getName());
    }
}





















