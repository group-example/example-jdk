package lang.reflect;

/**
 * @author: liuluming
 * @CreatedDate: 2017/10/31 下午4:13
 */
import java.lang.reflect.*;

public class ReflectDemo {
    /**
     * 两个预定义的需要被代理的接口
     */
    public static interface ProxiedInterface {

        void proxiedMethod();
    }
    public static interface ProxiedInterface2 {

        void proxiedMethod2();
    }

    /**
     * 真正的处理逻辑
     */
    public static class InvoHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("in proxy:" + method.getName());
            //其他逻辑
            System.out.println("in proxy end");
            return null;
        }

    }
    public static void main(String[] args) {
        InvoHandler ih = new InvoHandler();
        ProxiedInterface proxy = (ProxiedInterface) Proxy.newProxyInstance(ReflectDemo.class.getClassLoader(), new Class[]{ProxiedInterface.class, ProxiedInterface2.class}, ih);
        proxy.proxiedMethod();
        ProxiedInterface2 p = (ProxiedInterface2) proxy;
        p.proxiedMethod2();
    }
}
