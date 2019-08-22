## 实现一个简单的ioc容器


#### IOC，即控制反转
百度百科定义：
> 控制反转（Inversion of Control，缩写为IoC），是面向对象编程中的一种设计原则，可以用来减低计算机代码之间的耦合度。其中最常见的方式叫做依赖注入（Dependency 
Injection，简称DI），还有一种方式叫“依赖查找”（Dependency Lookup）。通过控制反转，对象在被创建的时候，由一个调控系统内所有对象的外界实体将其所依赖的对象的引用传递给它。也可以说，依赖被注入到对象中。

Spring ioc的工作流程

1. 根据Bean配置信息在容器内部创建Bean定义注册表
2. 根据注册表加载、实例化bean、建立Bean与Bean之间的依赖关系
3. 将这些准备就绪的Bean放到Map缓存池中，等待应用程序调用

实现：
1.声明我们的注解Service，后面会扫描声明了这个注解的所有类。
```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
    String value();
}
```

2.定义一个 Bean配置信息类BeanDefinition，里面声明了bean的名称，类名，参数，参数类型等。
```java
@Data
@ToString
public class BeanDefinition {

    /**
     * 对象的name
     */
    private String name;

    /**
     * 对象的类名
     */
    private String className;

    /**
     * 如果是接口，接口的名称
     */
    private String interfaceName;

    /**
     * 构造函数的传参的列表
     */
    private List<ConstructorArg> constructorArgs;

    /**
     * 需要注入的参数列表
     */
    private List<PropertyArg> propertyArgs;
}
```

使用了lombok插件，省略了get set 方法。

3.声明一个BeanFactory，这是一个接口，里面有一个方法，根据name获取bean。可以扩展，这里为了简单只声明了一个。
```java
public interface BeanFactory {
    /**
     * 根据name获取bean
     * @param name
     * @return
     * @throws Exception
     */
    Object getBean(String name) throws Exception;
}
```


具体的实现类：
```java
public class BeanFactoryImpl implements BeanFactory {

    /**
     * 缓存bean对象的Map集合
     * key -> Service注解声明的value
     * val -> bean对象
     */
    private static final Map<String, Object> beanMap = new ConcurrentHashMap<>();


    /**
     * 缓存bean注册信息的Map集合
     * key -> Service注解声明的value
     * val -> BeanDefinition
     */
    private static final Map<String, BeanDefinition> beanDefineMap = new ConcurrentHashMap<>();


    /**
     * 1.当调用 getBean() 的方法的时候。会先到 beanMap 里面查找，有没有实例化好的对象。
     * 2.如果没有，就会去beanDefineMap查找这个对象对应的 BeanDefination。再利用DeanDefination去实例化一个对象。
     * @param name
     * @return
     * @throws Exception
     */
    @Override
    public Object getBean(String name) throws Exception {
        Object bean = beanMap.get(name);
        if (bean != null) {
            return bean;
        }

        // 没有实例化好的对象,进行第2步
        bean = createBean(beanDefineMap.get(name));

        if (bean != null) {

            //对象创建成功以后，注入对象需要的参数
            populatebean(bean);

            // 把对象存入Map中方便下次使用
            beanMap.put(name, bean);
        }
        return bean;
    }

    private Object createBean(BeanDefinition beanDefinition) throws Exception {

        String className = beanDefinition.getClassName();
        Class clazz = ClassUtil.loadClass(className);
        if(clazz == null) {
            throw new Exception("can not find bean by beanName");
        }
        List<ConstructorArg> constructorArgs = beanDefinition.getConstructorArgs();
        if (constructorArgs != null && !constructorArgs.isEmpty()) {
            List<Object> objects = new ArrayList<>();
            for (ConstructorArg constructorArg : constructorArgs) {
                objects.add(getBean(constructorArg.getRef()));
            }
            return BeanUtil.instanceByCglib(clazz, clazz.getConstructor(), (Objects[])  objects.toArray());
        } else {
            return BeanUtil.instanceByCglib(clazz, null, null);
        }
    }


    protected void registerBean(String name, BeanDefinition beanDefinition) {
        beanDefineMap.put(name, beanDefinition);
    }

    /**
     * 对象实例化成功以后，我们还需要注入相应的参数，调用 populatebean()这个方法。在 populateBean 这个方法中，会扫描对象里面的Field，如果对象中的 Field 是我们IoC容器管理的对象，那就会调用 我们上文实现的 ReflectionUtils.injectField来注入对象。
     * @param bean
     *
     */
    private void populatebean(Object bean) throws Exception {
        Field[] fields = bean.getClass().getSuperclass().getDeclaredFields();
        if (fields.length > 0) {
            for (Field field : fields) {
                String className = field.getType().getName();
                // 在bean的注册表中找对应的类
                for (Map.Entry<String, BeanDefinition> entry : beanDefineMap.entrySet()) {
                    if (entry.getValue().getClassName().equals(className)) {
                        // 如果找到，获取到对象的实例，并通过反射设置到属性中
                        String name = entry.getKey();
                        Object fieldBean = getBean(name);
                        if (fieldBean != null) {
                            ReflectionUtil.injectField(field, bean, fieldBean);
                        }
                        break;
                    }
                }
            }
        }
    }
}
```

4.因为是根据注解注入的，我们声明一个AnnotationApplicationContext的类，它的作用就是扫描所有声明了@Service的类，然后调用registerBean()方法，把对象注入到bean的注册信息表中。
```java
public class AnnotationApplicationContext extends BeanFactoryImpl {
    public void init(){

        Reflections reflections = new Reflections("com.yuanshijia");
        // 获取 @Service 标注的接口
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(Service.class);

        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        for (Class<?> c : classSet) {
            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setClassName(c.getName());

            // 反射获取注解的value属性
            Service service = c.getDeclaredAnnotation(Service.class);
            beanDefinition.setName(service.value());
            beanDefinitions.add(beanDefinition);
        }
        // 注入
        for (BeanDefinition beanDefinition : beanDefinitions) {
            registerBean(beanDefinition.getName(), beanDefinition);
        }
    }
}
```

5.准备自己的测试类，这里用一个Car类，里面有Wheel(轮子)和Frame(车架)两个对象需要注入。
```java
@Service(value = "car")
public class Car {
    /**
     * 注入车架和轮子
     */
    private Frame frame;
    private Wheel wheel;


    public void makeCar() throws InterruptedException {
        System.out.println("开始生产汽车...");
        frame.make();
        wheel.make();
        System.out.println("汽车制作完成!");
    }
}
@Service(value = "frame")
public class Frame {
    public void make() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("车架制作中...");
        Thread.sleep(1000);
    }
}

@Service(value = "wheel")
public class Wheel {
    public void make() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("轮子制作中...");
        Thread.sleep(1000);

    }
}
```

6.运行
```java
public class Main {
    public static void main(String[] args) throws Exception {
        AnnotationApplicationContext context = new AnnotationApplicationContext();
        context.init();
        Car car = (Car) context.getBean("car");

        car.makeCar();
    }
}
```

我们向容器中获取Car类，容器会注入car里面的Wheel和Frame。
运行结果:
```
开始生产汽车...
车架制作中...
轮子制作中...
汽车制作完成!
```

博客地址:

完整代码https://github.com/xiaosamo/my-ioc
#### 参考博客：
* https://juejin.im/post/5a5875a4518825733a30a463
* https://juejin.im/post/5b040cf66fb9a07ab7748c8b
